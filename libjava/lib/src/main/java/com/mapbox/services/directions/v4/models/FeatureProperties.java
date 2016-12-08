package com.mapbox.services.directions.v4.models;

/**
 * Properties describing a {@link DirectionsFeature}.
 *
 * @since 1.0.0
 * @deprecated Use Directions v5 instead
 */
@Deprecated
public class FeatureProperties {

  private String name;

  /**
   * Gives the name of the closest street to the {@link DirectionsFeature} point.
   *
   * @return String name.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public String getName() {
    return name;
  }

  /**
   * @param name String name.
   * @since 1.0.0
   * @deprecated Use Directions v5 instead
   */
  @Deprecated
  public void setName(String name) {
    this.name = name;
  }
}
