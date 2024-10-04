package com.mapbox.api.directions.v5.models;

import com.google.gson.JsonPrimitive;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NotificationDetailsTest extends TestUtils {

  @Test
  public void sanity() {
    NotificationDetails details = NotificationDetails.builder().build();

    assertNotNull(details);
  }

  @Test
  public void serializationDefaultObject() throws Exception {
    NotificationDetails details = NotificationDetails.builder().build();

    byte[] serialized = TestUtils.serialize(details);
    assertEquals(details, deserialize(serialized, NotificationDetails.class));
  }

  @Test
  public void serializationFilledObject() throws Exception {
    NotificationDetails details = NotificationDetails.builder()
      .message("some message")
      .unit("meter")
      .actualValue("111")
      .requestedValue("99")
      .unrecognizedJsonProperties(Collections.singletonMap("key1", new JsonPrimitive("value1")))
      .build();

    byte[] serialized = TestUtils.serialize(details);
    assertEquals(details, deserialize(serialized, NotificationDetails.class));
  }

  @Test
  public void toFromJsonDefaultObject() {
    NotificationDetails details = NotificationDetails.builder().build();

    String serialized = details.toJson();
    assertEquals(details, NotificationDetails.fromJson(serialized));
  }

  @Test
  public void toFromJsonFilledObject() {
    NotificationDetails details = NotificationDetails.builder()
      .message("some message")
      .unit("meter")
      .actualValue("111")
      .requestedValue("99")
      .unrecognizedJsonProperties(Collections.singletonMap("key1", new JsonPrimitive("value1")))
      .build();

    String serialized = details.toJson();
    assertEquals(details, NotificationDetails.fromJson(serialized));
  }

  @Test
  public void fromJsonDefaultObject() {
    NotificationDetails expected = NotificationDetails.builder().build();

    assertEquals(expected, NotificationDetails.fromJson("{}"));
  }

  @Test
  public void fromJsonFilledObject() {
    NotificationDetails expected = NotificationDetails.builder()
      .message("some message")
      .unit("meter")
      .actualValue("111")
      .requestedValue("99")
      .unrecognizedJsonProperties(Collections.singletonMap("key1", new JsonPrimitive("value1")))
      .build();
    String json = "{\"message\": \"some message\", \"unit\": \"meter\", " +
      "\"actual_value\": \"111\", \"requested_value\": \"99\", " +
      "\"key1\": \"value1\"}";

    assertEquals(expected, NotificationDetails.fromJson(json));
  }

  @Test
  public void fromJsonWithNumericValuesFilledObject() {
    NotificationDetails expected = NotificationDetails.builder()
      .actualValue("111")
      .requestedValue("99")
      .build();
    String json = "{\"actual_value\": 111, \"requested_value\": 99}";

    assertEquals(expected, NotificationDetails.fromJson(json));
  }
}