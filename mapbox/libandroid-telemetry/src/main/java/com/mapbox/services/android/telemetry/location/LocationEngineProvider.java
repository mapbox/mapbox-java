package com.mapbox.services.android.telemetry.location;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class LocationEngineProvider {

  public static final String GOOGLE_PLAY_SERVICES = "Google Play Services";
  public static final String LOST = "Lost";
  public static final String ANDROID = "Android";
  private Map<String, LocationEngine> locationEngineDictionary;

  public LocationEngineProvider(Context context) {
    initAvailableLocationEngines(context);
  }

  public Map<String, LocationEngine> obtainAvailableLocationEngines() {
    return locationEngineDictionary;
  }

  private void initAvailableLocationEngines(Context context) {
    locationEngineDictionary = new HashMap<>();
    Map<String, LocationEngineSupplier> locationEnginesDictionary = obtainDefaultLocationEnginesDictionary();
    for (Map.Entry<String, LocationEngineSupplier> entry : locationEnginesDictionary.entrySet()) {
      LocationEngineSupplier locationEngineSupplier = entry.getValue();
      if (locationEngineSupplier.hasDependencyOnClasspath()) {
        LocationEngine available = locationEngineSupplier.supply(context);
        locationEngineDictionary.put(entry.getKey(), available);
      }
    }
  }

  private Map<String, LocationEngineSupplier> obtainDefaultLocationEnginesDictionary() {
    ClasspathChecker classpathChecker = new ClasspathChecker();
    Map<String, LocationEngineSupplier> locationSources = new HashMap<>();
    locationSources.put(GOOGLE_PLAY_SERVICES, new GoogleLocationEngineFactory(classpathChecker));
    locationSources.put(LOST, new LostLocationEngineFactory(classpathChecker));
    locationSources.put(ANDROID, new AndroidLocationEngineFactory());

    return locationSources;
  }
}
