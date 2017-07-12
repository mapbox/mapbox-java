package com.mapbox.services.android.telemetry.location;


import android.content.Context;

public class AndroidLocationSourceChain implements LocationSourceChain {

  @Override
  public void nextChain(LocationSourceChain nextChain) {
    // Last location source in the chain
  }

  @Override
  public LocationEngine supply(Context context) {
    return AndroidLocationEngine.getLocationEngine(context);
  }
}
