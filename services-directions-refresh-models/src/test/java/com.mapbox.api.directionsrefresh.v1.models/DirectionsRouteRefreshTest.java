package com.mapbox.api.directionsrefresh.v1.models;

import com.mapbox.geojson.TestUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DirectionsRouteRefreshTest extends TestUtils {

  private static final String DIRECTIONS_REFRESH_V1 = "directions_refresh_v1_obj.json";
  private static final String DIRECTIONS_REFRESH_V1_FIRST_LEG_PASSED = "directions_refresh_v1_obj_passed_first_leg.json";

  @Test
  public void sanity() {
    DirectionsRouteRefresh
      .builder()
      .legs(Collections.<RouteLegRefresh>emptyList())
      .build();
  }

  @Test
  public void testSerialization() throws IOException {
    DirectionsRouteRefresh directionsRouteRefresh =
      DirectionsRouteRefresh.fromJson(loadJsonFixture(DIRECTIONS_REFRESH_V1));

    assertNotNull(directionsRouteRefresh.legs());
    assertNotNull(directionsRouteRefresh.legs().get(0).annotation());
    assertNotNull(directionsRouteRefresh.legs().get(0).annotation().congestion());
  }

  @Test
  public void testSerializationFirstLegPassed() throws IOException {
    DirectionsRouteRefresh directionsRouteRefresh =
      DirectionsRouteRefresh.fromJson(loadJsonFixture(DIRECTIONS_REFRESH_V1_FIRST_LEG_PASSED));

    assertNotNull(directionsRouteRefresh.legs());
    assertNotNull(directionsRouteRefresh.legs().get(0).annotation());
    assertNull(directionsRouteRefresh.legs().get(0).annotation().congestion());
    assertNull(directionsRouteRefresh.legs().get(0).annotation().distance());
    assertNull(directionsRouteRefresh.legs().get(0).annotation().duration());
    assertNull(directionsRouteRefresh.legs().get(0).annotation().maxspeed());
    assertNull(directionsRouteRefresh.legs().get(0).annotation().speed());
  }

  @Test
  public void testSerializationDeserialization() throws IOException {
    List<DirectionsRouteRefresh> directionsRouteRefreshList = Arrays.asList(
      DirectionsRouteRefresh.fromJson(loadJsonFixture(DIRECTIONS_REFRESH_V1)),
      DirectionsRouteRefresh.fromJson(loadJsonFixture(DIRECTIONS_REFRESH_V1_FIRST_LEG_PASSED))
    );

    for (DirectionsRouteRefresh directionsRouteRefresh : directionsRouteRefreshList) {
      String json = directionsRouteRefresh.toJson();
      DirectionsRouteRefresh fromJson = DirectionsRouteRefresh.fromJson(json);

      assertEquals(directionsRouteRefresh, fromJson);
    }
  }
}
