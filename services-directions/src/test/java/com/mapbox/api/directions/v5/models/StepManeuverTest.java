package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
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
}
