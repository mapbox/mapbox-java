package com.mapbox.api.directions.v5.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.MapboxDirections;

import java.io.Serializable;

/**
 * A part of the {@link BannerText} which includes a snippet of the full banner text instruction. In
 * cases where data is avaliable, an image url will be provided to visually include a road shield.
 * To receive this information, your request must have {@link MapboxDirections#bannerInstructions()}
 * set to true.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class BannerComponents implements Serializable, Comparable<BannerComponents> {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_BannerComponents.Builder();
  }

  /**
   * A snippet of the full {@link BannerText#text()} which can be used for visually altering parts
   * of the full string.
   *
   * @return a single snippet of the full text instruction
   * @since 3.0.0
   */
  public abstract String text();

  /**
   * String giving you more context about the component which may help in visual markup/display
   * choices. If the type of the components is unknown it should be treated as text.
   * <p>
   * Possible values:
   * <ul>
   * <li><strong>text (default)</strong>: indicates the text is part of
   * the instructions and no other type</li>
   * <li><strong>icon</strong>: this is text that can be replaced by an icon, see imageBaseURL</li>
   * <li><strong>delimiter</strong>: this is text that can be dropped and
   * should be dropped if you are rendering icons</li>
   * <li><strong>exit-number</strong>: the exit number for the maneuver</li>
   * <li><strong>exit</strong>: the word for exit in the local language</li>
   * </ul>
   *
   * @return String type from above list
   * @since 3.0.0
   */
  public abstract String type();

  /**
   * The abbreviated form of text.
   * <p>
   * If this is present, there will also be an abbr_priority value.
   *
   * @return abbreviated form of {@link BannerComponents#text()}.
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("abbr")
  public abstract String abbreviation();

  /**
   * An integer indicating the order in which the abbreviation abbr should be used in
   * place of text. The highest priority is 0 and a higher integer value indicates a lower
   * priority. There are no gaps in integer values.
   * <p>
   * Multiple components can have the same abbreviationPriority and when this happens all
   * components with the same abbr_priority should be abbreviated at the same time.
   * Finding no larger values of abbreviationPriority indicates that the string is
   * fully abbreviated.
   *
   * @return Integer indicating the order of the abbreviation
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("abbr_priority")
  public abstract Integer abbreviationPriority();

  /**
   * In some cases when the {@link LegStep} is a highway or major roadway, there might be a shield
   * icon that's included to better identify to your user to roadway. Note that this doesn't
   * return the image itself but rather the url which can be used to download the file.
   *
   * @return the url which can be used to download the shield icon if one is available
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("imageBaseURL")
  public abstract String imageBaseUrl();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<BannerComponents> typeAdapter(Gson gson) {
    return new AutoValue_BannerComponents.GsonTypeAdapter(gson);
  }

  /**
   * Allows ability to sort/compare by abbreviation priority. This is null-safe for values of
   * abbreviationPriority, and treats BannerComponents with a null abreviationPriority as having an
   * abbreviationPriority of infinity. This method returns a negative integer, zero, or a positive
   * integer as this object is less than, equal to, or greater than the specified object.
   *
   * @param bannerComponents to compare to
   * @return the compareTo int value
   * @since 3.0.0
   */
  @Override
  public int compareTo(BannerComponents bannerComponents) {
    Integer ab1 = this.abbreviationPriority();
    Integer ab2 = bannerComponents.abbreviationPriority();

    if (ab1 == null && ab2 == null) {
      return 0;
    } else if (ab1 == null) {
      return 1;
    } else if (ab2 == null) {
      return -1;
    } else {
      return ab1.compareTo(ab2);
    }
  }

  /**
   * This builder can be used to set the values describing the {@link BannerComponents}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * A snippet of the full {@link BannerText#text()} which can be used for visually altering parts
     * of the full string.
     *
     * @param text a single snippet of the full text instruction
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder text(String text);

    /**
     * String giving you more context about the component which may help in visual markup/display
     * choices. If the type of the components is unknown it should be treated as text.
     * <p>
     * Possible values:
     * <ul>
     * <li><strong>text (default)</strong>: indicates the text is part of the instructions
     * and no other type</li>
     * <li><strong>icon</strong>: this is text that can be replaced by an icon,
     * see imageBaseURL</li>
     * <li><strong>delimiter</strong>: this is text that can be dropped and should be dropped
     * if you are rendering icons</li>
     * <li><strong>exit-number</strong>: the exit number for the maneuver</li>
     * <li><strong>exit</strong>: the word for exit in the local language</li>
     * </ul>
     *
     * @param type String type from above list
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder type(String type);


    /**
     * The abbreviated form of text.
     *
     * @param abbreviation for the given text of this component
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder abbreviation(@Nullable String abbreviation);

    /**
     * An integer indicating the order in which the abbreviation abbr should be used in
     * place of text. The highest priority is 0 and a higher integer value indicates a lower
     * priority. There are no gaps in integer values.
     * <p>
     * Multiple components can have the same abbreviationPriority and when this happens all
     * components with the same abbr_priority should be abbreviated at the same time.
     * Finding no larger values of abbreviationPriority indicates that the string is
     * fully abbreviated.
     *
     * @param abbreviationPriority Integer indicating the order of the abbreviation
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder abbreviationPriority(@Nullable Integer abbreviationPriority);

    /**
     * In some cases when the {@link LegStep} is a highway or major roadway, there might be a shield
     * icon that's included to better identify to your user to roadway. Note that this doesn't
     * return the image itself but rather the url which can be used to download the file.
     *
     * @param imageBaseUrl the url which can be used to download the shield icon if one is avaliable
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder imageBaseUrl(@Nullable String imageBaseUrl);

    /**
     * Build a new {@link BannerComponents} object.
     *
     * @return a new {@link BannerComponents} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract BannerComponents build();
  }
}
