package com.mapbox.services.android.telemetry.location;


import android.content.Context;

public class LostLocationSourceChain implements LocationSourceChain {

  private static final String LOST_LOCATION_SERVICES = "com.mapzen.android.lost.api.LocationServices";
  private final ClasspathChecker classpathChecker;
  private LocationSourceChain chain;

  public LostLocationSourceChain(ClasspathChecker classpathChecker) {
    this.classpathChecker = classpathChecker;
  }

  @Override
  public void nextChain(LocationSourceChain nextChain) {
    this.chain = nextChain;
  }

  @Override
  public LocationEngine supply(Context context) {
    if (classpathChecker.hasDependencyOnClasspath(LOST_LOCATION_SERVICES)) {
      return LostLocationEngine.getLocationEngine(context);
    } else {
      return chain.supply(context);
    }
  }
}
