package com.vxapp.sdk.session;

import android.content.Context;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0003\u0004\u0005\u0006B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0004\u0007\b\t\n\u00a8\u0006\u000b"}, d2 = {"Lcom/vxapp/sdk/session/SessionState;", "", "()V", "Authenticated", "Error", "Loading", "Unauthenticated", "Lcom/vxapp/sdk/session/SessionState$Authenticated;", "Lcom/vxapp/sdk/session/SessionState$Error;", "Lcom/vxapp/sdk/session/SessionState$Loading;", "Lcom/vxapp/sdk/session/SessionState$Unauthenticated;", "sdk-session_debug"})
public abstract class SessionState {
    
    private SessionState() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0016"}, d2 = {"Lcom/vxapp/sdk/session/SessionState$Authenticated;", "Lcom/vxapp/sdk/session/SessionState;", "session", "Lcom/vxapp/sdk/session/UserSession;", "profile", "Lcom/vxapp/sdk/session/UserProfile;", "(Lcom/vxapp/sdk/session/UserSession;Lcom/vxapp/sdk/session/UserProfile;)V", "getProfile", "()Lcom/vxapp/sdk/session/UserProfile;", "getSession", "()Lcom/vxapp/sdk/session/UserSession;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "sdk-session_debug"})
    public static final class Authenticated extends com.vxapp.sdk.session.SessionState {
        @org.jetbrains.annotations.NotNull()
        private final com.vxapp.sdk.session.UserSession session = null;
        @org.jetbrains.annotations.NotNull()
        private final com.vxapp.sdk.session.UserProfile profile = null;
        
        public Authenticated(@org.jetbrains.annotations.NotNull()
        com.vxapp.sdk.session.UserSession session, @org.jetbrains.annotations.NotNull()
        com.vxapp.sdk.session.UserProfile profile) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vxapp.sdk.session.UserSession getSession() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vxapp.sdk.session.UserProfile getProfile() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vxapp.sdk.session.UserSession component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vxapp.sdk.session.UserProfile component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vxapp.sdk.session.SessionState.Authenticated copy(@org.jetbrains.annotations.NotNull()
        com.vxapp.sdk.session.UserSession session, @org.jetbrains.annotations.NotNull()
        com.vxapp.sdk.session.UserProfile profile) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/vxapp/sdk/session/SessionState$Error;", "Lcom/vxapp/sdk/session/SessionState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "sdk-session_debug"})
    public static final class Error extends com.vxapp.sdk.session.SessionState {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String message = null;
        
        public Error(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMessage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vxapp.sdk.session.SessionState.Error copy(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/vxapp/sdk/session/SessionState$Loading;", "Lcom/vxapp/sdk/session/SessionState;", "()V", "sdk-session_debug"})
    public static final class Loading extends com.vxapp.sdk.session.SessionState {
        @org.jetbrains.annotations.NotNull()
        public static final com.vxapp.sdk.session.SessionState.Loading INSTANCE = null;
        
        private Loading() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/vxapp/sdk/session/SessionState$Unauthenticated;", "Lcom/vxapp/sdk/session/SessionState;", "()V", "sdk-session_debug"})
    public static final class Unauthenticated extends com.vxapp.sdk.session.SessionState {
        @org.jetbrains.annotations.NotNull()
        public static final com.vxapp.sdk.session.SessionState.Unauthenticated INSTANCE = null;
        
        private Unauthenticated() {
        }
    }
}