package com.mapbox.services.api.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * A route between only two {@link DirectionsWaypoint}.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class RouteLeg implements Serializable {

  public static Builder builder() {
    return new AutoValue_RouteLeg.Builder();
  }

  /**
   * The distance traveled from one waypoint to another.
   *
   * @return a double number with unit meters
   * @since 1.0.0
   */
  public abstract double distance();

  /**
   * The estimated travel time from one waypoint to another.
   *
   * @return a double number with unit seconds
   * @since 1.0.0
   */
  public abstract double duration();

  /**
   * A short human-readable summary of major roads traversed. Useful to distinguish alternatives.
   *
   * @return String with summary
   * @since 1.0.0
   */
  public abstract String summary();

  /**
   * Gives a List including all the steps to get from one waypoint to another.
   *
   * @return List of {@link LegStep}
   * @since 1.0.0
   */
  @Nullable
  public abstract List<LegStep> steps();

  /**
   * A {@link LegAnnotation} that contains additional details about each line segment along the
   * route geometry. If you'd like to receiving this, you must request it inside your Directions
   * request before executing the call.
   *
   * @return a {@link LegAnnotation} object
   * @since 2.1.0
   */
  @Nullable
  public abstract LegAnnotation annotation();


  public static TypeAdapter<RouteLeg> typeAdapter(Gson gson) {
    return new AutoValue_RouteLeg.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder distance(double distance);

    public abstract Builder duration(double duration);

    public abstract Builder summary(@Nullable String summary);

    public abstract Builder steps(@Nullable List<LegStep> steps);

    public abstract Builder annotation(@Nullable LegAnnotation annotation);

    public abstract RouteLeg build();
  }
}
