## Changelog for the Mapbox Java SDK

Mapbox welcomes participation and contributions from everyone.

### main

### v7.4.0 - April 11, 2025

- Added `IntersectionLanes#access` property which provides lane access attributes, such as allowed vehicle types for designated lanes. [#1608](https://github.com/mapbox/mapbox-java/pull/1608)


### v7.3.2 - March 13, 2025

- Updated `auto-value-gson` to version [0.0.2](https://github.com/mapbox/auto-value-gson/releases/tag/mapbox-v0.0.2). [#1605](https://github.com/mapbox/mapbox-java/pull/1605)


### v7.3.1 - October 15, 2024
- Fixed malformed URL that resulted from previous inclusion of `contours_meters` as an Isochrone option. [#1599](https://github.com/mapbox/mapbox-java/pull/1599)

- Fixed an issue where Isochrone API's `contours_meters` required at least two input values, instead of only one.


### v7.3.0 - October 04, 2024

- Updated IsoChrone API to support new query parameters including contours_meters, road/route exclusions and departure time. [#1596](https://github.com/mapbox/mapbox-java/pull/1596)
- Bumped `okhttp` version to `4.10.0`. [#1595](https://github.com/mapbox/mapbox-java/pull/1595)


### v7.2.0 - August 28, 2024

- Added `PaymentMethods#etc2` payment method.
- Added `V6StructuredInputQuery#country` option.


### v7.1.0 - July 8, 2024

- Optimize memory usage in Directions API model classes by interning frequently occurring strings in JSON


### v7.0.0 - April 23, 2024

- Added Geocoding V6 Support.


### v6.15.0 - November 13, 2023

- Added `DirectionsCriteria#PAYMENT_METHOD_ETC2` option to pass as `RouteOptions#computeTollCosts` and to receive in `IntersectionsLanes#paymentMethods`.


### v6.14.0 - October 26, 2023

- Added `LegAnnotation#freeflowSpeed` and `LegAnnotation#currentSpeed`. To receive these annotations, pass `DirectionsCriteria.ANNOTATION_FREEFLOW_SPEED` and `DirectionsCriteria.ANNOTATION_CURRENT_SPEED` in the request in the "annotations" parameter.

### v6.13.0 - July 31, 2023
- No additional changes

### v6.13.0-beta.2 - July 28, 2023
- Added `Incident#trafficCodes`.


### v6.13.0-beta.1 - July 11, 2023

- Added `StepIntersection.mergingArea`.


### v6.12.0 - May 30, 2023

- No additional changes

### v6.12.0-beta.1 - May 25, 2023
- Added `DirectionsResponse.fromJson(Reader)` and `DirectionsResponse.fromJson(Reader, RouteOptions)` to support streaming serialisation of `DirectionsResponse`.


### v6.11.0 - March 03, 2023
- No additional changes

### v6.11.0-beta.1 - February 03, 2023
- No additional changes


### v.6.10.0 - January 19, 2023
- No additional changes



### v6.10.0-beta.4 - December 02, 2022
- Added "routable points" support for `services-geocoding` [#1522](https://github.com/mapbox/mapbox-java/pull/1522)


### v6.10.0-beta.3 - November 30, 2022
- Specified supported amenity types via `DirectionsCriteria#AmenityTypeCriteria`. [#1515](https://github.com/mapbox/mapbox-java/pull/1515)
- Fixed an issue where the `OptimizationResponse#trips#routeIndex` and `OptimizationResponse#trips#routeOptions` weren't correctly assigned. [#1517](https://github.com/mapbox/mapbox-java/pull/1517)


### v6.10.0-beta.2 - November 11, 2022
- Added `paymentMethods` parameter for `RouteOptions` and `IntersectionLanes`. [#1449](https://github.com/mapbox/mapbox-java/pull/1449)


### v6.10.0-beta.1 - November 09, 2022
- Added `RouteOptions#waypointsPerRoute()` and `RouteOption.Builders#waypointsPerRoute()` methods. [#1509](https://github.com/mapbox/mapbox-java/pull/1509)
- Added `DirectionsRoute#waypoints`. [#1509](https://github.com/mapbox/mapbox-java/pull/1509)


### v6.9.0 - November 09, 2022
- Removed `RouteOptions#waypointsPerRoute()` and `RouteOption.Builders#waypointsPerRoute()` methods. [#1506](https://github.com/mapbox/mapbox-java/pull/1506)
- Removed `DirectionsRoute#waypoints` and made non-deprecated `DirectionsResponse#waypoints`. [#1506](https://github.com/mapbox/mapbox-java/pull/1506)


### v6.9.0-beta.2 - November 03, 2022
- Added `RouteOptions#waypointsPerRoute()` and `RouteOption.Builders#waypointsPerRoute()` methods. [#1503](https://github.com/mapbox/mapbox-java/pull/1503)
- Added `DirectionsRoute#waypoints` and deprecated `DirectionsResponse#waypoints`. [#1503](https://github.com/mapbox/mapbox-java/pull/1503)


### v6.9.0-beta.1 - October 21, 2022
- Added `getUnrecognizedJsonProperties()` method to `DirectionsRefreshJsonObject` so that unrecognized properties can be received from refresh response. [#1500](https://github.com/mapbox/mapbox-java/pull/1500)
- Added `DirectionsRefreshJsonObject.Builder#unrecognizedJsonProperties`. [#1500](https://github.com/mapbox/mapbox-java/pull/1500)
- Added `getUnrecognizedJsonProperties()` method to `DirectionsJsonObject` so that a map of unrecognized properties can be received from route response. [#1500](https://github.com/mapbox/mapbox-java/pull/1500)
- Added `DirectionsJsonObject.Builder#unrecognizedJsonProperties`. [#1500](https://github.com/mapbox/mapbox-java/pull/1500)


### v6.8.0 - September 29, 2022
- Replaced `TurfSimplify#simplify` with `TurfTransformation#simplify`. [#1496](https://github.com/mapbox/mapbox-java/pull/1496)
- Deprecated `PolylineUtils#simplify` in favour of `TurfTransformation#simplify`. [#1496](https://github.com/mapbox/mapbox-java/pull/1496)

### v6.8.0-beta.4 - September 5, 2022
- Added `TurfSimplify#simplify` method to simplify `LineString` using Ramer-Douglas-Peucker algorithm. [#1486](https://github.com/mapbox/mapbox-java/pull/1486)
- Added `List<Closure>` to `RouteLegRefresh` to make `DirectionsRoute#RouteLeg` refreshed with closures. [#1489](https://github.com/mapbox/mapbox-java/pull/1489)

### v6.8.0-beta.3 - September 1, 2022
- Added `subTypes` to `BannerComponents`. [#1485](https://github.com/mapbox/mapbox-java/pull/1485)

### v6.8.0-beta.2 - August 17, 2022
- Added traffic tendency annotations. [#1479](https://github.com/mapbox/mapbox-java/pull/1479)

### v6.8.0-beta.1 - August 12, 2022
- Added support for `RestStop#guideMap` associated with the `RestStop`. [#1471](https://github.com/mapbox/mapbox-java/pull/1471)
- Fixed an issue where the `RouteOptions#...List` parameters were not reset correctly (for example, `routeOptions.toBuilder().bearingsList(null).build()` did not reset bearings to null). [#1469](https://github.com/mapbox/mapbox-java/pull/1469)
- Added `RouteOptions.snappingIncludeStaticClosures` and `RouteOptions.snappingIncludeStaticClosuresList`. [#1469](https://github.com/mapbox/mapbox-java/pull/1469) 
- Added `RouteOptions#computeTollCost` request parameter that when set to `true` can return `DirectionsRoute#tollCosts`, if data is available. [#1458](https://github.com/mapbox/mapbox-java/pull/1458)

### v6.7.0 - August 12, 2022
- No additional changes


### v6.7.0-beta.1 - Jul 22, 2022
- Added `Incident.affectedRoadNames`. [#1457](https://github.com/mapbox/mapbox-java/pull/1457)
- Added `StepIntersections.trafficSignal`,`StepIntersections.stopSign` and `StepIntersections.yieldSign`. [#1464](https://github.com/mapbox/mapbox-java/pull/1464)
- Fixed an issue where the `MapMatchingMathcing#routeIndex` wasn't correctly assigned. [#1463](https://github.com/mapbox/mapbox-java/pull/1463)

### v6.6.0 - Jun 30, 2022
- Fixed `RouteOptions#toUrl` for a case when `RouteOptions` was deserialized from a json generated by an old version of mapbox-java. [#1447](https://github.com/mapbox/mapbox-java/pull/1447)
- Added support for `Amenities` by exposing them as a part of `RestStop`. [#1454](https://github.com/mapbox/mapbox-java/pull/1454)

### v6.5.0 - May 25, 2022
- No additional changes


### v6.5.0-beta.6 - May 17, 2022
- Added `max_weight` field to `RouteOptions` to provide max vehicle weight, metric tons. [#1439](https://github.com/mapbox/mapbox-java/pull/1439)

### v6.5.0-beta.5 - May 11, 2022
- Added `TollCollection#name` field which contains a name of the toll booth/gantry, when available. [#1432](https://github.com/mapbox/mapbox-java/pull/1432)
- Improved `RouteOptions#toUrl` query parameters encoding. This (in between other improvements) adds handling for rarer unescaped characters occurrences (like '&' baked within other string). [#1433](https://github.com/mapbox/mapbox-java/pull/1433)
- Added `Incidents` to `RouteLegRefresh` which allow to refresh incidents along a route. [#1421](https://github.com/mapbox/mapbox-java/pull/1421)

### v6.5.0-beta.4 - May 5, 2022
- Added `RestStop#name` field which contains a name of the service/rest area, when available. [#1428](https://github.com/mapbox/mapbox-java/pull/1428)

### v6.5.0-beta.3 - April 27, 2022
- Added `ignore` parameter to Map Matching API. [#1424](https://github.com/mapbox/mapbox-java/pull/1424)
- Added roundtrip of unrecognized JSON properties. [#1394](https://github.com/mapbox/mapbox-java/pull/1394)
- Added API to access unrecognized JSON properties. See `#getUnrecognizedProperty` and `#getUnrecognizedPropertiesNames` in each Directions API model object.  [#1416](https://github.com/mapbox/mapbox-java/pull/1416)
- Added `RouteOptions.Builder#unrecognizedProperties` to set custom request parameters to a URL generated by `RouteOptions#toUrl` only. The custom request parameters are not supported by `MapboxDirections` service. [#1420](https://github.com/mapbox/mapbox-java/pull/1420)
- Removed experimental EV routing request/response parameters. Please use the `RouteOptions.Builder#unrecognizedProperties` to set experimental request parameters, and `#getUnrecognizedProperty` to get values from relevant response objects. EV routing parameters documentation is available [here](https://docs.mapbox.com/api/navigation/directions/#electric-vehicle-routing). [#1422](https://github.com/mapbox/mapbox-java/pull/1422)

#### Known issues
- `RouteOptions#toUrl` might still produce invalid URLs (and fail a request or later crash on `RouteOptions#fromUrl`) if query parameters contain `&` chars that do not exactly match the `<white space>&<white space>` pattern. Only this specific pattern is escaped correctly.

### v6.4.1 - April 22, 2022
- Added correct escaping for `<white space>&<white space>` pattern in query parameters for `RouteOptions#toUrl`. [#1410](https://github.com/mapbox/mapbox-java/pull/1410).

#### Known issues
- `RouteOptions#toUrl` might still produce invalid URLs (and fail a request or later crash on `RouteOptions#fromUrl`) if query parameters contain `&` chars that do not exactly match the `<white space>&<white space>` pattern. Only this specific pattern is escaped correctly.

### v6.5.0-beta.2 - April 22, 2022
- Added correct escaping for `<white space>&<white space>` pattern in query parameters for `RouteOptions#toUrl`. [#1410](https://github.com/mapbox/mapbox-java/pull/1410).

#### Known issues
- `RouteOptions#toUrl` might still produce invalid URLs (and fail a request or later crash on `RouteOptions#fromUrl`) if query parameters contain `&` chars that do not exactly match the `<white space>&<white space>` pattern. Only this specific pattern is escaped correctly.

### v6.5.0-beta.1 - April 14, 2022
- Added experimental EV routing request/response parameters. These params will only be part of pre-releases (removed from final versions) until refactored into stable APIs in the future.[#1405](https://github.com/mapbox/mapbox-java/pull/1405)

### v6.4.0 - April 14, 2022
- Removed experimental EV routing request/response parameters. These params will only be part of pre-releases until refactored into stable APIs in the future.[#1395](https://github.com/mapbox/mapbox-java/pull/1395)

### v6.4.0-beta.4 - March 30, 2022
- Added route index to `MapMatchingMatching`. [#1385](https://github.com/mapbox/mapbox-java/pull/1385)

### v6.4.0-beta.3 - March 25, 2022
- Fixed an issue where `RouteOptions#fromUrl` didn't deserialize the port number. [#1382](https://github.com/mapbox/mapbox-java/pull/1382)

### v6.4.0-beta.2 - March 10, 2022
- :warning: Fixed an issue where `RouteOptions#avoidManeuverRadius` field was represented as integer instead of double. This change is breaking but necessary to correctly represent the Directions API structure. [#1376](https://github.com/mapbox/mapbox-java/pull/1376)
- Added `railwayCrossing` field to `StepIntersection` which indicates whether there is a railway crossing at the intersection. [#1375](https://github.com/mapbox/mapbox-java/pull/1375)
- Added ability to handle encoded coordinates in `RouteOptions#toUrl`. [#1377](https://github.com/mapbox/mapbox-java/pull/1377)

### v6.4.0-beta.1 - February 22, 2022
- Exposed `viaWaypoints` in `RouteLeg` [#1364](https://github.com/mapbox/mapbox-java/pull/1364)
- Added `RouteOptions#excludeObject` for providing points that should be avoided in the returned route. [#1362](https://github.com/mapbox/mapbox-java/pull/1362)
- Updated nullability of `DirectionsWaypoint` properties to reflect the API structure. [#1360](https://github.com/mapbox/mapbox-java/pull/1360)
- Added `DirectionsWaypoint#distance` property. [#1360](https://github.com/mapbox/mapbox-java/pull/1360)
- Added experimental EV routing request/response parameters. These change will only be part of pre-releases (removed from final versions) until refactored into stable APIs in the future. [#1365](https://github.com/mapbox/mapbox-java/pull/1365)

### v6.3.0 - February 22, 2022
- No additional changes.

### v6.3.0-beta.1 - January 28, 2022
- Exposed [turf.lineIntersect](https://turfjs.org/docs/#lineIntersect) via `TurfMisc#lineIntersect` using an algorithm with O(nm) time complexity which should suite small to medium sized geometries until a more performant solution is implemented. [#1348](https://github.com/mapbox/mapbox-java/pull/1348)
- Fixed an issue where ends of lines where not considered as intersection points. [#1348](https://github.com/mapbox/mapbox-java/pull/1348)

### v6.2.0 - January 20, 2022
- No additional changes.

### v6.2.0-beta.2 - November 30, 2021
- Exposed `ShieldSprites`, `ShieldSprite` and `ShieldSpriteAttribute` in support for mapbox designed route shields. [#1336](https://github.com/mapbox/mapbox-java/pull/1336)
- Added a fix so that no forbidden characters are found in the encoded URL. [#1337](https://github.com/mapbox/mapbox-java/pull/1337)
- Removed optimization coordinates limit check. [#1339](https://github.com/mapbox/mapbox-java/pull/1339/)

### v6.2.0-beta.1 - November 18, 2021
- Added `EXCLUDE_CASH_ONLY_TOLLS`([#1309](https://github.com/mapbox/mapbox-java/issues/1309)) and `EXCLUDE_UNPAVED`([#1308](https://github.com/mapbox/mapbox-java/issues/1308)) options to `DirectionsCriteria`.
- Exposed `DirectionsRoute.Builder#routeIndex` to allow construction when route object is not generated by `mapbox-java` library. [#1328](https://github.com/mapbox/mapbox-java/pull/1328)
- Added data models for `MapboxShield`. [#1330](https://github.com/mapbox/mapbox-java/pull/1330/)

### v6.1.0 - November 11, 2021
- Added `avoid_maneuver_radius` to Directions API params and `avoidManeuverRadius` to `RouteOptions`. [#1310](https://github.com/mapbox/mapbox-java/pull/1310)
- Added encoding for parameters transformed with `RouteOptions.toUrl`. [#1317](https://github.com/mapbox/mapbox-java/pull/1317)

### v6.0.1 - November 04, 2021
- Refactoring related to URL building for `RouteOptions.toUrl`. [#1313](https://github.com/mapbox/mapbox-java/pull/1314)

### v6.0.0 - October 13, 2021
- No additional changes.

### v6.0.0-alpha.9 - September 29, 2021
- Fixed `RouteOptions#toUrl()` function. [#1302](https://github.com/mapbox/mapbox-java/pull/1302)

### v6.0.0-alpha.8 - September 28, 2021
- Exposed `RouteOptions#toUrl()` function.

### v6.0.0-alpha.7 - September 23, 2021
 - Exposed include-hov/hot parameters and added exclude list option [#1296](https://github.com/mapbox/mapbox-java/pull/1296)

### v6.0.0-alpha.6 - September 16, 2021
 - Added support for `active_direction` encapsulated in lane banner component. [#1294](https://github.com/mapbox/mapbox-java/pull/1294)

### v6.0.0-alpha.5 - September 1, 2021
 - Added support for layers to `RouteOptions` object. [#1279](https://github.com/mapbox/mapbox-java/pull/1279)
 - Added support for missing country code and lanes blocked properties to `Incident` object. [#1288](https://github.com/mapbox/mapbox-java/pull/1288)
 - Added support for missing congestion numeric property to `LegAnnotation` object. [#1290](https://github.com/mapbox/mapbox-java/pull/1290)

### v6.0.0-alpha.4 - August 26, 2021
 - Added support for `metadata` response from Directions API. [#1282](https://github.com/mapbox/mapbox-java/pull/1282)
 - Added `max_width` and `max_height` to Directions API params. [#1283](https://github.com/mapbox/mapbox-java/pull/1283)

### v6.0.0-alpha.3 - August 19, 2021
- Bumped `okhttp` version to `4.9.0`. It requires java 1.8 for compatibility. [#1271](https://github.com/mapbox/mapbox-java/pull/1271)

### v6.0.0-alpha.2 - July 28, 2021
- Removed `accessToken` from `RouteOptions` and moved it to `MapboxDirections` instead. This simplifies serialization and deserialization of route objects without potential leaks of data under the assumption that an entity that makes a route request has an ability to provide an access token without the route object having to transport it. [#1276](https://github.com/mapbox/mapbox-java/pull/1276)

### v6.0.0-alpha.1 - July 20, 2021
- Refactored `MapboxDirections` to accept `RouteOptions` as an argument that defines request parameters [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Moved the `requestUuid` object out of `RouteOptions` and into `DirectionsRoute` since the UUID is not a request parameter, it's a response value [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Added `enableRefresh` to `RouteOptions` since it is a route request parameter [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Removed a separate `WalkingOptions` in favor of storing the parameter directly in the `RouteOptions` [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Exposed `arriveBy` and `departAt` request parameters [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Exposed `ANNOTATION_CONGESTION_NUMERIC` parameter [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Updated documentation for various route request parameters [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Exposed `DirectionsResponse#fromJson(json, route options, request uuid)` that allows developers to provide information about the original route request which response they deserialize so that Nav SDK can operate correctly [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Exposed `DirectionsRoute#fromJson(json, route options, request uuid)` for same reason as above [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Exposed `RouteOptions#fromUrl` utility function that creates the object from the request URL which might be useful when paired with the above functions [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Added default values for `RouteOptions`: base URL, user, and geometry encoding (polyline6) [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Added a List overload in `RouteOptions` for coordinates, now there are `RouteOptions#coordinates(string)` and `RouteOptions#coordinatesList(list)`. ⚠️ If there are routes that were previously serialized with toJson methods and contained `RouteOptions`, they will not be possible to deserialize anymore since coordinates are now stored as a string and not an array. This breaking change can typically impact test fixtures [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Fixed an issue where coordinates were rounded to 6 decimal places instead of 7 decimal places that the `GeoJSON` specification expects [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Changed the default encoding for MapMatching to be polyline6 and aligns it with regular route requests [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Added a `Bearing` object that represents a pair of values for {angle, degrees} [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Removed route request parameters verification in favor of letting the service respond with a correct error [#1265](https://github.com/mapbox/mapbox-java/pull/1265)
- Stopped serializing access tokens [#1267](https://github.com/mapbox/mapbox-java/pull/1267)
- Replaced `Closure` `@Nullable` annotations with `@NonNull` [#1268](https://github.com/mapbox/mapbox-java/pull/1268)

### 5.9.0-alpha.5 - April 9, 2021
- Exposed a method to pass a single boolean snapping closure parameter [#1253](https://github.com/mapbox/mapbox-java/pull/1253)

### 5.9.0-alpha.4 - March 31, 2021
- Added support for route closures coming from the directions API [#1250](https://github.com/mapbox/mapbox-java/pull/1250)

### 5.9.0-alpha.3 - March 24, 2021
 - Fixed added SnappingClosures to response options [#1232](https://github.com/mapbox/mapbox-java/pull/1243)

### 5.9.0-alpha.2 - March 23, 2021
 - Added to RouteOptions SnappingClosures [#1232](https://github.com/mapbox/mapbox-java/pull/1232)
 - Stopped releasing artifacts to jfrog and now to Mapbox's sdk-registry

### 5.9.0-alpha-1 - February 12, 2021
- Fixed `DirectionsRefreshResponse` flow: introduced `DirectionsRouteRefresh` and restricted dependency on `DirectionsRoute` which is a critical bug fixes. Even though this is a semver-breaking change, we're not considering it as one because the refresh feature was non-functional beforehand. [#1225](https://github.com/mapbox/mapbox-java/pull/1225)

### 5.8.0 - January 20, 2021
- No additional changes.

### 5.8.0-beta.5 - December 21, 2020
- Add `BannerComponentSubType` to `BannerComponents` [#1222](https://github.com/mapbox/mapbox-java/pull/1222)

### 5.8.0-beta.4 - November 23, 2020
- Expose `active` and `validIndications` to `IntersectionLanes` [#1217](https://github.com/mapbox/mapbox-java/pull/1217)

### 5.8.0-beta.3 - October 23, 2020

- Add `speedLimitUnit` and `speedLimitSign` to `LegStep` to  [#1204](https://github.com/mapbox/mapbox-java/pull/1204)

### 5.8.0-beta.2 - October 21, 2020

- Move `Incident` to `RouteLeg` [#1207](https://github.com/mapbox/mapbox-java/pull/1207)

### 5.8.0-beta.1 - October 9, 2020

- Add test `services-cli` module for json schema testing [#1198](https://github.com/mapbox/mapbox-java/pull/1198)
- Added `Incident` to `LegStep` [#1201](https://github.com/mapbox/mapbox-java/pull/1201)

### 5.7.0 - October 1, 2020

- Adjust javadoc info about Turf circle methods [#1192](https://github.com/mapbox/mapbox-java/pull/1192)
- Bump retrofit to 2.7.2 to be android API 30 compatible [#1194](https://github.com/mapbox/mapbox-java/pull/1194)
- Add `tunnelName` field to `StepIntersection` [#1197](https://github.com/mapbox/mapbox-java/pull/1197)

### 5.7.0-beta - September 23, 2020
- Exposed border crossings, rest stops and toll collection points [#1188](https://github.com/mapbox/mapbox-java/pull/1188)

### 5.6.0 - September 16, 2020

Add fields to `StepIntersection`:
- `geometryIndex` [#1181](https://github.com/mapbox/mapbox-java/pull/1181)
- `isUrban`, `MapboxStreetsV8` [#1182](https://github.com/mapbox/mapbox-java/pull/1182)

### 5.5.0 - September 3, 2020

- Update VERSION_NAME: 5.4.1-SNAPSHOT -> 5.5.0-SNAPSHOT ((#1173)
- DirectionRoute: added `durationTypical` for Route, Leg and Step; VERSION_NAME: updated to `5.4.1-SNAPSHOT` (#1172)
- Remove min/max latitude/longitude range annotations (#1168)
- Update annotation for NonNull attributes. (#1087)

### 5.4.1 - July 29, 2020

- [geocoding] Correct handling of a single element batch geocoding response (#1160)

### 5.3.0 - June 15, 2020

- [turf] Ported TurfMeasurement.center() method (#1150)
- [geojson] move gson package to com.mapbox.geojson.internal (#1153)

### 5.2.1 - May 13, 2020

- Update maneuver modifier type to be public (#1146)

### 5.2.0 - May 12, 2020

- Add step maneuver modifier constants (#1142)
- Refactor turf measurement functions (#1139)
- remove stale bot settings for this repo (#1138)
- Fix typo in DirectionsJsonObject documentation (#1137)

### 5.1.0 - March 30, 2020

- Adding flexibility to Matrix API builder to set custom max coordinate list size limit [#1133](https://github.com/mapbox/mapbox-java/pull/1133)

### 5.1.0-beta.2 - March 23, 2020

- Updated SNAPSHOT link in README [#1126](https://github.com/mapbox/mapbox-java/pull/1126)
- Use Strings instead of Lists to store data inside RouteOptions [#1123](https://github.com/mapbox/mapbox-java/pull/1123)
- Bearing formatting fix [#1121](https://github.com/mapbox/mapbox-java/pull/1121)
- RouteOptions and MapboxDirections refactoring [#1118](https://github.com/mapbox/mapbox-java/pull/1118)
- Bumped StaticMapCriteria guidance day and night to v4 [#1116](https://github.com/mapbox/mapbox-java/pull/1116)

### 5.1.0-beta.1 - January 24, 2020

- Removed typo in `BannerView` class javadocs [#1111](https://github.com/mapbox/mapbox-java/pull/1111)
- Extract refresh models into separate module [#1107](https://github.com/mapbox/mapbox-java/pull/1107)
- Bumped `StaticMapCriteria` constant style strings to new versions [#1109](https://github.com/mapbox/mapbox-java/pull/1109)
- Add `toBuilder()` method to `RouteOptions` and `WalkingOptions` [#1110](https://github.com/mapbox/mapbox-java/pull/1110)
- Split Directions models from the implementation into a separate module [#1104](https://github.com/mapbox/mapbox-java/pull/1104)

### 5.0.0 - January 8, 2020

- Update to AndroidX annotation [#1095](https://github.com/mapbox/mapbox-java/pull/1095)
- Rename references of `mapIds` to `tileSetIds` [#1047](https://github.com/mapbox/mapbox-java/pull/  1047)
- Removed deprecated getCall() method in IsochroneService [#1099](https://github.com/mapbox/mapbox-java/pull/  1099)
- Directions junction view api [#1097](https://github.com/mapbox/mapbox-java/pull/1097)
- Add banner component type [#1098](https://github.com/mapbox/mapbox-java/pull/1098)
- Remove references to enterprise from geocoding docs [#1084](https://github.com/mapbox/mapbox-java/pull/1084)
- Add network interceptor to directions [#1093](https://github.com/mapbox/mapbox-java/pull/  1093)
- Replaced NotNull with NonNull annotation in TurfConversion class [#1092](https://github.com/mapbox/mapbox-java/pull/  1092)
- BannerText, RouteOptions builders: removed nullable annotations [#1091](https://github.com/mapbox/mapbox-java/pull/  1091)
- Adding and adjusting repo issue templates [#1069](https://github.com/mapbox/mapbox-java/pull/  1069)
- Adding combine turf method [#1008](https://github.com/mapbox/mapbox-java/pull/  1008)
- Add turf area measurement method. [#1079](https://github.com/mapbox/mapbox-java/pull/1079)

### 4.9.0 - September 23, 2019
- Added intersection search support to MapboxGeocoding [#1074](https://github.com/mapbox/mapbox-java/pull/1074)
- Added support for Turf polygonToLine method [#1075](https://github.com/mapbox/mapbox-java/pull/1075)

### v4.9.0-alpha.1 - September 4, 2019

- Fixed up bintray publish script, generate sources and javadoc jars [#1065](https://github.com/mapbox/mapbox-java/pull/1065)
- Keep *.java files in source.jar artifacts [#1064](https://github.com/mapbox/mapbox-java/pull/1064)
- Added setup for bintray release process [#1037](https://github.com/mapbox/mapbox-java/pull/1037)
- Add an API to check if a custom shifter is set. [#1062](https://github.com/mapbox/mapbox-java/pull/1062)
- Added new nearestPointOnLine default and custom distance unit [#1058](https://github.com/mapbox/mapbox-java/pull/1058)
- Adjustments to TurfMeasurement#bboxPolygon() to return a Feature instead of Polygon [#1055](https://github.com/mapbox/mapbox-java/pull/1055)
- Added fuzzyMatch support to MapboxGeocoding [#1051](https://github.com/mapbox/mapbox-java/pull/1051)
- Add Interceptor support to Directions Refresh API and enable Debug logging [#1039](https://github.com/mapbox/mapbox-java/pull/1039)
- Add square() api to TurfMeasurement [#1019](https://github.com/mapbox/mapbox-java/pull/1019)
- Adjusted link to Turf installation instructions [#1038](https://github.com/mapbox/mapbox-java/pull/1038)
- Removed the last updated line from ported Turf method list [#1032](https://github.com/mapbox/mapbox-java/pull/1032)
- Making GeoJson extend Serializable [#1034](https://github.com/mapbox/mapbox-java/pull/1034)
- Add support for Turf envelope [#1020](https://github.com/mapbox/mapbox-java/pull/1020)

### v4.8.0 - May 10, 2019
### v4.8.0-alpha.3 - May 10, 2019
- Added walking options serialize names adjustments [#1028](https://github.com/mapbox/mapbox-java/pull/1028)

### v4.8.0-alpha.2 - May 9, 2019
- Added walking options [#991](https://github.com/mapbox/mapbox-java/pull/991)

### v4.8.0-alpha.1 - May 7, 2019
- Adding explode turf method [#1007](https://github.com/mapbox/mapbox-java/pull/1007)
- Adding coordAll() versions for Feature, FeatureCollection, and Geometry parameters [#1009](https://github.com/mapbox/mapbox-java/pull/1009)

### v4.7.0 - April 25, 2019
- Added @Keep annotation to ease integration of geojson library [#1005](https://github.com/mapbox/mapbox-java/pull/1005)
- Fix possible IndexOutOfBoundsException exception [#1013](https://github.com/mapbox/mapbox-java/pull/1013)

### v4.7.0-alpha.1 - April 23, 2019
- Adding BoundingBoxPolygon turf method [#1006](https://github.com/mapbox/mapbox-java/pull/1006)
- Feature.get*Property should return null for non-existent property [#1001](https://github.com/mapbox/mapbox-java/pull/1001)
- added overloaded method bbox that takes in a BoundingBox  [#999](https://github.com/mapbox/mapbox-java/pull/999)
- Adding userAgent parameter to Isochrone request retrofit call URL  [#997](https://github.com/mapbox/mapbox-java/pull/997)

### v4.6.0 - April 11, 2019
- Exclude *.java from being added to library jars [#992](https://github.com/mapbox/mapbox-java/pull/992)
- Adjust @Nullable and @NonNull annotations to match what comes from Directions API [#990](https://github.com/mapbox/mapbox-java/pull/990)
- Add Isochrone API support [#988](https://github.com/mapbox/mapbox-java/pull/988)
- Remove sonarqube integration [#986](https://github.com/mapbox/mapbox-java/pull/986)
- Remove android tooling [#984](https://github.com/mapbox/mapbox-java/pull/984)
- Remove dependency of Turf library on services-core [#981](https://github.com/mapbox/mapbox-java/pull/981)
- Update MapMatching to use a POST request if the url for a GET request is larger than the maximum allowed size  [#978](https://github.com/mapbox/mapbox-java/pull/978)
- Update Directions to use a POST request if the url for a GET request is larger than the maximum allowed size [#977](https://github.com/mapbox/mapbox-java/pull/977)
- Add Interceptor support for MapboxRouteTiles [#976](https://github.com/mapbox/mapbox-java/pull/976)
- Add POST support to Directions [#975](https://github.com/mapbox/mapbox-java/pull/975)
- Add explicit US locale to String.Format of bounding box [#973](https://github.com/mapbox/mapbox-java/pull/973)
- Removed the use of AutoValue from geoJson implementation
  Deprecated PointDeserializer, PointSerializer, BoundingBoxDeserializer and BoundingBoxSerializer [#953](https://github.com/mapbox/mapbox-java/pull/953)

### v4.5.0 - February 13, 2019
- Directions refresh was not being included in services artifact [#966](https://github.com/mapbox/mapbox-java/pull/966)

### v4.4.0 - February 12, 2019
- Add waypointIndices to MapboxDirections, update MapboxMapMaptching to have waypointIndices instead of waypoints
  [#959](https://github.com/mapbox/mapbox-java/pull/959),
  [#961](https://github.com/mapbox/mapbox-java/pull/961),
  [#962](https://github.com/mapbox/mapbox-java/pull/962),
  [#963](https://github.com/mapbox/mapbox-java/pull/963)
- Add Interceptor and EventListener to MapboxDirections [#958](https://github.com/mapbox/mapbox-java/pull/958)
- Directions Refresh new implementation [#955](https://github.com/mapbox/mapbox-java/pull/955)
- Update gradle tools versions [#954](https://github.com/mapbox/mapbox-java/pull/954)
- Add getting mapmatching through POST method [#948](https://github.com/mapbox/mapbox-java/pull/948)

### v4.3.0 - December 18, 2018
- Do not use BigDecimal for rounding [#938](https://github.com/mapbox/mapbox-java/pull/938)
- Remove coordinate limit from MapboxMapMatching [#940](https://github.com/mapbox/mapbox-java/pull/940)
- Remove character waypoint names limit from mapbox map matching [#941](https://github.com/mapbox/mapbox-java/pull/941)
- Adding additional tests to files related to coordinate shifting [#936](https://github.com/mapbox/mapbox-java/pull/936)
- Added waypoint targets to MapboxDirections request [#942](https://github.com/mapbox/mapbox-java/pull/942)

### v4.2.0 - December 4, 2018
- Adding Shifter implementation [#922](https://github.com/mapbox/mapbox-java/pull/922)
- Fix v4.1.1 release

### v4.1.1 - November 30, 2018
- Remove waypoint limit check [#928](https://github.com/mapbox/mapbox-java/pull/928)
- Adding code coverage report generation and upload to Codecov [#925](https://github.com/mapbox/mapbox-java/pull/925)

### v4.1.0 - November 12, 2018
- Added wrappers for route tile APIs Feature [#913](https://github.com/mapbox/mapbox-java/pull/913)
- Add annotations, approaches request parameter & distances to the response object [#911](https://github.com/mapbox/mapbox-java/pull/911)
- Add interceptor and network interceptor support to Mapbox Speech [#910](https://github.com/mapbox/mapbox-java/pull/910)
- Remove dependency on BindingContextFactory [#909](https://github.com/mapbox/mapbox-java/pull/909)
- Archive stale issues and pull request [#906](https://github.com/mapbox/mapbox-java/pull/906)
- Added StepManeuver.Type for ease of use [#898](https://github.com/mapbox/mapbox-java/pull/898)
- Route options should be serialized/deserialized using backend names [#895](https://github.com/mapbox/mapbox-java/pull/895)
- BoundingBox deserialization [#891](https://github.com/mapbox/mapbox-java/pull/891)

### v4.0.0 - September 19, 2018
- downgrade to java 7 [#876](https://github.com/mapbox/mapbox-java/pull/876)
- The first and last positions should be equivalent in polygon ring Bug Ready For Review refactor [#886](https://github.com/mapbox/mapbox-java/pull/886)
- Fix circle generation in TurfTransformation [#885](https://github.com/mapbox/mapbox-java/pull/885)
- coordinates() should have @NotNull annotation in Geometry implementations [#882](https://github.com/mapbox/mapbox-java/pull/882)
- Tilequery API [#879](https://github.com/mapbox/mapbox-java/pull/879)
- all accesToken fields should be annotated with @nonnull [#875](https://github.com/mapbox/mapbox-java/pull/875)

### v3.4.1 - August 9, 2018
- DirectionsRoute#fromJson RuntimeException [#864](https://github.com/mapbox/mapbox-java/pull/864)


### v3.4.0 - August 6, 2018
- Fixed typo in static map annotations and added tests for map annotations [#855](https://github.com/mapbox/mapbox-java/pull/855)
- Added toJson and fromJson to Directions models [#854](https://github.com/mapbox/mapbox-java/pull/854)
- Upgraded dependencies [#853](https://github.com/mapbox/mapbox-java/pull/853)
- Missing space [#846](https://github.com/mapbox/mapbox-java/pull/846)
- Add voiceLanguage to MapMatchingMatching [#847](https://github.com/mapbox/mapbox-java/pull/847)

### v3.3.0
- add reverseMode parameter to Geocoding Service [#843](https://github.com/mapbox/mapbox-java/pull/843)
- doc changes [#840) [#835](https://github.com/mapbox/mapbox-java/pull/835)
- static map request format #836 [#839](https://github.com/mapbox/mapbox-java/pull/839)
- add waypoint names [#831](https://github.com/mapbox/mapbox-java/pull/831)
- add approaches to RouteOptions [#830](https://github.com/mapbox/mapbox-java/pull/830)

### v3.2.0
- Added sub - to BannerInstructions [#823](https://github.com/mapbox/mapbox-java/pull/823)
- Added directions & active - to BannerText [#823](https://github.com/mapbox/mapbox-java/pull/823)
- Properties passed to Feature.fromGeometry and CarmenFeature.properties should be Nullable [#815](https://github.com/mapbox/mapbox-java/pull/815)
- Move TurfAssertions.getCoord() to TurfMeta.getCoord() to follow Turf.js [#818](https://github.com/mapbox/mapbox-java/pull/818)
- Semicolon should be used as radiuses and timestamps delimiters [#821](https://github.com/mapbox/mapbox-java/pull/821)
- Added approaches request paramenter to Directions and MapMatching [#827](https://github.com/mapbox/mapbox-java/pull/827)

### v3.1.0
- Added voiceLocale to DirectionsRoute  [#804](https://github.com/mapbox/mapbox-java/pull/804)
- Added line-slice-along() and degreesToRadians() to Turf functions(#800)
- Removed java.awt.Color dependency in static map [#793](https://github.com/mapbox/mapbox-java/pull/793)
- Add missing RouteOptions params for MapMatching [#792](https://github.com/mapbox/mapbox-java/pull/792)
- Added BoundingBox.fromLngLats() for consistency [#789](https://github.com/mapbox/mapbox-java/pull/789)
- added toBuilder() to model classes [#787](https://github.com/mapbox/mapbox-java/pull/787)
### v3.0.1
- Adjust maxspeed annotation [#777](https://github.com/mapbox/mapbox-java/pull/777)

### v3.0.0
- Added MapMatching Error reporting [#758](https://github.com/mapbox/mapbox-java/pull/758)
- MapMaptchingMatching to DirectionsRoute [#759](https://github.com/mapbox/mapbox-java/pull/759)
- Add Abbreviations and type to BannerComponent [#762](https://github.com/mapbox/mapbox-java/pull/762)
- Add voiceUnits to MapMatching [#764](https://github.com/mapbox/mapbox-java/pull/764)
- Implemented Comparable in BannerComponents [#768](https://github.com/mapbox/mapbox-java/pull/768)
- Adds a bbox() utility method for Geometry [#769](https://github.com/mapbox/mapbox-java/pull/769)
- Adds a method to Geometry to load from json [#770](https://github.com/mapbox/mapbox-java/pull/770)
- Add MaxSpeed Annotation to Directions API [#772](https://github.com/mapbox/mapbox-java/pull/772)

### v3.0.0-beta.4
- Rework dependency tree, add api instead of implementation
- Remove unprintable chars in api request
- Make GeometryCollection a Geometry as specified in the spec at sections 1.4 and 3.1.8
- Add Degrees and Driving Side to BannerText
- Add tunnel and restricted to possible values of exclude criteria
- Turf functions name changes
- Make getOkHttpClient synchronized
- Add type and modifier to BannerText
- Add baseUrl to RouteOptions

### v3.0.0-beta.3

- Added back fromLngLats() that take double arrays for coordinates [#722](https://github.com/mapbox/mapbox-java/pull/722)
- Adds `waypoints`, `roundaboutExits`, `voiceInstructions`, `bannerInstructions` params to MapMatching API [#718](https://github.com/mapbox/mapbox-java/pull/718)
- Added back Bounding box deserializer to `CarmenFeature` [#712](https://github.com/mapbox/mapbox-java/pull/712)

### v3.0.0-beta.2
- Added static initializer methods with parameters accepting single objects rather than list in GeoJSON classes - [#691](https://github.com/mapbox/mapbox-java/issues/691)
- Annotations added to the `PolylineUtils` class. - [#687](https://github.com/mapbox/mapbox-java/pull/687)
- Adds check to `PointOnLine` Turf method to ensure 2 or more non-identical `Point`s are used. - [#689](https://github.com/mapbox/mapbox-java/pull/689)
- Added a missing return statement inside the internal `MapboxDirections` response code. - [#685](https://github.com/mapbox/mapbox-java/pull/685)
- User header agent constant is now using the generated BuildConfig file to add device information (Identical behavior to what was happening in 2.X). - [#679](https://github.com/mapbox/mapbox-java/pull/679)
- Save access token in RouteOptions. - [#669](https://github.com/mapbox/mapbox-java/pull/669)
- Resolved bug where the generated type adapter in `CarmenFeature` class wasn't being used causing the `SeralizedName` annotation to be ignored. - [#698](https://github.com/mapbox/mapbox-java/pull/698)

### v3.0.0-beta.1

- Banner instructions added to the Directions API.
- DrivingSide API in `RouteStep` class added.
- `CarmenFeature` class now has `toJson` and `fromJson` APIs.
- Direction Bearing param is no longer placed in order added, but rather, the matching of coordinate.
- `RouteOptions` object added to Directions API to allow for making identical request.
- In the Directions API, errors returned from server are now returned in the `message` API.
- Exclusions added to the directions API.
- Mapbox builders no longer require unnecessary generics.
- Model classes are now serializable.
- Directions Matrix API changed to Matrix API.
- Directions Optimization API name changed to Optimization API.
- Object classes now contains `equals`, `hash`, and `toString` methods.
- Gradle `java` plugin switched to `java-library`.
- Annotations library added so methods and classes can convey more information.
- Model and GeoJSON classes made immutable using AutoValue.
- Directions API UUID added inside the `DirectionsResponse` model.
- Position object removed in favor of GeoJSON `Point` for coordinate representation.
- Support for direction `class` has been added.
- Requesting `RoundaboutExit` is now possible and adds an additional instruction when traversing inside a roundabout or rotary.
- Retrofit, OkHttp, and other dependencies updated to use the latest.
- API service module split up in project to reflect individual Mapbox APIs.

### v2.2.10

- Updates OkHttp version to `3.10.0` [#727](https://github.com/mapbox/mapbox-java/pull/727)
- Moves internal method previously used in OkHttp and places it directly into telemetry package [#727](https://github.com/mapbox/mapbox-java/pull/727)

### v2.2.9

- Deactivate location engine when shutting down telemetry service [#638](https://github.com/mapbox/mapbox-java/pull/638)
- Add obtain location engine type javadoc [#635](https://github.com/mapbox/mapbox-java/pull/635)
- Bump Lost version to 3.0.4 [#636](https://github.com/mapbox/mapbox-java/pull/636)
- Downgrade minimum sdk version to 14 [#630](https://github.com/mapbox/mapbox-java/pull/630)

### v2.2.8

- Revert mapbox-android-core dependency PR [#627](https://github.com/mapbox/mapbox-java/pull/627)

### v2.2.7

- mapbox-android-core dependency [#622](https://github.com/mapbox/mapbox-java/pull/622)
- Add telem audio type support [#604](https://github.com/mapbox/mapbox-java/pull/604)
- Add telem cancel rating and comment track support [#617](https://github.com/mapbox/mapbox-java/pull/617)
- Add telem percent time in foreground and in portrait track support [#616](https://github.com/mapbox/mapbox-java/pull/616)
- Add telem absolute distance to destination support [#615](https://github.com/mapbox/mapbox-java/pull/615)
- Add telem current location engine being used track support [#605](https://github.com/mapbox/mapbox-java/pull/605)
- Get correct volume level [#600](https://github.com/mapbox/mapbox-java/pull/600)
- Flush Navigation Events Manually [#601](https://github.com/mapbox/mapbox-java/pull/601)
- NPE for BatteryStatus [#602](https://github.com/mapbox/mapbox-java/pull/602)

### v2.2.6

- Fix for Matrix source and destination returning null rather than the true value [#588](https://github.com/mapbox/mapbox-java/pull/588)
- Add optional locationEngine dependencies [#502](https://github.com/mapbox/mapbox-java/pull/502)

### v2.2.5

- set sdkIdentifier and sdkVersion when pushing turnstile event [#574](https://github.com/mapbox/mapbox-java/pull/574)

### v2.2.4

- Bumped Navigation Events `EVENT_VERSION` variable to version 3 [#570](https://github.com/mapbox/mapbox-java/pull/570)
- Adds missing event parameters to the NavigationEvent methods [#569](https://github.com/mapbox/mapbox-java/pull/569)
- Adds newUserAgent to telemetry [#568](https://github.com/mapbox/mapbox-java/pull/568)
- Fixes arrival timestamp formatting [#567](https://github.com/mapbox/mapbox-java/pull/567)
- Omit null optional fields in Nav events [#566](https://github.com/mapbox/mapbox-java/pull/566)
- Adds missing device attribute to Navigation events [#565](https://github.com/mapbox/mapbox-java/pull/565)
- Adds roundaboutExits param to MapboxDirections [#562](https://github.com/mapbox/mapbox-java/pull/562)

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
* Added support for direction [annotations](https://github.com/mapbox/mapbox-java/blob/main/mapbox/libjava-services/src/main/java/com/mapbox/services/api/directions/v5/MapboxDirections.java#L345-L358) [#417](https://github.com/mapbox/mapbox-java/pull/417)
* Added `setInterval`, `setFastestInterval` and `setSmallestDisplacement` to allow configurable [LocationEngine](https://github.com/mapbox/mapbox-java/blob/main/mapbox/libandroid-telemetry/src/main/java/com/mapbox/services/android/telemetry/location/LocationEngine.java) parameters [#402](https://github.com/mapbox/mapbox-java/pull/402)
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
