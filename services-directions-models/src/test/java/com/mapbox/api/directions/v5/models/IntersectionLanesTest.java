package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class IntersectionLanesTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .indications(new ArrayList<String>())
      .valid(true)
      .build();
    assertNotNull(intersectionLanes);
  }

  @Test
  public void testSerializable() throws Exception {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .indications(new ArrayList<String>())
      .valid(true)
      .build();
    byte[] serialized = TestUtils.serialize(intersectionLanes);
    assertEquals(intersectionLanes, deserialize(serialized, IntersectionLanes.class));
  }

  @Test
  public void testToFromJson() {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .indications(Arrays.asList("straight","slight left"))
      .valid(true)
      .build();

    String jsonString = intersectionLanes.toJson();
    IntersectionLanes intersectionLanesFromJson = IntersectionLanes.fromJson(jsonString);

    assertEquals(intersectionLanes, intersectionLanesFromJson);
  }
}
