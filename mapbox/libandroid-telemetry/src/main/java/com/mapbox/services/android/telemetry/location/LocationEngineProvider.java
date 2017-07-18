package com.mapbox.services.android.telemetry.location;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationEngineProvider {

  private static final String GOOGLE_PLAY_SERVICES = "Google Play Services";
  private static final String LOST = "Lost";
  private static final String ANDROID = "Android";
  private Map<String, LocationEngine> locationEngineDictionary;
  private List<LocationEngine> locationEngines;

  public LocationEngineProvider(Context context) {
    initAvailableLocationEngines(context);
  }

  public Map<String, LocationEngine> obtainLocationEngineDictionary() {
    return locationEngineDictionary;
  }

  public List<LocationEngine> obtainAvailableLocationEngines() {
    return locationEngines;
  }

  private void initAvailableLocationEngines(Context context) {
    locationEngineDictionary = new HashMap<>();
    Map<String, LocationEngineChain> locationEnginesDictionary = obtainDefaultLocationEnginesDictionary();
    locationEngines = new ArrayList<>();
    for (Map.Entry<String, LocationEngineChain> entry : locationEnginesDictionary.entrySet()) {
      LocationEngineChain locationEngineChain = entry.getValue();
      if (locationEngineChain.hasDependencyOnClasspath()) {
        LocationEngine available = locationEngineChain.supply(context);
        locationEngineDictionary.put(entry.getKey(), available);
        locationEngines.add(available);
      }
    }
  }

  private Map<String, LocationEngineChain> obtainDefaultLocationEnginesDictionary() {
    ClasspathChecker classpathChecker = new ClasspathChecker();
    Map<String, LocationEngineChain> locationSources = new HashMap<>();
    locationSources.put(GOOGLE_PLAY_SERVICES, new GoogleLocationEngineChain(classpathChecker));
    locationSources.put(LOST, new LostLocationEngineChain(classpathChecker));
    locationSources.put(ANDROID, new AndroidLocationEngineChain());

    return locationSources;
  }
}
