package com.vxapp.sdk.ui

import com.vxapp.sdk.session.SimpleSessionManager

/**
 * Simple SDK module factory
 */
object SimpleSdkModule {
    
    /**
     * Create a simple SDK fragment
     */
    fun createSimpleSdkFragment(
        onResult: ((String) -> Unit)? = null
    ): SimpleSdkFragment {
        return SimpleSdkFragment.newInstance().apply {
            onResult?.let { callback ->
                setOnResultCallback(callback)
            }
        }
    }
    
    /**
     * Get SDK version
     */
    fun getVersion(): String {
        return "1.0.0"
    }
    
    /**
     * Check if SDK is properly initialized
     */
    fun isInitialized(): Boolean {
        return true
    }
}
