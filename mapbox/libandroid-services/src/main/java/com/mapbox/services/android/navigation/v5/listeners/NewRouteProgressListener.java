package com.mapbox.services.android.navigation.v5.listeners;

import com.mapbox.services.api.navigation.v5.RouteProgress;

public interface NewRouteProgressListener {
  void onRouteProgressChange(RouteProgress routeProgress);
}
