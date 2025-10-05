package com.vxapp.sdk.session

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Simple session manager for demo purposes
 */
class SimpleSessionManager(private val context: Context) {
    
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Unauthenticated)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()
    
    private var currentSession: UserSession? = null
    private var currentProfile: UserProfile? = null
    
    fun login(userId: String, accessToken: String, refreshToken: String, expiresAt: Long, profile: UserProfile) {
        currentSession = UserSession(
            userId = userId,
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresAt = expiresAt
        )
        currentProfile = profile
        _sessionState.value = SessionState.Authenticated(currentSession!!, profile)
    }
    
    fun logout() {
        currentSession = null
        currentProfile = null
        _sessionState.value = SessionState.Unauthenticated
    }
    
    fun getCurrentSession(): UserSession? = currentSession
    fun getCurrentProfile(): UserProfile? = currentProfile
    fun isTokenExpired(): Boolean {
        return currentSession?.let { System.currentTimeMillis() >= it.expiresAt } ?: true
    }
    
    fun updateLastUsed() {
        // Simple implementation - just update the timestamp
        currentSession?.let { session ->
            currentSession = session.copy(lastUsedAt = System.currentTimeMillis())
        }
    }
}

/**
 * Simple data classes
 */
data class UserSession(
    val userId: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long = System.currentTimeMillis()
)

data class UserProfile(
    val id: String,
    val email: String,
    val name: String,
    val avatar: String? = null,
    val createdAt: String,
    val updatedAt: String
)

sealed class SessionState {
    object Loading : SessionState()
    object Unauthenticated : SessionState()
    data class Authenticated(val session: UserSession, val profile: UserProfile) : SessionState()
    data class Error(val message: String) : SessionState()
}
