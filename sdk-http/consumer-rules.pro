# Consumer ProGuard rules for SDK consumers
# These rules will be applied when the SDK is used in other projects

# Keep SDK public APIs
-keep public class com.vxapp.sdk.http.** { *; }

# Keep data classes
-keep class com.vxapp.sdk.http.models.** { *; }

# Keep interfaces
-keep interface com.vxapp.sdk.http.** { *; }
