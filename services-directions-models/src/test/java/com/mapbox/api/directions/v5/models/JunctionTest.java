package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class JunctionTest extends TestUtils {

  private static final String DIRECTIONS_V5_AMENITIES_FIXTURE = "directions_v5_amenities.json";

  @Test
  public void sanity() throws Exception {
    Junction junction = Junction.builder()
        .name("JCT TEST NAME")
        .build();
    assertNotNull(junction);
  }

  @Test
  public void whenJunctionNameNull() {
    Junction junction = Junction.builder()
        .name(null)
        .build();
    assertNotNull(junction);
  }

  @Test
  public void junctionNameNull() {
    Junction junction = Junction.builder().build();
    assertNull(junction.name());
  }

  @Test
  public void serializableObject() throws Exception {
    Junction expected = Junction.builder()
        .name("JCT TEST NAME")
        .build();

    Junction actual = deserialize(
        TestUtils.serialize(expected),
        Junction.class
    );

    assertEquals(expected, actual);
  }

  @Test
  public void toFromJson() {
    Junction expected = Junction.builder()
        .name("JCT TEST NAME")
        .build();

    Junction actual = Junction.fromJson(expected.toJson());

    assertEquals(expected, actual);
  }

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_AMENITIES_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);

    Junction junction = response.routes().get(0).legs().get(0).steps().get(1).intersections().get(0).junction();

    assertNotNull(junction);
    assertEquals("JCT NAME", junction.name());
  }
}
