package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.geojson.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.Assert;
import org.junit.Test;

public class StepManeuverTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    StepManeuver stepManeuver = StepManeuver.builder().rawLocation(new double[] {2.0, 1.0}).build();
    assertNotNull(stepManeuver);
  }

  @Test
  public void location_properlyGetsCreatedToPoint() throws Exception {
    Point testPoint = Point.fromLngLat(12.123456789, 65.0918273948);
    StepManeuver stepManeuver = StepManeuver.builder()
      .rawLocation(new double[] {12.123456789, 65.0918273948})
      .build();
    assertEquals(testPoint, stepManeuver.location());
  }

  @Test
  public void testSerializable() throws Exception {
    StepManeuver stepManeuver = StepManeuver.builder().rawLocation(new double[] {2.0, 1.0}).build();
    byte[] serialized = TestUtils.serialize(stepManeuver);
    assertEquals(stepManeuver, deserialize(serialized, StepManeuver.class));
  }

  @Test
  public void testToFromJson2() {
    StepManeuver stepManeuver = StepManeuver.builder()
      .rawLocation(new double[]{13.424671, 52.508812})
      .type("turn")
      .modifier(ManeuverModifier.LEFT)
      .bearingBefore(299.0)
      .bearingAfter(202.0)
      .instruction("Turn left onto Adalbertstraße")
      .exit(5)
      .build();

    String jsonString = stepManeuver.toJson();
    StepManeuver stepManeuverFromJson = StepManeuver.fromJson(jsonString);

    assertEquals(stepManeuver, stepManeuverFromJson);
  }

  @Test
  public void testFromJson() {
    String stepManeuverJsonString =
      "{\"bearing_before\": 299," +
        "\"bearing_after\": 202, " +
        "\"type\": \"turn\", " +
        "\"modifier\": \"left\", " +
        "\"location\": [ 13.424671,52.508812]," +
        "\"instruction\": \"Turn left onto Adalbertstraße\"}";

    StepManeuver stepManeuver = StepManeuver.fromJson(stepManeuverJsonString);

    Point location = stepManeuver.location();
    Assert.assertEquals(13.424671, location.longitude(), 0.0001);
    Assert.assertEquals(52.508812, location.latitude(), 0.0001);

    String jsonStr = stepManeuver.toJson();

    compareJson(stepManeuverJsonString, jsonStr);
  }
}
