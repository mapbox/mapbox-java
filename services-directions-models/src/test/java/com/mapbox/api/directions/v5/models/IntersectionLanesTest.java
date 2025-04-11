package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
      .access(IntersectionLaneAccess.builder().designated(Arrays.asList("bus", "taxi")).build())
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
      .access(
        IntersectionLaneAccess.builder()
          .designated(Arrays.asList("bus", "taxi", "hov")).
          build()
      )
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

  @Test
  public void testFromJson_etc2Payment() {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .paymentMethods(Arrays.asList(
        DirectionsCriteria.PAYMENT_METHOD_ETC2
      ))
      .build();

    String jsonString = "{\"payment_methods\": [\"etc2\"]}";
    IntersectionLanes intersectionLanesFromJson = IntersectionLanes.fromJson(jsonString);

    assertEquals(intersectionLanes, intersectionLanesFromJson);
  }

  @Test
  public void testFromJson_access() {
    final IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .active(true)
      .indications(Arrays.asList("straight", "right"))
      .access(IntersectionLaneAccess.builder().designated(Arrays.asList("hov", "bus")).build())
      .build();

    final String jsonString = "{\n" +
      "  \"active\": true,\n" +
      "  \"indications\": [\"straight\", \"right\"],\n" +
      "  \"access\": {\n" +
      "    \"designated\": [\"hov\", \"bus\"]\n" +
      "  }\n" +
      "}";

    final IntersectionLanes intersectionLanesFromJson = IntersectionLanes.fromJson(jsonString);
    assertEquals(intersectionLanes, intersectionLanesFromJson);
  }

  @Test
  public void testIndicationsAreInterned() {
    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .validIndication("straight")
      .indications(Arrays.asList("straight","straight"))
      .build();

    IntersectionLanes deserialized = IntersectionLanes.fromJson(
      intersectionLanes.toJson()
    );

    List<String> indications = deserialized.indications();
    assertEquals(Arrays.asList("straight","straight"), indications);
    assertEquals("straight", deserialized.validIndication());

    assertSame(indications.get(0), indications.get(1));
    assertSame(indications.get(0), deserialized.validIndication());
  }

  @Test
  public void testPaymentMethodsAreInterned() {
    List<String> paymentMethods = Arrays.asList(
      DirectionsCriteria.PAYMENT_METHOD_GENERAL,
      DirectionsCriteria.PAYMENT_METHOD_GENERAL
    );

    IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .paymentMethods(paymentMethods)
      .build();

    IntersectionLanes deserialized = IntersectionLanes.fromJson(
      intersectionLanes.toJson()
    );

    assertEquals(paymentMethods, deserialized.paymentMethods());

    assertSame(
      deserialized.paymentMethods().get(0),
      deserialized.paymentMethods().get(1)
    );
  }

  @Test
  public void testAccessValuesAreInterned() {
    final List<String> designated = Arrays.asList("bus", "taxi");

    final IntersectionLanes intersectionLanes = IntersectionLanes.builder()
      .access(
        IntersectionLaneAccess.builder()
          .designated(designated)
          .build()
      )
      .build();

    final IntersectionLanes deserialized = IntersectionLanes.fromJson(intersectionLanes.toJson());
    final List<String> designatedDeserialized = Objects.requireNonNull(deserialized.access())
      .designated();

    assertNotNull(designatedDeserialized);
    assertEquals(designated, designatedDeserialized);

    for (int i = 0; i < designated.size(); ++i) {
      assertSame(designated.get(i), designatedDeserialized.get(i));
    }
  }
}
