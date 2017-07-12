package com.mapbox.services.android.telemetry.location;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class LocationSourceProvider {

  private Map<String, LocationEngine> locationSourceDictionary;

  public LocationSourceProvider(Context context) {
    initLocationSourceDictionary(context);
  }

  public Map<String, LocationEngine> obtainLocationSourceDictionary() {
    return locationSourceDictionary;
  }

  private void initLocationSourceDictionary(Context context) {
    locationSourceDictionary = new HashMap<>(3);
    locationSourceDictionary.put("Google Play Services", GoogleLocationEngine.getLocationEngine(context));
    locationSourceDictionary.put("Lost", LostLocationEngine.getLocationEngine(context));
    locationSourceDictionary.put("Android", AndroidLocationEngine.getLocationEngine(context));
  }
}
