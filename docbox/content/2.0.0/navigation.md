## Navigation

The navigation part of Mapbox Java is built on top of our Directions API and contains logic needed to get timed navigation instructions. The calculations use the users current location and compare it to the current route the user's traversing to provide critical information at any given moment.

> Currently this is only offered for Driving instructions and takes traffic into consideration by default.

Much of the navigation APIs require being inside an Android application. However, we do expose some of the lower level logic inside the RouteUtils class.

Ensure your Android project includes the `mapbox-android-services` dependency to gain full access to the navigation APIs.

## MapboxNavigation object

Most of the navigation options are found inside the MapboxNavigation class including fetching the route, starting and ending the navigation session and attaching listeners for events you'd like to handle. Assign and initialize a new instance of MapboxNavigation inside your Navigation activity. When initializing, you'll need to pass in a `Context` and your Mapbox access token. Checkout the access token section in the getting started section to learn how to attain a free access token.

```java
MapboxNavigation navigation = new MapboxNavigation(this, MAPBOX_ACCESS_TOKEN);
```

### LocationEngine

Navigation requires the users location in order to operate, this is done using the LocationEngine class introduced in 2.0. For detailed instructions on how to use this class, visit the LocationEngine documentation. You'll need to setup an instance of a location engine and pass it into the MapboxNavigation object.

```java
LocationEngine locationEngine = LostLocationEngine.getLocationEngine(this);
navigation.setLocationEngine(locationEngine);
```

### Requesting a route

Now that you have setup a way for the MapboxNavigation object to get the users location, the other thing it will need is a route. The order in which these Positions are passed in doesn't matter as long as they are both provided before calling `getRoute`.

```java
navigation.setStart(Position.fromCoordinates());
navigation.setEnd(Position.fromCoordinates());
```

Actually requesting the route's done by calling `getRoute` passing in a new Callback. If you've ever worked with Retrofit, the callback here will look familiar since this is what we are using under the hood.

```java
navigation.getRoute(new Callback<DirectionsResponse>() {
  @Override
  public void onResponse(
    Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

  }

  @Override
  public void onFailure(Call<DirectionsResponse> call, Throwable t) {

  }
});
```

## RouteProgress object

TODO

### Listeners

Chances are, if you are using the navigation SDK, you'll want to listen in to events such as when the user makes progress along the route or when they go off route. By listening into these events provide the user with instructions at the proper times.

### NavigationRunning

TODO
navigation.setEventCallback(this);

### AlertLevelChange

```java
navigation.setAlertLevelChangeListener(new AlertLevelChangeListener() {
              @Override
              public void onAlertLevelChange(int alertLevel, RouteProgress routeProgress) {

              }
            });
```

### OnProgressChange

### OffRoute

#### RouteUtils
