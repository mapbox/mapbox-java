package com.mapbox.services.api.navigation.v5.osrm;

/**
 * Created by antonio on 3/3/17.
 */

public interface TokenizedInstructionHook {
  String compile(String instruction);
}
