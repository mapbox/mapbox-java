package com.mapbox.services.directions.v4.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the geometry of a {@link DirectionsFeature}.
 *
 * @since 1.0.0
 */
public class FeatureGeometry {

  private String type;
  private List<Double> coordinates;

  /**
   * Builder
   *
   * @since 1.0.0
   */
  public FeatureGeometry() {
    coordinates = new ArrayList<>();
  }

  /**
   * Gives GeoJSON geometry type.
   *
   * @return string naming GeoJSON geometry type.
   * @since 1.0.0
   */
  public String getType() {
    return type;
  }

  /**
   * @param type The GeoJSON geometry type.
   * @since 1.0.0
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gives the coordinate of the point. Longitude will always be first (index 0) in list and
   * latitude will be second (index 1).
   *
   * @return List of Double objects containing a point with longitude and latitude values.
   * @since 1.0.0
   */
  public List<Double> getCoordinates() {
    return coordinates;
  }

  /**
   * @param coordinates List of Double objects containing a point with longitude and latitude
   *                    values.
   * @since 1.0.0
   */
  public void setCoordinates(List<Double> coordinates) {
    this.coordinates = coordinates;
  }
}
