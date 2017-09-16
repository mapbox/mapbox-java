package com.mapbox.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.GeometryDeserializer;
import com.mapbox.geojson.gson.MapboxAdapterFactory;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.PointSerializer;

import java.io.Serializable;
import java.util.List;

/**
 * This is the root Mapbox Directions API response. Inside this class are several nested classes
 * chained together to make up a similar structure to the original APIs JSON response.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#directions-response-object">Direction Response Object</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsResponse implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_DirectionsResponse.Builder();
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a GeoJson Directions Response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static DirectionsResponse fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
    gson.registerTypeAdapterFactory(MapboxAdapterFactory.create());
    return gson.create().fromJson(json, DirectionsResponse.class);
  }

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code.
   * On normal valid responses, the value will be Ok. The possible responses are listed below:
   * <ul>
   * <li><strong>Ok</strong>:200 Normal success case</li>
   * <li><strong>NoRoute</strong>: 200 There was no route found for the given coordinates. Check
   * for impossible routes (e.g. routes over oceans without ferry connections).</li>
   * <li><strong>NoSegment</strong>: 200 No road segment could be matched for coordinates. Check for
   * coordinates too far away from a road.</li>
   * <li><strong>ProfileNotFound</strong>: 404 Use a valid profile as described above</li>
   * <li><strong>InvalidInput</strong>: 422</li>
   * </ul>
   *
   * @return a string with one of the given values described in the list above
   * @since 1.0.0
   */
  @NonNull
  public abstract String code();

  /**
   * List of {@link DirectionsWaypoint} objects. Each {@code waypoint} is an input coordinate
   * snapped to the road and path network. The {@code waypoint} appear in the list in the order of
   * the input coordinates.
   *
   * @return list of {@link DirectionsWaypoint} objects ordered from start of route till the end
   * @since 1.0.0
   */
  @Nullable
  public abstract List<DirectionsWaypoint> waypoints();

  /**
   * List containing all the different route options. It's ordered by descending recommendation
   * rank. In other words, object 0 in the List is the highest recommended route. if you don't
   * setAlternatives to true (default is false) in your builder this should always be a List of
   * size 1. At most this will return 2 {@link DirectionsRoute} objects.
   *
   * @return list of {@link DirectionsRoute} objects
   * @since 1.0.0
   */
  @Nullable
  public abstract List<DirectionsRoute> routes();

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Geocoding Response
   * @since 1.0.0
   */
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer());
    return gson.create().toJson(this);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<DirectionsResponse> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsResponse.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link DirectionsResponse}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * String indicating the state of the response. This is a separate code than the HTTP status
     * code. On normal valid responses, the value will be Ok. For a full list of possible responses,
     * see {@link DirectionsResponse#code()}.
     *
     * @param code a string with one of the given values described in the list above
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder code(@NonNull String code);

    /**
     * List of {@link DirectionsWaypoint} objects. Each {@code waypoint} is an input coordinate
     * snapped to the road and path network. The {@code waypoint} appear in the list in the order of
     * the input coordinates.
     *
     * @param waypoints list of {@link DirectionsWaypoint} objects ordered from start of route till
     *                  the end
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder waypoints(@Nullable List<DirectionsWaypoint> waypoints);

    /**
     * List containing all the different route options. It's ordered by descending recommendation
     * rank. In other words, object 0 in the List is the highest recommended route. if you don't
     * setAlternatives to true (default is false) in your builder this should always be a List of
     * size 1. At most this will return 2 {@link DirectionsRoute} objects.
     *
     * @param routes list of {@link DirectionsRoute} objects
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder routes(@Nullable List<DirectionsRoute> routes);

    /**
     * Build a new {@link DirectionsResponse} object.
     *
     * @return a new {@link DirectionsResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract DirectionsResponse build();
  }
}
