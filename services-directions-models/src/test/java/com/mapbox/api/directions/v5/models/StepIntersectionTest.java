package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
      .mergingArea(MergingArea.builder().type(MergingArea.TYPE_FROM_LEFT).build())
      .duration(12.34)
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
      .mergingArea(MergingArea.builder().type(MergingArea.TYPE_FROM_LEFT).build())
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
    + "\"merging_area\": {\"type\": \"from_right\"},"
    + "\"yield_sign\": true,"
    + "\"duration\": 12.34"
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
    assertEquals(MergingArea.builder().type(MergingArea.TYPE_FROM_RIGHT).build(), stepIntersection.mergingArea());
    assertEquals(12.34, stepIntersection.duration(), 0.0001);

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

  @Test
  public void testDuration() {
    String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ],"
      + "\"duration\": 12.34"
      + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertEquals(12.34, stepIntersection.duration(), 0.0001);
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullDuration() {
    String stepIntersectionJsonString = "{"
            + "\"location\": [ 13.426579, 52.508068 ]"
            + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertNull(stepIntersection.duration());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testMergingArea() {
    String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ],"
      + "\"merging_area\": { \"type\": \"from_left\" }"
      + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertEquals(MergingArea.builder().type(MergingArea.TYPE_FROM_LEFT).build(), stepIntersection.mergingArea());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullMergingArea() {
    String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ]"
      + "}";

    StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    Assert.assertNull(stepIntersection.mergingArea());
    String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testFormOfWay() {
    final String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ],"
      + "\"form_of_way\": [\n"
      + "    \"freeway\",\n"
      + "    \"roundabout\",\n"
      + "    \"ramp\",\n"
      + "    null\n"
      + "]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    final List<String> formOfWay = Arrays.asList(
      "freeway", "roundabout", "ramp", null
    );

    assertEquals(formOfWay, stepIntersection.formOfWay());

    final String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullFormOfWay() {
    final String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);
    assertNull(stepIntersection.formOfWay());
  }

  @Test
  public void testGeometries() {
    final String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ],"
      + "\"geometries\": [\n"
      + "    \"{{vacBwuenX?^?|OAzXBdP\",\n"
      + "    \"}xfbcB_yjkXcEdAcF`KwClEyH|NoQz[iOdUqOhSeT|TgH`FyHnFuKbF}ChAcFhBoGpAqFXeIXeEHuVl@\",\n"
      + "    null\n"
      + "]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    final List<String> geometries = Arrays.asList(
      "{{vacBwuenX?^?|OAzXBdP",
      "}xfbcB_yjkXcEdAcF`KwClEyH|NoQz[iOdUqOhSeT|TgH`FyHnFuKbF}ChAcFhBoGpAqFXeIXeEHuVl@",
      null
    );

    assertEquals(geometries, stepIntersection.geometries());

    final String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullGeometries() {
    String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);
    assertNull(stepIntersection.geometries());
  }

  @Test
  public void testAccess() {
    final String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ],"
      + "\"access\": [\n"
      + "    0,\n"
      + "    -1,\n"
      + "    1,\n"
      + "    2,\n"
      + "    null"
      + "]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    final List<Integer> access = Arrays.asList(
      0, -1, 1, 2, null
    );

    assertEquals(access, stepIntersection.access());

    final String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullAccess() {
    String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);
    assertNull(stepIntersection.access());
  }

  @Test
  public void testElevated() {
    final String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ],"
      + "\"elevated\": [\n"
      + "    true,\n"
      + "    false,\n"
      + "    null"
      + "]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    final List<Boolean> elevated = Arrays.asList(
      true, false, null
    );

    assertEquals(elevated, stepIntersection.elevated());

    final String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullElevated() {
    String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);
    assertNull(stepIntersection.elevated());
  }

  @Test
  public void testBridges() {
    final String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ],"
      + "\"bridges\": [\n"
      + "    true,\n"
      + "    false,\n"
      + "    true,\n"
      + "    null"
      + "]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);

    final List<Boolean> bridge = Arrays.asList(
      true, false, true, null
    );

    assertEquals(bridge, stepIntersection.bridges());

    final String jsonStr = stepIntersection.toJson();
    compareJson(stepIntersectionJsonString, jsonStr);
  }

  @Test
  public void testNullBridges() {
    String stepIntersectionJsonString = "{"
      + "\"location\": [ 13.426579, 52.508068 ]"
      + "}";

    final StepIntersection stepIntersection = StepIntersection.fromJson(stepIntersectionJsonString);
    assertNull(stepIntersection.bridges());
  }
}
