package com.mapbox.api.directions.v5.models;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.geojson.Point;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Gives maneuver information about one {@link LegStep}.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class StepManeuver extends DirectionsJsonObject {

  /**
   * A basic turn in the direction of the modifier.
   *
   * @since 4.1.0
   */
  public static final String TURN = "turn";

  /**
   * The road name changes (after a mandatory turn).
   *
   * @since 4.1.0
   */
  public static final String NEW_NAME = "new name";

  /**
   * Indicates departure from a leg.
   * The  modifier value indicates the position of the departure point
   * in relation to the current direction of travel.
   *
   * @since 4.1.0
   */
  public static final String DEPART = "depart";

  /**
   * Indicates arrival to a destination of a leg.
   * The modifier value indicates the position of the arrival point
   * in relation to the current direction of travel.
   *
   * @since 4.1.0
   */
  public static final String ARRIVE = "arrive";

  /**
   * Merge onto a street.
   *
   * @since 4.1.0
   */
  public static final String MERGE = "merge";

  /**
   * Take a ramp to enter a highway.
   * @since 4.1.0
   */
  public static final String ON_RAMP = "on ramp";

  /**
   * Take a ramp to exit a highway.
   *
   * @since 4.1.0
   */
  public static final String OFF_RAMP = "off ramp";

  /**
   * Take the left or right side of a fork.
   *
   * @since 4.1.0
   */
  public static final String FORK  = "fork";

  /**
   * Road ends in a T intersection.
   *
   * @since 4.1.0
   */
  public static final String END_OF_ROAD  = "end of road";

  /**
   * Continue on a street after a turn.
   *
   * @since 4.1.0
   */
  public static final String CONTINUE  = "continue";

  /**
   * Traverse roundabout.
   * Has an additional property  exit in the route step that contains
   * the exit number. The  modifier specifies the direction of entering the roundabout.
   *
   * @since 4.1.0
   */
  public static final String ROUNDABOUT  = "roundabout";

  /**
   * A traffic circle. While very similar to a larger version of a roundabout,
   * it does not necessarily follow roundabout rules for right of way.
   * It can offer {@link LegStep#rotaryName()}  parameters,
   * {@link LegStep#rotaryPronunciation()} ()}  parameters, or both,
   *  in addition to the {@link #exit()} property.
   *
   * @since 4.1.0
   */
  public static final String ROTARY  = "rotary";

  /**
   * A small roundabout that is treated as an intersection.
   *
   * @since 4.1.0
   */
  public static final String ROUNDABOUT_TURN  = "roundabout turn";

  /**
   * Indicates a change of driving conditions, for example changing the  mode
   * from driving to ferry.
   *
   * @since 4.1.0
   */
  public static final String NOTIFICATION  = "notification";

  /**
   * Indicates the exit maneuver from a roundabout.
   * Will not appear in results unless you supply true to the {@link #exit()} query
   * parameter in the request.
   *
   * @since 4.1.0
   */
  public static final String EXIT_ROUNDABOUT  = "exit roundabout";

  /**
   * Indicates the exit maneuver from a rotary.
   * Will not appear in results unless you supply true
   * to the <tt>MapboxDirections.Builder#roundaboutExits()</tt> query parameter in the request.
   *
   * @since 4.1.0
   */
  public static final String EXIT_ROTARY  = "exit rotary";

  /**
   * Maneuver types.
   *
   * @since 4.1.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    TURN,
    NEW_NAME,
    DEPART,
    ARRIVE,
    MERGE,
    ON_RAMP,
    OFF_RAMP,
    FORK,
    END_OF_ROAD,
    CONTINUE,
    ROUNDABOUT,
    ROTARY,
    ROUNDABOUT_TURN,
    NOTIFICATION,
    EXIT_ROUNDABOUT,
    EXIT_ROTARY
  })
  public @interface StepManeuverType {
  }

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
  @NonNull
  @SerializedName("location")
  @SuppressWarnings( {"mutable", "WeakerAccess"})
  protected abstract double[] rawLocation();

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
   * This indicates the type of maneuver.
   * @see StepManeuverType
   * @return String with type of maneuver
   * @since 1.0.0
   */
  @Nullable
  @StepManeuverType
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
   * Convert the current {@link StepManeuver} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link StepManeuver}.
   *
   * @return a {@link StepManeuver.Builder} with the same values set to match the ones defined
   *   in this {@link StepManeuver}
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
  public static TypeAdapter<StepManeuver> typeAdapter(Gson gson) {
    return new AutoValue_StepManeuver.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a StepManeuver
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static StepManeuver fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, StepManeuver.class);
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
    public abstract Builder rawLocation(@NonNull double[] rawLocation);

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
     * @see StepManeuverType
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder type(@Nullable @StepManeuverType String type);

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
