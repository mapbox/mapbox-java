package com.mapbox.api.directions.v5.models;

import com.mapbox.geojson.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;


public class MapboxStreetsV8Test extends TestUtils {
  @Test
  public void sanity() {
    assertNotNull(buildDefaultStreet());
  }

  @Test
  public void testSerializable() throws Exception {
    MapboxStreetsV8 street = buildDefaultStreet();
    byte[] serialized = TestUtils.serialize(street);
    assertEquals(street, deserialize(serialized, MapboxStreetsV8.class));
  }

  @Test
  public void testFromJson() {
    String streetJsonString = "{"
        + "\"class\": \"street\""
        + "}";

    MapboxStreetsV8 street = MapboxStreetsV8.fromJson(streetJsonString);
    Assert.assertEquals("street", street.roadClass());

    String jsonStr = street.toJson();
    compareJson(streetJsonString, jsonStr);
  }

  @Test
  public void testToFromJson() {
    MapboxStreetsV8 street = buildDefaultStreet();

    String jsonString = street.toJson();
    MapboxStreetsV8 streetFromJson = MapboxStreetsV8.fromJson(jsonString);

    Assert.assertEquals(street, streetFromJson);
  }

  private MapboxStreetsV8 buildDefaultStreet() {
    return MapboxStreetsV8.builder()
        .roadClass("street")
        .build();
  }
}
