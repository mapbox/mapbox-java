package com.mapbox.api.directionsrefresh.v1.models;

import com.mapbox.core.TestUtils;
import java.io.IOException;
import java.util.Collections;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DirectionsRefreshResponseTest extends TestUtils {

  private static final String DIRECTIONS_REFRESH_V1 = "directions_refresh_v1.json";

  @Test
  public void sanity() {
    DirectionsRefreshResponse.builder()
      .code("200")
      .message("Message")
      .route(
        DirectionsRouteRefresh.builder()
          .legs(Collections.<RouteLegRefresh>emptyList())
          .build()
      )
      .build();
  }

  @Test
  public void testSerialization() throws IOException {
    DirectionsRefreshResponse directionsRefreshResponse =
      DirectionsRefreshResponse.fromJson(loadJsonFixture(DIRECTIONS_REFRESH_V1));

    assertEquals(directionsRefreshResponse.code(), "Ok");
    assertNotNull(directionsRefreshResponse.route());
    assertNotNull(directionsRefreshResponse.route().legs());
    assertNotNull(directionsRefreshResponse.route().legs().get(0));
    assertNotNull(directionsRefreshResponse.route().legs().get(0).annotation());
    assertTrue(directionsRefreshResponse.route().legs().get(0).annotation().congestion().size() > 0);
    assertTrue(directionsRefreshResponse.route().legs().get(0).incidents().size() > 0);
    assertTrue(directionsRefreshResponse.route().legs().get(0).annotation().trafficTendency().size() > 0);
  }

  @Test
  public void testSerializationDeserialization() throws IOException {
    String json = loadJsonFixture(DIRECTIONS_REFRESH_V1);
    DirectionsRefreshResponse fromJson1 = DirectionsRefreshResponse.fromJson(json);

    String jsonFromObj = fromJson1.toJson();
    DirectionsRefreshResponse fromJson2 = DirectionsRefreshResponse.fromJson(jsonFromObj);

    assertEquals(fromJson1, fromJson2);
  }
}
