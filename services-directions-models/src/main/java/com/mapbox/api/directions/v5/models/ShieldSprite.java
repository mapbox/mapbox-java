package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

import java.io.Serializable;

/**
 * ShieldSprite.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class ShieldSprite extends DirectionsJsonObject implements Serializable {

  /**
   * Create a new instance of this class by using the {@link ShieldSprite.Builder} class.
   *
   * @return {@link ShieldSprite.Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_ShieldSprite.Builder();
  }

  /**
   * Shield sprite's name.
   */
  @NonNull
  public abstract String spriteName();

  /**
   * Shield sprite's attributes.
   */
  @NonNull
  public abstract ShieldSpriteAttribute spriteAttributes();

  /**
   * Convert the current {@link ShieldSprite} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link ShieldSprite}.
   *
   * @return a {@link ShieldSprite.Builder} with the same values set to match the ones defined
   *   in this {@link ShieldSprite}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<ShieldSprite> typeAdapter(Gson gson) {
    return new AutoValue_ShieldSprite.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link ShieldSprite}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Shield sprite's name.
     *
     * @param spriteName sprite's name
     */
    @NonNull
    public abstract Builder spriteName(@NonNull String spriteName);

    /**
     * Shield sprite's attributes.
     *
     * @param spriteAttributes sprite's attributes
     */
    @NonNull
    public abstract Builder spriteAttributes(@NonNull ShieldSpriteAttribute spriteAttributes);

    /**
     * Build a new {@link ShieldSprite} object.
     *
     * @return a new {@link ShieldSprite} using the provided values in this builder
     */
    @NonNull
    public abstract ShieldSprite build();
  }
}
