## Changelog for Mapbox Java and Android Services

Mapbox welcomes participation and contributions from everyone.

### v2.2.3

- Introduce setDebugLoggingEnabled API [#549](https://github.com/mapbox/mapbox-java/pull/549)

### v2.2.2

- Adds setter for session rotation time [#544](https://github.com/mapbox/mapbox-java/pull/544)

### v2.2.1

- Updates release script for CircleCI [#541](https://github.com/mapbox/mapbox-java/pull/541)

### v2.2.0

- Adds ProGuard rules for Optimization and Directions Matrix API [#516](https://github.com/mapbox/mapbox-java/pull/516)
- Adds support for multiple languages in geocoder auto complete widget: [#512}(https://github.com/mapbox/mapbox-java/pull/512)
- Adds support for multiple languages in geocoder API: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Reduced API access in `GeocoderAdapter.java` to non-public since user has no reason for using: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Auto complete widget now extends to Support Library widget: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- `TYPE_DISTRICT` and `TYPE_LOCALITY` result filters added: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Adds `place_type` support in Carman Feature: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Adds `matching_text` support in Carman Feature: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Adds `matching_place_name` support in Carman Feature: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Adds `language` support in Carman Feature: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Several Javadoc improvements: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Updated fixtures and test: [#512](https://github.com/mapbox/mapbox-java/pull/512)
- Adds exception and test for Turf methods which require at least 2 coordinates: [#513](https://github.com/mapbox/mapbox-java/pull/513)
- Resolves Fatal Exception: java.lang.OutOfMemoryError with the TelemetryClient: [#467](https://github.com/mapbox/mapbox-java/issues/467)
- Fixes Optimization API `getRadius()` method which wasn't being converted to a string: [#506](https://github.com/mapbox/mapbox-java/pull/506)
- Fixes Optimization API `getBearing()` method which wasn't being converted to a string: [#506](https://github.com/mapbox/mapbox-java/pull/506)
- Adds support for Optimization API language parameter: [#506](https://github.com/mapbox/mapbox-java/pull/506)
- Adds support for Optimization API distributions parameter: [#506](https://github.com/mapbox/mapbox-java/pull/506)
- Adds missing Optimization API `waypoint_index` inside the OptimizationWaypoint class: [#506](https://github.com/mapbox/mapbox-java/pull/506)
- Deprecates Optimization API `annotation` in favor of the correct name `annotations`: [#506](https://github.com/mapbox/mapbox-java/pull/506)
- Adds directions API support for languages [#504](https://github.com/mapbox/mapbox-java/pull/504)
- Adds directions API support for congestions annotation: [#469](https://github.com/mapbox/mapbox-java/pull/469)
- Adds `alternatives_count` to MapMatchingTraceoint class: [#507](https://github.com/mapbox/mapbox-java/pull/507)
- Adds support for the language param in MapMatching API: [#507](https://github.com/mapbox/mapbox-java/pull/507)
- Mock LocationEngine exposed in `libandroid-services`: [#476](https://github.com/mapbox/mapbox-java/pull/476)
- Adds Convert distance methods in TurfHelper class: [#476](https://github.com/mapbox/mapbox-java/pull/481)

### v2.1.3

* Downgrade LOST to v1.1.1 to address stability issues [#493](https://github.com/mapbox/mapbox-java/pull/493)

### v2.1.2

* Update to LOST `3.0.1` which fixes a bug where location requests were too aggressive [#486](https://github.com/mapbox/mapbox-java/pull/486)
* Expose `MockLocationEngine` as part of the public API [#476](https://github.com/mapbox/mapbox-java/pull/476)
* Update style versions to `v10` and add traffic styles [#478](https://github.com/mapbox/mapbox-java/pull/478)
* Fix static marker annotation URL generation [#479](https://github.com/mapbox/mapbox-java/pull/479)
* Add convert distance method to `TurfHelper` [#481](https://github.com/mapbox/mapbox-java/pull/481)
* Add new SSL certs for 2018/2019 [#484](https://github.com/mapbox/mapbox-java/pull/484)

### v2.1.1

* Updates to [LOST `3.0.0`](https://github.com/mapbox/mapbox-java/pull/462)
* Fixed location [timestamp issue](https://github.com/mapbox/mapbox-java/pull/463)
* Updated [library dependencies](https://github.com/mapbox/mapbox-java/pull/464)
* correct [units for `LegAnnotation.speed`](https://github.com/mapbox/mapbox-java/pull/460)

### v2.1.0

* The Mapbox Navigation SDK for Android has been moved into its own repository. Code and tickets can now be followed on [mapbox-navigation-android](https://github.com/mapbox/mapbox-navigation-android) [#439](https://github.com/mapbox/mapbox-java/pull/439)
* Introduced support for the [Optimized Trips API](https://www.mapbox.com/api-documentation/#optimized-trips) [#437](https://github.com/mapbox/mapbox-java/pull/437)
* Added support for the [Directions Matrix API](https://www.mapbox.com/api-documentation/#directions-matrix). This replaces the Distance API [#418](https://github.com/mapbox/mapbox-java/pull/418), which is now deprectated [#405](https://github.com/mapbox/mapbox-java/pull/405)
* Added support for direction [annotations](https://github.com/mapbox/mapbox-java/blob/master/mapbox/libjava-services/src/main/java/com/mapbox/services/api/directions/v5/MapboxDirections.java#L345-L358) [#417](https://github.com/mapbox/mapbox-java/pull/417)
* Added `setInterval`, `setFastestInterval` and `setSmallestDisplacement` to allow configurable [LocationEngine](https://github.com/mapbox/mapbox-java/blob/master/mapbox/libandroid-telemetry/src/main/java/com/mapbox/services/android/telemetry/location/LocationEngine.java) parameters [#402](https://github.com/mapbox/mapbox-java/pull/402)
* Added setters in `DirectionsResponse` to simplify object creation [#422](https://github.com/mapbox/mapbox-java/pull/422)
* Added `weight` and `weight_name` support to the Directions API response [#438](https://github.com/mapbox/mapbox-java/pull/438)
* Fixed issue where some coordinates were overwritten when setting all origin, destination and a coordinates list inside the directions builder [#420](https://github.com/mapbox/mapbox-java/pull/420)
* Fixed bug when using `TurfMisc.pointOnLine()` where incorrect warnings would state your `Position` values were outside the recommended range [#416](https://github.com/mapbox/mapbox-java/pull/416)
* Enhanced coordinates formatting to remove trailing zeros [#424](https://github.com/mapbox/mapbox-java/pull/424)

### v2.0.0 - March 17th, 2017

* Mapbox Navigation SDK [#346](https://github.com/mapbox/mapbox-java/pull/346)
* Introduce support for Mapbox Distance API [#219](https://github.com/mapbox/mapbox-java/pull/219)
* Update Map Matching API to support new `v5`
* Divide library into smaller modules to reduce method count on Android projects [#251](https://github.com/mapbox/mapbox-java/pull/251)
* AppEngine support [#344](https://github.com/mapbox/mapbox-java/pull/344)
* Introduce support for Mapbox Telemetry [#282](https://github.com/mapbox/mapbox-java/pull/282)
  * Added reusable components for permission and connectivity management [#276](https://github.com/mapbox/mapbox-java/pull/276)
  * Abstracted the location engine from a specific provider [#269](https://github.com/mapbox/mapbox-java/pull/269) and provide default implementations [#320](https://github.com/mapbox/mapbox-java/pull/320)
* Shared improvements across clients
  * RxJava support [#304](https://github.com/mapbox/mapbox-java/pull/304)
  * Enabled setting a base URL to facilitate testing [#245](https://github.com/mapbox/mapbox-java/pull/245)
* Directions improvements:
  * Added direction v5 lanes [#214](https://github.com/mapbox/mapbox-java/pull/214)
  * Added support for the `driving-traffic` profile [#292](https://github.com/mapbox/mapbox-java/pull/292)
  * Added support for polyline6  [#287](https://github.com/mapbox/mapbox-java/pull/287)
  * Added rotary convenience methods [#329](https://github.com/mapbox/mapbox-java/pull/329)
  * Added bearing query parameter [#337](https://github.com/mapbox/mapbox-java/pull/337)
  * Remove support for `v4` [#260](https://github.com/mapbox/mapbox-java/pull/260)
* Geocoding improvements:
  * Batch requests support [#267](https://github.com/mapbox/mapbox-java/pull/267)
  * Added landmark, limit, country parameters support [#209](https://github.com/mapbox/mapbox-java/pull/209) [#217](https://github.com/mapbox/mapbox-java/pull/217)
  * Added cancel method [#226](https://github.com/mapbox/mapbox-java/pull/226), setting multiple types [#230](https://github.com/mapbox/mapbox-java/pull/230), and language, base URL method setting to autocomplete widget [#315](https://github.com/mapbox/mapbox-java/pull/315) [#328](https://github.com/mapbox/mapbox-java/pull/328)

### v2.0.0-beta.3

* Make `ServicesException` and `TurfException` unchecked exceptions

### v2.0.0-beta.2

* Mapbox Navigation SDK
* Bearing query parameter and rotary convenience methods added to the Directions API
* Base URL method setting added to geocoder autocomplete widget
* AppEngine support

### v2.0.0-beta.1

* Introduce support for Mapbox Distance API
* Update Map Matching API to support new `v5`
* Divide library into smaller modules to reduce method count on Android projects (`libjava-core`, `libjava-geojson`, `libjava-services`, `libjava-services-rx`, `libandroid-telemetry`, `libandroid-services`, `libandroid-ui`)
* Introduce support for Mapbox Telemetry
  * Added reusable components for permission and connectivity management
  * Abstracted the location engine from a specific provider and provide default implementations
* Shared improvements across clients:
  * RxJava support
  * Enabled setting a base URL to facilitate testing
* Directions improvements:
  * Added direction v5 lanes
  * Added support for the `driving-traffic` profile
  * Added support for polyline6
  * Remove support for `v4`
* Geocoding improvements:
  * Batch requests support
  * Added landmark, limit, country parameters support
  * Added cancel method, setting multiple types, and language method setting to autocomplete widget

### v1.3.2

* Geocoding: added poi.landmark type

### v1.3.1

* GeoJson: Feature properties are now properly serialized and raw array tests have been added
* Introduce checkstyle to the project

### v1.3.0

* `RouteUtils` class to simplify work with `RouteLeg` objects
* `geojson-tidy` integrated with Map Matching client
* Added Turf's `midpoint`, `along`, and `within` methods
* Added an example to showcase Turf's `lineslice`
* Geocoding widget now supports the `bbox` parameter

### v1.2.1

* Bring back `toString()` method in `CarmenFeature`
* Fixes regresion in `GeocoderAutoCompleteView`

Full changelog: https://github.com/mapbox/mapbox-java/issues?q=milestone%3Av1.2.1+is%3Aclosed

### v1.2.0

* Support for the Map Matching API
* Improved Geocoder API (including support for `wikidata` and `bbox`)
* Better Static Image API
* Some [Turf](http://turfjs.org/) methods ported to Java: `TurfMeasurement.bearing`, `TurfMeasurement.destination`, `TurfMeasurement.distance`, `TurfMeasurement.lineDistance`, `TurfHelpers.distanceToRadians`, `TurfHelpers.radiansToDistance`, `TurfMisc.lineSlice`
* [`simplify-js`](https://github.com/mourner/simplify-js) ported to Java
* Updated to `libjava` v1.2.0
* Several examples added to the TestApp
* Improved documentation

Full changelog: https://github.com/mapbox/mapbox-java/issues?q=milestone%3Av1.2.0+is%3Aclosed

### v1.1.0

* Support for directions API v5
* Support for Maki 3 and directions icons in Test App
* Fixed a bug where locale was different from `Locale.US`
* Updated to `libjava` v1.1.0
* Removed extra dependencies to reduce method count

Full changelog: https://github.com/mapbox/mapbox-java/issues?q=milestone%3Av1.1.0+is%3Aclosed

### v1.0.0

* Initial release: Java module with no Android dependencies
* Support for Geocoding API v5
* Support for Directions API v4 and v5
* Support for Static API v1
* RxJava support
* Support for GeoJSON objects
* Polyline encoder and decoder
* Initial release
* Full replacement for the stock `AndroidGeocoder`
* Geocoder autocomplete widget based on Android's `AutoCompleteTextView`
* Utility class to handle the new permission system in 6.0
* TestApp with sample code for directions v4 and v5, geocoding (reverse, widget, service), and static image with Picasso
