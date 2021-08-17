package com.mapbox.api.directions.v5.models;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

  @Test
  public void testToFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    DirectionsResponse responseFromJson1 = DirectionsResponse.fromJson(json);

    String jsonString = responseFromJson1.toJson();
    DirectionsResponse responseFromJson2 = DirectionsResponse.fromJson(jsonString);


    Assert.assertEquals(responseFromJson1, responseFromJson2);
    Assert.assertEquals(responseFromJson2, responseFromJson1);
  }

  @Test
  public void fromJson_correctlyBuildsFromJsonWithOptionsAndUuid() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    RouteOptions options = RouteOptions.builder()
      .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
      .coordinatesList(new ArrayList<Point>() {{
        add(Point.fromLngLat(1.0, 1.0));
        add(Point.fromLngLat(2.0, 2.0));
      }})
      .build();
    String uuid = "123";
    DirectionsResponse response = DirectionsResponse.fromJson(json, options, uuid);

    assertEquals(options, response.routes().get(0).routeOptions());
    assertEquals(uuid, response.routes().get(0).requestUuid());
  }
}
