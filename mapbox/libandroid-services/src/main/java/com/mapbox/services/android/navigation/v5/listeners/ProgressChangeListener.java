package com.mapbox.services.android.navigation.v5.listeners;

import android.location.Location;

import com.mapbox.services.Experimental;
import com.mapbox.services.api.navigation.v5.RouteProgress;

/**
 * This is an experimental API. Experimental APIs are quickly evolving and
 * might change or be removed in minor versions.
 */
@Experimental
public interface ProgressChangeListener {
  void onProgressChange(Location location, RouteProgress routeProgress);
}
