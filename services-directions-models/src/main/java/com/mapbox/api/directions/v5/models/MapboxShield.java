package com.mapbox.api.directions.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

/**
 * A part of the {@link BannerComponents} which includes a snippet of the route shield associated
 * with the instruction. In cases where data is available, a base url will be provided to help in
 * constructing an actual URL that can then be used to fetch a shield in the form of SVG.
 * To receive this information, your request must have
 * <tt>MapboxDirections.Builder#bannerInstructions()</tt> set to true.
 */
@AutoValue
public abstract class MapboxShield extends DirectionsJsonObject {
  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_MapboxShield.Builder();
  }

  /**
   * Base url to query the styles endpoint.
   *
   * @return base url to query the styles endpoint
   */
  @SerializedName("base_url")
  public abstract String baseUrl();

  /**
   * String indicating the name of the route shield.
   *
   * @return name of the route shield
   */
  public abstract String name();

  /**
   * String indicating the color of the text to be rendered on the route shield.
   *
   * @return String color of the text to be rendered on the route shield
   */
  @SerializedName("text_color")
  public abstract String textColor();

  /**
   * String indicating the display ref.
   *
   * @return String display ref
   */
  @SerializedName("display_ref")
  public abstract String displayRef();

  /**
   * Convert the current {@link MapboxShield} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link MapboxShield}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined
   *   in this {@link MapboxShield}
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
  public static TypeAdapter<MapboxShield> typeAdapter(Gson gson) {
    return new AutoValue_MapboxShield.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a MapboxShield
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static MapboxShield fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, MapboxShield.class);
  }

  /**
   * This builder can be used to set the values describing the {@link MapboxShield}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Base url to query the styles endpoint.
     *
     * @param baseUrl base url to query the styles endpoint
     * @return a {@link Builder} object
     */
    public abstract Builder baseUrl(String baseUrl);

    /**
     * String indicating the name of the route shield.
     *
     * @param name name of the shield
     * @return a {@link Builder} object
     */
    public abstract Builder name(String name);

    /**
     * String indicating the color of the text to be rendered on the route shield.
     *
     * @param textColor color of the text to be rendered on the route shield
     * @return a {@link Builder} object
     */
    public abstract Builder textColor(String textColor);

    /**
     * String indicating the display ref.
     *
     * @param displayRef display ref for the shield
     * @return a {@link Builder} object
     */
    public abstract Builder displayRef(String displayRef);

    /**
     * Build a new {@link MapboxShield} object.
     *
     * @return a new {@link MapboxShield} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract MapboxShield build();
  }
}
