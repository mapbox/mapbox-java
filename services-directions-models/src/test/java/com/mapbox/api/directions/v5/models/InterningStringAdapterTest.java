package com.mapbox.api.directions.v5.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.core.TestUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class InterningStringAdapterTest extends TestUtils {

  private static final Gson GSON = new GsonBuilder()
    .registerTypeAdapter(String.class, new InterningStringAdapter())
    .create();

  @Test
  public void testSerializeDeserialize() {
    final String json = "Keep left to take B 14 toward Esslingen, Stuttgart";

    final String jsonString = GSON.toJson(json);
    assertEquals("\"Keep left to take B 14 toward Esslingen, Stuttgart\"", jsonString);

    final String deserialized = GSON.fromJson(jsonString, String.class);
    assertEquals(json, deserialized);
  }

  @Test
  public void testSerializeDeserializeNull() {
    final String json = null;

    final String jsonString = GSON.toJson(json);
    assertEquals("null", jsonString);

    final String deserialized = GSON.fromJson(jsonString, String.class);
    assertNull(deserialized);
  }

  @Test
  public void testSerializeDeserializeEmptyString() {
    final String json = "";

    final String jsonString = GSON.toJson(json);
    assertEquals("\"\"", jsonString);

    final String deserialized = GSON.fromJson(jsonString, String.class);
    assertEquals(json, deserialized);
  }

  @Test
  public void testInternedString() {
    final String json = "\"Munich\"";
    assertSame(
      GSON.fromJson(json, String.class),
      GSON.fromJson(json, String.class)
    );
  }
}
