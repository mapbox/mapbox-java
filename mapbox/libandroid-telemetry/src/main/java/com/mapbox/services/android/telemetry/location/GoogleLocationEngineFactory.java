package com.mapbox.services.android.telemetry.location;


import android.content.Context;

class GoogleLocationEngineFactory implements LocationEngineSupplier {

  private static final String GOOGLE_LOCATION_SERVICES = "com.google.android.gms.location.LocationServices";
  private final ClasspathChecker classpathChecker;

  GoogleLocationEngineFactory(ClasspathChecker classpathChecker) {
    this.classpathChecker = classpathChecker;
  }

  @Override
  public LocationEngine supply(Context context) {
    return GoogleLocationEngine.getLocationEngine(context);
  }

  @Override
  public boolean hasDependencyOnClasspath() {
    return classpathChecker.hasDependencyOnClasspath(GOOGLE_LOCATION_SERVICES);
  }
}
