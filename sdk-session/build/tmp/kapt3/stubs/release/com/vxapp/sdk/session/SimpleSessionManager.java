package com.vxapp.sdk.session;

import android.content.Context;
import kotlinx.coroutines.flow.StateFlow;

/**
 * Simple session manager for demo purposes
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0010\u001a\u0004\u0018\u00010\tJ\b\u0010\u0011\u001a\u0004\u0018\u00010\u000bJ\u0006\u0010\u0012\u001a\u00020\u0013J.\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\tJ\u0006\u0010\u001d\u001a\u00020\u0015J\u0006\u0010\u001e\u001a\u00020\u0015R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001f"}, d2 = {"Lcom/vxapp/sdk/session/SimpleSessionManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_sessionState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/vxapp/sdk/session/SessionState;", "currentProfile", "Lcom/vxapp/sdk/session/UserProfile;", "currentSession", "Lcom/vxapp/sdk/session/UserSession;", "sessionState", "Lkotlinx/coroutines/flow/StateFlow;", "getSessionState", "()Lkotlinx/coroutines/flow/StateFlow;", "getCurrentProfile", "getCurrentSession", "isTokenExpired", "", "login", "", "userId", "", "accessToken", "refreshToken", "expiresAt", "", "profile", "logout", "updateLastUsed", "sdk-session_release"})
public final class SimpleSessionManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.vxapp.sdk.session.SessionState> _sessionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.vxapp.sdk.session.SessionState> sessionState = null;
    @org.jetbrains.annotations.Nullable()
    private com.vxapp.sdk.session.UserSession currentSession;
    @org.jetbrains.annotations.Nullable()
    private com.vxapp.sdk.session.UserProfile currentProfile;
    
    public SimpleSessionManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.vxapp.sdk.session.SessionState> getSessionState() {
        return null;
    }
    
    public final void login(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    java.lang.String refreshToken, long expiresAt, @org.jetbrains.annotations.NotNull()
    com.vxapp.sdk.session.UserProfile profile) {
    }
    
    public final void logout() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.vxapp.sdk.session.UserSession getCurrentSession() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.vxapp.sdk.session.UserProfile getCurrentProfile() {
        return null;
    }
    
    public final boolean isTokenExpired() {
        return false;
    }
    
    public final void updateLastUsed() {
    }
}