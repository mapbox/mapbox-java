## Geocoder
Included in the mapbox-java-services module you'll find the wrapper for our geocoder API. Specifically, the MapboxGeocoding is used to request both geocoding and reverse geocoding. Forward geocoding will take a String such as a street address or point of interest and transform it into a [Position](#position-and-point-objects) object. Reverse geocoding does the opposite, taking in a Position object and transforming it into an address. The amount of detail provided in the response varies, for example one response might contain a full address while another response will only contain the city and country.

It's recommended to first read over the general [geocoding API](https://www.mapbox.com/api-documentation/#geocoding) documentation since not all available parameters are listed here.

> **Note:** Make sure you have included to correct permissions inside your AndroidManifest if you plan to use this API inside an Android application.

### Geocoding request
Before making the geocoding request, you must build the MapboxGeocoding object passing in two required parameters; a valid Mapbox [access token](#access-token) and a location/query (typically an address or description). Many other parameters are available to help bias and manipulate the response you receive.

> **Note:** If you are using our geocoder to find locations around the users position you can use `setProximity()` passing in their location as a Position object to bias results to around their location.

```java
MapboxGeocoding mapboxGeocoding = new MapboxGeocoding.Builder()
  .setAccessToken(Mapbox.getAccessToken())
  .setLocation("1600 Pennsylvania Ave NW")
  .build();

```

### Geocoding response
Once you have built your MapboxGeocoding object with all the parameters you'd like to use in the request, you'll need to asynchronously send the request using enqueueCall. Once the request receives a response it will notify the Callback where you can handle the response appropriately.

> **Note:** incase your user leaves the activity or application before the callback's notified, you should use `mapboxGeocoding.cancelCall()` within your onDestroy lifecycle method.

```java
mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
  @Override
  public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
    List<CarmenFeature> results = response.body().getFeatures();
    if (results.size() > 0) {
      // Log the first results position.
      Position firstResultPos = results.get(0).asPosition();
      Log.d(TAG, "onResponse: " + firstResultPos.toString());
    } else {
      // No result for your request were found.
      Log.d(TAG, "onResponse: No result found");
    }
  }

  @Override
  public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
    throwable.printStackTrace();
  }
});
```

### Reverse geocoding
The process of turning a string address to a coordinate is called reverse geocoding. Instead of supplying the builder with a string address you'd pass in coordinates instead. Handling the response is similar to forward geocoding. While one coordinate is given, you'll oftentimes get multiple results from the response all valid ways to describe to specific location. For example, one might be the street name while another result will be the country. The ordering of the list usually goes from most relevant to least.

You can narrow the response similar to forward geocoding by biasing the result using the available parameters provided in the builder.

```java
MapboxGeocoding reverseGeocode = new MapboxGeocoding.Builder()
  .setAccessToken(Mapbox.getAccessToken())
        .setCoordinates(Position.fromCoordinates(-77.03655, 38.89770))
        .setGeocodingType(GeocodingCriteria.TYPE_ADDRESS)
        .build();

// The result of this reverse geocode will give you "Pennsylvania Ave NW"
```
### Android widgets
In the geocoding examples above, we have been performing request to the API by directly using the builder, making the call, and then handling the response. The Android widgets found in the mapbox-android-ui module make the process of geocoding a bit easier by offering basic widgets.

For forward geocoding, the AutoCompleteWidget is offered. It provides a search box with support for autocomplete results. When the user clicks a result, `onFeatureClick`'s invoked and you can get the addresses coordinates and handle them accordingly.

<!-- TODO link Example -->
```xml
<com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView
  android:id="@+id/autoCompleteWidget"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:hint="Search Location"
  android:maxLines="1"/>
```

```java
// Set up autocomplete widget
GeocoderAutoCompleteView autoComplete = (GeocoderAutoCompleteView) findViewById(R.id.autoCompleteWidget);
autoComplete.setAccessToken(Mapbox.getAccessToken());
autoComplete.setType(GeocodingCriteria.TYPE_POI);
autoComplete.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
  @Override
  public void onFeatureClick(CarmenFeature feature) {
    Position position = feature.asPosition();
    // using the position you can drop a marker or move the maps camera.
  }
});
```

### Batch geocoding
Batch requests have the same parameters as normal requests, but can include more than one query by separating queries with the ; character. The mode parameter also needs to use `MODE_PLACES_PERMANENT`. You can do up to 50 forward or reverse geocoding queries in a single request. The response is an array of individual geocoder responses formatted the same as individual results. Each query in a batch request counts individually against your account's rate limits.

```java
MapboxGeocoding client = new MapboxGeocoding.Builder()
  .setAccessToken(Mapbox.getAccessToken())
  .setMode(GeocodingCriteria.MODE_PLACES_PERMANENT)
  .setLocation("20001;20009;22209")
  .setBaseUrl(mockUrl.toString())
  .build();
```
