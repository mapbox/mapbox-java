package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ShieldSprites.
 */
@AutoValue
public abstract class ShieldSprites implements Serializable {

  /**
   * Create a new instance of this class by using the {@link ShieldSprites.Builder} class.
   *
   * @return {@link ShieldSprites.Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_ShieldSprites.Builder();
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a shield sprite
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  @NonNull
  public static ShieldSprites fromJson(@NonNull String json) {
    List<ShieldSprite> sprites = new ArrayList<>();
    GsonBuilder gson = new GsonBuilder();
    JsonObject jsonObject = gson.create().fromJson(json, JsonObject.class);
    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    for (Map.Entry<String, JsonElement> entry : entries) {
      String spriteName = entry.getKey();
      ShieldSpriteAttribute spriteAttribute = ShieldSpriteAttribute.fromJson(
          jsonObject.get(spriteName).toString()
      );
      ShieldSprite sprite = ShieldSprite.builder()
          .spriteName(spriteName)
          .spriteAttributes(spriteAttribute)
          .build();
      sprites.add(sprite);
    }
    return ShieldSprites.builder()
        .sprites(sprites)
        .build();
  }

  /**
   * List of {@link ShieldSprite}.
   */
  @NonNull
  public abstract List<ShieldSprite> sprites();

  /**
   * Convert the current {@link ShieldSprites} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link ShieldSprites}.
   *
   * @return a {@link ShieldSprites.Builder} with the same values set to match the ones defined
   *   in this {@link ShieldSprites}
   */
  public abstract Builder toBuilder();

  /**
   * This takes the currently defined values found inside the {@link ShieldSprites} instance and
   * converts it to a {@link ShieldSprites} string.
   *
   * @return a JSON string which represents a {@link ShieldSprites}
   */
  @NonNull
  public String toJson() {
    JsonParser parser = new JsonParser();
    Gson gson = new Gson();
    JsonObject json = new JsonObject();
    for (ShieldSprite sprite : this.sprites()) {
      json.add(sprite.spriteName(), parser.parse(sprite.spriteAttributes().toJson()));
    }
    return gson.toJson(json);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<ShieldSprites> typeAdapter(Gson gson) {
    return new AutoValue_ShieldSprites.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link ShieldSprites}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * List of {@link ShieldSprite}.
     *
     * @param sprites list of sprites
     */
    @NonNull
    public abstract Builder sprites(@NonNull List<ShieldSprite> sprites);

    /**
     * Build a new {@link ShieldSprites} object.
     *
     * @return a new {@link ShieldSprites} using the provided values in this builder
     */
    @NonNull
    public abstract ShieldSprites build();
  }
}
