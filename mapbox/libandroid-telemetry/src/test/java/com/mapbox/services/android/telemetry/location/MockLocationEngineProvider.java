package com.mapbox.services.android.telemetry.location;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;

class MockLocationEngineProvider implements LocationEngineChain {

  private final ClasspathChecker classpathChecker;
  private final String classpath;
  private LocationEngineChain chain;
  private Map<String, LocationEngine> locationEngineDictionary;

  MockLocationEngineProvider(ClasspathChecker classpathChecker, String classpath) {
    this.classpathChecker = classpathChecker;
    this.classpath = classpath;
    initLocationEngineDictionary();
  }

  @Override
  public void nextChain(LocationEngineChain nextChain) {
    this.chain = nextChain;
  }

  @Override
  public LocationEngine supply(Context context) {
    if (hasDependencyOnClasspath()) {
      return locationEngineDictionary.get(classpath);
    } else {
      return chain.supply(context);
    }
  }

  @Override
  public boolean hasDependencyOnClasspath() {
    return classpathChecker.hasDependencyOnClasspath(classpath);
  }

  private void initLocationEngineDictionary() {
    locationEngineDictionary = new HashMap<>(3);
    locationEngineDictionary.put("Google", new MockGoogleLocationSource());
    locationEngineDictionary.put("Lost", new MockLostLocationSource());
    locationEngineDictionary.put("Android", new MockAndroidLocationSource());
  }
}
