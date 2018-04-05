package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MaxSpeedTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    MaxSpeed maxSpeed = MaxSpeed.builder()
      .speed(65)
      .unit("mph")
      .build();
    assertNotNull(maxSpeed);
  }

  @Test
  public void testSerializable() throws Exception {
    MaxSpeed maxSpeed = MaxSpeed.builder()
      .unknown(true)
      .build();
    byte[] serialized = TestUtils.serialize(maxSpeed);
    assertEquals(maxSpeed, deserialize(serialized, MaxSpeed.class));
  }
}
