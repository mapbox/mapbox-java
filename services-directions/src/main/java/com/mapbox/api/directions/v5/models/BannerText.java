package com.mapbox.api.directions.v5.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.MapboxDirections;

import java.io.Serializable;
import java.util.List;

/**
 * Includes both plain text information that can be visualized inside your navigation application
 * along with the text string broken down into {@link BannerComponents} which may or may not
 * include a image url. To receive this information, your request must have
 * {@link MapboxDirections#bannerInstructions()} set to true.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class BannerText implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_BannerText.Builder();
  }

  /**
   * Plain text with all the {@link BannerComponents} text combined.
   *
   * @return plain text with all the {@link BannerComponents} text items combined
   * @since 3.0.0
   */
  @Nullable
  public abstract String text();

  /**
   * A part or element of the {@link BannerInstructions}.
   *
   * @return a {@link BannerComponents} specific to a {@link LegStep}
   * @since 3.0.0
   */
  @Nullable
  public abstract List<BannerComponents> components();

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
   * <li>roundabout - traverse roundabout, has additional property exit in RouteStep
   * containing the exit number. The modifier specifies the direction of entering the roundabout.
   * </li>
   * <li>rotary - a traffic circle. While very similar to a larger version of a roundabout, it does
   * not necessarily follow roundabout rules for right of way. It can offer
   * {@link LegStep#rotaryName()} and/or {@link LegStep#rotaryPronunciation()} parameters in
   * addition to the exit property.</li>
   * <li>roundabout turn - small roundabout that is treated as an intersection</li>
   * <li>notification - change of driving conditions, e.g. change of mode from driving to ferry</li>
   * </ul>
   *
   * @return String with type of maneuver
   * @since 3.0.0
   */
  @Nullable
  public abstract String type();

  /**
   * This indicates the mode of the maneuver. If type is of turn, the modifier indicates the
   * change in direction accomplished through the turn. If the type is of depart/arrive, the
   * modifier indicates the position of waypoint from the current direction of travel.
   *
   * @return String with modifier
   * @since 3.0.0
   */
  @Nullable
  public abstract String modifier();


  /**
   * The degrees at which you will be exiting a roundabout, assuming `180` indicates
   * going straight through the roundabout.
   *
   * @return at which you will be exiting a roundabout
   * @since 3.0.0
   */
  @Nullable
  public abstract Double degrees();

  /**
   * A string representing which side the of the street people drive on
   * in that location. Can be 'left' or 'right'.
   *
   * @return String either `left` or `right`
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("driving_side")
  public abstract String drivingSide();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<BannerText> typeAdapter(Gson gson) {
    return new AutoValue_BannerText.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link BannerText}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Plain text with all the {@link BannerComponents} text combined.
     *
     * @param text plain text with all the {@link BannerComponents} text items combined
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @Nullable
    public abstract Builder text(String text);

    /**
     * A part or element of the {@link BannerInstructions}.
     *
     * @param components a {@link BannerComponents} specific to a {@link LegStep}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @Nullable
    public abstract Builder components(List<BannerComponents> components);

    /**
     * This indicates the type of maneuver. See {@link BannerText#type()} for a full list of
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
     * The degrees at which you will be exiting a roundabout, assuming `180` indicates
     * going straight through the roundabout.
     *
     * @param degrees at which you will be exiting a roundabout
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder degrees(Double degrees);

    /**
     * A string representing which side the of the street people drive on in
     * that location. Can be 'left' or 'right'.
     *
     * @param drivingSide either `left` or `right`
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder drivingSide(@Nullable String drivingSide);

    /**
     * Build a new {@link BannerText} object.
     *
     * @return a new {@link BannerText} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract BannerText build();
  }
}
