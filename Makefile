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
	cd mapbox; ./gradlew :libjava-core:uploadArchives
	cd mapbox; ./gradlew :libjava-geojson:uploadArchives
	cd mapbox; ./gradlew :libjava-services:uploadArchives
	cd mapbox; ./gradlew :libjava-services-rx:uploadArchives

publish-android:
	cd mapbox; ./gradlew :libandroid-telemetry:uploadArchives
	cd mapbox; ./gradlew :libandroid-services:uploadArchives
	cd mapbox; ./gradlew :libandroid-ui:uploadArchives

dex-count:
	cd mapbox; ./gradlew countDebugDexMethods
	cd mapbox; ./gradlew countReleaseDexMethods

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
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/directions_v5.json

mapmatching-fixtures:
	# Geometry polyline
	curl -X POST --header "Content-Type:application/json" -d @mapbox/libjava-services/src/test/fixtures/mapmatching_trace.json \
		"https://api.mapbox.com/matching/v4/mapbox.driving.json?geometry=polyline&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/mapmatching_v5_polyline.json

	# No geometry
	curl -X POST --header "Content-Type:application/json" -d @mapbox/libjava-services/src/test/fixtures/mapmatching_trace.json \
		"https://api.mapbox.com/matching/v4/mapbox.driving.json?geometry=false&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/mapmatching_v5_no_geometry.json

distance-fixtures:
	# Retrieve a duration matrix
	curl -X POST --header "Content-Type:application/json" -d @mapbox/libjava-services/src/test/fixtures/distance_coordinates.json \
		"https://api.mapbox.com/distances/v1/mapbox/driving?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o mapbox/libjava-services/src/test/fixtures/distance_v1.json
