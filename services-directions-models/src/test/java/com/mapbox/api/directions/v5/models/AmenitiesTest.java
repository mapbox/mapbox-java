package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AmenitiesTest extends TestUtils {

  private static final String DIRECTIONS_V5_AMENITIES_FIXTURE = "directions_v5_amenities.json";
  @Test
  public void sanity(){
    assertNotNull(getDefault());
  }

  private Amenities getDefault() {
    return Amenities.builder()
        .type("restaurant")
        .name("starbucks")
        .build();
  }

  @Test
  public void serializableObject() throws Exception {
    Amenities expected = Amenities
        .builder()
        .type("restaurant")
        .name("starbucks")
        .build();

    Amenities actual = deserialize(
        TestUtils.serialize(expected),
        Amenities.class
    );

    assertEquals(expected, actual);
  }

  @Test
  public void toFromJson() {
    Amenities expected = Amenities
        .builder()
        .type("restaurant")
        .name("starbucks")
        .build();

    Amenities actual = Amenities.fromJson(expected.toJson());

    assertEquals(expected, actual);
  }

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_AMENITIES_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);

    RestStop restStop = response.routes().get(0).legs().get(0).steps().get(1).intersections().get(0).restStop();
    Amenities amenities1 = restStop.amenities().get(0);
    Amenities amenities2 = restStop.amenities().get(1);

    assertNotNull(amenities1);
    assertNotNull(amenities2);
    assertNull(amenities1.brand());
    assertEquals("restaurant", amenities1.type());
    assertEquals("Panda Express", amenities1.name());
    assertNull(amenities2.name());
    assertEquals("gas_station", amenities2.type());
    assertEquals("Shell", amenities2.brand());
  }
}
