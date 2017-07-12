package com.mapbox.services.android.telemetry.location;


import android.content.Context;

import java.util.List;

public class LocationSourceChainSupplier {

  private final List<LocationSourceChain> locationSources;

  public LocationSourceChainSupplier(List<LocationSourceChain> locationSources) {
    this.locationSources = locationSources;
    configChainPriority();
  }

  public LocationEngine supply(Context context) {
    checkLocationSourcesEmpty();
    LocationSourceChain firstChain = locationSources.get(0);
    return firstChain.supply(context);
  }

  private void configChainPriority() {
    for (int i = 0; i < locationSources.size() - 1; i++) {
      LocationSourceChain followingChain = locationSources.get(i + 1);
      locationSources.get(i).nextChain(followingChain);
    }
  }

  private void checkLocationSourcesEmpty() {
    if (locationSources.isEmpty()) {
      locationSources.add(new AndroidLocationSourceChain());
    }
  }
}
