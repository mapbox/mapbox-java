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
    Notification notification = Notification.builder()
            .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
            .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_STATIC)
            .build();

    assertNotNull(notification);
  }

  @Test
  public void serializationDefaultObject() throws Exception {
    Notification notification = Notification.builder()
            .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
            .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_STATIC)
            .build();

    byte[] serialized = TestUtils.serialize(notification);
    assertEquals(notification, deserialize(serialized, Notification.class));
  }

  @Test
  public void serializationFilledObject() throws Exception {
    Notification notification = Notification.builder()
      .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
      .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_MAX_WIDTH)
      .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_STATIC)
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
    Notification notification = Notification.builder()
            .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
            .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_STATIC)
            .build();

    String serialized = notification.toJson();
    assertEquals(notification, Notification.fromJson(serialized));
  }

  @Test
  public void toFromJsonFilledObject() {
    Notification notification = Notification.builder()
      .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
      .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_MAX_WIDTH)
      .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_STATIC)
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
    Notification expected = Notification.builder()
            .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
            .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_STATIC)
            .build();

    assertEquals(expected, Notification.fromJson("{\"type\": \"violation\", \"refresh_type\": \"static\"}"));
  }

  @Test
  public void fromJsonFilledObject1() {
    Notification expected = Notification.builder()
      .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
      .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_MAX_WIDTH)
      .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_STATIC)
      .geometryIndexStart(100)
      .geometryIndexEnd(123)
      .details(NotificationDetails.builder().message("some message").build())
      .unrecognizedJsonProperties(Collections.singletonMap("key1", new JsonPrimitive("value1")))
      .build();
    String json = "{\"type\": \"violation\", \"refresh_type\": \"static\", \"subtype\": \"maxWidth\", " +
      "\"geometry_index_start\": 100, \"geometry_index_end\": 123, " +
      "\"details\": {\"message\": \"some message\"}, \"key1\": \"value1\"}";

    assertEquals(expected, Notification.fromJson(json));
  }

  @Test
  public void fromJsonFilledObject2() {
    Notification expected = Notification.builder()
            .type(DirectionsCriteria.NOTIFICATION_TYPE_ALERT)
            .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_EV_STATION_UNAVAILABLE)
            .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_DYNAMIC)
            .reason(DirectionsCriteria.NOTIFICATION_EV_STATION_OUT_OF_ORDER)
            .unrecognizedJsonProperties(Collections.singletonMap("station_id", new JsonPrimitive("station1")))
            .build();
    String json = "{\"type\": \"alert\", \"refresh_type\": \"dynamic\", \"subtype\": \"stationUnavailable\", " +
            "\"reason\": \"outOfOrder\", \"station_id\": \"station1\"}";

    assertEquals(expected, Notification.fromJson(json));
  }
}