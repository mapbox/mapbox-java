package com.mapbox.services.mapmatching.v4.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Mapbox map matching API response
 *
 * @since 1.2.0
 */
public class MapMatchingResponse extends FeatureCollection {

  private static final String KEY_MATCHED_POINTS = "matchedPoints";

  private String code;

  public MapMatchingResponse(List<Feature> features) {
    super(features);
  }

  /**
   * A string depicting the state of the response.
   * <ul>
   * <li>"Ok" - Normal case</li>
   * <li>"NoMatch" - The input did not produce any matches. {@link Feature} will be an empty array.</li>
   * <li>"TooManyCoordinates" - There are more than 100 points in the request.</li>
   * <li>"InvalidInput" - message will hold an explanation of the invalid input.</li>
   * <li>"ProfileNotFound" - Profile should be {@code mapbox.driving}, {@code mapbox.walking},
   * or {@code mapbox.cycling}.</li>
   * </ul>
   *
   * @return String containing the code.
   * @since 1.2.0
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code String containing the code.
   * @since 1.2.0
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Convenience method to obtain the list of matched points.
   *
   * @return Array of {@link Position} that have been map matched.
   * @since 1.2.0
   */
  public Position[] getMatchedPoints() {
    return getMatchedPoints(0);
  }

  /**
   * Convenience method to obtain the list of matched points for other matches. When the matching
   * algorithm cannot decide the correct match between two points, it will omit that line and
   * create several sub-matches, each as a feature. The higher the number of features, the more
   * likely that the input traces are poorly aligned to the road network.
   *
   * @param submatch Which sub-match you want to get the position for.
   * @return Array of {@link Position} that have been map matched.
   * @since 1.2.0
   */
  public Position[] getMatchedPoints(int submatch) {
    JsonObject properties = getFeatures().get(submatch).getProperties();
    JsonArray points = properties.getAsJsonArray(KEY_MATCHED_POINTS);

    Position[] positions = new Position[points.size()];
    for (int i = 0; i < points.size(); i++) {
      positions[i] = Position.fromCoordinates(
        points.get(i).getAsJsonArray().get(0).getAsDouble(),
        points.get(i).getAsJsonArray().get(1).getAsDouble());
    }
    return positions;
  }

}
