# Used for Map Matching
MAP_MATCHING_COORDINATES = 13.418946862220764,52.50055852688439;13.419011235237122,52.50113000479732;13.419756889343262,52.50171780290061;13.419885635375975,52.50237416816131;13.420631289482117,52.50294888790448

checkstyle:
	cd mapbox; ./gradlew checkstyle

test-java:
	cd mapbox; ./gradlew :libjava-core:test
	cd mapbox; ./gradlew :libjava-geojson:test
	cd mapbox; ./gradlew :libjava-services:test
	cd mapbox; ./gradlew :libjava-services-rx:test

test-android:
	cd mapbox; ./gradlew :libandroid-telemetry:test
	cd mapbox; ./gradlew :libandroid-services:test
	cd mapbox; ./gradlew :libandroid-ui:test

build-release-java:
	cd mapbox; ./gradlew :libjava-core:assemble
	cd mapbox; ./gradlew :libjava-geojson:assemble
	cd mapbox; ./gradlew :libjava-services:assemble
	cd mapbox; ./gradlew :libjava-services-rx:assemble

build-release-android:
	cd mapbox; ./gradlew :libandroid-telemetry:assembleRelease
	cd mapbox; ./gradlew :libandroid-services:assembleRelease
	cd mapbox; ./gradlew :libandroid-ui:assembleRelease

javadoc:
	# Java modules
	# Output is in ./mapbox/*/build/docs/javadoc
	cd mapbox; ./gradlew :libjava-core:javadocGeneration
	cd mapbox; ./gradlew :libjava-geojson:javadocGeneration
	cd mapbox; ./gradlew :libjava-services:javadocGeneration
	cd mapbox; ./gradlew :libjava-services-rx:javadocGeneration

	# Android modules
	# Output is ./mapbox/*/build/docs/javadoc/release
	cd mapbox; ./gradlew :libandroid-telemetry:javadocrelease
	cd mapbox; ./gradlew :libandroid-services:javadocrelease
	cd mapbox; ./gradlew :libandroid-ui:javadocrelease

publish-java:
	cd mapbox; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :libjava-core:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :libjava-geojson:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :libjava-services:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :libjava-services-rx:uploadArchives

publish-android:
	cd mapbox; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :libandroid-telemetry:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :libandroid-services:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :libandroid-ui:uploadArchives

publish-local:
	# This publishes to ~/.m2/repository/com/mapbox/mapboxsdk
	cd mapbox; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :libjava-core:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :libjava-geojson:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :libjava-services:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :libjava-services-rx:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :libandroid-telemetry:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :libandroid-services:uploadArchives
	cd mapbox; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :libandroid-ui:uploadArchives

dex-count:
	cd mapbox; ./gradlew countDebugDexMethods
	cd mapbox; ./gradlew countReleaseDexMethods

directions-matrix-fixtures:
	# request a symmetric 3x3 matrix for cars
	curl "https://api.mapbox.com/directions-matrix/v1/mapbox/driving/-122.42,37.78;-122.45,37.91;-122.48,37.73?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/directions_matrix_3x3.json

	# request an asymmetric 2x3 matrix for bicycles
	curl "https://api.mapbox.com/directions-matrix/v1/mapbox/cycling/-122.42,37.78;-122.45,37.91;-122.48,37.73?sources=0;2&destinations=all&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/directions_matrix_2x3.json.json

geocoding-fixtures:
	# Geocoding: 1600 Pennsylvania Ave NW
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/1600+pennsylvania+ave+nw.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/geocoding.json

	# Reverse geocoding: -77.0366, 38.8971
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/-77.0366,38.8971.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/geocoding_reverse.json

	# Not supported country
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/1600+pennsylvania+ave+nw.json?country=aq&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/geocoding_country_not_supported.json

geocoding-batch-fixtures:
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places-permanent/20001;20009;22209.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/geocoding_batch.json

directions-fixtures:
	# Directions: polyline geometry with precision 5
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/directions_v5.json

	# Directions: request annotations
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline&steps=true&annotations=distance,duration,speed&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/directions_annotations_v5.json

	# Directions: polyline geometry with precision 6
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline6&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/directions_v5_precision_6.json

directions-fixtures-rotary:
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-77.04430818557739,38.908650612656864;-77.04192638397217,38.90963574367117?geometries=polyline&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/directions_v5_fixtures_rotary.json

directions-traffic-fixtures:
	curl "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/directions_v5_traffic.json

mapmatching-fixtures:
	curl "https://api.mapbox.com/matching/v5/mapbox/driving/$(MAP_MATCHING_COORDINATES)?geometries=polyline&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/mapmatching_v5_polyline.json

optimized-trips-fixtures:
	# request an optimized car trip with no additional options
	curl "https://api.mapbox.com/optimized-trips/v1/mapbox/driving/-122.42,37.78;-122.45,37.91;-122.48,37.73?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/optimized_trip.json
