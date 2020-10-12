package com.mapbox.api.directions.v5.models;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The file exposes speed limit annotations.
 */
public class SpeedLimit {
  /**
   * Speed limit unit in km/h.
   */
  public static final String KMPH = "km/h";

  /**
   * Speed limit unit in mph.
   */
  public static final String MPH = "mph";

  /**
   * Speed limit unit.
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
      MPH,
      KMPH
  })
  public @interface Unit {
  }
}
