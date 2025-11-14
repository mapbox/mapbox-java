package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Object representing lane access attributes.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class IntersectionLaneAccess extends DirectionsJsonObject {

  /**
   * Indicates lanes that is designated to bicycles.
   */
  public static final String BICYCLE = "bicycle";

  /**
   * Indicates lanes that is designated to buses.
   */
  public static final String BUS = "bus";

  /**
   * Indicates High-occupancy vehicle lanes.
   */
  public static final String HOV = "hov";

  /**
   * Indicates lanes that is designated to mopeds.
   */
  public static final String MOPED = "moped";

  /**
   * Indicates lanes that is designated to motorcycles.
   */
  public static final String MOTORCYCLE = "motorcycle";

  /**
   * Indicates lanes that is designated to taxis.
   */
  public static final String TAXI = "taxi";

  /**
   * Represents the allowed vehicle types for a designated lane.
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef({
    BICYCLE,
    BUS,
    HOV,
    MOPED,
    MOTORCYCLE,
    TAXI
  })
  public @interface LaneDesignatedVehicleType {
  }

  /**
   * Indicates whether a lane is designated to specified vehicle types.
   * A vehicle type can be any of (but not limited to) the predefined constants in
   * {@link LaneDesignatedVehicleType}, such as {@link #BICYCLE},
   * {@link #BUS}, {@link #HOV}, {@link #MOPED}, {@link #MOTORCYCLE}, or {@link #TAXI}.
   * For example, when a lane is designated to buses and taxis, this list should have
   * {@link #BUS} and {@link #TAXI} as its values.
   *
   * @return a list of vehicle types for which this lane is designated
   * @see LaneDesignatedVehicleType
   */
  @Nullable
  @SerializedName("designated")
  public abstract List<String> designated();

  /**
   * Convert the current {@link IntersectionLaneAccess} to its builder holding the
   * currently assigned values. This allows you to modify a single property and then rebuild
   * the object resulting in an updated and modified {@link IntersectionLaneAccess}.
   *
   * @return a {@link IntersectionLaneAccess.Builder} with the same values set to match the ones
   *   defined in this {@link IntersectionLaneAccess}
   */
  public abstract IntersectionLaneAccess.Builder toBuilder();

  /**
   * Create a new instance of this class by using the {@link IntersectionLaneAccess.Builder} class.
   *
   * @return this classes {@link IntersectionLaneAccess.Builder} for creating a new instance
   */
  public static IntersectionLaneAccess.Builder builder() {
    return new AutoValue_IntersectionLaneAccess.Builder();
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<IntersectionLaneAccess> typeAdapter(Gson gson) {
    final Gson customGson = gson.newBuilder()
      .registerTypeAdapter(String.class, new InterningStringAdapter())
      .create();
    return new AutoValue_IntersectionLaneAccess.GsonTypeAdapter(customGson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an IntersectionLaneAccess
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static IntersectionLaneAccess fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, IntersectionLaneAccess.class);
  }

  /**
   * This builder can be used to set the values describing the {@link IntersectionLaneAccess}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends
    DirectionsJsonObject.Builder<IntersectionLaneAccess.Builder> {

    /**
     * Provide a list of vehicle types for which this lane is designated. A vehicle type can be
     * any of (but not limited to) the predefined constants in
     * {@link LaneDesignatedVehicleType}, such as {@link #BICYCLE}, {@link #BUS}, {@link #HOV},
     * {@link #MOPED}, {@link #MOTORCYCLE}, or {@link #TAXI}
     *
     * @param designated a list of vehicle types for which this lane is designated.
     * @return this builder for chaining options together
     */
    public abstract IntersectionLaneAccess.Builder designated(@Nullable List<String> designated);

    /**
     * Build a new {@link IntersectionLaneAccess} object.
     *
     * @return a new {@link IntersectionLaneAccess} using the provided values in this builder
     */
    public abstract IntersectionLaneAccess build();
  }
}
