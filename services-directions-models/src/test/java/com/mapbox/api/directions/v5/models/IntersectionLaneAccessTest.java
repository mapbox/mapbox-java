package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class IntersectionLaneAccessTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    final IntersectionLaneAccess intersectionLaneAccess = IntersectionLaneAccess.builder()
      .designated(Arrays.asList("bus", "taxi"))
      .build();
    assertNotNull(intersectionLaneAccess);
  }

  @Test
  public void testSerializable() throws Exception {
    final IntersectionLaneAccess intersectionLaneAccess = IntersectionLaneAccess.builder()
      .designated(Arrays.asList("bus", "taxi", "motorcycle"))
      .build();
    final byte[] serialized = TestUtils.serialize(intersectionLaneAccess);
    assertEquals(intersectionLaneAccess, deserialize(serialized, IntersectionLaneAccess.class));
  }

  @Test
  public void testJsonSerializable() {
    final IntersectionLaneAccess intersectionLaneAccess = IntersectionLaneAccess.builder()
      .designated(Arrays.asList("bicycle", "moped"))
      .build();

    final String jsonString = intersectionLaneAccess.toJson();
    final IntersectionLaneAccess fromJson = IntersectionLaneAccess.fromJson(jsonString);

    assertEquals(intersectionLaneAccess, fromJson);
  }

  @Test
  public void testFromJson() {
    final IntersectionLaneAccess intersectionLaneAccess = IntersectionLaneAccess.builder()
      .designated(Arrays.asList("hov", "bus", "test"))
      .build();

    final String jsonString = "{\"designated\":[\"hov\",\"bus\",\"test\"]}";
    final IntersectionLaneAccess fromJson = IntersectionLaneAccess.fromJson(jsonString);
    assertEquals(intersectionLaneAccess, fromJson);
  }

  @Test
  public void testFromJsonNullValue() {
    final IntersectionLaneAccess intersectionLaneAccess = IntersectionLaneAccess.builder()
      .build();

    final String jsonString = "{}";
    final IntersectionLaneAccess fromJson = IntersectionLaneAccess.fromJson(jsonString);
    assertEquals(intersectionLaneAccess, fromJson);
  }

  @Test
  public void testFromEmptyValue() {
    final IntersectionLaneAccess intersectionLaneAccess = IntersectionLaneAccess.builder()
      .designated(Collections.emptyList())
      .build();

    final String jsonString = "{\"designated\":[]}";
    final IntersectionLaneAccess fromJson = IntersectionLaneAccess.fromJson(jsonString);
    assertEquals(intersectionLaneAccess, fromJson);
  }

  @Test
  public void testDesignatedNullValue() {
    final IntersectionLaneAccess intersectionLaneAccess = IntersectionLaneAccess.builder()
      .designated(Arrays.asList("bus", null))
      .build();

    final String serializedJson = intersectionLaneAccess.toJson();
    final String jsonString = "{\"designated\":[\"bus\",null]}";

    assertEquals(intersectionLaneAccess, IntersectionLaneAccess.fromJson(serializedJson));
    assertEquals(intersectionLaneAccess, IntersectionLaneAccess.fromJson(jsonString));
  }

  @Test
  public void testAccessValuesAreInterned() {
    final List<String> designated = Arrays.asList("bus", "taxi", "hov");

    final IntersectionLaneAccess laneAccess = IntersectionLaneAccess.builder()
      .designated(designated)
      .build();

    final IntersectionLaneAccess deserialized = IntersectionLaneAccess.fromJson(laneAccess.toJson());
    final List<String> designatedDeserialized = Objects.requireNonNull(deserialized.designated());

    assertNotNull(designatedDeserialized);
    assertEquals(designated, designatedDeserialized);

    for (int i = 0; i < designated.size(); ++i) {
      assertSame(designated.get(i), designatedDeserialized.get(i));
    }
  }

  @Test
  public void testConstants() {
    assertEquals("bicycle", IntersectionLaneAccess.BICYCLE);
    assertEquals("bus", IntersectionLaneAccess.BUS);
    assertEquals("hov", IntersectionLaneAccess.HOV);
    assertEquals("moped", IntersectionLaneAccess.MOPED);
    assertEquals("motorcycle", IntersectionLaneAccess.MOTORCYCLE);
    assertEquals("taxi", IntersectionLaneAccess.TAXI);
  }
}
