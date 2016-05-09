[![](https://www.mapbox.com/android-sdk/images/service-splash.png)](https://www.mapbox.com/android-sdk/#mapbox_android_services)

[![Build Status](https://www.bitrise.io/app/a7eea7d04be1e2e5.svg?token=OruuJNhnjyeRnlBv0wXsFQ&branch=master)](https://www.bitrise.io/app/a7eea7d04be1e2e5) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mapbox.mapboxsdk/mapbox-android-services/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mapbox.mapboxsdk/mapbox-android-services)

# Mapbox Android Services (MAS)

<<<<<<< HEAD
An open source toolset for building applications that need directions, geocoding, or static map imagery. Two libraries are at your disposal: a **Java library** compatible with any Java application and an **Android library** intended to be used specifically for Android applications.
=======
To install the current _release_ version add this to your `build.gradle`:

```

dependencies {
    compile ('com.mapbox.mapboxsdk:mapbox-android-services:1.0.0-SNAPSHOT@aar'){
        transitive=true
    }
}
```

To install the current _SNAPSHOT_ version add this to your `build.gradle`:
>>>>>>> master

### Installation
To install and use MAS in an application, see the [Mapbox Android Services website](https://www.mapbox.com/android-sdk/#mapbox_android_services).

<<<<<<< HEAD
### Javadoc
Located within the `gh-pages` branch, you can build locally by running: `bundle exec jekyll serve`. If you prefer to directly view online there are two sections, one for each library:
=======
dependencies {
    compile ('com.mapbox.mapboxsdk:mapbox-android-services:1.1.0-SNAPSHOT@aar'){
        transitive=true
    }
}
```
>>>>>>> master

- [Mapbox Java Services](http://mapbox.github.io/mapbox-java/api/libjava)
- [Mapbox Android Services](http://mapbox.github.io/mapbox-java/api/libandroid)

### Sample code
Check the [Test App](https://github.com/mapbox/mapbox-java/tree/master/libandroid/app) for a complete Android demo app using this library. You can also visit the [Mapbox Android SDK examples page](https://www.mapbox.com/android-sdk/examples/) for additional code examples.
