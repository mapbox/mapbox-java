package com.mapbox.services.android.testapp;

import android.app.Application;

import com.mapbox.mapboxsdk.MapboxAccountManager;

public class MasApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    MapboxAccountManager.start(getApplicationContext(), Utils.getMapboxAccessToken(getApplicationContext()));

  }
}
