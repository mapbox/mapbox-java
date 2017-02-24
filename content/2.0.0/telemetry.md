## Telemetry
Mapbox SDKs collect anonymous data about the map and device location to continuously update and improve your maps. For Android, it is designed to be lightweight and minimize impact on performance and battery life. If you are using our maps SDK, telemetry's already included in your project. In other cases you might want to use our locationEngine or permissionManager inside your project. The Telemetry module has no dependencies on any of the other Mapbox-Java modules and only depends on having the Android API plugin inside your project.

> Find out more about telemetry on [our website](https://www.mapbox.com/telemetry/)

### Setup
To start using telemetry independently inside your application, you'll first need to include the telemetry dependency and then add the following service and permissions inside your `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>

<application>

  ...

  <service android:name="com.mapbox.services.android.telemetry.service.TelemetryService"/>

</application>
```

## PermissionsManager
If your Android project is built targeting API level 23 or higher, your application will need to request permission during runtime. Handling this directly in your activity produces a bunch of boilerplate and can oftentimes be hard to get correct. That's where the PermissionsManager comes into play. With the PermissionsManager, you can check if the user has granted location permission and request permissions if the user hasn't granted them yet.

You'll notice that once you have setup your permission manager, you will still need to override your activities `onRequestPermissionsResult` and call the permissionsManagers identical method.

> **Note:** The PermissionsManager can be used for requesting additional permissions and not just location.

```java
permissionsManager = new PermissionsManager(<a new PermissionsListener>);
if (!permissionsManager.areLocationPermissionsGranted(this)) {
  permissionsManager.requestLocationPermissions(this);
}

@Override
public void onRequestPermissionsResult(
int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
  permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
```

### PermissionsListener
The permissionsListener needs to also be setup and passed into the PermissionsManager constructor. You'll notice that it overrides two methods, `onExplanationNeeded` and `onPermissionResult`. an explanation isn't required but strongly encouraged to allow the user to understand why you are requesting this permission.

The permission result is invoked once the user decided whether or not to allow or deny the permission. A boolean value is given which you can use to write an if statement. Both cases should be handled appropriate, if they approve, proceed with your permission sensitive logic. Otherwise, if they deny, it's good to display a message to the user if the permission is required for your application to work.

```java
PermissionsListener permissionsListener = new PermissionsListener() {
  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {

  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      // Permission sensitive logic called here
    } else {
      // User denied the permission
    }
  }
};
```

## LocationEngine
If your application needs location information, the locationEngine can help you acquire this information while also simplifying the process and being flexible enough to use different services. The LocationEngine found in the telemetry module currently supports the following location providers:

- [LOST](https://github.com/mapzen/lost/)
- Google Play Services
- Android Location
- Mock Location Engine

> **Note:** if you are using our Android Maps SDK, you can also set the locationEngine equal to the `LocationSource.getLocationEngine(this);` and it will use the same location engine used by the maps SDK. This eliminates the need to create a new locationEngine from scratch.

### Getting location updates
To start off, it's required to either include the mapbox-android-services or copy over the [LostLocationEngine class](https://github.com/mapbox/mapbox-java/blob/master/mapbox/libandroid-services/src/main/java/com/mapbox/services/android/location/LostLocationEngine.java) into your project. You'll then want to initialize a new instance of LocationEngine, activate it, and optionally add a location listener. Inside the `onConnected` you can begin requesting for location updates or wait for the appropriate time to do so.

```java
LocationEngine locationEngine = LostLocationEngine.getLocationEngine(this);
locationEngine.activate();
locationEngine.addLocationEngineListener(new LocationEngineListener() {
  @Override
  public void onConnected() {
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {

  }
});
```

To prevent your application from having a memory leak. It is a good idea to stop requesting location updated inside your activities `onStop` method and continue requesting them in `onStart`.

### Last location
If your application needs to quickly get a user location, you can call the `getLastLocation` which will return the users last position. You can then use the location object returned to determine the timing the location was given.

> **Note:** Careful with requesting the users last location since the location has the possibility of being null.

```java
Location lastLocation = locationEngine.getLastLocation();
if (lastLocation != null) {
  // Location logic here
}
```

### Mock Location Engine

<!-- preview -->

Not implemented in SDK yet.
