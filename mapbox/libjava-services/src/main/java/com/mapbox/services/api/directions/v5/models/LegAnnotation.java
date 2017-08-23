package com.mapbox.services.api.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An annotations object that contains additional details about each line segment along the route
 * geometry. Each entry in an annotations field corresponds to a coordinate along the route
 * geometry.
 *
 * @since 2.1.0
 */
@AutoValue
public abstract class LegAnnotation implements Serializable {

  public static Builder builder() {
    return new AutoValue_LegAnnotation.Builder();
  }

  /**
   * The distance, in meters, between each pair of coordinates.
   *
   * @return a double array with each entry being a distance value between two of the routeLeg
   * geometry coordinates
   * @since 2.1.0
   */
  @SuppressWarnings("mutable")
  public abstract double[] distance();

  /**
   * The speed, in meters per second, between each pair of coordinates.
   *
   * @return a double array with each entry being a speed value between two of the routeLeg geometry
   * coordinates
   * @since 2.1.0
   */
  @SuppressWarnings("mutable")
  public abstract double[] duration();

  /**
   * The speed, in meters per second, between each pair of coordinates.
   *
   * @return a double array with each entry being a speed value between two of the routeLeg geometry
   * coordinates
   * @since 2.1.0
   */
  @SuppressWarnings("mutable")
  @Nullable
  public abstract double[] speed();

  /**
   * The congestion between each pair of coordinates.
   *
   * @return a String array with each entry being a congestion value between two of the routeLeg
   * geometry coordinates
   * @since 2.2.0
   */
  public abstract List<String> congestion();

  public static TypeAdapter<LegAnnotation> typeAdapter(Gson gson) {
    return new AutoValue_LegAnnotation.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    private List<String> congestion = new ArrayList<>();

    public abstract Builder distance(double[] distance);

    public abstract Builder duration(double[] duration);

    public abstract Builder speed(double[] speed);

    public Builder congestion(String[] congestion) {
      this.congestion.addAll(Arrays.asList(congestion));
      return this;
    }

    abstract Builder congestion(List<String> congestion);

    public abstract LegAnnotation build();
  }
}