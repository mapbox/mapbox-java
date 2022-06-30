package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RestStopTest extends TestUtils {

  private static final String DIRECTIONS_V5_AMENITIES_FIXTURE = "directions_v5_amenities.json";

  @Test
  public void sanity() throws Exception {
    RestStop restStop = RestStop.builder()
        .name("ABCD")
        .type("service_area")
        .amenities(Collections.<Amenities>emptyList())
        .build();
    assertNotNull(restStop);
  }

  @Test
  public void serializableObject() throws Exception {
    RestStop expected = RestStop
        .builder()
        .name("ABCD")
        .type("service_area")
        .amenities(Collections.<Amenities>emptyList())
        .build();

    RestStop actual = deserialize(
        TestUtils.serialize(expected),
        RestStop.class
    );

    assertEquals(expected, actual);
  }

  @Test
  public void toFromJson() {
    RestStop expected = RestStop
        .builder()
        .name("ABCD")
        .type("service_area")
        .amenities(Collections.<Amenities>emptyList())
        .build();

    RestStop actual = RestStop.fromJson(expected.toJson());

    assertEquals(expected, actual);
  }

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_AMENITIES_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);

    RestStop restStop = response.routes().get(0).legs().get(0).steps().get(1).intersections().get(0).restStop();

    assertNotNull(restStop);
    assertNotNull(restStop.amenities());
    assertEquals("SA", restStop.name());
    assertEquals("service_area", restStop.type());
  }
}
