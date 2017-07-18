package com.mapbox.services.android.telemetry.location;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class LocationEngineChainSupplier {

  private final List<LocationEngineChain> locationEngines;

  public LocationEngineChainSupplier() {
    this.locationEngines = new ArrayList<>();
  }

  public LocationEngineChainSupplier(List<LocationEngineChain> locationEngines) {
    this.locationEngines = locationEngines;
    configChainPriority();
  }

  public LocationEngine supply(Context context) {
    checkLocationSourcesEmpty();
    LocationEngineChain firstChain = locationEngines.get(0);
    return firstChain.supply(context);
  }

  private void configChainPriority() {
    for (int i = 0; i < locationEngines.size() - 1; i++) {
      LocationEngineChain followingChain = locationEngines.get(i + 1);
      locationEngines.get(i).nextChain(followingChain);
    }
  }

  private void checkLocationSourcesEmpty() {
    if (locationEngines.isEmpty()) {
      locationEngines.add(new AndroidLocationEngineChain());
    }
  }
}
