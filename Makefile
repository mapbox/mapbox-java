javadoc:
	# Output is ./libjava/lib/build/docs/javadoc
	cd libjava; ./gradlew clean javadoc

	# Output is ./libandroid/lib/build/docs/javadoc/release
	cd libandroid; ./gradlew clean javadocrelease

geocoding-fixtures:
	# Geocoding: 1600 Pennsylvania Ave NW
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/1600+pennsylvania+ave+nw.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o libjava/lib/src/test/fixtures/geocoding.json

	# Reverse geocoding: -77.0366, 38.8971
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/-77.0366,38.8971.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o libjava/lib/src/test/fixtures/geocoding_reverse.json

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
