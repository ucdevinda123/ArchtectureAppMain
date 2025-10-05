# Consumer ProGuard rules for SDK consumers
# These rules will be applied when the SDK is used in other projects

# Keep SDK public APIs
-keep public class com.vxapp.sdk.ui.** { *; }

# Keep data classes
-keep class com.vxapp.sdk.ui.models.** { *; }

# Keep interfaces
-keep interface com.vxapp.sdk.ui.** { *; }

# Keep Fragment classes
-keep class * extends androidx.fragment.app.Fragment { *; }

# Keep Compose related classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**
