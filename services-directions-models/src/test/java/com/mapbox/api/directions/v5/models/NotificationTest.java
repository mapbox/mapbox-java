package com.mapbox.api.directions.v5.models;

import com.google.gson.JsonPrimitive;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NotificationTest extends TestUtils {

  @Test
  public void sanity() {
    Notification notification = Notification.builder().type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION).build();

    assertNotNull(notification);
  }

  @Test
  public void serializationDefaultObject() throws Exception {
    Notification notification = Notification.builder().type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION).build();

    byte[] serialized = TestUtils.serialize(notification);
    assertEquals(notification, deserialize(serialized, Notification.class));
  }

  @Test
  public void serializationFilledObject() throws Exception {
    Notification notification = Notification.builder()
      .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
      .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_MAX_WIDTH)
      .geometryIndexStart(100)
      .geometryIndexEnd(123)
      .details(NotificationDetails.builder().message("some message").build())
      .unrecognizedJsonProperties(Collections.singletonMap("key1", new JsonPrimitive("value1")))
      .build();

    byte[] serialized = TestUtils.serialize(notification);
    assertEquals(notification, deserialize(serialized, Notification.class));
  }

  @Test
  public void toFromJsonDefaultObject() {
    Notification notification = Notification.builder().type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION).build();

    String serialized = notification.toJson();
    assertEquals(notification, Notification.fromJson(serialized));
  }

  @Test
  public void toFromJsonFilledObject() {
    Notification notification = Notification.builder()
      .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
      .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_MAX_WIDTH)
      .geometryIndexStart(100)
      .geometryIndexEnd(123)
      .details(NotificationDetails.builder().message("some message").build())
      .unrecognizedJsonProperties(Collections.singletonMap("key1", new JsonPrimitive("value1")))
      .build();

    String serialized = notification.toJson();
    assertEquals(notification, Notification.fromJson(serialized));
  }

  @Test
  public void fromJsonDefaultObject() {
    Notification expected = Notification.builder().type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION).build();

    assertEquals(expected, Notification.fromJson("{\"type\": \"violation\"}"));
  }

  @Test
  public void fromJsonFilledObject() {
    Notification expected = Notification.builder()
      .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
      .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_MAX_WIDTH)
      .geometryIndexStart(100)
      .geometryIndexEnd(123)
      .details(NotificationDetails.builder().message("some message").build())
      .unrecognizedJsonProperties(Collections.singletonMap("key1", new JsonPrimitive("value1")))
      .build();
    String json = "{\"type\": \"violation\", \"subtype\": \"maxWidth\", " +
      "\"geometry_index_start\": 100, \"geometry_index_end\": 123, " +
      "\"details\": {\"message\": \"some message\"}, \"key1\": \"value1\"}";

    assertEquals(expected, Notification.fromJson(json));
  }
}