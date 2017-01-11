package com.mapbox.services.api.mapmatching.v4.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapbox map matching API response
 *
 * @since 1.2.0
 */
public class MapMatchingResponse extends FeatureCollection {

  private static final String KEY_MATCHED_POINTS = "matchedPoints";

  private String code;

  /** A placeholder for the best matched points for optimizing multiple calls */
  private Position[] bestMatchedPoints;

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
    return bestMatchedPoints != null ? bestMatchedPoints : (bestMatchedPoints = getMatchedPoints(0));
  }

  /**
   * Convenience method to obtain the list of matched points for other matches. When the matching
   * algorithm cannot decide the correct match between two points, it will omit that line and
   * create several sub-matches, each as a feature. The higher the number of features, the more
   * likely that the input traces are poorly aligned to the road network.
   *
   * <p>
   * Implementation Note:<br>
   * A caller should consider using returned array as an instance rather than as a reference to the array
   *
   * <pre>
   *   for (int index = 0; index < MapMatchingResponse.getMatchedPoints(bestIndex).length; index++) {
   *     .. do some work
   *     double latitude = MapMatchingResponse.getMatchedPoints(bestIndex).getLatitude();
   *     .. do more work
   *   }
   * </pre>
   * <p>This can be optimized in performance by doing below</p>
   * <pre>
   *   Position[] points = MapMatchingResponse.getMatchedPoints(bestIndex);
   *   for (int index = 0; index < points.length; index++) {
   *     .. do some work
   *     double latitude = points[index].getLatitude();
   *     .. do more work
   *   }
   * </pre>
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

  /**
   * Convenience method to get a confidence value from Map Matching Response
   * @see <a href="https://www.mapbox.com/api-documentation/#retrieve-a-match">Map Matching API Documentation</a>
   *
   * @param submatch A MapMatching sub-match value
   * @return a confidence value, null when property doesn't exist
   */
  public Number getConfidence(int submatch) {
    final String CONFIDENCE = "confidence";
    if (!getFeatures().get(submatch).hasProperty(CONFIDENCE)) {
      return null;
    }

    return getFeatures().get(submatch).getNumberProperty(CONFIDENCE);
  }

  /**
   * Convenience method to get distance property on Map Matching Response
   * @see <a href="https://www.mapbox.com/api-documentation/#retrieve-a-match">Map Matching API Documentation</a>
   *
   * @param submatch A MapMatching sub-match value
   * @return total distance in meters, null when property doesn't exist
   */
  public Number getDistance(int submatch) {
    final String DISTANCE = "distance";
    if (!getFeatures().get(submatch).hasProperty(DISTANCE)) {
      return null;
    }

    return getFeatures().get(submatch).getNumberProperty(DISTANCE);
  }

  /**
   * Convenience method to get duration property on Map Matching Response
   * @see <a href="https://www.mapbox.com/api-documentation/#retrieve-a-match">Map Matching API Documentation</a>
   *
   * @param submatch A MapMatching sub-match value
   * @return travel time in seconds, null when property doesn't exist
   */
  public Number getDuration(int submatch) {
    final String DURATION = "duration";
    if (!getFeatures().get(submatch).hasProperty(DURATION)) {
      return null;
    }

    return getFeatures().get(submatch).getNumberProperty(DURATION);
  }

  /**
   * Convenience method to get coordinate times property on Feature
   * @see <a href="https://www.mapbox.com/api-documentation/#retrieve-a-match">Map Matching API Documentation</a>
   *
   * @param submatch A MapMatching sub-match value
   * @return Array of indices
   */
  public List<Integer> getIndices(int submatch) {
    final String INDICES = "indices";
    if (!getFeatures().get(submatch).hasProperty(INDICES)) {
      return null;
    }

    JsonArray rawIndices = getFeatures().get(submatch).getProperty(INDICES).getAsJsonArray();
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < rawIndices.size(); i ++) {
      indices.add(rawIndices.get(i).getAsInt());
    }

    return indices;
  }
}
