<h1 align="center">
  <br>
  <a href="https://www.mapbox.com/android-docs/mapbox-services/overview/"><img src="https://github.com/mapbox/mapbox-java/blob/master/.github/mbxservice-logo.png" alt="Mapbox Service" width="400"></a>
</h1>

<h4 align="center">Build powerful Java apps using Mapbox's tools and services</h4>

<p align="center">
  <a href="https://maven-badges.herokuapp.com/maven-central/com.mapbox.mapboxsdk/mapbox-android-services">
    <img src="https://maven-badges.herokuapp.com/maven-central/com.mapbox.mapboxsdk/mapbox-android-services/badge.svg"
         alt="Maven Central">
  </a>
  <a href="https://circleci.com/gh/mapbox/mapbox-java">
    <img src="https://circleci.com/gh/mapbox/mapbox-java.svg?style=shield&circle-token=:circle-token">
  </a>
  <a href="https://sonarcloud.io/dashboard?id=mapbox-java-services%3Amaster"><img src="https://sonarcloud.io/api/badges/gate?key=mapbox-java-services%3Amaster"/></a>
</p>
<br>

The Mapbox Java SDK contains directions, geocoding, and many more APIs to use inside your Android or Java application. This repository holds the source code for the project and is divided into several modules to make it easier for developers to only include the dependencies needed for their project.

## Getting Started

If you are looking to include this inside your project, please take a look at [the detailed instructions](https://www.mapbox.com/android-docs/java-sdk/overview/#installation) found in our docs. If you are interested in building from source, read the contributing guide inside of this project.

## Documentation

You'll find all of the documentation for this SDK on [our Mapbox Java documentation page](https://www.mapbox.com/android-docs/java-sdk/overview/). This includes information on installation, using the APIs, and links to the API reference.

## Getting Help

- **Need help with your code?**: Look for previous questions on the [#mapbox tag](https://stackoverflow.com/questions/tagged/mapbox+android) — or [ask a new question](https://stackoverflow.com/questions/tagged/mapbox+android).
- **Have a bug to report?** [Open an issue](https://github.com/mapbox/mapbox-java/issues/new). If possible, include the version of Mapbox Java, a full log, and a project that shows the issue.
- **Have a feature request?** [Open an issue](https://github.com/mapbox/mapbox-java/issues/new). Tell us what the feature should do and why you want the feature.

## Using Snapshots

If you want to test recent bugfixes or features that have not been packaged in an official release yet, you can use a `-SNAPSHOT` release of the current development version of the Mapbox Java SDK via Gradle, available on [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/com/mapbox/mapboxsdk/). There are several different snapshots built nightly. Feel free to use any of the modules as needed.

```gradle
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-sdk-services:3.1.0-SNAPSHOT'
}
```

## Sample code

Check the [the Mapbox Java SDK's Android Test App](https://github.com/mapbox/mapbox-java/tree/master/mapbox/app) for examples or download the [Mapbox Demo App](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo) to see what's possible with the Mapbox Java SDK. You can also visit the [Mapbox Android examples page](https://www.mapbox.com/android-docs/java-sdk/examples) for additional code examples.

## Contributing

All libraries are contained within the `mapbox` folder. You can import the project using Android Studio or IntelliJ IDEA. Read [the contribution guide](https://github.com/mapbox/mapbox-java/blob/master/CONTRIBUTING.md) to get setup properly.
