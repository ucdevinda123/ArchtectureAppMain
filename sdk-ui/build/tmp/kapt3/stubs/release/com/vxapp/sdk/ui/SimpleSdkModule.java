package com.vxapp.sdk.ui;

import com.vxapp.sdk.session.SimpleSessionManager;

/**
 * Simple SDK module factory
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0016\b\u0002\u0010\u0005\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b\u0018\u00010\u0006J\u0006\u0010\t\u001a\u00020\u0007J\u0006\u0010\n\u001a\u00020\u000b\u00a8\u0006\f"}, d2 = {"Lcom/vxapp/sdk/ui/SimpleSdkModule;", "", "()V", "createSimpleSdkFragment", "Lcom/vxapp/sdk/ui/SimpleSdkFragment;", "onResult", "Lkotlin/Function1;", "", "", "getVersion", "isInitialized", "", "sdk-ui_release"})
public final class SimpleSdkModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.vxapp.sdk.ui.SimpleSdkModule INSTANCE = null;
    
    private SimpleSdkModule() {
        super();
    }
    
    /**
     * Create a simple SDK fragment
     */
    @org.jetbrains.annotations.NotNull()
    public final com.vxapp.sdk.ui.SimpleSdkFragment createSimpleSdkFragment(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onResult) {
        return null;
    }
    
    /**
     * Get SDK version
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVersion() {
        return null;
    }
    
    /**
     * Check if SDK is properly initialized
     */
    public final boolean isInitialized() {
        return false;
    }
}