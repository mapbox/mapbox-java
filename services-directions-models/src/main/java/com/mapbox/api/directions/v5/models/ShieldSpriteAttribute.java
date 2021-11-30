package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

import java.io.Serializable;
import java.util.List;

/**
 * ShieldSpriteAttribute.
 */
@AutoValue
public abstract class ShieldSpriteAttribute implements Serializable {

  /**
   * Create a new instance of this class by using the {@link ShieldSpriteAttribute.Builder} class.
   *
   * @return {@link ShieldSpriteAttribute.Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_ShieldSpriteAttribute.Builder();
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a shield sprite
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  @NonNull
  public static ShieldSpriteAttribute fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, ShieldSpriteAttribute.class);
  }

  /**
   * Shield sprite's width.
   */
  @NonNull
  public abstract Integer width();

  /**
   * Shield sprite's height.
   */
  @NonNull
  public abstract Integer height();

  /**
   * Shield sprite's x position.
   */
  @NonNull
  public abstract Integer x();

  /**
   * Shield sprite's y position.
   */
  @NonNull
  public abstract Integer y();

  /**
   * Shield sprite's pixel ratio.
   */
  @NonNull
  public abstract Integer pixelRatio();

  /**
   * Shield sprite's placeholder (optional).
   */
  @Nullable
  public abstract List<Double> placeholder();

  /**
   * Shield sprite's visibility.
   */
  @NonNull
  public abstract Boolean visible();

  /**
   * Convert the current {@link ShieldSpriteAttribute} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link ShieldSpriteAttribute}.
   *
   * @return a {@link ShieldSpriteAttribute.Builder} with the same values set to match the ones
   *   defined in this {@link ShieldSpriteAttribute}
   */
  public abstract Builder toBuilder();

  /**
   * This takes the currently defined values found inside the {@link ShieldSpriteAttribute} instance
   * and converts it to a {@link ShieldSpriteAttribute} string.
   *
   * @return a JSON string which represents a {@link ShieldSpriteAttribute}
   */
  @NonNull
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().toJson(this);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<ShieldSpriteAttribute> typeAdapter(Gson gson) {
    return new AutoValue_ShieldSpriteAttribute.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link ShieldSpriteAttribute}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Shield sprite's width.
     *
     * @param width sprite's width
     */
    @NonNull
    public abstract Builder width(@NonNull Integer width);

    /**
     * Shield sprite's height.
     *
     * @param height sprite's height
     */
    @NonNull
    public abstract Builder height(@NonNull Integer height);

    /**
     * Shield sprite's x position.
     *
     * @param x sprite's x position
     */
    @NonNull
    public abstract Builder x(@NonNull Integer x);

    /**
     * Shield sprite's y position.
     *
     * @param y sprite's x position
     */
    @NonNull
    public abstract Builder y(@NonNull Integer y);

    /**
     * Shield sprite's pixel ratio.
     *
     * @param pixelRatio sprite's pixel ratio
     */
    @NonNull
    public abstract Builder pixelRatio(@NonNull Integer pixelRatio);

    /**
     * Shield sprite's placeholder.
     *
     * @param placeholder sprite's placeholder
     */
    @NonNull
    public abstract Builder placeholder(@Nullable List<Double> placeholder);

    /**
     * Shield sprite's visibility.
     *
     * @param visible sprite's visibility
     */
    @NonNull
    public abstract Builder visible(@NonNull Boolean visible);

    /**
     * Build a new {@link ShieldSpriteAttribute} object.
     *
     * @return a new {@link ShieldSpriteAttribute} using the provided values in this builder
     */
    @NonNull
    public abstract ShieldSpriteAttribute build();
  }
}
