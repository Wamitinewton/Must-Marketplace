# Keep generic signatures for TypeToken
-keepattributes Signature

# For using TypeToken with Gson
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken { *; }

# Gson specific rules
-keepattributes *Annotation*
-keep class com.google.gson.stream.** { *; }

# Keep Base Application
