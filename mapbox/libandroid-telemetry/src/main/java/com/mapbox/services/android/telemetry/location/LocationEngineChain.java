package com.mapbox.services.android.telemetry.location;


import android.content.Context;

public interface LocationEngineChain {

  void nextChain(LocationEngineChain nextChain);

  LocationEngine supply(Context context);

  boolean hasDependencyOnClasspath();
}
