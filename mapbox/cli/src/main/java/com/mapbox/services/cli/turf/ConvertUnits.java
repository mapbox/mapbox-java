package com.mapbox.services.cli.turf;

import com.mapbox.services.api.utils.turf.TurfHelpers;

public class ConvertUnits {

  public static void main(String[] args) {
    double z = TurfHelpers.convertDistance(-10d, " ", null);
  }
}
