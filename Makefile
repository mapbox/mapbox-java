build-config:
	./gradlew compileBuildConfig

checkstyle:
	./gradlew checkstyle

sonarqube:
	./gradlew JacocoTestReport
	./gradlew sonarqube

test:
	./gradlew test

build-release:
	./gradlew assemble

publish:
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :services-core:uploadArchives ; \
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :services-geojson:uploadArchives ; \
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :services:uploadArchives ; \
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :services-turf:uploadArchives ; \

publish-local:
	# This publishes to ~/.m2/repository/com/mapbox/mapboxsdk
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :services-core:uploadArchives ; \
    export IS_LOCAL_DEVELOPMENT=true; ./gradlew :services-geojson:uploadArchives ; \
    export IS_LOCAL_DEVELOPMENT=true; ./gradlew :services:uploadArchives ; \
    export IS_LOCAL_DEVELOPMENT=true; ./gradlew :services-turf:uploadArchives ; \

directions-matrix-fixtures:
	# request a symmetric 3x3 matrix for cars
	curl "https://api.mapbox.com/directions-matrix/v1/mapbox/driving/-122.42,37.78;-122.45,37.91;-122.48,37.73?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matrix/src/test/resources/directions_matrix_3x3.json

	# request an asymmetric 2x3 matrix for bicycles
	curl "https://api.mapbox.com/directions-matrix/v1/mapbox/cycling/-122.42,37.78;-122.45,37.91;-122.48,37.73?sources=0;2&destinations=all&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matrix/src/test/resources/directions_matrix_2x3.json

geocoding-fixtures:
	# Geocoding: 1600 Pennsylvania Ave NW
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/1600+pennsylvania+ave+nw.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-geocoding/src/test/resources/geocoding.json

	# Reverse geocoding: -77.0366, 38.8971
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/-77.0366,38.8971.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-geocoding/src/test/resources/geocoding_reverse.json

	# Not supported country
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places/1600+pennsylvania+ave+nw.json?country=aq&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-geocoding/src/test/resources/geocoding_country_not_supported.json

geocoding-batch-fixtures:
	curl "https://api.mapbox.com/geocoding/v5/mapbox.places-permanent/20001;20009;22209.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-geocoding/src/test/resources/geocoding_batch.json

directions-fixtures:
	# Directions: polyline geometry with precision 5
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_v5.json

	# Directions: request annotations
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline&language=sv&steps=true&annotations=distance,duration,speed&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_annotations_v5.json

	# Directions: polyline geometry with precision 6
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline6&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_v5_precision_6.json

	# Directions: route with a rotary
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-77.04430818557739,38.908650612656864;-77.04192638397217,38.90963574367117?geometries=polyline&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_v5_fixtures_rotary.json

	# Directions: route with traffic
	curl "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/-122.416667,37.783333;-121.900000,37.333333?geometries=polyline&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_v5_traffic.json

	# Directions: allow roundabout exits
	curl "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/-77.04014240930304,38.91313201360546;-77.04573453985853,38.90725177816208.json?steps=true&overview=full&geometries=polyline&roundabout_exits=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_v5_roundabout_exits.json

	# Directions: voice announcements fixture
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-77.04014240930304,38.91313201360546;-77.04573453985853,38.90725177816208.json?steps=true&overview=full&geometries=polyline6&roundabout_exits=true&voice_instructions=true&banner_instructions=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
    		-o services-directions/src/test/resources/directions_v5_voice_banner.json

	# Directions: No route found
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/149.72227,-37.59764;170.72975,-42.96489.json?steps=true&overview=full&geometries=polyline6&roundabout_exits=true&voice_instructions=true&banner_instructions=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
	  		-o services-directions/src/test/resources/directions_v5_no_route.json

	# Directions: route with banner shield
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-95.69263,29.78771;-95.54899,29.78284.json?steps=true&overview=full&geometries=polyline6&roundabout_exits=true&voice_instructions=true&banner_instructions=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
	  		-o services-directions/src/test/resources/directions_v5_banner_with_shield.json

mapmatching-fixtures:
	curl "https://api.mapbox.com/matching/v5/mapbox/driving/$(MAP_MATCHING_COORDINATES)?geometries=polyline&language=sv&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matching/src/test/resources/mapmatching_v5_polyline.json

optimization-fixtures:
	# request an optimized car trip with no additional options
	curl "https://api.mapbox.com/optimized-trips/v1/mapbox/driving/-122.42,37.78;-122.45,37.91;-122.48,37.73?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-optimization/src/test/resources/optimized_trip.json

	curl "https://api.mapbox.com/optimized-trips/v1/mapbox/cycling/-122.42,37.78;-122.45,37.91;-122.48,37.73?steps=true&language=sv&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-optimization/src/test/resources/optimized_trip_steps.json

	curl "https://api.mapbox.com/optimized-trips/v1/mapbox/driving/13.388860,52.517037;13.397634,52.529407;13.428555,52.523219;13.418555,52.523215?roundtrip=true&distributions=3,1&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-optimization/src/test/resources/optimized_trip_distributions.json
