[![Build Status](https://www.bitrise.io/app/a7eea7d04be1e2e5.svg?token=OruuJNhnjyeRnlBv0wXsFQ&branch=master)](https://www.bitrise.io/app/a7eea7d04be1e2e5)

# Mapbox Android Services (MAS)

Java and Android libraries for Mapbox APIs.

## Installation

We recommend installing this library with Gradle. This will automatically install the necessary dependencies and pull the library binaries from the Mapbox Android repository on Maven Central.

To install the current _release_ version add this to your `build.gradle`:

```

dependencies {
    compile ('com.mapbox.mapboxsdk:mapbox-android-services:1.0.0-SNAPSHOT@aar'){
        transitive=true
    }
}
```

To install the current _SNAPSHOT_ version add this to your `build.gradle`:

```
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

dependencies {
    compile ('com.mapbox.mapboxsdk:mapbox-android-services:1.1.0-SNAPSHOT@aar'){
        transitive=true
    }
}
```

## Sample code

Check the [Test App](https://github.com/mapbox/mapbox-java/tree/master/libandroid/app) for a complete demo app using this library.
