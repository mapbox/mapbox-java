package com.mapbox.services.android.telemetry.location;


import android.content.Context;

public class AndroidLocationEngineChain implements LocationEngineChain {

  @Override
  public void nextChain(LocationEngineChain nextChain) {
    // Last location source in the chain
  }

  @Override
  public LocationEngine supply(Context context) {
    return AndroidLocationEngine.getLocationEngine(context);
  }

  @Override
  public boolean hasDependencyOnClasspath() {
    return true;
  }
}
