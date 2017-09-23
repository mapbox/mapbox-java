package com.mapbox.directions.v5.models;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Point;

import java.io.Serializable;

/**
 * Gives maneuver information about one {@link LegStep}.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class StepManeuver implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_StepManeuver.Builder();
  }

  /**
   * A {@link Point} representing this intersection location.
   *
   * @return GeoJson Point representing this intersection location
   * @since 3.0.0
   */
  @NonNull
  public Point location() {
    return Point.fromLngLat(rawLocation()[0], rawLocation()[1]);
  }

  /**
   * A {@link Point} representing this intersection location. Since the rawLocation isn't public,
   * it's okay to be mutable as long as nothing in this SDK changes values.
   *
   * @return GeoJson Point representing this intersection location
   * @since 3.0.0
   */
  @SerializedName("location")
  @SuppressWarnings("mutable")
  abstract double[] rawLocation();

  /**
   * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
   * travel right before the maneuver.
   *
   * @return double with value from 0 to 360
   * @since 1.0.0
   */
  @Nullable
  @SerializedName("bearing_before")
  public abstract Double bearingBefore();

  /**
   * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
   * travel right after the maneuver.
   *
   * @return double with value from 0 to 360
   * @since 1.0.0
   */
  @Nullable
  @SerializedName("bearing_after")
  public abstract Double bearingAfter();

  /**
   * A human-readable instruction of how to execute the returned maneuver. This String is built
   * using OSRM-Text-Instructions and can be further customized inside either the Mapbox Navigation
   * SDK for Android or using the OSRM-Text-Instructions.java project in Project-OSRM.
   *
   * @return String with instruction
   * @see <a href='https://github.com/mapbox/mapbox-navigation-android'>Navigation SDK</a>
   * @see <a href='https://github.com/Project-OSRM/osrm-text-instructions.java'>
   *   OSRM-Text-Instructions.java</a>
   * @since 1.0.0
   */
  @Nullable
  public abstract String instruction();

  /**
   * This indicates the type of maneuver. It can be any of these listed:
   * <br>
   * <ul>
   * <li>turn - a basic turn into direction of the modifier</li>
   * <li>new name - the road name changes (after a mandatory turn)</li>
   * <li>depart - indicates departure from a leg</li>
   * <li>arrive - indicates arrival to a destination of a leg</li>
   * <li>merge - merge onto a street</li>
   * <li>on ramp - take a ramp to enter a highway</li>
   * <li>off ramp - take a ramp to exit a highway</li>
   * <li>fork - take the left/right side of a fork</li>
   * <li>end of road - road ends in a T intersection</li>
   * <li>continue - continue on a street after a turn</li>
   * <li>roundabout - traverse roundabout, has additional property {@link #exit()} in RouteStep
   * containing the exit number. The modifier specifies the direction of entering the roundabout.
   * </li>
   * <li>rotary - a traffic circle. While very similar to a larger version of a roundabout, it does
   * not necessarily follow roundabout rules for right of way. It can offer
   * {@link LegStep#rotaryName()} and/or {@link LegStep#rotaryPronunciation()} parameters in
   * addition to the {@link #exit()} property.</li>
   * <li>roundabout turn - small roundabout that is treated as an intersection</li>
   * <li>notification - change of driving conditions, e.g. change of mode from driving to ferry</li>
   * </ul>
   *
   * @return String with type of maneuver
   * @since 1.0.0
   */
  @Nullable
  public abstract String type();

  /**
   * This indicates the mode of the maneuver. If type is of turn, the modifier indicates the
   * change in direction accomplished through the turn. If the type is of depart/arrive, the
   * modifier indicates the position of waypoint from the current direction of travel.
   *
   * @return String with modifier
   * @since 1.0.0
   */
  @Nullable
  public abstract String modifier();

  /**
   * An optional integer indicating number of the exit to take. If exit is undefined the destination
   * is on the roundabout. The property exists for the following type properties:
   * <p>
   * else - indicates the number of intersections passed until the turn.
   * roundabout - traverse roundabout
   * rotary - a traffic circle
   * </p>
   *
   * @return an integer indicating number of the exit to take
   * @since 2.0.0
   */
  @Nullable
  public abstract Integer exit();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<StepManeuver> typeAdapter(Gson gson) {
    return new AutoValue_StepManeuver.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link StepManeuver}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The rawLocation as a double array. Once the {@link StepManeuver} object's created, this raw
     * location gets converted into a {@link Point} object and is public exposed as such. The double
     * array should have a length of two, index 0 being the longitude and index 1 being latitude.
     *
     * @param rawLocation a double array with a length of two, index 0 being the longitude and
     *                    index 1 being latitude.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder rawLocation(double[] rawLocation);

    /**
     * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
     * travel right before the maneuver.
     *
     * @param bearingBefore double with value from 0 to 360
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bearingBefore(
      @Nullable @FloatRange(from = 0, to = 360) Double bearingBefore);

    /**
     * Number between 0 and 360 indicating the clockwise angle from true north to the direction of
     * travel right after the maneuver.
     *
     * @param bearingAfter double with value from 0 to 360
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bearingAfter(
      @Nullable @FloatRange(from = 0, to = 360) Double bearingAfter);

    /**
     * A human-readable instruction of how to execute the returned maneuver. This String is built
     * using OSRM-Text-Instructions and can be further customized inside either the Mapbox
     * Navigation SDK for Android or using the OSRM-Text-Instructions.java project in Project-OSRM.
     *
     * @param instruction String with instruction
     * @return this builder for chaining options together
     * @see <a href='https://github.com/mapbox/mapbox-navigation-android'>Navigation SDK</a>
     * @see <a href='https://github.com/Project-OSRM/osrm-text-instructions.java'>OSRM-Text-Instructions.java</a>
     * @since 3.0.0
     */
    public abstract Builder instruction(@Nullable String instruction);

    /**
     * This indicates the type of maneuver. See {@link StepManeuver#type()} for a full list of
     * options.
     *
     * @param type String with type of maneuver
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder type(@Nullable String type);

    /**
     * This indicates the mode of the maneuver. If type is of turn, the modifier indicates the
     * change in direction accomplished through the turn. If the type is of depart/arrive, the
     * modifier indicates the position of waypoint from the current direction of travel.
     *
     * @param modifier String with modifier
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder modifier(@Nullable String modifier);

    /**
     * An optional integer indicating number of the exit to take. If exit is undefined the
     * destination is on the roundabout. The property exists for the following type properties:
     * <p>
     * else - indicates the number of intersections passed until the turn.
     * roundabout - traverse roundabout
     * rotary - a traffic circle
     * </p>
     *
     * @param exit an integer indicating number of the exit to take
     * @return this builder for chaining options together
     * @since 2.0.0
     */
    public abstract Builder exit(@Nullable Integer exit);

    /**
     * Build a new {@link StepManeuver} object.
     *
     * @return a new {@link StepManeuver} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract StepManeuver build();
  }
}
