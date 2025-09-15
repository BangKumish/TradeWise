# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Dagger - Hilt
-keep class dagger.hilt.internal.aggregatedroot.codegen.*
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper
-keep class *_*HiltModules* { *; }
-keep class **.HiltWrapper_* { *; }

# Retrofit & OkHttp
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }

# GSON (Critically important for Retrofit)
# This prevents Proguard from renaming the fields in your data models,
# which would break JSON parsing.
-keep class com.google.gson.annotations.** { *; }
-keep class com.yourpackage.tradewise.data.model.** { *; } #<-- CHANGE THIS TO YOUR PACKAGE NAME

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.internal.LimitedDispatcher { *; }
-keepnames class kotlinx.coroutines.flow.internal.SafeCollector_commonKt { *; }