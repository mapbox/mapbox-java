package com.mapbox.services.android.telemetry.location;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationEngineProvider {
  private Map<LocationEngine.Type, LocationEngine> locationEngineDictionary;
  private static final List<LocationEngine.Type> OPTIONAL_LOCATION_ENGINES = new ArrayList<LocationEngine.Type>() {
    {
      add(LocationEngine.Type.GOOGLE_PLAY_SERVICES);
      add(LocationEngine.Type.LOST);
    }
  };

  public LocationEngineProvider(Context context) {
    initAvailableLocationEngines(context);
  }

  @NonNull
  public LocationEngine obtainBestLocationEngineAvailable() {
    return obtainBestLocationEngine();
  }

  @Nullable
  public LocationEngine obtainLocationEngineBy(LocationEngine.Type type) {
    LocationEngine locationEngine = locationEngineDictionary.get(type);
    return locationEngine;
  }

  private void initAvailableLocationEngines(Context context) {
    locationEngineDictionary = new HashMap<>();
    Map<LocationEngine.Type, LocationEngineSupplier> locationEnginesDictionary =
      obtainDefaultLocationEnginesDictionary();
    for (Map.Entry<LocationEngine.Type, LocationEngineSupplier> entry : locationEnginesDictionary.entrySet()) {
      LocationEngineSupplier locationEngineSupplier = entry.getValue();
      if (locationEngineSupplier.hasDependencyOnClasspath()) {
        LocationEngine available = locationEngineSupplier.supply(context);
        locationEngineDictionary.put(entry.getKey(), available);
      }
    }
  }

  private Map<LocationEngine.Type, LocationEngineSupplier> obtainDefaultLocationEnginesDictionary() {
    ClasspathChecker classpathChecker = new ClasspathChecker();
    Map<LocationEngine.Type, LocationEngineSupplier> locationSources = new HashMap<>();
    locationSources.put(LocationEngine.Type.GOOGLE_PLAY_SERVICES, new GoogleLocationEngineFactory(classpathChecker));
    locationSources.put(LocationEngine.Type.LOST, new LostLocationEngineFactory(classpathChecker));
    locationSources.put(LocationEngine.Type.ANDROID, new AndroidLocationEngineFactory());

    return locationSources;
  }

  private LocationEngine obtainBestLocationEngine() {
    LocationEngine bestLocationEngine = locationEngineDictionary.get(LocationEngine.Type.ANDROID);
    for (LocationEngine.Type type : OPTIONAL_LOCATION_ENGINES) {
      bestLocationEngine = locationEngineDictionary.get(type);
      if (bestLocationEngine != null) {
        return bestLocationEngine;
      }
    }
    return bestLocationEngine;
  }
}
