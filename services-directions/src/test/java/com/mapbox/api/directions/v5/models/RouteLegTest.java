package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

public class RouteLegTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    RouteLeg routeLeg = RouteLeg.builder().distance(100d).duration(200d).build();
    assertNotNull(routeLeg);
  }

  @Test
  public void testSerializable() throws Exception {
    RouteLeg routeLeg = RouteLeg.builder().distance(100d).duration(200d).build();
    byte[] serialized = TestUtils.serialize(routeLeg);
    assertEquals(routeLeg, deserialize(serialized, RouteLeg.class));
  }
}
