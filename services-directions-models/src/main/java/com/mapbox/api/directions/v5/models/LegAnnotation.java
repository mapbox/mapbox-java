package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

import java.util.List;

/**
 * An annotations object that contains additional details about each line segment along the route
 * geometry. Each entry in an annotations field corresponds to a coordinate along the route
 * geometry.
 *
 * @since 2.1.0
 */
@AutoValue
public abstract class LegAnnotation extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_LegAnnotation.Builder();
  }

  /**
   * The distance, in meters, between each pair of coordinates.
   *
   * @return a list with each entry being a distance value between two of the routeLeg geometry
   *   coordinates
   * @since 2.1.0
   */
  @Nullable
  public abstract List<Double> distance();

  /**
   * The speed, in meters per second, between each pair of coordinates.
   *
   * @return a list with each entry being a speed value between two of the routeLeg geometry
   *   coordinates
   * @since 2.1.0
   */
  @Nullable
  public abstract List<Double> duration();

  /**
   * The speed, in meters per second, between each pair of coordinates.
   *
   * @return a list with each entry being a speed value between two of the routeLeg geometry
   *   coordinates
   * @since 2.1.0
   */
  @Nullable
  public abstract List<Double> speed();

  /**
   * The posted speed limit, between each pair of coordinates.
   * Maxspeed is only available for the `mapbox/driving` and `mapbox/driving-traffic`
   * profiles, other profiles will return `unknown`s only.
   *
   * @return a list with each entry being a {@link MaxSpeed} value between two of
   *   the routeLeg geometry coordinates
   * @since 3.0.0
   */
  @Nullable
  public abstract List<MaxSpeed> maxspeed();

  /**
   * The congestion between each pair of coordinates.
   *
   * @return a list of Strings with each entry being a congestion value between two of the routeLeg
   *   geometry coordinates
   * @since 2.2.0
   */
  @Nullable
  public abstract List<String> congestion();

  /**
   * The congestion between each pair of coordinates.
   *
   * @return a list of Integers with each entry being a congestion value between two of the
   *   routeLeg geometry coordinates
   */
  @Nullable
  @SerializedName("congestion_numeric")
  public abstract List<Integer> congestionNumeric();

  /**
   * Object representing experimental value.
   * <p>
   * All available experimental values are subject to change at any time.
   */
  @Nullable
  @SerializedName("state_of_charge")
  public abstract List<Integer> experimentalStateOfCharge();

  /**
   * Convert the current {@link LegAnnotation} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link LegAnnotation}.
   *
   * @return a {@link LegAnnotation.Builder} with the same values set to match the ones defined
   *   in this {@link LegAnnotation}
   * @since 3.1.0
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<LegAnnotation> typeAdapter(Gson gson) {
    return new AutoValue_LegAnnotation.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a LegAnnotation
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static LegAnnotation fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, LegAnnotation.class);
  }

  /**
   * This builder can be used to set the values describing the {@link LegAnnotation}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * The distance, in meters, between each pair of coordinates.
     *
     * @param distance a list with each entry being a distance value between two of the routeLeg
     *                 geometry coordinates
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder distance(@Nullable List<Double> distance);

    /**
     * The speed, in meters per second, between each pair of coordinates.
     *
     * @param duration a list with each entry being a speed value between two of the routeLeg
     *                 geometry coordinates
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder duration(@Nullable List<Double> duration);

    /**
     * The speed, in meters per second, between each pair of coordinates.
     *
     * @param speed a list with each entry being a speed value between two of the routeLeg geometry
     *              coordinates
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder speed(@Nullable List<Double> speed);

    /**
     * The posted speed limit, between each pair of coordinates.
     * Maxspeed is only available for the `mapbox/driving` and `mapbox/driving-traffic`
     * profiles, other profiles will return `unknown`s only.
     *
     * @param maxspeed list of speeds between each pair of coordinates
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder maxspeed(@Nullable List<MaxSpeed> maxspeed);

    /**
     * The congestion between each pair of coordinates.
     *
     * @param congestion a list of Strings with each entry being a congestion value between two of
     *                   the routeLeg geometry coordinates
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder congestion(@Nullable List<String> congestion);

    /**
     * The congestion between each pair of coordinates.
     *
     * @param congestionNumeric a list of Integers with each entry being a congestion value between
     *   two of the routeLeg geometry coordinates
     * @return this builder for chaining options together
     */
    public abstract Builder congestionNumeric(@Nullable List<Integer> congestionNumeric);

    /**
     * Object representing experimental value.
     * <p>
     * All available experimental values are subject to change at any time.
     *
     * @param experimentalStateOfCharge experimentalStateOfCharge
     */
    @NonNull
    public abstract Builder experimentalStateOfCharge(
      @Nullable List<Integer> experimentalStateOfCharge);

    /**
     * Build a new {@link LegAnnotation} object.
     *
     * @return a new {@link LegAnnotation} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract LegAnnotation build();
  }
}
