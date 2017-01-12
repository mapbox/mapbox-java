package com.mapbox.services.android.telemetry.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Helps request permissions at runtime
 */

public class PermissionsManager {

  public static final String COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
  public static final String FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

  private final int REQUEST_PERMISSIONS_CODE = 0;

  private Activity activity;
  private PermissionsListener listener;

  public PermissionsManager(Activity activity) {
    this.activity = activity;
  }

  public PermissionsManager(Activity activity, PermissionsListener listener) {
    this.activity = activity;
    this.listener = listener;
  }

  public PermissionsListener getListener() {
    return listener;
  }

  public void setListener(PermissionsListener listener) {
    this.listener = listener;
  }

  public static boolean isPermissionGranted(Context context, String permission) {
    return ContextCompat.checkSelfPermission(context, permission)
      == PackageManager.PERMISSION_GRANTED;
  }

  public boolean isCoarseLocationPermissionGranted() {
    return isPermissionGranted(activity, COARSE_LOCATION_PERMISSION);
  }

  public boolean isFineLocationPermissionGranted() {
    return isPermissionGranted(activity, FINE_LOCATION_PERMISSION);
  }

  public boolean areLocationPermissionsGranted() {
    return isCoarseLocationPermissionGranted() && isFineLocationPermissionGranted();
  }

  public void requestLocationPermissions() {
    // Request fine location permissions by default
    requestLocationPermissions(true);
  }

  public void requestLocationPermissions(boolean requestFineLocation) {
    String[] permissions = requestFineLocation
      ? new String[] {FINE_LOCATION_PERMISSION}
      : new String[] {COARSE_LOCATION_PERMISSION};
    requestPermissions(permissions);
  }

  public void requestPermissions(String[] permissions) {
    ArrayList<String> permissionsToExplain = new ArrayList<>();
    for (String permission : permissions) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        permissionsToExplain.add(permission);
      }
    }

    if (listener != null && permissionsToExplain.size() > 0) {
      // The developer should show an explanation to the user asynchronously
      listener.onExplanationNeeded(permissionsToExplain);
    }


    ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSIONS_CODE);
  }

  /**
   * You should call this method from your activity onRequestPermissionsResult.
   *
   * @param requestCode  The request code passed in requestPermissions(android.app.Activity, String[], int)
   * @param permissions  The requested permissions. Never null.
   * @param grantResults The grant results for the corresponding permissions which is either
   *                     PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
   */
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case REQUEST_PERMISSIONS_CODE:
        if (listener != null) {
          boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
          listener.onPermissionResult(granted);
        }
        break;
      default:
        // Ignored
    }
  }

}
