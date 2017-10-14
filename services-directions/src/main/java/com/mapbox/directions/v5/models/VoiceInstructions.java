package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * This class provides information thats useful for properly making navigation announcements at the
 * correct time. Essentially, a distance and a string are given, using Turf Distance measurement
 * methods you can measure the users current location to the next steps maneuver point and if the
 * measured distance is less than the one the API provides, the announcement should be made.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class VoiceInstructions {

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
   * This builder can be used to set the values describing the {@link VoiceInstructions}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

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
