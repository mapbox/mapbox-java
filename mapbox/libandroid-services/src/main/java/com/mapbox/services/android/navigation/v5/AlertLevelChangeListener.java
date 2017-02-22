package com.mapbox.services.android.navigation.v5;

import com.mapbox.services.Experimental;
import com.mapbox.services.api.navigation.v5.RouteProgress;

/**
 * This is an experimental API. Experimental APIs are quickly evolving and
 * might change or be removed in minor versions.
 */
@Experimental
public interface AlertLevelChangeListener {
  void onAlertLevelChange(int alertLevel, RouteProgress routeProgress);
}
