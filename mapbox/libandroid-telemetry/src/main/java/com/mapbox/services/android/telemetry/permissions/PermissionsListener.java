package com.mapbox.services.android.telemetry.permissions;

import java.util.List;

/**
 * Created by antonio on 1/11/17.
 */

public interface PermissionsListener {

  void onExplanationNeeded(List<String> permissionsToExplain);
  void onPermissionResult(boolean granted);

}
