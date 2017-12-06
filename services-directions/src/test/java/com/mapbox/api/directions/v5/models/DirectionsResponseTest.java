package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;

public class DirectionsResponseTest extends TestUtils {

  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "directions_v5_precision_6.json";

  @Test
  public void sanity() throws Exception {
    DirectionsResponse response = DirectionsResponse.builder()
      .code("100")
      .routes(new ArrayList<DirectionsRoute>())
      .build();
    assertNotNull(response);
  }

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);
    assertNotNull(response);
    assertEquals(1, response.routes().size());
  }
}
