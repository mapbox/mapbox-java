package com.mapbox.services.android.telemetry.location;


import android.content.Context;

interface LocationEngineSupplier {

  LocationEngine supply(Context context);

  boolean hasDependencyOnClasspath();
}
