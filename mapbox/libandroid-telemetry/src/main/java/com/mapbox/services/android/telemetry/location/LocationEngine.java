package com.mapbox.services.android.telemetry.location;

import android.Manifest;
import android.location.Location;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresPermission;
import android.support.annotation.Size;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract implementation of a location engine. A location engine makes it simple to switch between providers without
 * the hassle of including boilerplate inside your code. This allows developers to use Google Play Services location
 * provider or the default LOST location provider, among others. For a good example setting up a location engine from
 * this abstract class, have a look at the {@code LostLocationEngine} found inside the {@code libandroid-services}
 * module.
 *
 * @since 2.0.0
 */
public abstract class LocationEngine {

  private static final int TWO_MINUTES = 1000 * 60 * 2;

  protected int priority;
  protected Integer interval = 1000;
  protected Integer fastestInterval = 1000;
  protected Float smallestDisplacement = 3.0f;
  protected CopyOnWriteArrayList<LocationEngineListener> locationListeners;

  /**
   * Construct a location engine.
   *
   * @since 2.0.0
   */
  public LocationEngine() {
    locationListeners = new CopyOnWriteArrayList<>();
  }

  /**
   * Activate the location engine which will connect whichever location provider you are using. You'll need to call
   * this before requesting user location updates using {@link LocationEngine#requestLocationUpdates()}.
   *
   * @since 2.0.0
   */
  public abstract void activate();

  /**
   * Disconnect the location engine, useful when you no longer need location updates or requesting the users
   * {@link LocationEngine#getLastLocation()}. Before deactivating you'll need to stop request user location updates
   * using {@link LocationEngine#removeLocationUpdates()}.
   *
   * @since 2.0.0
   */
  public abstract void deactivate();

  /**
   * Check if your location provider has been activated/connected. This is mainly used internally but is also useful in
   * the rare cases when you'd like to know if your location engine is connected or not.
   *
   * @return boolean true if the location engine has been activated/connected, else false.
   * @since 2.0.0
   */
  public abstract boolean isConnected();

  /**
   * When first initializing the location engine the location updates oftentimes aren't immediate and your user
   * experience might diminish since they are forced to wait till a more accurate update arrives. A solution to this is
   * to request the last known user location. There are no guarantees this won't return null since the {@link Location}
   * object is simply stored in cache.
   *
   * @return The last known user location as a {@link Location} object.
   * @since 2.0.0
   */
  @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
  public abstract Location getLastLocation();

  /**
   * if a {@link LocationEngineListener} is setup, registering for location updates will tell the provider to begin
   * sending updates.
   *
   * @since 2.0.0
   */
  @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
  public abstract void requestLocationUpdates();

  /**
   * When you no longer wish to receive location updates, you should call this method to prevent the devices battery
   * from draining. It's important to note that your location listeners will remain intake until you call
   * {@link LocationEngine#removeLocationEngineListener(LocationEngineListener)}.
   *
   * @since 2.0.0
   */
  public abstract void removeLocationUpdates();

  /**
   * Get the current priority being used.
   *
   * @return Integer representing one of the priorities listed inside the {@link LocationEnginePriority} file.
   * @since 2.0.0
   */
  public int getPriority() {
    return priority;
  }

  /**
   * This method sets the priority of the request, providing this will help the location provide know which location
   * sources to use. You'll find more information on the different priority values inside the
   * {@link LocationEnginePriority} file.
   *
   * @param priority One of the {@link LocationEnginePriority}s listed.
   * @since 2.0.0
   */
  public void setPriority(@IntRange(from = 0, to = 3) int priority) {
    this.priority = priority;
  }

  /**
   * Get the current interval being used to receive location updates. Default is 1 second.
   *
   * @return the current interval value being used in milliseconds.
   * @since 2.1.0
   */
  public int getInterval() {
    return interval;
  }

  /**
   * Set the rate in milliseconds at which your app prefers to receive location updates. Note that the location updates
   * may be faster than this rate if another app is receiving updates at a faster rate, or slower than this rate, or
   * there may be no updates at all (if the device has no connectivity, for example).
   *
   * @param interval integer in milliseconds defining the rate in which you'd like to receive location updates.
   * @since 2.1.0
   */
  public void setInterval(@Size(min = 0) int interval) {
    this.interval = interval;
  }

