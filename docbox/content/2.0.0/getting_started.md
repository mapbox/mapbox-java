## Introduction
Mapbox Java is an open source toolset for building applications that need navigation, directions, geocoding, static map imagery, etc. It conveniently wraps Mapbox APIs and builds off of them with with tools useful for your applications. This SDK consist of two parts:

1. **Mapbox-Java-Services:** Has no dependency on the Android API and can be used in any of your Java projects.
2. **Mapbox-Android-Services:** Contains specific code for Android applications such as widgets and UI elements.

### Support and contributions

- If you are looking for support using this SDK, either reach out through [Stack Overflow](https://stackoverflow.com/questions/tagged/mapbox+android) or through our [contact page](https://www.mapbox.com/contact/).
- If you have found a bug, and can provide steps to reliably reproduce it, open an issue in the /mapbox-java repository on Github and apply the bug label.
- If you have a feature request, open an issue in the /mapbox-java repository on Github, and apply the enhancement label.
- If you want to contribute, look over our contribution guild lines and open a pull request with your changes.

### API reference
All public methods in all the project modules are well documented and even include a since tag so you can determine when an API was first added. A link for all the module javadoc pages can be found in the list below:

- [mapbox-java-core](http://mapbox.github.io/mapbox-java/api/libjava-core/2.0.0-beta.1/)
- [mapbox-java-geojson](http://mapbox.github.io/mapbox-java/api/libjava-geojson/2.0.0-beta.1/)
- [mapbox-java-services](http://mapbox.github.io/mapbox-java/api/libjava-services/2.0.0-beta.1/)
- [mapbox-java-services-rx](http://mapbox.github.io/mapbox-java/api/libjava-services-rx/2.0.0-beta.1/)
- [mapbox-android-services](http://mapbox.github.io/mapbox-java/api/libandroid-services/2.0.0-beta.1/)
- [mapbox-android-telemetry](http://mapbox.github.io/mapbox-java/api/libandroid-telemetry/2.0.0-beta.1/)
- [mapbox-android-ui](http://mapbox.github.io/mapbox-java/api/libandroid-ui/2.0.0-beta.1/)

### Access tokens
If you plan to use any of our APIs such as directions, geocoding, navigation, etc. you'll need to have a Mapbox access token which you'll pass in as a parameter. An access token isn't needed if you plan to use Mapbox Java only for GeoJSON parsing, or Turf calculations. To learn more about access tokens, read [this document](https://www.mapbox.com/help/create-api-access-token/).

## Installation
To start developing your application using Mapbox Java, you'll need to first determine which installation method works best for you. The SDK is fully compatible with Android using Gradle and most of the project (besides the Android dependent modules) can also be included in a generic Java project using either Gradle or Maven. All dependencies given below can be found on MavenCentral.

### Gradle

1. Start Android Studio
2. Open up your applications `build.gradle`
3. Ensure your projects `minSdkVersion` is at API 15 or higher
4. Under dependencies add a new build rule for the latest mapbox-android-services
5. Click the Sync Project with Gradle Files near the toolbar in Studio

> **Note:** If your application is close or exceeds the 65k method count limit, you can mitigate this problem by specifying only the specific Mapbox Android Service APIs instead of all of them. See the selectively compiling APIs section below.

```groovy
compile 'com.mapbox.mapboxsdk:mapbox-android-services:2.0.0'
```

### Maven

If your project's using Maven instead of Gradle, you can add the dependency inside your projects `POM.xml` file.

```xml
<dependency>
    <groupId>com.mapbox.mapboxsdk</groupId>
    <artifactId>mapbox-java-services</artifactId>
    <version>1.3.1</version>
</dependency>
```

### Selectively compiling APIs

In previous versions of Mapbox Java prior to 2.0, you would have to compile the entire package of APIs. This in some cases, could cause Android applications to go over the 65,536 method count limit.

```groovy
compile 'com.mapbox.mapboxsdk:mapbox-java-geojson:2.0.0'
```

Starting with 2.0, you now have the option to include either the entire package of APIs (using the dependencies listed above) or you can now selectively choose which specific APIs your application needs. For example, if you only need to handle GeoJSON serialization or deserialization inside your application you only need to include the GeoJSON dependency in your project.

The list below shows all the current separated dependencies you can use in your Android application.

```groovy
compile 'com.mapbox.mapboxsdk:mapbox-java-core:2.0.0'
compile 'com.mapbox.mapboxsdk:mapbox-java-geojson:2.0.0'
compile 'com.mapbox.mapboxsdk:mapbox-java-services:2.0.0'
compile 'com.mapbox.mapboxsdk:mapbox-java-services-rx:2.0.0'
compile 'com.mapbox.mapboxsdk:mapbox-android-services:2.0.0'
compile 'com.mapbox.mapboxsdk:mapbox-android-telemetry:2.0.0'
compile 'com.mapbox.mapboxsdk:mapbox-android-ui:2.0.0'
```

- Mapbox Java core
- GeoJSON
- Mapbox Java Services
- Mapbox Java Services for RxJava projects
- Mapbox Android Services
- Telemetry
- Mapbox Android UI

> **Note:** ProGuard directives are included in the Android dependencies to preserve the required classes.
