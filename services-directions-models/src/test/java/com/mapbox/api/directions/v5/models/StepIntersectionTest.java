package com.mapbox.api.directions.v5.models;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;


public class StepIntersectionTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    StepIntersection intersection
      = StepIntersection.builder().rawLocation(new double[]{1.0, 2.0}).build();
    assertNotNull(intersection);
  }

  @Test
  public void location_properlyGetsCreatedToPoint() throws Exception {
    Point testPoint = Point.fromLngLat(12.123456789, 65.0918273948);
    StepIntersection intersection
      = StepIntersection.builder().rawLocation(new double[]{12.123456789, 65.0918273948}).build();
    assertEquals(testPoint, intersection.location());
  }

  @Test
  public void testSerializable() throws Exception {
    StepIntersection intersection
      = StepIntersection.builder().rawLocation(new double[]{1.0, 2.0}).build();
    byte[] serialized = TestUtils.serialize(intersection);
    assertEquals(intersection, deserialize(serialized, StepIntersection.class));
  }

  @Test
  public void testToFromJson1() {
    StepIntersection intersection = StepIntersection.builder()
      .out(0)
      .entry(Arrays.asList(true))
      .bearings(Arrays.asList(125))
      .rawLocation(new double[]{13.426579, 52.508068})
      .tunnelName("test tunnel")
      .build();

    String jsonString = intersection.toJson();
    StepIntersection intersectionFromJson = StepIntersection.fromJson(jsonString);

    Assert.assertEquals(intersection, intersectionFromJson);
  }

  @Test
  public void testToFromJson2() {
    StepIntersection intersection = StepIntersection.builder()
      .in(0)
      .out(1)
      .entry(Arrays.asList(false, true, true))
      .bearings(Arrays.asList(120, 210, 300))
      .rawLocation(new double[]{13.424671, 52.508812})
      .tunnelName("test tunnel")
      .build();

    String jsonString = intersection.toJson();
    StepIntersection intersectionFromJson = StepIntersection.fromJson(jsonString);

    Assert.assertEquals(intersection, intersectionFromJson);
  }


  @Test
  public void testFromJson() {
    String stepIntersectionJsonString = "{\"out\": 0, \"entry\": [true], \"bearings\": [ 125 ], "
      + "\"tunnel_name\": \"test tunnel name\","
      + "\"location\": [ 13.426579, 52.508068 ],"
      + "\"incidents\": ["
      + "  {\"incident_type\": \"road_closure\","
      + "   \"start_time\": \"2020-08-12T14:18:20Z\","
      + "   \"end_time\": \"2020-08-12T14:46:40Z\","
      + "   \"creation_time\": \"2020-08-12T14:16:40Z\","
      + "   \"id\": 5 }"
      + "  ] "
      + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);
    Assert.assertEquals("test tunnel name", stepIntersection.tunnelName());

    Point location = stepIntersection.location();
    Assert.assertEquals(13.426579, location.longitude(), 0.0001);
    Assert.assertEquals(52.508068, location.latitude(), 0.0001);

    List<Incident> incidents = stepIntersection.incidents();
    Assert.assertNotNull(incidents);
    Assert.assertEquals(1, incidents.size());

    Incident incident = incidents.get(0);
    Assert.assertNotNull(incident);
    Assert.assertEquals("road_closure", incident.incidentType());
    Assert.assertEquals("2020-08-12T14:16:40Z", incident.creationTime());
    Assert.assertEquals("2020-08-12T14:18:20Z", incident.startTime());
    Assert.assertEquals("2020-08-12T14:46:40Z", incident.endTime());
    Assert.assertEquals(5, incident.id(), 0);

    String jsonStr = stepIntersection.toJson();

    compareJson(stepIntersectionJsonString, jsonStr);
  }
}
