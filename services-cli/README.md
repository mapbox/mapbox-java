# Mapbox Java CLI

This is a wrapper on top of our mapbox-java services. This was made to be a fast
debugging and testing tool, that can be used to verify new and old json contracts.

If you'd like to add a feature, add an issue to the mapbox-java repository
and we can help. Also, pull requests are always welcome!

### Developing

From the command line
1. cd mapbox-java
1. Build with ./gradlew shadowJar, or make build-cli
1. Run with java -jar services-cli/build/libs/services-cli.jar

From Android Studio
1. Open mapbox-java with Android Studio
1. Set MapboxJavaCli as the main configuration
1. Open the runtime configuration and add your "Program arguments"
