[![](https://raw.githubusercontent.com/mapbox/mapbox-java/master/.github/splash-img.png)](https://www.mapbox.com/android-docs/mapbox-services/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mapbox.mapboxsdk/mapbox-android-services/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mapbox.mapboxsdk/mapbox-android-services) [![CircleCI](https://circleci.com/gh/mapbox/mapbox-java.svg?style=svg)](https://circleci.com/gh/mapbox/mapbox-java)

Mapbox Android Services contains directions, geocoding, and many more APIs to use inside your Android or Java application. This repository holds the source code for the project and is divided into several modules to make it easier for developers to only include the dependencies needed for their project.

## Getting Started

If you are looking to include this inside your project, please take a look at [the detailed instructions](https://www.mapbox.com/android-docs/mapbox-services/) found in our docs. If you are interested in building from source, read the contributing guide inside this project.

## Documentation

You'll find all of the documentation for this SDK on [our Mapbox Services page](https://www.mapbox.com/android-docs/mapbox-services/). This includes information on installation, using the APIs, and links to the API reference.

## Getting Help

- **Need help with your code?**: Look for previous questions on the [#mapbox tag](https://stackoverflow.com/questions/tagged/mapbox+android) — or [ask a new question](https://stackoverflow.com/questions/tagged/mapbox+android).
- **Have a bug to report?** [Open an issue](https://github.com/mapbox/mapbox-java/issues/new). If possible, include the version of Mapbox Services, a full log, and a project that shows the issue.
- **Have a feature request?** [Open an issue](https://github.com/mapbox/mapbox-java/issues/new). Tell us what the feature should do and why you want the feature.

## Using Snapshots

If you want to test recent bugfixes or features that have not been packaged in an official release yet, you can use a `-SNAPSHOT` release of the current development version of Mapbox Services via Gradle, available on [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/com/mapbox/mapboxsdk/). There are several different snapshots built nightly. Feel free to use any of the modules as needed.

```gradle
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-services:2.3.0-SNAPSHOT'
}
```

## Sample code

Check the [Android Test App](https://github.com/mapbox/mapbox-java/tree/master/mapbox/app) for examples or download the [Mapbox Demo App](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo) to see what's possible with Mapbox Android Services. You can also visit the [Mapbox Android examples page](https://www.mapbox.com/android-docs/mapbox-services/examples/) for additional code examples.

## Contributing

All libraries are contained within the `mapbox` folder. You can import the project using Android Studio or IntelliJ IDEA. Read [the contribution guide](https://github.com/mapbox/mapbox-java/blob/master/CONTRIBUTING.md) to get setup properly.
