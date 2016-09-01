package com.mapbox.services.android.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Utilities to handle the new permission system in 6.0. Weonly focus on ACCESS_FINE_LOCATION.
 *
 * @see <a href="http://developer.android.com/training/permissions/index.html">Working with System Permissions</a>
 * @since 1.0.0
 */
public class PermissionsUtils {

  private static final String permission = Manifest.permission.ACCESS_FINE_LOCATION;

  private static final int LOCATION_PERMISSIONS_REQUEST = 0;

  private static final String MESSAGE_RATIONALE =
    "This app needs access to your GPS location to locate you on the map.";

  private static final String MESSAGE_FALLBACK =
    "Access to your GPS location has been disabled.";

  /**
   * We only need to check for ACCESS_FINE_LOCATION as this implies ACCESS_COARSE_LOCATION:
   *
   * @param activity The activity where this is methods being used.
   * @return boolean true if location permission has been granted.
   * @see <a href="http://developer.android.com/guide/topics/location/strategies.html#Permission">Requesting User Permissions</a>
   * @see <a href="https://developers.google.com/maps/documentation/android-api/location?#location_permissions">Location permissions</a>
   * @since 1.0.0
   */
  public static boolean isLocationGranted(Activity activity) {
    int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
    return (permissionCheck == PackageManager.PERMISSION_GRANTED);
  }

  /**
   * Request user permission
   *
   * @param activity The activity where this is methods being used.
   * @since 1.0.0
   */
  public static void startPermissionFlow(Activity activity) {
    startPermissionFlow(activity, MESSAGE_RATIONALE);
  }

  /**
   * Request user permission
   *
   * @param activity  The activity where this is methods being used.
   * @param rationale Defaults "MESSAGE_RATIONALE"
   * @since 1.0.0
   */
  public static void startPermissionFlow(Activity activity, String rationale) {
    // Should we show an explanation?
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
      // Show an explanation to the user *asynchronously* -- don't block
      // this thread waiting for the user's response! After the user
      // sees the explanation, try again to request the permission.
      Toast.makeText(activity, rationale, Toast.LENGTH_LONG).show();
      requestPermissions(activity);
    } else {
      // No explanation needed, we can request the permission.
      requestPermissions(activity);
    }
  }

  private static void requestPermissions(Activity activity) {
    ActivityCompat.requestPermissions(activity,
      new String[] {permission},
      LOCATION_PERMISSIONS_REQUEST);
  }

  /**
   * Use this method to check if the request was successful or not.
   *
   * @param requestCode  The request code passed in requestPermissions(android.app.Activity, String[], int)
   * @param permissions  The requested permissions. Never null.
   * @param grantResults The grant results for the corresponding permissions which is either
   *                     PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
   * @return boolean true if permission was granted.
   * @since 1.0.0
   */
  public static boolean isRequestSuccessful(int requestCode, String[] permissions, int[] grantResults) {
    boolean result = false;
    if (requestCode == LOCATION_PERMISSIONS_REQUEST) {
      // If request is cancelled, the result arrays are empty
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // permission was granted
        result = true;
      }
    }
    return result;
  }

  /**
   * Display to the user when their device GPS has been disabled.
   *
   * @param activity The activity where the message is being used.
   * @since 1.0.0
   */
  public static void explainFallback(Activity activity) {
    explainFallback(activity, MESSAGE_FALLBACK);
  }

  /**
   * Display to the user when their device GPS has been disabled.
   *
   * @param activity The activity where the message is being used.
   * @param message  The message, should either be MESSAGE_RATIONALE or MESSAGE_FALLBACK.
   * @since 1.0.0
   */
  public static void explainFallback(Activity activity, String message) {
    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
  }

}
