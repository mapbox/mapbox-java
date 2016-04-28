# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/antonio/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# Retrofit 2
# See: http://square.github.io/retrofit/#download
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# RxJava
# See: https://github.com/ReactiveX/RxAndroid/pull/220
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontwarn sun.misc.Unsafe

# MAS Data Models
-keep class com.mapbox.services.directions.v4.models.** { *; }
-keep class com.mapbox.services.directions.v5.models.** { *; }
-keep class com.mapbox.services.geocoding.v5.models.** { *; }
