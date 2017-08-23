package com.mapbox.services.api.directions.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Object representing lanes in an intersection.
 *
 * @since 2.0.0
 */
@AutoValue
public abstract class IntersectionLanes implements Serializable {

  public static AutoValue_IntersectionLanes.Builder builder() {
    return new AutoValue_IntersectionLanes.Builder();
  }

  /**
   * Provides a boolean value you can use to determine if the given lane is valid for the user to
   * complete the maneuver.
   *
   * @return Boolean value for whether this lane can be taken to complete the maneuver. For
   * instance, if the lane array has four objects and the first two are marked as valid, then the
   * driver can take either of the left lanes and stay on the route.
   * @since 2.0.0
   */
  public abstract boolean valid();

  /**
   * Array that can be made up of multiple signs such as {@code left}, {@code right}, etc.
   *
   * @return Array of signs for each turn lane. There can be multiple signs. For example, a turning
   * lane can have a sign with an arrow pointing left and another sign with an arrow pointing
   * straight.
   * @since 2.0.0
   */
  public abstract List<String> indications();

  public static TypeAdapter<IntersectionLanes> typeAdapter(Gson gson) {
    return new AutoValue_IntersectionLanes.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    private List<String> indications = new ArrayList<>();

    public abstract Builder valid(boolean valid);

    public Builder indications(String[] indications) {
      this.indications.addAll(Arrays.asList(indications));
      return this;
    }

    abstract Builder indications(List<String> indications);

    public abstract IntersectionLanes build();
  }
}
