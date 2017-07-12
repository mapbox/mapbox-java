package com.mapbox.services.android.telemetry.location;


public class ClasspathChecker {

  boolean hasDependencyOnClasspath(String className) {
    try {
      Class.forName(className);
      return true;
    } catch (ClassNotFoundException ignored) {
      // Empty
    }
    return false;
  }
}
