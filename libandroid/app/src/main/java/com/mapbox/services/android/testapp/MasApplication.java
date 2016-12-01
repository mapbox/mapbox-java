package com.mapbox.services.android.testapp;

import android.app.Application;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.squareup.leakcanary.LeakCanary;

public class MasApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    LeakCanary.install(this);
    MapboxAccountManager.start(getApplicationContext(), Utils.getMapboxAccessToken(getApplicationContext()));

  }
}
