package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AmenityTest extends TestUtils {

  private static final String DIRECTIONS_V5_AMENITIES_FIXTURE = "directions_v5_amenities.json";
  @Test
  public void sanity(){
    assertNotNull(getDefault());
  }

  private Amenity getDefault() {
    return Amenity.builder()
        .type("restaurant")
        .name("starbucks")
        .build();
  }

  @Test
  public void serializableObject() throws Exception {
    Amenity expected = Amenity
        .builder()
        .type("restaurant")
        .name("starbucks")
        .build();

    Amenity actual = deserialize(
        TestUtils.serialize(expected),
        Amenity.class
    );

    assertEquals(expected, actual);
  }

  @Test
  public void toFromJson() {
    Amenity expected = Amenity
        .builder()
        .type("restaurant")
        .name("starbucks")
        .build();

    Amenity actual = Amenity.fromJson(expected.toJson());

    assertEquals(expected, actual);
  }

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_AMENITIES_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);

    RestStop restStop = response.routes().get(0).legs().get(0).steps().get(1).intersections().get(0).restStop();
    Amenity amenity1 = restStop.amenities().get(0);
    Amenity amenity2 = restStop.amenities().get(1);

    assertNotNull(amenity1);
    assertNotNull(amenity2);
    assertNull(amenity1.brand());
    assertEquals("restaurant", amenity1.type());
    assertEquals("Panda Express", amenity1.name());
    assertNull(amenity2.name());
    assertEquals("gas_station", amenity2.type());
    assertEquals("Shell", amenity2.brand());
  }
}
