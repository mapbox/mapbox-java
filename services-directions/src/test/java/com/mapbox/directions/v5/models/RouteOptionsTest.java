package com.mapbox.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mapbox.directions.v5.DirectionsCriteria;
import com.mapbox.directions.v5.MapboxDirections;
import com.mapbox.geojson.Point;
import com.mapbox.services.TestUtils;
import com.mapbox.services.constants.Constants;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import retrofit2.Response;

import java.io.IOException;
import java.util.Locale;

public class RouteOptionsTest extends TestUtils {

  private static final String DIRECTIONS_V5_FIXTURE = "directions_v5.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();

    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String body = loadJsonFixture(DIRECTIONS_V5_FIXTURE);
          return new MockResponse().setBody(body);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });
    server.start();
    mockUrl = server.url("");
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    RouteOptions routeOptions = RouteOptions.builder().profile("hello").build();
    assertNotNull(routeOptions);
    assertEquals("hello", routeOptions.profile());
  }

  @Test
  public void requestResult_doesContainTheOriginalRequestData() throws Exception {
    Response<DirectionsResponse> response = MapboxDirections.builder()
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .origin(Point.fromLngLat(1.0, 1.0))
      .destination(Point.fromLngLat(5.0, 5.0))
      .profile(DirectionsCriteria.PROFILE_WALKING)
      .continueStraight(false)
      .language(Locale.CANADA)
      .alternatives(true).build().executeCall();
    DirectionsRoute route = response.body().routes().get(0);
    assertEquals(Locale.CANADA.getLanguage(), route.routeOptions().language());
    assertEquals(DirectionsCriteria.PROFILE_WALKING, route.routeOptions().profile());
    assertEquals(Constants.MAPBOX_USER, route.routeOptions().user());
    assertEquals(false, route.routeOptions().continueStraight());

    // Never set values
    assertNull(route.routeOptions().annotations());
    assertNull(route.routeOptions().bearings());
  }
}
