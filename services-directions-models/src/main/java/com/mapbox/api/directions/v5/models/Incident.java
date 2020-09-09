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
 * Object representing an incident along the intersection.
 *
 */
@AutoValue
public abstract class Incident extends DirectionsJsonObject {

  /**
   * Incident type.
   *
   * @return incident type
   */
  @Nullable
  @SerializedName("incident_type")
  public abstract String incidentType();

  /**
   * Incident start time.
   *
   * @return incident start time String in ISO8601 format
   */
  @Nullable
  @SerializedName("start_time")
  public abstract String startTime();

  /**
   * Incident end time.
   *
   * @return incident end time String in ISO8601 format
   */
  @Nullable
  @SerializedName("end_time")
  public abstract String endTime();

  /**
   * Incident creation time.
   *
   * @return incident creation time String in ISO8601 format
   */
  @Nullable
  @SerializedName("creation_time")
  public abstract String creationTime();

  /**
   * Incident id.
   *
   * @return incident id
   */
  @NonNull
  public abstract Integer id();

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_Incident.Builder();
  }

  /**
   * Convert the current {@link Incident} to its builder holding the currently assigned values. This
   * allows you to modify a single property and then rebuild the object resulting in an updated and
   * modified {@link Incident}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined in this {@link
   * Incident}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Incident> typeAdapter(Gson gson) {
    return new AutoValue_Incident.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Incident
   * @return a new instance of this class defined by the values passed in the method
   */
  public static Incident fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Incident.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Incident}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Type of the incident.
     *
     * @param incidentType String with incident type
     * @return this builder for chaining options together
     */
    public abstract Builder incidentType(@Nullable String incidentType);

    /**
     * Time when incident started.
     *
     * @param startTime String value in ISO8601 format describing when incident started
     * @return this builder for chaining options together
     */
    public abstract Builder startTime(@Nullable String startTime);

    /**
     * Time when incident finished.
     *
     * @param endTime String value in ISO8601 format describing when incident finished
     * @return this builder for chaining options together
     */
    public abstract Builder endTime(@Nullable String endTime);

    /**
     * Time when incident created.
     *
     * @param creationTime String value in ISO8601 format describing when incident created
     * @return this builder for chaining options together
     */
    public abstract Builder creationTime(@Nullable String creationTime);

    /**
     * Id of the incident.
     *
     * @param id int value describing incident's identifier
     * @return this builder for chaining options together
     */
    public abstract Builder id(@NonNull Integer id);

    /**
     * Build a new {@link Incident} object.
     *
     * @return a new {@link Incident} using the provided values in this builder
     */
    public abstract Incident build();
  }
}
