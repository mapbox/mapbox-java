package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

/**
 * Object representing experimental value.
 * <p>
 * All available experimental values are subject to change at any time.
 */
@AutoValue
public abstract class ExperimentalWaypointMetadata extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link ExperimentalWaypointMetadata.Builder}
   * class.
   *
   * @return {@link ExperimentalWaypointMetadata.Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_ExperimentalWaypointMetadata.Builder();
  }

  /**
   * Object representing experimental value.
   * <p>
   * All available experimental values are subject to change at any time.
   */
  @Nullable
  @SerializedName("type")
  public abstract String type();

  /**
   * Object representing experimental value.
   * <p>
   * All available experimental values are subject to change at any time.
   */
  @Nullable
  @SerializedName("name")
  public abstract String name();

  /**
   * Object representing experimental value.
   * <p>
   * All available experimental values are subject to change at any time.
   */
  @Nullable
  @SerializedName("charge_time")
  public abstract Integer chargeTime();

  /**
   * Object representing experimental value.
   * <p>
   * All available experimental values are subject to change at any time.
   */
  @Nullable
  @SerializedName("charge_to")
  public abstract Integer chargeTo();

  /**
   * Object representing experimental value.
   * <p>
   * All available experimental values are subject to change at any time.
   */
  @Nullable
  @SerializedName("plug_type")
  public abstract String plugType();

  /**
   * Object representing experimental value.
   * <p>
   * All available experimental values are subject to change at any time.
   */
  @Nullable
  @SerializedName("power_kw")
  public abstract Integer powerKiloWatt();

  /**
   * Convert the current {@link ExperimentalWaypointMetadata} to its builder holding the currently
   * assigned values. This allows you to modify a single property and then rebuild the object
   * resulting in an updated and modified {@link ExperimentalWaypointMetadata}.
   *
   * @return a {@link ExperimentalWaypointMetadata.Builder} with the same values set to match
   *   the ones defined in this {@link ExperimentalWaypointMetadata}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<ExperimentalWaypointMetadata> typeAdapter(Gson gson) {
    return new AutoValue_ExperimentalWaypointMetadata.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a Metadata
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static ExperimentalWaypointMetadata fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, ExperimentalWaypointMetadata.class);
  }

  /**
   * This builder can be used to set the values describing the {@link ExperimentalWaypointMetadata}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Object representing experimental value.
     * <p>
     * All available experimental values are subject to change at any time.
     *
     * @param type type
     */
    @NonNull
    public abstract Builder type(@Nullable String type);

    /**
     * Object representing experimental value.
     * <p>
     * All available experimental values are subject to change at any time.
     *
      @param name name
     */
    @NonNull
    public abstract Builder name(@Nullable String name);

    /**
     * Object representing experimental value.
     * <p>
     * All available experimental values are subject to change at any time.
     *
      @param chargeTime chargeTime
     */
    @NonNull
    public abstract Builder chargeTime(@Nullable Integer chargeTime);

    /**
     * Object representing experimental value.
     * <p>
     * All available experimental values are subject to change at any time.
     *
      @param chargeTo chargeTo
     */
    @NonNull
    public abstract Builder chargeTo(@Nullable Integer chargeTo);

    /**
     * Object representing experimental value.
     * <p>
     * All available experimental values are subject to change at any time.
     *
      @param plugType plugType
     */
    @NonNull
    public abstract Builder plugType(@Nullable String plugType);

    /**
     * Object representing experimental value.
     * <p>
     * All available experimental values are subject to change at any time.
     *
      @param powerKiloWatt powerKiloWatt
     */
    @NonNull
    public abstract Builder powerKiloWatt(@Nullable Integer powerKiloWatt);

    /**
     * Build a new {@link ExperimentalWaypointMetadata} object.
     *
     * @return a new {@link ExperimentalWaypointMetadata} using the provided values in this builder
     */
    public abstract ExperimentalWaypointMetadata build();
  }
}
