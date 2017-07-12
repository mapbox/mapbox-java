package com.mapbox.services.android.telemetry.location;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;

class MockLocationSourceProvider implements LocationSourceChain {

  private final ClasspathChecker classpathChecker;
  private final String classpath;
  private LocationSourceChain chain;
  private Map<String, LocationEngine> locationSourceDictionary;

  MockLocationSourceProvider(ClasspathChecker classpathChecker, String classpath) {
    this.classpathChecker = classpathChecker;
    this.classpath = classpath;
    initLocationSourceDictionary();
  }

  @Override
  public void nextChain(LocationSourceChain nextChain) {
    this.chain = nextChain;
  }

  @Override
  public LocationEngine supply(Context context) {
    if (classpathChecker.hasDependencyOnClasspath(classpath)) {
      return locationSourceDictionary.get(classpath);
    } else {
      return chain.supply(context);
    }
  }

  private void initLocationSourceDictionary() {
    locationSourceDictionary = new HashMap<>(3);
    locationSourceDictionary.put("Google", new MockGoogleLocationSource());
    locationSourceDictionary.put("Lost", new MockLostLocationSource());
    locationSourceDictionary.put("Android", new MockAndroidLocationSource());
  }
}
