checkstyle:
	cd mapbox; ./gradlew checkstyle

test:
	cd mapbox; ./gradlew test

build-release:
	# Java modules
	cd mapbox; ./gradlew :libjava-core:assemble
	cd mapbox; ./gradlew :libjava-geojson:assemble
	cd mapbox; ./gradlew :libjava-services:assemble
	cd mapbox; ./gradlew :libjava-services-rx:assemble

	# Android modules
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

publish-local:
	# Installs the artifacts into the local Maven repository
	# Java modules
	cd mapbox; ./gradlew :libjava-core:install
	cd mapbox; ./gradlew :libjava-geojson:install
	cd mapbox; ./gradlew :libjava-services:install
	cd mapbox; ./gradlew :libjava-services-rx:install

	# Android modules
	cd mapbox; ./gradlew :libandroid-telemetry:install
	cd mapbox; ./gradlew :libandroid-services:install
	cd mapbox; ./gradlew :libandroid-ui:install

dex-count:
	cd mapbox; ./gradlew countDebugDexMethods
	cd mapbox; ./gradlew countReleaseDexMethods

geocoding-fixtures:
	# Geocoding: 1600 Pennsylvania Ave NW
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/1600+pennsylvania+ave+nw.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o libjava/lib/src/test/fixtures/geocoding.json

	# Reverse geocoding: -77.0366, 38.8971
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/-77.0366,38.8971.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o libjava/lib/src/test/fixtures/geocoding_reverse.json

	#
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/1600+pennsylvania+ave+nw.json?country=aq&access_token=$(MAPBOX_ACCESS_TOKEN)" \
	  -o libjava/lib/src/test/fixtures/geocoding_country_not_supported.json

directions-fixtures:
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o libjava/lib/src/test/fixtures/directions_v5.json

mapmatching-fixtures:
	# Geometry polyline
	curl -X POST --header "Content-Type:application/json" -d @libjava/lib/src/test/fixtures/mapmatching_trace.json \
		"https://api.mapbox.com/matching/v4/mapbox.driving.json?geometry=polyline&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o libjava/lib/src/test/fixtures/mapmatching_v5_polyline.json

	# No geometry
	curl -X POST --header "Content-Type:application/json" -d @libjava/lib/src/test/fixtures/mapmatching_trace.json \
		"https://api.mapbox.com/matching/v4/mapbox.driving.json?geometry=false&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o libjava/lib/src/test/fixtures/mapmatching_v5_no_geometry.json

distance-fixtures:
	# Retrieve a duration matrix
	curl -X POST --header "Content-Type:application/json" -d @libjava/lib/src/test/fixtures/distance_coordinates.json \
		"https://api.mapbox.com/distances/v1/mapbox/driving?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o libjava/lib/src/test/fixtures/distance_v1.json
