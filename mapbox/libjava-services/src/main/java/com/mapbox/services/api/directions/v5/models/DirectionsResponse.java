package com.mapbox.services.api.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import android.support.annotation.NonNull;


import java.io.Serializable;
import java.util.List;

/**
 * This is the root Mapbox directions API response. Inside this class are several nested classes
 * chained together to make up a similar structure to the original APIs JSON response.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#directions-response-object">Direction
 * Response Object</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsResponse implements Serializable {

  public static Builder builder() {
    return new AutoValue_DirectionsResponse.Builder();
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

  // TODO test that waypoints appear in correct order in list, see javadoc below

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

  public static TypeAdapter<DirectionsResponse> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsResponse.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder code(@NonNull String code);

    public abstract Builder waypoints(@Nullable List<DirectionsWaypoint> waypoints);

    public abstract Builder routes(@Nullable List<DirectionsRoute> routes);

    public abstract DirectionsResponse build();
  }
}
