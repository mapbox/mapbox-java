package com.mapbox.api.directions.v5.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

/**
 * This class provides information thats useful for properly making navigation announcements at the
 * correct time. Essentially, a distance and a string are given, using Turf Distance measurement
 * methods you can measure the users current location to the next steps maneuver point and if the
 * measured distance is less than the one the API provides, the announcement should be made.
 *
 * @since 3.0.0
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class VoiceInstructions extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_VoiceInstructions.Builder();
  }

  /**
   * This provides the missing piece in which is needed to announce instructions at accurate
   * times. If the user is less distance away from the maneuver than what this
   * {@code distanceAlongGeometry()} than, the announcement should be called.
   *
   * @return double value representing the distance to next maneuver in unit meters
   * @since 3.0.0
   */
  @Nullable
  public abstract Double distanceAlongGeometry();

  /**
   * Provides the instruction string which was build on the server-side and can sometimes
   * concatenate instructions together if maneuver instructions are too close to each other.
   *
   * @return a string with the readable instructions ready to be read or displayed to a user
   * @since 3.0.0
   */
  @Nullable
  public abstract String announcement();

  /**
   * Get the same instruction string you'd get from {@link #announcement()} but this one includes
   * Speech Synthesis Markup Language which helps voice synthesiser read information more humanely.
   *
   * @return a string with the SSML instructions
   * @since 3.0.0
   */
  @Nullable
  public abstract String ssmlAnnouncement();

  /**
   * Convert the current {@link VoiceInstructions} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link VoiceInstructions}.
   *
   * @return a {@link VoiceInstructions.Builder} with the same values set to match the ones defined
   *   in this {@link VoiceInstructions}
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
  public static TypeAdapter<VoiceInstructions> typeAdapter(Gson gson) {
    return new AutoValue_VoiceInstructions.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a VoiceInstructions
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static VoiceInstructions fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, VoiceInstructions.class);
  }

  /**
   * This builder can be used to set the values describing the {@link VoiceInstructions}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Returns the missing piece in which is needed to announce instructions at accurate
     * times. If the user is less distance away from the maneuver than what this
     * {@code distanceAlongGeometry()} than, the announcement should be called.
     *
     * @param distanceAlongGeometry double value representing the distance to next maneuver in unit
     *                              meters
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder distanceAlongGeometry(Double distanceAlongGeometry);

    /**
     * Provides the instruction string which was build on the server-side and can sometimes
     * concatenate instructions together if maneuver instructions are too close to each other.
     *
     * @param announcement a string with the readable instructions ready to be read or displayed to
     *                     a user
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder announcement(String announcement);

    /**
     * Get the same instruction string you'd get from {@link #announcement()} but this one includes
     * Speech Synthesis Markup Language which helps voice synthesiser read information more
     * humanely.
     *
     * @param ssmlAnnouncement a string with the SSML instructions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder ssmlAnnouncement(String ssmlAnnouncement);

    /**
     * This uses the provided parameters set using the {@link Builder} and creates a new instance of
     * {@link VoiceInstructions}.
     *
     * @return a new instance of Voice Instructions
     * @since 3.0.0
     */
    public abstract VoiceInstructions build();
  }
}
