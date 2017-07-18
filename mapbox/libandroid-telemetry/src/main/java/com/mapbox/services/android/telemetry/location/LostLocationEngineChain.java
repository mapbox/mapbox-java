package com.mapbox.services.android.telemetry.location;


import android.content.Context;

public class LostLocationEngineChain implements LocationEngineChain {

  private static final String LOST_LOCATION_SERVICES = "com.mapzen.android.lost.api.LocationServices";
  private final ClasspathChecker classpathChecker;
  private LocationEngineChain chain;

  public LostLocationEngineChain(ClasspathChecker classpathChecker) {
    this.classpathChecker = classpathChecker;
  }

  @Override
  public void nextChain(LocationEngineChain nextChain) {
    this.chain = nextChain;
  }

  @Override
  public LocationEngine supply(Context context) {
    if (hasDependencyOnClasspath()) {
      return LostLocationEngine.getLocationEngine(context);
    } else {
      return chain.supply(context);
    }
  }

  @Override
  public boolean hasDependencyOnClasspath() {
    return classpathChecker.hasDependencyOnClasspath(LOST_LOCATION_SERVICES);
  }
}
