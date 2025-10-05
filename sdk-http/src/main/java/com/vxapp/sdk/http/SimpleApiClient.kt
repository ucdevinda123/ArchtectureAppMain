package com.vxapp.sdk.http

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

/**
 * Simple API client interface
 */
interface SimpleApiClient {
    suspend fun <T> execute(call: suspend () -> Response<T>): ApiResult<T>
}

/**
 * Sealed class representing API operation results
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
    data class NetworkError(val exception: IOException) : ApiResult<Nothing>()
    data class HttpError(val code: Int, val message: String) : ApiResult<Nothing>()
}

/**
 * Simple implementation of ApiClient
 */
class SimpleApiClientImpl : SimpleApiClient {
    
    override suspend fun <T> execute(call: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    ApiResult.Success(body)
                } ?: ApiResult.Error(NullPointerException("Response body is null"))
            } else {
                ApiResult.HttpError(response.code(), response.message())
            }
        } catch (e: IOException) {
            ApiResult.NetworkError(e)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
