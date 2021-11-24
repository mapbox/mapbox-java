package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ShieldSpritesTest extends TestUtils {

  @Test
  public void sanity() {
    assertNotNull(getDefault());
  }

  @Test
  public void serializable() throws Exception {
    ShieldSprites sprites = getDefault();
    byte[] serialized = TestUtils.serialize(sprites);

    assertEquals(sprites, deserialize(serialized, ShieldSprites.class));
  }

  @Test
  public void jsonComparingDefaultSprites() {
    ShieldSprites sprites = getDefault();
    String json = sprites.toJson();

    ShieldSprites fromJson = ShieldSprites.fromJson(json);

    assertEquals(sprites, fromJson);
  }

  @Test
  public void jsonFromFixture() throws Exception {
    String json = loadJsonFixture("styles_shield_sprites.json");
    ShieldSprites sprites = ShieldSprites.fromJson(json);

    String spritesJson = sprites.toJson();

    compareJson(json, spritesJson);
  }

  private ShieldSprites getDefault() {
    List<Double> placeholder = new ArrayList<>();
    placeholder.add(0.0);
    placeholder.add(17.0);
    placeholder.add(20.0);
    placeholder.add(40.0);
    ShieldSpriteAttribute shieldSpriteAttribute = ShieldSpriteAttribute.builder()
        .width(200)
        .height(200)
        .x(100)
        .y(100)
        .pixelRatio(2)
        .placeholder(placeholder)
        .visible(true)
        .build();
    ShieldSprite shieldSpriteOne = ShieldSprite.builder()
        .spriteName("sprite-1")
        .spriteAttributes(shieldSpriteAttribute)
        .build();
    ShieldSprite shieldSpriteTwo = ShieldSprite.builder()
        .spriteName("sprite-2")
        .spriteAttributes(shieldSpriteAttribute)
        .build();
    List<ShieldSprite> sprites = new ArrayList<>();
    sprites.add(shieldSpriteOne);
    sprites.add(shieldSpriteTwo);

    return ShieldSprites.builder()
        .sprites(sprites)
        .build();
  }
}
