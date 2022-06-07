package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
  public void testSerializableRoundTripping() throws Exception {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .valid(true)
      .active(true)
      .validIndication("straight")
      .indications(Arrays.asList("straight","slight left"))
      .build();
    byte[] serialized = TestUtils.serialize(intersectionLanes);
    assertEquals(intersectionLanes, deserialize(serialized, IntersectionLanes.class));
  }

  @Test
  public void testJsonRoundTripping() {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .valid(true)
      .active(true)
      .validIndication("straight")
      .indications(Arrays.asList("straight","slight left"))
      .build();

    String jsonString = intersectionLanes.toJson();
    IntersectionLanes intersectionLanesFromJson = IntersectionLanes.fromJson(jsonString);

    assertEquals(intersectionLanes, intersectionLanesFromJson);
  }

  @Test
  public void testFromJson_active() {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .active(true)
      .build();

    String jsonString = "{\"active\":true}";
    IntersectionLanes intersectionLanesFromJson = IntersectionLanes.fromJson(jsonString);

    assertEquals(intersectionLanes, intersectionLanesFromJson);
  }

  @Test
  public void testFromJson_validIndication() {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .validIndication("straight")
      .build();

    String jsonString = "{\"valid_indication\":\"straight\"}";
    IntersectionLanes intersectionLanesFromJson = IntersectionLanes.fromJson(jsonString);

    assertEquals(intersectionLanes, intersectionLanesFromJson);
  }

  @Test
  public void testFromJson_etcAndGeneralPayment() {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .paymentMethods(Arrays.asList(
        DirectionsCriteria.PAYMENT_METHOD_GENERAL,
        DirectionsCriteria.PAYMENT_METHOD_ETC
      ))
      .build();

    String jsonString = "{\"payment_methods\": [\"general\", \"etc\"]}";
    IntersectionLanes intersectionLanesFromJson = IntersectionLanes.fromJson(jsonString);

    assertEquals(intersectionLanes, intersectionLanesFromJson);
  }
}
