# Used for Map Matching
MAP_MATCHING_COORDINATES = 13.418946862220764,52.50055852688439;13.419011235237122,52.50113000479732;13.419756889343262,52.50171780290061;13.419885635375975,52.50237416816131;13.420631289482117,52.50294888790448

# Used for directions
DIRECTIONS_POST_COORDINATES = 2.344003915786743,48.85805170891599;2.346750497817993,48.85727523615161;2.348681688308716,48.85936462637049;2.349550724029541,48.86084691113991;2.349550724029541,48.8608892614883;2.349625825881958,48.86102337068847;2.34982967376709,48.86125629633996

build-config:
	./gradlew compileBuildConfig

checkstyle:
	./gradlew checkstyleMain

sonarqube:
	./gradlew JacocoTestReport
	./gradlew sonarqube

test:
	./gradlew test

build-release:
	./gradlew assemble

javadoc:
	mkdir documentation
	mkdir documentation/core/
	mkdir documentation/geojson/
	mkdir documentation/turf/
	mkdir documentation/services/
	./gradlew :services-core:javadoc; mv services-core/build/docs/javadoc/ ./documentation/core/javadoc/ ; \
	./gradlew :services-geojson:javadoc; mv services-geojson/build/docs/javadoc/ ./documentation/geojson/javadoc/ ; \
	./gradlew :services-turf:javadoc; mv services-turf/build/docs/javadoc/ ./documentation/turf/javadoc/ ; \
	./gradlew :services:javadoc; mv services/build/docs/javadoc/ ./documentation/services/javadoc/ ; \

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

graphs:
	./gradlew :services-core:generateDependencyGraphMapboxLibraries
	./gradlew :services-geojson:generateDependencyGraphMapboxLibraries
	./gradlew :services:generateDependencyGraphMapboxLibraries
	./gradlew :services-turf:generateDependencyGraphMapboxLibraries

directions-matrix-fixtures:
	# request a symmetric 1x3 matrix for pedestrians
	curl "https://api.mapbox.com/directions-matrix/v1/mapbox/walking/-122.42,37.78;-122.45,37.91;-122.48,37.73?sources=1&annotations=distance,duration&approaches=curb;curb;curb&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matrix/src/test/resources/directions_matrix_1x3.json

	# request a symmetric 3x3 matrix for cars
	curl "https://api.mapbox.com/directions-matrix/v1/mapbox/driving/-122.42,37.78;-122.45,37.91;-122.48,37.73?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matrix/src/test/resources/directions_matrix_3x3.json

	# request an asymmetric 2x3 matrix for bicycles
	curl "https://api.mapbox.com/directions-matrix/v1/mapbox/cycling/-122.42,37.78;-122.45,37.91;-122.48,37.73?sources=0;2&access_token=$(MAPBOX_ACCESS_TOKEN)" \
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

	# Directions: voice announcements invalid locale
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-77.04014240930304,38.91313201360546;-77.04573453985853,38.90725177816208.json?steps=true&overview=full&geometries=polyline6&roundabout_exits=true&voice_instructions=true&language=he&access_token=$(MAPBOX_ACCESS_TOKEN)" \
        -o services-directions/src/test/resources/directions_v5_voice_invalid.json

	# Directions: No route found
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-151.2302,-33.9283;-174.7654,-36.8641.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
        -o services-directions/src/test/resources/directions_v5_no_route.json

	# Directions: route with banner shield
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-95.69263,29.78771;-95.54899,29.78284.json?steps=true&overview=full&geometries=polyline6&roundabout_exits=true&voice_instructions=true&banner_instructions=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
        -o services-directions/src/test/resources/directions_v5_banner_with_shield.json

	# Directions: route with bannerText
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.03067988107114,37.331808179989494;-122.03178702099605,37.3302383113533?voice_units=imperial&roundabout_exits=true&geometries=polyline&overview=full&steps=true&voice_instructions=true&banner_instructions=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_v5_banner_text.json

	# Directions: route with maxspeed
	curl "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/9.950072,52.150015;7.569915,52.916751?alternatives=true&geometries=polyline6&overview=full&steps=true&bearings=%3B&continue_straight=true&annotations=maxspeed&language=en&access_token=$(MAPBOX_ACCESS_TOKEN)" \
        -o services-directions/src/test/resources/directions_v5_max_speed_annotation.json

	# Directions: route with sub (and lane data) in BannerInstructions
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/-122.403561,37.777689;-122.405786,37.770369.json?access_token=$(MAPBOX_ACCESS_TOKEN)&steps=true&geometries=polyline&banner_instructions=true" \
        -o services-directions/src/test/resources/directions_v5_banner_instructions.json

	# Directions: route with approaches in request
	curl "https://api.mapbox.com/directions/v5/mapbox/driving/13.4301,52.5109;13.432507621760521,52.501725088556014?approaches=unrestricted;curb&access_token=$(MAPBOX_ACCESS_TOKEN)" \
        -o services-directions/src/test/resources/directions_v5_approaches.json

	# Directions: includes waypoint_names
	curl "https://api.mapbox.com/directions/v5/mapbox/cycling/-122.42,37.78;-77.03,38.91?steps=true&voice_instructions=true&banner_instructions=true&voice_units=imperial&waypoint_names=Home;Work&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_v5_waypoint_names.json

	# Directions: includes waypoint_targets
	curl "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/-6.80904429026134,62.00015328799685;-6.800065040588378,62.00012400993553?waypoint_targets=;-6.799936294555664,61.99987216574813&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-directions/src/test/resources/directions_v5_waypoint_targets.json

    # Directions: post request
	curl -d "coordinates=$(DIRECTIONS_POST_COORDINATES)&steps=true&waypoints=0;6&waypoint_names=Home;Work&banner_instructions=true" "https://api.mapbox.com/directions/v5/mapbox/driving?access_token=$(MAPBOX_ACCESS_TOKEN)" \
	    -o services-directions/src/test/resources/directions_v5_post.json

