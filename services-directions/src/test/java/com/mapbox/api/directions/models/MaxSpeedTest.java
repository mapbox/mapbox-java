package com.mapbox.api.directions.models;

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

  @Test
  public void testToFromJson1() {

    MaxSpeed maxSpeed = MaxSpeed.builder()
      .unknown(true)
      .build();

    String jsonString = maxSpeed.toJson();
    MaxSpeed maxSpeedFromJson = MaxSpeed.fromJson(jsonString);

    assertEquals(maxSpeed, maxSpeedFromJson);
  }

  @Test
  public void testToFromJson2() {

    MaxSpeed maxSpeed = MaxSpeed.builder()
      .none(true)
      .build();

    String jsonString = maxSpeed.toJson();
    MaxSpeed maxSpeedFromJson = MaxSpeed.fromJson(jsonString);

    assertEquals(maxSpeed, maxSpeedFromJson);
  }

  @Test
  public void testToFromJson3() {

    MaxSpeed maxSpeed = MaxSpeed.builder()
      .speed(65)
      .unit("mph")
      .build();

    String jsonString = maxSpeed.toJson();
    MaxSpeed maxSpeedFromJson = MaxSpeed.fromJson(jsonString);

    assertEquals(maxSpeed, maxSpeedFromJson);
  }
}
