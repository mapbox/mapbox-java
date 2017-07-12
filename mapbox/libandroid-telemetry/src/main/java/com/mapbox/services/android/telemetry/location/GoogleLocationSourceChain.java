package com.mapbox.services.android.telemetry.location;


import android.content.Context;

public class GoogleLocationSourceChain implements LocationSourceChain {

  private static final String GOOGLE_LOCATION_SERVICES = "com.google.android.gms.location.LocationServices";
  private final ClasspathChecker classpathChecker;
  private LocationSourceChain chain;

  public GoogleLocationSourceChain(ClasspathChecker classpathChecker) {
    this.classpathChecker = classpathChecker;
  }

  @Override
  public void nextChain(LocationSourceChain nextChain) {
    this.chain = nextChain;
  }

  @Override
  public LocationEngine supply(Context context) {
    if (classpathChecker.hasDependencyOnClasspath(GOOGLE_LOCATION_SERVICES)) {
      return GoogleLocationEngine.getLocationEngine(context);
    } else {
      return chain.supply(context);
    }
  }
}
