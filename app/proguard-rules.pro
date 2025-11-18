# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Hilt ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-keepclassmembers class kotlinx.serialization.json.** {
    *;
}
-keep,includedescriptorclasses class com.houseofalgorithms.catalogviewer.data.model.**$$serializer { *; }
-keepclassmembers class com.houseofalgorithms.catalogviewer.data.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.houseofalgorithms.catalogviewer.data.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep DataStore
-keep class androidx.datastore.** { *; }

# Keep Compose runtime
-keep class androidx.compose.runtime.** { *; }

# Keep Timber
-keep class timber.log.** { *; }
-dontwarn timber.log.**