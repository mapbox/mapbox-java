package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class containing information about merging area,
 * i.e. an area where traffic is being merged into the current road.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
public abstract class MergingArea extends DirectionsJsonObject {

  /**
   * {@link Type} value meaning that traffic is being merged into current road from the left side.
   */
  public static final String TYPE_FROM_LEFT = "from_left";

  /**
   * {@link Type} value meaning that traffic is being merged into current road from the right side.
   */
  public static final String TYPE_FROM_RIGHT = "from_right";

  /**
   * {@link Type} value meaning that traffic is being merged into current road from both sides.
   */
  public static final String TYPE_FROM_BOTH_SIDES = "from_both_sides";

  /**
   * Merging Area type.
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef({
    TYPE_FROM_LEFT,
    TYPE_FROM_RIGHT,
    TYPE_FROM_BOTH_SIDES
  })
  public @interface Type {
  }

  /**
   * Type of the merging area. See {@link Type} for possible values.
   *
   * @return type of the merging area.
   */
  @Nullable
  public abstract @Type String type();

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_MergingArea.Builder();
  }

  /**
   * Convert the current {@link MergingArea} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link MergingArea}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined in this
   * {@link MergingArea}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<MergingArea> typeAdapter(Gson gson) {
    return new AutoValue_MergingArea.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a Merging Area
   * @return a new instance of this class defined by the values passed in the method
   */
  public static MergingArea fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, MergingArea.class);
  }

  /**
   * This builder can be used to set the values describing the {@link MergingArea}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Type of the merging area.
     *
     * @param type type, see {@link Type} for possible values.
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder type(@Nullable @Type String type);

    /**
     * Build a new {@link MergingArea} object.
     *
     * @return a new {@link MergingArea} using the provided values in this builder
     */
    @NonNull
    public abstract MergingArea build();
  }
}
