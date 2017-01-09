package com.mapbox.services.android.testapp;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.squareup.leakcanary.LeakCanary;

public class MasApplication extends Application {

  private static final String LOG_TAG = MasApplication.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();

    // Leak canary
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    LeakCanary.install(this);

    // Access token
    String mapboxAccessToken = Utils.getMapboxAccessToken(getApplicationContext());
    if (TextUtils.isEmpty(mapboxAccessToken)) {
      Log.w(LOG_TAG, "Warning: access token isn't set.");
    }
    MapboxAccountManager.start(getApplicationContext(), mapboxAccessToken);
  }
}
