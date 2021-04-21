### RELEASE_VERSION_NUMBER-alpha.1 pre-release checklist 

- [ ] Create a new branch off of `main`.
- [ ] Update the SNAPSHOT version in this repo's `README.md` file
- [ ] Update this repo's `CHANGELOG.md`
- [ ] Change version name to `RELEASE_VERSION_NUMBER-alpha.1` in this repo's `gradle.properties` (make sure to remove `-SNAPSHOT`)
- [ ] [Create a new Github release](https://github.com/mapbox/mapbox-java/releases/new) with a release title of `Mapbox Java SDK vRELEASE_VERSION_NUMBER-alpha.1`, a tag version of `vRELEASE_VERSION_NUMBER-alpha.1`, targeting the release branch you made above in step #1, and checking the `This is a pre-release` box. This will publish artifacts to Bintray. 

<details>
<summary>What to do in Bintray</summary>
After the tag is created, the CI build starts automatically at https://circleci.com/gh/mapbox/mapbox-java and our CircleCI circle.yml file instructs CircleCI to push the artifact to Bintray.

Once CircleCI successfully pushes the artifact files to Bintray, log into Bintray with the Mapbox credentials. Once you're signed in, visit this release numbers's `Files` page for each of the Java SDK modules:

- https://bintray.com/mapbox/mapbox/mapbox-sdk-turf#files/com/mapbox/mapboxsdk/mapbox-sdk-turf

- https://bintray.com/mapbox/mapbox/mapbox-sdk-geojson#files/com/mapbox/mapboxsdk/mapbox-sdk-geojson

- https://bintray.com/mapbox/mapbox/mapbox-sdk-services#files/com/mapbox/mapboxsdk/mapbox-sdk-services

- https://bintray.com/mapbox/mapbox/mapbox-sdk-core#files/com/mapbox/mapboxsdk/mapbox-sdk-core

Verify the attached files for each module, especially the POM which contains the artifact's dependencies. Go to the `Maven Central` tab and click the `Sync` button to sync the artifact with the repository.
</details>

- [ ] Change version name back to having `-SNAPSHOT` at the end


### RELEASE_VERSION_NUMBER-alpha.1 pre-release testing
- [ ] Create a pull request in [`/mapbox-gl-native`](https://github.com/mapbox/mapbox-gl-native) that updates the Mapbox Maps SDK for Android's Java SDK dependency to `RELEASE_VERSION_NUMBER-alpha.1`
- [ ] Create a pull request in [the Mapbox Navigation SDK for Android repo](https://github.com/mapbox/mapbox-navigation-android) that updates the Nav SDK's Java SDK dependency to `RELEASE_VERSION_NUMBER-alpha.1`
- [ ] Report any regressions on this ticket

### Final RELEASE_VERSION_NUMBER release checklist 
- [ ] Create a new branch off of `main`.
- [ ] Update this repo's `README.md` file 
- [ ] Update `CHANGELOG.md`
- [ ] Change version name to `RELEASE_VERSION_NUMBER` in `gradle.properties` (removing `-SNAPSHOT`)
- [ ] [Create a new Github release](https://github.com/mapbox/mapbox-java/releases/new) with a release title of `Mapbox Java SDK vRELEASE_VERSION_NUMBER`, a tag version of `vRELEASE_VERSION_NUMBER`, targeting the final release branch you made above in step #1, and _not_ checking the `This is a pre-release` box. This will publish artifacts to Bintray. 

<details>
<summary>What to do in Bintray</summary>
After the tag is created, the CI build starts automatically at https://circleci.com/gh/mapbox/mapbox-java and our CircleCI circle.yml file instructs CircleCI to push the artifact to Bintray.

Once CircleCI successfully pushes the artifact files to Bintray, log into Bintray with the Mapbox credentials. Once you're signed in, visit this release numbers's `Files` page for each of the Java SDK modules:

- https://bintray.com/mapbox/mapbox/mapbox-sdk-turf#files/com/mapbox/mapboxsdk/mapbox-sdk-turf

- https://bintray.com/mapbox/mapbox/mapbox-sdk-geojson#files/com/mapbox/mapboxsdk/mapbox-sdk-geojson

- https://bintray.com/mapbox/mapbox/mapbox-sdk-services#files/com/mapbox/mapboxsdk/mapbox-sdk-services

- https://bintray.com/mapbox/mapbox/mapbox-sdk-core#files/com/mapbox/mapboxsdk/mapbox-sdk-core

Verify the attached files for each module, especially the POM which contains the artifact's dependencies. Go to the `Maven Central` tab and click the `Sync` button to sync the artifact with the repository.
</details>

- [ ] Change version name back to having `-SNAPSHOT` at the end

### Post final release checklist   
- [ ] Update dependencies in the Maps SDK and Navigation SDK to the final release version
- [ ] Update [the dependencies file in the Mapbox Android Plugins `/mapbox-plugins-android` repo](https://github.com/mapbox/mapbox-plugins-android/blob/master/gradle/dependencies.gradle#L11)
- [ ] Update version # in config files in the `/help` repo
- [ ] Update [the Mapox Android demo app](https://github.com/mapbox/mapbox-android-demo/blob/master/gradle/dependencies.gradle)
- [ ] After updating the demo app, make the following changes in the `/android-docs` repo: 
  - [ ] Update API reference Javadocs for the Mapbox Java docs section in `/android-docs` repo:
  - [ ] Update [the `JAVA_SDK_VERSION` constant in the `constants.js` file in the `/android-docs` repo](https://github.com/mapbox/android-docs/blob/publisher-production/src/constants.json)
- [ ] Update version # in the Studio Preview for Android app
- [ ] Update version # in the China plugin test app

/cc: @mapbox/android 