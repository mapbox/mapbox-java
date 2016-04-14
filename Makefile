javadoc:
	# Output is ./libjava/lib/build/docs/javadoc
	cd libjava; ./gradlew clean javadoc

	# Output is ./libandroid/lib/build/docs/javadoc/release
	cd libandroid; ./gradlew clean javadocrelease

fixtures:
	# Geocoding: 1600 Pennsylvania Ave NW
	curl https://api.mapbox.com/geocoding/v5/mapbox.places/1600+pennsylvania+ave+nw.json?access_token=$(MAPBOX_ACCESS_TOKEN) \
		-o libjava/lib/src/test/fixtures/geocoding.json

	# Reverse geocoding: -77.0366, 38.8971
	curl https://api.mapbox.com/geocoding/v5/mapbox.places/-77.0366,38.8971.json?access_token=$(MAPBOX_ACCESS_TOKEN) \
		-o libjava/lib/src/test/fixtures/geocoding_reverse.json
