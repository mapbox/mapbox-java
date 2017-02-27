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
The process of turning a string address to a coordinate is called reverse geocoding.

### Android widgets

<!-- TODO link Example -->

### Batch geocoding
