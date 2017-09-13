package com.mapbox.services.api.mapmatching.v5;

import com.mapbox.services.api.BaseTest;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingTracepoint;
import com.mapbox.services.commons.geojson.Point;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapMatchingTracepointTest extends BaseTest {


  private static final String ACCESS_TOKEN = "pk.XXX";

  private static final String MAP_MATCHING_FIXTURE = "map_matching/map_matching_v5_polyline.json";

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

  @Test
  public void sanity() throws Exception {
    MapMatchingTracepoint tracepoint = MapMatchingTracepoint.builder()
      .waypointIndex(10)
      .build();
    assertNotNull(tracepoint);
  }

  @Test
  public void location_doesTransformIntoPointObject() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
    Response<MapMatchingResponse> response = mapMatching.executeCall();
    assertEquals(5, response.body().tracepoints().size());
    System.out.println(response.body().tracepoints().get(0).location());
    assertTrue(response.body().tracepoints().get(0).location()
      .equals(Point.fromLngLat(13.418807, 52.500595)));
  }

  @Test
  public void waypointIndex_matchesJsonValue() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
    Response<MapMatchingResponse> response = mapMatching.executeCall();
    assertEquals(1, response.body().tracepoints().get(1).waypointIndex(), DELTA);
  }

  @Test
  public void alternativesCount_matchesJsonValue() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
    Response<MapMatchingResponse> response = mapMatching.executeCall();
    assertEquals(0, response.body().tracepoints().get(0).alternativesCount(), DELTA);
  }

  @Test
  public void matchingsIndex_matchesJsonValue() throws Exception {
    MapboxMapMatching mapMatching = MapboxMapMatching.builder()
      .coordinates(coordinates)
      .baseUrl(mockUrl.toString())
      .accessToken(ACCESS_TOKEN)
      .build();
    Response<MapMatchingResponse> response = mapMatching.executeCall();
    assertEquals(0, response.body().tracepoints().get(1).matchingsIndex(), DELTA);
  }

  @Test
  public void builder_setsAllValuesCorrectly() throws Exception {
    MapMatchingTracepoint tracepoint = MapMatchingTracepoint.builder()
      .alternativesCount(10)
      .matchingsIndex(20)
      .rawLocation(new double[] {2.0, 2.0})
      .waypointIndex(15)
      .build();

    assertEquals(10, tracepoint.alternativesCount(), DELTA);
    assertEquals(20, tracepoint.matchingsIndex(), DELTA);
    assertEquals(15, tracepoint.waypointIndex(), DELTA);
    assertTrue(tracepoint.location().equals(Point.fromLngLat(2.0, 2.0)));
  }

  @Test
  public void testSerializable() throws Exception {
    MapMatchingTracepoint tracepoint = MapMatchingTracepoint.builder()
      .alternativesCount(10)
      .matchingsIndex(20)
      .rawLocation(new double[] {2.0, 2.0})
      .waypointIndex(15)
      .build();
    byte[] bytes = serialize(tracepoint);
    assertEquals(tracepoint, deserialize(bytes, MapMatchingTracepoint.class));
  }
}
