package com.mapbox.services.android.telemetry.location;


import android.content.Context;

class AndroidLocationEngineFactory implements LocationEngineSupplier {

  @Override
  public LocationEngine supply(Context context) {
    return AndroidLocationEngine.getLocationEngine(context);
  }

  @Override
  public boolean hasDependencyOnClasspath() {
    return true;
  }
}
