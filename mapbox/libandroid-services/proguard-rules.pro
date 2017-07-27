# Retrofit 2 (https://square.github.io/retrofit/#ProGuard)
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# Gson (https://github.com/google/gson/blob/master/examples/android-proguard-example/proguard.cfg)
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# MAS data models that will be serialized/deserialized over Gson
-keep class com.mapbox.services.api.directions.v5.models.** { *; }
-keep class com.mapbox.services.api.distance.v1.models.** { *; }
-keep class com.mapbox.services.api.geocoding.v5.models.** { *; }
-keep class com.mapbox.services.api.mapmatching.v5.models.** { *; }
-keep class com.mapbox.services.api.optimizedtrips.v1.models.** { *; }
-keep class com.mapbox.services.api.directionsmatrix.v1.models.** { *; }
-keep class com.mapbox.services.commons.geojson.** { *; }

-dontwarn javax.annotation.**

-keep class com.google.**
-dontwarn com.google.**
