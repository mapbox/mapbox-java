package com.mapbox.services.android.telemetry.location;


import android.content.Context;

public class GoogleLocationEngineChain implements LocationEngineChain {

  private static final String GOOGLE_LOCATION_SERVICES = "com.google.android.gms.location.LocationServices";
  private final ClasspathChecker classpathChecker;
  private LocationEngineChain chain;

  public GoogleLocationEngineChain(ClasspathChecker classpathChecker) {
    this.classpathChecker = classpathChecker;
  }

  @Override
  public void nextChain(LocationEngineChain nextChain) {
    this.chain = nextChain;
  }

  @Override
  public LocationEngine supply(Context context) {
    if (hasDependencyOnClasspath()) {
      return GoogleLocationEngine.getLocationEngine(context);
    } else {
      return chain.supply(context);
    }
  }

  @Override
  public boolean hasDependencyOnClasspath() {
    return classpathChecker.hasDependencyOnClasspath(GOOGLE_LOCATION_SERVICES);
  }
}
