[![](https://www.mapbox.com/android-sdk/images/service-splash.png)](https://www.mapbox.com/android-sdk/#mapbox_android_services)

[![Build Status](https://www.bitrise.io/app/a7eea7d04be1e2e5.svg?token=OruuJNhnjyeRnlBv0wXsFQ&branch=master)](https://www.bitrise.io/app/a7eea7d04be1e2e5) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mapbox.mapboxsdk/mapbox-android-services/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mapbox.mapboxsdk/mapbox-android-services)
<a href="http://www.methodscount.com/?lib=com.mapbox.mapboxsdk%3Amapbox-java-services%3A1.3.1"><img src="https://img.shields.io/badge/Methods count-core: 690 | deps: 4795-e91e63.svg"/></a>

# Mapbox Android Services (MAS)

An open source toolset for building applications that need navigation, directions, geocoding, or static map imagery.

To avoid bringing unnecessary dependencies to Android projects, this project is split into specialized libraries:

* `mapbox-java-core`: Java models shared across all modules.
* `mapbox-java-geojson`: Java support for GeoJSON objects (`Feature`, `FeatureCollection`, `Geometry`, `GeometryCollection`, `LineString`, `MultiLineString`, `MultiPoint`, `MultiPolygon`, `Point`, and `Polygon`).
* `mapbox-java-services`: Support for Mapbox APIs (directions, geocoding, map matching, distance, static image) including Turf.
* `mapbox-java-services-rx`: RxJava extensions for `mapbox-java-services`.
* `mapbox-android-services`: Drop-in replacement for the Android Geocoder using Mapbox services.
* `mapbox-android-telemetry`: Telemetry component for the [Mapbox Android SDK](https://www.mapbox.com/android-sdk/).
* `mapbox-android-ui`: Android-specific widgets, includes an autocomplete view for the Mapbox Geocoding API.

### Features

**Java & Android:**

* [x] Integrate with the [Directions API](https://www.mapbox.com/directions/)
* [x] Integrate with the [Geocoding API](https://www.mapbox.com/geocoding/)
* [x] Integrate with the [Map Matching API](https://www.mapbox.com/blog/map-matching/)
* [x] Integrate with the [Distance API](https://www.mapbox.com/blog/distance-api/)
* [x] Integrate with the [Static Image API](https://www.mapbox.com/api-documentation/#static)
* [x] [Line simplification](https://www.mapbox.com/android-sdk/examples/polyline-simplification/)
* [x] Geospatial analysis functionality, adapted from the [Turf](http://turfjs.org/) project
* [x] `RouteUtils` for detecting when a user or marker location becomes off route to help you build navigation apps
* [x] Tidy which cleans noisy GPS traces removing repetitive coordinates from a GeoJSON Feature Collection

**Android:**

* [x] Ready-to-go geocoder input view with autocompletion.
* [x] Drop-in replacement for stock Android Geocoder object.

### Installation

All libraries listed above are available on Maven Central. You could simply:

```
<dependency>
    <groupId>com.mapbox.mapboxsdk</groupId>
    <artifactId>mapbox-java-services</artifactId>
    <version>1.3.1</version>
</dependency>
```

or, in Gradle:

```
compile 'com.mapbox.mapboxsdk:mapbox-java-services:1.3.1'
```

Nightly snapshots are available on Sonatype.

For more details on how to install and use MAS in an application, see the [Mapbox Android Services website](https://www.mapbox.com/android-sdk/#mapbox_android_services).

### Sample code

Check the [Android Test App](https://github.com/mapbox/mapbox-java/tree/master/libandroid/app) for examples or download the [Mapbox Demo App](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo) to see what's possible with Mapbox Android Services. You can also visit the [Mapbox Android SDK examples page](https://www.mapbox.com/android-sdk/examples/) for additional code examples.

### Contributing

All libraries are contained within the `mapbox` folder. You can import the project using Android Studio or IntelliJ IDEA.