  /**
   * Get the current fastest interval being used to receive location updates. Default is 1 second.
   *
   * @return the current fastest interval value being used in milliseconds.
   * @since 2.1.0
   */
  public int getFastestInterval() {
    return fastestInterval;
  }

  /**
   * Set the fastest rate in milliseconds at which your application can handle location updates. You need to set
   * this rate because other apps also affect the rate at which updates are sent. If this rate is faster than your app
   * can handle, you may encounter problems with UI flicker or data overflow. To prevent this, use this method to set
   * an upper limit to the update rate.
   *
   * @param fastestInterval in milliseconds at which your app can handle location updates.
   * @since 2.1.0
   */
  public void setFastestInterval(@Size(min = 0) int fastestInterval) {
    this.fastestInterval = fastestInterval;
  }

  /**
   * Get the current smallest displacement being used to receive location updates. Default is 3 meters.
   *
   * @return the current smallest displacement value being used in meters.
   * @since 2.1.0
   */
  public float getSmallestDisplacement() {
    return smallestDisplacement;
  }

  /**
   * Set the smallest distance value in meters at which the user must move before a new location update comes in.
   *
   * @param smallestDisplacement in meters which the user must move before a new location update's supplied.
   * @since 2.1.0
   */
  public void setSmallestDisplacement(@Size(min = 0) float smallestDisplacement) {
    this.smallestDisplacement = smallestDisplacement;
  }

  /**
   * Useful when you'd like to add a location listener to handle location connections and update events. It is important
   * to note, that the callback will continue getting called even when your application isn't in the foreground.
   * Therefore, it is a good idea to use {@link LocationEngine#removeLocationEngineListener(LocationEngineListener)}
   * inside your activities {@code onStop()} method.
   *
   * @param listener A {@link LocationEngineListener} which you'd like to add to your location engine.
   * @since 2.0.0
   */
  public void addLocationEngineListener(LocationEngineListener listener) {
    if (!this.locationListeners.contains(listener)) {
      this.locationListeners.add(listener);
    }
  }

  /**
   * If you no longer need your {@link LocationEngineListener} to be invoked with every location update, use this
   * method to remove it. It's also important to remove your listeners before the activity is destroyed to prevent any
   * potential memory leaks.
   *
   * @param listener the {@link LocationEngineListener} you'd like to remove from this {@link LocationEngine}.
   * @return true if the listener has been removed, else false.
   * @since 2.0.0
   */
  public boolean removeLocationEngineListener(LocationEngineListener listener) {
    return this.locationListeners.remove(listener);
  }

  /**
   * Determines whether one Location reading is better than the current Location fix. This is great to check whether
   * the most recent location fix is more accurate then a previous. The result of this criteria also varies depending
   * on the use-cases of the application and field testing. More specifically, this method checks if the location
   * retrieved is significantly newer than the previous estimate, checks if the accuracy claimed by the location is
   * better or worse than the previous estimate, and check which provider the new location is from and determine if
   * you trust it more.
   *
   * @param location            The new Location that you want to evaluate.
   * @param currentBestLocation The current Location fix, to which you want to compare the new one.
   * @since 2.0.0
   */
  protected static boolean isBetterLocation(Location location, Location currentBestLocation) {
    if (currentBestLocation == null) {
      // A new location is always better than no location
      return true;
    }

    // Check whether the new location fix is newer or older
    long timeDelta = location.getTime() - currentBestLocation.getTime();
    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
    boolean isNewer = timeDelta > 0;

    if (isSignificantlyNewer) {
      // If it's been more than two minutes since the current location, use the new location
      // because the user has likely moved
      return true;
    } else if (isSignificantlyOlder) {
      // If the new location is more than two minutes older, it must be worse
      return false;
    }

    // Check whether the new location fix is more or less accurate
    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
    boolean isLessAccurate = accuracyDelta > 0;
    boolean isMoreAccurate = accuracyDelta < 0;
    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

    // Check if the old and new location are from the same provider
    boolean isFromSameProvider = isSameProvider(location.getProvider(),
      currentBestLocation.getProvider());

    // Determine location quality using a combination of timeliness and accuracy
    if (isMoreAccurate) {
      return true;
    } else if (isNewer && !isLessAccurate) {
      return true;
    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
      return true;
    }

    return false;
  }

  private static boolean isSameProvider(String provider1, String provider2) {
    if (provider1 == null) {
      return provider2 == null;
    }

    return provider1.equals(provider2);
  }

}
