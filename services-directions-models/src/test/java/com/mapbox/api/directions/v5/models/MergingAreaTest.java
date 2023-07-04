package com.mapbox.api.directions.v5.models;

import com.google.gson.JsonPrimitive;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MergingAreaTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    MergingArea mergingArea = MergingArea.builder().build();
    assertNotNull(mergingArea);
  }

  @Test
  public void testSerializableDefault() throws Exception {
    MergingArea intersection = MergingArea.builder().build();
    byte[] serialized = TestUtils.serialize(intersection);
    assertEquals(intersection, deserialize(serialized, MergingArea.class));
  }

  @Test
  public void testSerializableFilled() throws Exception {
    MergingArea mergingArea = MergingArea.builder().type(MergingArea.TYPE_FROM_LEFT).build();
    byte[] serialized = TestUtils.serialize(mergingArea);
    assertEquals(mergingArea, deserialize(serialized, MergingArea.class));
  }

  @Test
  public void testToFromJsonDefault() {
    MergingArea mergingArea = MergingArea.builder().build();

    String jsonString = mergingArea.toJson();
    MergingArea mergingAreaFromJson = MergingArea.fromJson(jsonString);

    assertEquals(mergingArea, mergingAreaFromJson);
  }

  @Test
  public void testToFromJsonFilled() {
    MergingArea mergingArea = MergingArea.builder().type(MergingArea.TYPE_FROM_LEFT).build();

    String jsonString = mergingArea.toJson();
    MergingArea mergingAreaFromJson = MergingArea.fromJson(jsonString);

    assertEquals(mergingArea, mergingAreaFromJson);
  }

  @Test
  public void testFromJsonDefault() {
    String mergingAreaJsonString = "{}";

    MergingArea mergingArea = MergingArea.fromJson(mergingAreaJsonString);

    assertNull(mergingArea.type());
  }

  @Test
  public void testFromJsonFromLeft() {
    String mergingAreaJsonString = "{\"type\":\"from_left\"}";

    MergingArea mergingArea = MergingArea.fromJson(mergingAreaJsonString);

    assertEquals(MergingArea.TYPE_FROM_LEFT, mergingArea.type());
  }

  @Test
  public void testFromJsonFromRight() {
    String mergingAreaJsonString = "{\"type\":\"from_right\"}";

    MergingArea mergingArea = MergingArea.fromJson(mergingAreaJsonString);

    assertEquals(MergingArea.TYPE_FROM_RIGHT, mergingArea.type());
  }

  @Test
  public void testFromJsonFromBothSides() {
    String mergingAreaJsonString = "{\"type\":\"from_both_sides\"}";

    MergingArea mergingArea = MergingArea.fromJson(mergingAreaJsonString);

    assertEquals(MergingArea.TYPE_FROM_BOTH_SIDES, mergingArea.type());
  }

  @Test
  public void testFromJsonUnknownType() {
    String mergingAreaJsonString = "{\"type\":\"unknown\"}";

    MergingArea mergingArea = MergingArea.fromJson(mergingAreaJsonString);

    assertEquals("unknown", mergingArea.type());
  }

  @Test
  public void testFromJsonUnrecognizedProperties() {
    String mergingAreaJsonString = "{\"key\":\"value\"}";

    MergingArea mergingArea = MergingArea.fromJson(mergingAreaJsonString);

    assertEquals(
      Collections.singletonMap("key", new JsonPrimitive("value")),
      mergingArea.getUnrecognizedJsonProperties()
    );
  }
}
