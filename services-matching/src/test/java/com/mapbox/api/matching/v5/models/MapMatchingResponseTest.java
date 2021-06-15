package com.mapbox.api.matching.v5.models;

import com.mapbox.api.matching.v5.MapboxMapMatching;
import com.mapbox.geojson.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

public class MapMatchingResponseTest extends TestUtils {

  private static final String MAP_MATCHING_FIXTURE = "map_matching_v5_polyline.json";

  private List<Point> coordinates;
  private MockWebServer server;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.setDispatcher(new okhttp3.mockwebserver.Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {
          String response = loadJsonFixture(MAP_MATCHING_FIXTURE);
          return new MockResponse().setBody(response);
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
      }
    });
    server.start();
    mockUrl = server.url("");

    coordinates = new ArrayList<>();
    coordinates.add(Point.fromLngLat(13.418946862220764, 52.50055852688439));
    coordinates.add(Point.fromLngLat(13.419011235237122, 52.50113000479732));
    coordinates.add(Point.fromLngLat(13.419756889343262, 52.50171780290061));
    coordinates.add(Point.fromLngLat(13.419885635375975, 52.50237416816131));
    coordinates.add(Point.fromLngLat(13.420631289482117, 52.50294888790448));
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void code_doesReturnCodeFromJson() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
    Response<MapMatchingResponse> response = mapMatching.executeCall();
    System.out.println(response.body().code());
    assertTrue(response.body().code().contains(""));
  }

  @Test
  public void matchings_doesReturnMatchingsListFromJson() throws Exception {
    // TODO
  }

  @Test
  public void tracepoints_doesReturnTracepointsListFromJson() throws Exception {
    // TODO
  }
}
