package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;

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
}
