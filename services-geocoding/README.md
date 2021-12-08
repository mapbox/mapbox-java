# Geocoder

[The Mapbox Search SDK for Android](https://docs.mapbox.com/android/search/guides/) is the recommended way to access the [Mapbox Geocoding API](https://docs.mapbox.com/api/search/geocoding/) on the Android platform. If you've used `Geocoder` to integrate search functionality into an Android application, you should switch to Search SDK using the [Migrate from Geocoder](https://docs.mapbox.com/android/search/guides/geocoder-migration/) guide.

## Overview

`Geocoder` artifact provides a Java wrapper around [Mapbox Geocoding API](https://docs.mapbox.com/api/search/geocoding/) service. 

[MapboxGeocoding](https://github.com/mapbox/mapbox-java/blob/main/services-geocoding/src/main/java/com/mapbox/api/geocoding/v5/MapboxGeocoding.java#L64) is used to request both forward and reverse geocoding information. **Forward geocoding** will take a `String`, such as a street address or point of interest, and transform it into a `Point` object. **Reverse geocoding** does the opposite, taking in a `Point` object and transforming it into an address. The amount of detail provided in the response varies. For example, one response might contain a full address while another response will only contain the city and country.

All of the documentation is available on a [Geocoder documentation page](https://docs.mapbox.com/android/java/guides/geocoder/).