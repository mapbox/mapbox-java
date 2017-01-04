package com.mapbox.services.api.distance.v1.models;

import com.google.gson.JsonArray;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import java.util.List;

public class DistanceResponse extends FeatureCollection {

  private String code;
  private JsonArray durations;

  /**
   * Protected constructor.
   * Unlike other GeoJSON objects in this package, this constructor is protected to enable
   * the deserialization of the Distance service response.
   *
   * @param features List of {@link Feature}.
   * @since 2.0.0
   */
  protected DistanceResponse(List<Feature> features) {
    super(features);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int[][] getDurations() {

    int[][] tempDurations = new int[durations.size()][durations.size()];

    for (int i = 0; i < durations.size(); i++) {
      for (int j = 0; j < durations.size(); j++) {
        tempDurations[i][j] = durations.get(i).getAsJsonArray().get(j).getAsInt();
      }
    }
    return tempDurations;
  }
}
