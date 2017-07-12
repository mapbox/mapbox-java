package com.mapbox.services.android.telemetry.location;


import android.content.Context;

public interface LocationSourceChain {

  void nextChain(LocationSourceChain nextChain);

  LocationEngine supply(Context context);
}
