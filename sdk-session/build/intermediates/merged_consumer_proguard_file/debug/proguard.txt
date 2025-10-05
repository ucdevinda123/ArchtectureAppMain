# Consumer ProGuard rules for SDK consumers
# These rules will be applied when the SDK is used in other projects

# Keep SDK public APIs
-keep public class com.vxapp.sdk.session.** { *; }

# Keep data classes
-keep class com.vxapp.sdk.session.models.** { *; }

# Keep interfaces
-keep interface com.vxapp.sdk.session.** { *; }

# Keep Room entities
-keep @androidx.room.Entity class *
-keep class * extends androidx.room.RoomDatabase
