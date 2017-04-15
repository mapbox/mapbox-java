## Changelog for Mapbox Java and Android Services

Mapbox welcomes participation and contributions from everyone.

### v2.1.0

* Navigation removed from mapbox-java and moved to the [mapbox-navigation-android](https://github.com/mapbox/mapbox-navigation-android) repo [#439](https://github.com/mapbox/mapbox-java/pull/439)
* Introduce support for the Optimized Trip API [#437](https://github.com/mapbox/mapbox-java/pull/437)
* Support for the Distance Matrix has been added, this replaces the Distance API [#418](https://github.com/mapbox/mapbox-java/pull/418)
* Support for direction annotations has been added [#417](https://github.com/mapbox/mapbox-java/pull/417)
* We have deprecated the Distance API [#405](https://github.com/mapbox/mapbox-java/pull/405)
* Added interval, fastest interval, and displacement getters/setters inside the LocationEngine class [#402](https://github.com/mapbox/mapbox-java/pull/402)
* Added setters inside Direction models making it easier to build a DirectionsResponse without needing the call the Directions API [#422](https://github.com/mapbox/mapbox-java/pull/422)
* Added `weight` and `weight_name` APIs to directions response [#438](https://github.com/mapbox/mapbox-java/pull/438)
* Setting both origin/destination and the coordinates inside your directions builder, the coordinate list no longer get's overwritten [#420](https://github.com/mapbox/mapbox-java/pull/420)
* Fixed bug when using Turf point on line, false warnings would occur stating your Position coordinates are outside the recommended range
* Added better format support for coordinates to remove trailing zeros [#424](https://github.com/mapbox/mapbox-java/pull/424)

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
