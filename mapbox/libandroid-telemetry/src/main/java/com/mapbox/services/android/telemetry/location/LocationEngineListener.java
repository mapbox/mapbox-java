package com.mapbox.services.android.telemetry.location;

import android.location.Location;

/**
 * Created by antonio on 1/11/17.
 */

public interface LocationEngineListener {

  void onConnected();

  void onLocationChanged(Location location);

}
