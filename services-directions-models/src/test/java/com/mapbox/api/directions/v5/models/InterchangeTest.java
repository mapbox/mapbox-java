package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class InterchangeTest extends TestUtils {

  private static final String DIRECTIONS_V5_AMENITIES_FIXTURE = "directions_v5_amenities.json";

  @Test
  public void sanity() throws Exception {
    Interchange interchange = Interchange.builder()
        .name("IC TEST NAME")
        .build();
    assertNotNull(interchange);
  }

  @Test
  public void whenInterchangeNameNull() {
    Interchange interchange = Interchange.builder()
        .name(null)
        .build();
    assertNotNull(interchange);
  }

  @Test
  public void interchangeNameNull() {
    Interchange interchange = Interchange.builder().build();
    assertNull(interchange.name());
  }

  @Test
  public void serializableObject() throws Exception {
    Interchange expected = Interchange.builder()
        .name("IC TEST NAME")
        .build();

    Interchange actual = deserialize(
        TestUtils.serialize(expected),
        Interchange.class
    );

    assertEquals(expected, actual);
  }

  @Test
  public void toFromJson() {
    Interchange expected = Interchange.builder()
        .name("IC TEST NAME")
        .build();

    Interchange actual = Interchange.fromJson(expected.toJson());

    assertEquals(expected, actual);
  }

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_AMENITIES_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);

    Interchange interchange = response.routes().get(0).legs().get(0).steps().get(1).intersections().get(0).interchange();

    assertNotNull(interchange);
    assertEquals("IC NAME", interchange.name());
  }
}