mapmatching-fixtures:
	curl "https://api.mapbox.com/matching/v5/mapbox/driving/$(MAP_MATCHING_COORDINATES)?geometries=polyline&language=sv&steps=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matching/src/test/resources/map_matching_v5_polyline.json

	# Unmatchable MapMatching request
	curl "https://api.mapbox.com/matching/v5/mapbox/driving/0,-40;0,-20?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matching/src/test/resources/mapmatching_nosegment_v5_polyline.json

	# MapMatching request with approaches
	curl "https://api.mapbox.com/matching/v5/mapbox/driving/-117.1728265285492,32.71204416018209;-117.17334151268004,32.71254065549407?approaches=unrestricted;curb&access_token=$(MAPBOX_ACCESS_TOKEN)" \
			-o services-matching/src/test/resources/mapmatching_v5_approaches.json

	# MapMatching request with waypoint_names:
	curl "https://api.mapbox.com/matching/v5/mapbox/driving/2.344003915786743,48.85805170891599;2.346750497817993,48.85727523615161;2.348681688308716,48.85936462637049;2.349550724029541,48.86084691113991;2.349550724029541,48.8608892614883;2.349625825881958,48.86102337068847;2.34982967376709,48.86125629633996?steps=true&tidy=true&waypoints=0;6&waypoint_names=Home;Work&banner_instructions=true&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matching/src/test/resources/mapmatching_v5_waypoint_names.json

	# MapMatching with valid voiceLanguage
	 curl "https://api.mapbox.com/matching/v5/mapbox/driving/$(MAP_MATCHING_COORDINATES)?steps=true&overview=full&geometries=polyline6&roundabout_exits=true&voice_instructions=true&language=en&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-matching/src/test/resources/map_matching_v5_voice_language.json

	# MapMatching with invalid voiceLanguage
	curl "https://api.mapbox.com/matching/v5/mapbox/driving/$(MAP_MATCHING_COORDINATES)?steps=true&overview=full&geometries=polyline6&roundabout_exits=true&voice_instructions=true&language=he&access_token=$(MAPBOX_ACCESS_TOKEN)" \
	    -o services-matching/src/test/resources/map_matching_v5_invalid_voice_language.json

	# MapMatching with post request
	curl -d "coordinates=2.344003915786743,48.85805170891599;2.346750497817993,48.85727523615161;2.348681688308716,48.85936462637049;2.349550724029541,48.86084691113991;2.349550724029541,48.8608892614883;2.349625825881958,48.86102337068847;2.34982967376709,48.86125629633996&steps=true&tidy=true&waypoints=0;6" "https://api.mapbox.com/matching/v5/mapbox/driving?access_token=$(MAPBOX_ACCESS_TOKEN)" \
	    -o services-matching/src/test/resources/map_matching_v5_post.json

optimization-fixtures:
	# request an optimized car trip with no additional options
	curl "https://api.mapbox.com/optimized-trips/v1/mapbox/driving/-122.42,37.78;-122.45,37.91;-122.48,37.73?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-optimization/src/test/resources/optimized_trip.json

	curl "https://api.mapbox.com/optimized-trips/v1/mapbox/cycling/-122.42,37.78;-122.45,37.91;-122.48,37.73?steps=true&language=sv&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-optimization/src/test/resources/optimized_trip_steps.json

	curl "https://api.mapbox.com/optimized-trips/v1/mapbox/driving/13.388860,52.517037;13.397634,52.529407;13.428555,52.523219;13.418555,52.523215?roundtrip=true&distributions=3,1&access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-optimization/src/test/resources/optimized_trip_distributions.json

tilequery-fixtures:
	# Fetch features at Fort Mason, CA
	curl "https://api.mapbox.com/v4/mapbox.mapbox-streets-v7/tilequery/-122.42901,37.806332.json?access_token=$(MAPBOX_ACCESS_TOKEN)" \
		-o services-tilequery/src/test/resources/tilequery.json

	# Fetch features at Fort Mason, CA
	curl "https://api.mapbox.com/v4/mapbox.mapbox-streets-v7/tilequery/-122.42901,37.806332.json?access_token=$(MAPBOX_ACCESS_TOKEN)&" \
		-o services-tilequery/src/test/resources/tilequery-all-params.json

clean:
	./gradlew clean
