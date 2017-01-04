package com.mapbox.services.android.testapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.constants.MapboxConstants;

public class Utils {

  /**
   * <p>
   * Returns the Mapbox access token set in the app resources.
   * </p>
   * It will first search the application manifest for a {@link MapboxConstants#KEY_META_DATA_MANIFEST}
   * meta-data value. If not found it will then attempt to load the access token from the
   * {@code res/values/dev.xml} development file.
   *
   * @param context The {@link Context} of the {@link android.app.Activity} or {@link android.app.Fragment}.
   * @return The Mapbox access token or null if not found.
   * @see MapboxConstants#KEY_META_DATA_MANIFEST
   */
  public static String getMapboxAccessToken(@NonNull Context context) {
    try {
      // Read out AndroidManifest
      PackageManager packageManager = context.getPackageManager();
      ApplicationInfo appInfo = packageManager
        .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      String token = appInfo.metaData.getString(MapboxConstants.KEY_META_DATA_MANIFEST);
      if (token == null || token.isEmpty()) {
        throw new IllegalArgumentException();
      }
      return token;
    } catch (Exception exception) {
      // Use fallback on string resource, used for development
      int tokenResId = context.getResources()
        .getIdentifier("mapbox_access_token", "string", context.getPackageName());
      return tokenResId != 0 ? context.getString(tokenResId) : null;
    }
  }

}
