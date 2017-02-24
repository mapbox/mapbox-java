## Navigation

<!-- preview -->

The navigation part of Mapbox Java is built on top of our Directions API and contains logic needed to get timed navigation instructions. The calculations use the users current location and compare it to the current route the user's traversing to provide critical information at any given moment.

> Currently this is only offered for Driving instructions and takes traffic into consideration by default.

Much of the navigation APIs require being inside an Android application. However, we do expose some of the lower level logic inside the RouteUtils class.

Ensure your Android project includes the `mapbox-android-ui` dependency to gain full access to the navigation APIs. Much of the navigation logic is handled in an Android service meaning you'll be able to continue tracking the users progress along the navigation route even when your application is not in the foreground. Since this is a service, it's required to add the service inside your `AndroidManifest.xml` file. In addition, navigation requires adding two permissions, internet and fine location. The user location permission should also be checked during runtime using the PermissionManager.

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>

<application>

  ...

  <service android:name="com.mapbox.services.android.navigation.NavigationService"/>

</application>
```

## MapboxNavigation object

<!-- preview -->

Most of the navigation options are found inside the MapboxNavigation class including fetching the route, starting and ending the navigation session and attaching listeners for events you'd like to handle. Assign and initialize a new instance of MapboxNavigation inside your Navigation activity. When initializing, you'll need to pass in a `Context` and your Mapbox access token. Checkout the access token section in the getting started section to learn how to attain a free access token.

```java
MapboxNavigation navigation = new MapboxNavigation(this, MAPBOX_ACCESS_TOKEN);
```

### LocationEngine

<!-- preview -->

Navigation requires the users location in order to operate, this is done using the LocationEngine class introduced in 2.0. For detailed instructions on how to use this class, visit the LocationEngine documentation. You'll need to setup an instance of a location engine and pass it into the MapboxNavigation object.

```java
LocationEngine locationEngine = LostLocationEngine.getLocationEngine(this);
navigation.setLocationEngine(locationEngine);
```

### Requesting a route

<!-- preview -->

Now that you have setup a way for the MapboxNavigation object to get the users location, the other thing it will need is a route. The order in which these Positions are passed in doesn't matter as long as they are both provided before calling `getRoute`.

```java
navigation.setStart(Position.fromCoordinates());
navigation.setEnd(Position.fromCoordinates());
```

Actually requesting the route's done by calling `getRoute` passing in a new callback. If you've ever worked with Retrofit, the callback here will look familiar since this is what we are using under the hood.

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

<!-- preview -->

The RouteProgress class contains all progress information of the user along the route, leg and steps. The objects provided inside `AlertLevelChangeListener` and `ProgressChangeListener` allowing you to get distance measurements, the percentage of route complete, current step index, and much more. A full list of the APIs exposed in RouteProgress is given in the table below.

| API                         | Description           |
| --------------------------- |:-------------:|
| getPreviousAlertLevel       | Returns the previous alert level provided by the AlertLevelChangeListener |
| getRoute                    | Get the route acquired from the directions API and being used for navigation. |
| getDistanceTraveledOnRoute  | The total distance the user has traveled along the route   |
| getFractionTraveledOnRoute  | A `float` value between 0 and 1 giving the total percentage the user has completed in the navigation session. |
| getDistanceRemainingOnRoute | The distance between the users current location to the next maneuver.      |
| getLegIndex                 | The routes current leg index the user's on.      |
| getCurrentLeg               | Returns the routes current leg as a `routeLeg` object.    |
| getUpComingStep             | Returns the next step the user will be on from their current location.      |
| getStepIndex                | The routes current step index the user's on.      |
| getCurrentStep              | Returns the current step the user's on.       |
| getDistanceTraveledOnStep   | Measures from the current steps maneuver to the users snapped position.      |
| getDurationRemainingOnStep  | The estimated time remaining till the user reaches end of current step.      |
| getDistanceRemainingOnStep  | Measures from the users current snapped position to the last coordinate in the step.       |

> This object will be null from when the NavigationService starts till the first location change occurs. Therefore if you are using this to initialize a variable, it's reasonable to check if null beforehand. Since this object is mutable, the values given can change at anytime.

## Listeners

<!-- preview -->

Chances are, if you are using the navigation SDK, you'll want to listen in to events such as when the user makes progress along the route or when they go off route. By listening into these events you are able to provide the user with instructions at the proper times. At the bare minimum, it is strongly encouraged to use the `OnProgressChange` listener which is called every time the users locations updated.

### NavigationRunning

<!-- preview -->

The event callback is handy for being notified when the navigation session has started, user has canceled the session, or the user has arrived at their final destination. From this information you are able to determine when to show navigation notifications, know when it's safe to stop requesting user location updates, and much more.

```java
navigation.setEventCallback(new NavigationEventListener() {
  @Override
  public void onRunning(boolean running) {

  }
});
```

### AlertLevelChange

<!-- preview -->

Listening in to the alertLevelChange is useful for correctly getting the timing of user notifications while the user is traversing along the route. The listener's invoked only when the user's reached a specific point along the current step they are on. The alert thresholds can be adjusted within the constants file while developing and are based on time (in seconds) till the user reaches the next maneuver.

| Alert level                         | Description           |
| --------------------------- |:-------------:|
| `DEPART_ALERT_LEVEL` | Occurs when the user first leaves the origin and shouldn't be invoked again for the rest of the navigation session. |
| `LOW_ALERT_LEVEL` | Invoked right after the user performs a maneuver and they have started traversing along a new step. |
| `MEDIUM_ALERT_LEVEL` | Occurs when the user's x seconds away from their next maneuver. By default this is 70 seconds from next maneuver. |
| `HIGH_ALERT_LEVEL` | Useful to know when the users about to perform the next maneuver on the route. By default this is 15 seconds. |
| `ARRIVE_ALERT_LEVEL` | Occurs when the user has performed the last maneuver along the route and reached their final destination. |
| `NONE_ALERT_LEVEL` | In a rare case when the alert level can not be determined this might be invoked. |

> While the AlertLevelChange listener can help you correctly notify your users to perform an action, it relies heavily on the users current position, this means that the alert is rarely changed exactly at the correct time and might be delayed till a new location update occurs and the user is within the alerts threshold.

```java
navigation.setAlertLevelChangeListener(new AlertLevelChangeListener() {
  @Override
  public void onAlertLevelChange(int alertLevel, RouteProgress routeProgress) {
    switch (alertLevel) {
      case HIGH_ALERT_LEVEL:
        Toast.makeText(MainActivity.this, "HIGH", Toast.LENGTH_LONG).show();
        break;
      case MEDIUM_ALERT_LEVEL:
        Toast.makeText(MainActivity.this, "MEDIUM", Toast.LENGTH_LONG).show();
        break;
      case LOW_ALERT_LEVEL:
        Toast.makeText(MainActivity.this, "LOW", Toast.LENGTH_LONG).show();
        break;
      case ARRIVE_ALERT_LEVEL:
        Toast.makeText(MainActivity.this, "ARRIVE", Toast.LENGTH_LONG).show();
        break;
      case NONE_ALERT_LEVEL:
        Toast.makeText(MainActivity.this, "NONE", Toast.LENGTH_LONG).show();
        break;
      case DEPART_ALERT_LEVEL:
        Toast.makeText(MainActivity.this, "DEPART", Toast.LENGTH_LONG).show();
        break;
    }
  }
});
```

### OnProgressChange

<!-- preview -->

Similar to listening into user location changes, this listener's invoked every time the user's location changes but provides an updated RouteProgress object. This listener is strong encouraged to use since you can typically update most of your application's user interface. An example of this would be if you are displaying the users current progress till they need to perform the next maneuver, every time this listeners invoked, you are able to update your view with the new information from RouteProgress.

Besides receiving information about the route progress, the callback also provides you with the users current location which can provide their current speed, bearing, etc. If you have snapping to the route enabled, the location object will be updated to provide the snapped coordinates.

```java
navigation.setProgressChangeListener(new ProgressChangeListener() {
      @Override
      public void onProgressChange(Location location, RouteProgress routeProgress) {

      }
    });
```

### OffRoute

<!-- preview -->

Not implemented in SDK yet.

## RouteUtils

<!-- preview -->

The RouteUtils class can be found in the mapbox-java-services module and provides many of the methods used for calculations done in the RouteProgress object. Examples of this are getting the total route distance left till the user reaches their destination. If you would like to perform these calculations on your own or would like the change the behavior of navigation, you can directly call these methods and handle the calculations yourself. 
