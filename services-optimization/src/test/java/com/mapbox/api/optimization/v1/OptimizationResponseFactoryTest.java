package com.mapbox.api.optimization.v1;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.api.optimization.v1.models.OptimizationWaypoint;
import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptimizationResponseFactoryTest {

  @Mock
  private MapboxOptimization optimization;
  private OptimizationResponseFactory factory;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new OptimizationResponseFactory(optimization);
  }

  @Test
  public void generate_successful() {
    final String profile = "driving-traffic";
    final String coordinates = "1.2,3.4,5.6,7.8";
    final String annotations = "congestion,duration";
    final String bearings = "123,456";
    final String language = "it";
    final String radiuses = "10,20";
    final String user = "some_user";
    final String overview = "full";
    final boolean steps = true;
    final String baseUrl = "some url";
    final String geometries = "polyline6";
    when(optimization.profile()).thenReturn(profile);
    when(optimization.coordinates()).thenReturn(coordinates);
    when(optimization.annotations()).thenReturn(annotations);
    when(optimization.bearings()).thenReturn(bearings);
    when(optimization.language()).thenReturn(language);
    when(optimization.radiuses()).thenReturn(radiuses);
    when(optimization.user()).thenReturn(user);
    when(optimization.overview()).thenReturn(overview);
    when(optimization.steps()).thenReturn(steps);
    when(optimization.baseUrl()).thenReturn(baseUrl);
    when(optimization.geometries()).thenReturn(geometries);
    RouteOptions expectedRouteOptions = RouteOptions.builder()
      .profile(profile)
      .coordinates(coordinates)
      .annotations(annotations)
      .bearings(bearings)
      .language(language)
      .radiuses(radiuses)
      .user(user)
      .overview(overview)
      .steps(steps)
      .baseUrl(baseUrl)
      .geometries(geometries)
      .build();

    List<DirectionsRoute> inputTrips = Arrays.asList(
      DirectionsRoute.builder().distance(1.0).duration(2.0).build(),
      DirectionsRoute.builder().distance(3.0).duration(4.0).build()
    );
    List<DirectionsRoute> expectedOutputTrips = Arrays.asList(
      DirectionsRoute.builder()
        .distance(1.0)
        .duration(2.0)
        .routeIndex("0")
        .routeOptions(expectedRouteOptions)
        .build(),
      DirectionsRoute.builder()
        .distance(3.0)
        .duration(4.0)
        .routeIndex("1")
        .routeOptions(expectedRouteOptions)
        .build()
    );
    Request request = mock(Request.class);
    Headers headers = Headers.of("header1", "header2");
    okhttp3.Response rawResponse = mock(okhttp3.Response.class);
    when(rawResponse.request()).thenReturn(request);
    when(rawResponse.protocol()).thenReturn(Protocol.HTTP_1_1);
    List<OptimizationWaypoint> waypoints = Arrays.asList(
      OptimizationWaypoint.builder().name("name1").waypointIndex(0).tripsIndex(0).build(),
      OptimizationWaypoint.builder().name("name2").waypointIndex(1).tripsIndex(1).build()
    );

    Response<OptimizationResponse> input = mock(Response.class);
    when(input.headers()).thenReturn(headers);
    when(input.raw()).thenReturn(rawResponse);
    when(input.body()).thenReturn(
      OptimizationResponse.builder()
        .code("201")
        .waypoints(waypoints)
        .trips(inputTrips)
        .build()
    );
    when(input.isSuccessful()).thenReturn(true);

    Response expectedOutput = Response.success(
      OptimizationResponse.builder()
        .code("201")
        .waypoints(waypoints)
        .trips(expectedOutputTrips)
        .build(),
      new okhttp3.Response.Builder()
        .code(200)
        .message("OK")
        .protocol(Protocol.HTTP_1_1)
        .headers(headers)
        .request(request)
        .build()
    );

    assertThat(factory.generate(input)).isEqualToComparingFieldByFieldRecursively(expectedOutput);
  }

  @Test
  public void generate_successful_nullGeometries() {
    final String profile = "driving-traffic";
    final String coordinates = "1.2,3.4,5.6,7.8";
    final String annotations = "congestion,duration";
    final String bearings = "123,456";
    final String language = "it";
    final String radiuses = "10,20";
    final String user = "some_user";
    final String overview = "full";
    final boolean steps = true;
    final String baseUrl = "some url";
    final String geometries = "polyline6";
    when(optimization.profile()).thenReturn(profile);
    when(optimization.coordinates()).thenReturn(coordinates);
    when(optimization.annotations()).thenReturn(annotations);
    when(optimization.bearings()).thenReturn(bearings);
    when(optimization.language()).thenReturn(language);
    when(optimization.radiuses()).thenReturn(radiuses);
    when(optimization.user()).thenReturn(user);
    when(optimization.overview()).thenReturn(overview);
    when(optimization.steps()).thenReturn(steps);
    when(optimization.baseUrl()).thenReturn(baseUrl);
    when(optimization.geometries()).thenReturn(null);
    RouteOptions expectedRouteOptions = RouteOptions.builder()
      .profile(profile)
      .coordinates(coordinates)
      .annotations(annotations)
      .bearings(bearings)
      .language(language)
      .radiuses(radiuses)
      .user(user)
      .overview(overview)
      .steps(steps)
      .baseUrl(baseUrl)
      .build();

    List<DirectionsRoute> inputTrips = Arrays.asList(
      DirectionsRoute.builder().distance(1.0).duration(2.0).build(),
      DirectionsRoute.builder().distance(3.0).duration(4.0).build()
    );
    List<DirectionsRoute> expectedOutputTrips = Arrays.asList(
      DirectionsRoute.builder()
        .distance(1.0)
        .duration(2.0)
        .routeIndex("0")
        .routeOptions(expectedRouteOptions)
        .build(),
      DirectionsRoute.builder()
        .distance(3.0)
        .duration(4.0)
        .routeIndex("1")
        .routeOptions(expectedRouteOptions)
        .build()
    );
    Request request = mock(Request.class);
    Headers headers = Headers.of("header1", "header2");
    okhttp3.Response rawResponse = mock(okhttp3.Response.class);
    when(rawResponse.request()).thenReturn(request);
    when(rawResponse.protocol()).thenReturn(Protocol.HTTP_1_1);
    List<OptimizationWaypoint> waypoints = Arrays.asList(
      OptimizationWaypoint.builder().name("name1").waypointIndex(0).tripsIndex(0).build(),
      OptimizationWaypoint.builder().name("name2").waypointIndex(1).tripsIndex(1).build()
    );

    Response<OptimizationResponse> input = mock(Response.class);
    when(input.headers()).thenReturn(headers);
    when(input.raw()).thenReturn(rawResponse);
    when(input.body()).thenReturn(
      OptimizationResponse.builder()
        .code("201")
        .waypoints(waypoints)
        .trips(inputTrips)
        .build()
    );
    when(input.isSuccessful()).thenReturn(true);

    Response expectedOutput = Response.success(
      OptimizationResponse.builder()
        .code("201")
        .waypoints(waypoints)
        .trips(expectedOutputTrips)
        .build(),
      new okhttp3.Response.Builder()
        .code(200)
        .message("OK")
        .protocol(Protocol.HTTP_1_1)
        .headers(headers)
        .request(request)
        .build()
    );

    assertThat(factory.generate(input)).isEqualToComparingFieldByFieldRecursively(expectedOutput);
  }

  @Test
  public void generate_unsuccessful() {
    Response<OptimizationResponse> input = mock(Response.class);
    when(input.isSuccessful()).thenReturn(false);

    assertThat(factory.generate(input)).isSameAs(input);
  }

  @Test
  public void generate_nullBody() {
    Response<OptimizationResponse> input = mock(Response.class);
    when(input.isSuccessful()).thenReturn(true);
    when(input.body()).thenReturn(null);

    assertThat(factory.generate(input)).isSameAs(input);
  }

  @Test
  public void generate_nullTrips() {
    Response<OptimizationResponse> input = mock(Response.class);
    when(input.isSuccessful()).thenReturn(true);
    when(input.body()).thenReturn(
      OptimizationResponse.builder()
        .code("201")
        .trips(null)
        .build()
    );

    assertThat(factory.generate(input)).isSameAs(input);
  }

  @Test
  public void generate_emptyTrips() {
    Response<OptimizationResponse> input = mock(Response.class);
    when(input.isSuccessful()).thenReturn(true);
    when(input.body()).thenReturn(
      OptimizationResponse.builder()
        .code("201")
        .trips(new ArrayList<>())
        .build()
    );

    assertThat(factory.generate(input)).isSameAs(input);
  }
}
