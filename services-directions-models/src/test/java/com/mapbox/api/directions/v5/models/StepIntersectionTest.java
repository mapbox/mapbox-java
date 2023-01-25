package com.mapbox.api.directions.v5.models;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

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
      .geometryIndex(123)
      .isUrban(true)
      .restStop(RestStop.builder().type("rest_area").name("stop_name").build())
      .tollCollection(TollCollection.builder().type("toll_gantry").name("toll_name").build())
      .adminIndex(2)
      .stopSign(true)
      .yieldSign(true)
      .trafficSignal(true)
      .mapboxStreetsV8(MapboxStreetsV8.builder().roadClass("street").build())
      .tunnelName("tunnel_name")
      .junction(Junction.builder().name("jct_name").build())
      .interchange(Interchange.builder().name("ic_name").build())
      .build();

    String jsonString = intersection.toJson();
    StepIntersection intersectionFromJson = StepIntersection.fromJson(jsonString);

    assertEquals(intersection, intersectionFromJson);
  }

  @Test
  public void testToFromJson2() {
    StepIntersection intersection = StepIntersection.builder()
      .in(0)
      .out(1)
      .entry(Arrays.asList(false, true, true))
      .bearings(Arrays.asList(120, 210, 300))
      .rawLocation(new double[]{13.424671, 52.508812})
      .mapboxStreetsV8(MapboxStreetsV8.builder().roadClass("street").build())
      .tunnelName("tunnel_name")
      .build();

    String jsonString = intersection.toJson();
    StepIntersection intersectionFromJson = StepIntersection.fromJson(jsonString);

    assertEquals(intersection, intersectionFromJson);
  }

  @Test
  public void testFromJson() {
    String stepIntersectionJsonString = "{"
    + "\"location\": [ 13.426579, 52.508068 ],"
    + "\"bearings\": [ 125 ], "
    + "\"entry\": [true], "
    + "\"out\": 0, "
    + "\"geometry_index\": 123,"
    + "\"is_urban\": true,"
    + "\"mapbox_streets_v8\": {\"class\": \"street\"},"
    + "\"tunnel_name\": \"test tunnel name\","
    + "\"railway_crossing\": true,"
    + "\"traffic_signal\": true,"
    + "\"stop_sign\": true,"
    + "\"yield_sign\": true"
    + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);
    assertEquals(123, stepIntersection.geometryIndex().intValue());
    assertTrue(stepIntersection.isUrban());
    assertEquals("street", stepIntersection.mapboxStreetsV8().roadClass());
    assertEquals("test tunnel name", stepIntersection.tunnelName());
    assertTrue(stepIntersection.railwayCrossing());
    assertTrue(stepIntersection.trafficSignal());
    assertTrue(stepIntersection.stopSign());
    assertTrue(stepIntersection.yieldSign());

    Point location = stepIntersection.location();
    assertEquals(13.426579, location.longitude(), 0.0001);
    assertEquals(52.508068, location.latitude(), 0.0001);

    String jsonStr = stepIntersection.toJson();

    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testAdminIndex() {
    String stepIntersectionJsonString = "{"
    + "\"location\": [ 13.426579, 52.508068 ],"
    + "\"admin_index\": 2"
    + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertEquals(2, stepIntersection.adminIndex().intValue());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testRestStop() {
    String stepIntersectionJsonString = "{"
    + "\"location\": [ 13.426579, 52.508068 ],"
    + "\"rest_stop\": { \"type\": \"rest_area\", \"name\": \"stop_name\" }"
    + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertEquals("rest_area", stepIntersection.restStop().type());
    Assert.assertEquals("stop_name", stepIntersection.restStop().name());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testTollCollection() {
    String stepIntersectionJsonString = "{"
    + "\"location\": [ 13.426579, 52.508068 ],"
    + "\"toll_collection\": { \"type\": \"toll_gantry\", \"name\": \"toll_name\" }"
    + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertEquals("toll_gantry", stepIntersection.tollCollection().type());
    Assert.assertEquals("toll_name", stepIntersection.tollCollection().name());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testJunction() {
    String stepIntersectionJsonString = "{"
        + "\"location\": [ 13.426579, 52.508068 ],"
        + "\"jct\": { \"name\": \"jct_name\" }"
        + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertEquals("jct_name", stepIntersection.junction().name());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullJunction() {
    String stepIntersectionJsonString = "{"
        + "\"location\": [ 13.426579, 52.508068 ]"
        + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertNull(stepIntersection.junction());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testInterchange() {
    String stepIntersectionJsonString = "{"
        + "\"location\": [ 13.426579, 52.508068 ],"
        + "\"ic\": { \"name\": \"ic_name\" }"
        + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertEquals("ic_name", stepIntersection.interchange().name());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullInterchange() {
    String stepIntersectionJsonString = "{"
        + "\"location\": [ 13.426579, 52.508068 ]"
        + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertNull(stepIntersection.interchange());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }
}
