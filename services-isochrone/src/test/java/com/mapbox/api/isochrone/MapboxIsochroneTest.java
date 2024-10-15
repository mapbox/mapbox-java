package com.mapbox.api.isochrone;

import com.google.gson.JsonSyntaxException;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

import org.hamcrest.core.StringStartsWith;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapboxIsochroneTest extends IsochroneTestUtils {

  // Test point representing downtown Los Angeles
  private Point testPoint = Point.fromLngLat(-118.22258,33.99038);
  private String testProfile = IsochroneCriteria.PROFILE_DRIVING;
  private String commaEquivalent = "%2C";

  /**
   * Test the most basic request (default response format)
   */
  @Test
  public void sanity() throws ServicesException, IOException {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
    Response<FeatureCollection> response = client.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
    assertNotNull(response.body().features());
  }

  @Test
  public void sanityUsingIntegerListForMinutes() throws ServicesException, IOException {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
    Response<FeatureCollection> response = client.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
    assertNotNull(response.body().features());
  }

  @Test
  public void sanityNoPolygonsDeclared() throws ServicesException, IOException {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .polygons(false)
      .baseUrl(mockUrl.toString())
      .build();
    Response<FeatureCollection> response = client.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
    assertNotNull(response.body().features());
  }

  @Test
  public void sanityPolygonsDeclared() throws ServicesException, IOException {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .polygons(true)
      .baseUrl(mockUrl.toString())
      .build();
    Response<FeatureCollection> response = client.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
    assertNotNull(response.body().features());
  }

  @Test
  public void query_acceptsPointsCorrectly() throws Exception {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();

    String requestUrlString = client.cloneCall().request().url().toString();

    assertTrue(requestUrlString.contains(String.valueOf(testPoint.latitude())));
    assertTrue(requestUrlString.contains(String.valueOf(testPoint.longitude())));
  }

  @Test
  public void query_acceptsPolygonsCorrectly() throws Exception {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .polygons(true)
      .baseUrl(mockUrl.toString())
      .build();

    String requestUrlString = client.cloneCall().request().url().toString();

    assertTrue(requestUrlString.contains("polygons=true"));
  }

  @Test
  public void query_rejectsPolygonsCorrectly() throws Exception {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .polygons(false)
      .baseUrl(mockUrl.toString())
      .build();

    String requestUrlString = client.cloneCall().request().url().toString();

    assertTrue(requestUrlString.contains("polygons=false"));
  }

  @Test
  public void build_usingIntegerListForMinutes() throws ServicesException, IOException {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(14,36,52)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
    String requestUrlString = client.cloneCall().request().url().toString();

    System.out.print(requestUrlString);

    assertTrue(requestUrlString.contains("contours_minutes=14" + commaEquivalent
        + "36" + commaEquivalent + "52"));
  }

  @Test
  public void build_usingIntegerListForMinutes_oneParam() throws ServicesException, IOException {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(14)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
    String requestUrlString = client.cloneCall().request().url().toString();

    System.out.print(requestUrlString);

    assertTrue(requestUrlString.contains("contours_minutes=14"));
  }

  @Test
  public void build_usingIntegerListForMeters_oneParam() throws ServicesException, IOException {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMeters(14)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
    String requestUrlString = client.cloneCall().request().url().toString();

    System.out.print(requestUrlString);

    assertTrue(requestUrlString.contains("contours_meters=14"));
  }

  @Test
  public void build_usingRawStringForMinutes() throws ServicesException, IOException {

    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
    String requestUrlString = client.cloneCall().request().url().toString();
    assertTrue(requestUrlString.contains("contours_minutes=5" + commaEquivalent +
      "30" + commaEquivalent + "55"));
  }

  @Test
  public void build_noAccessTokenExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: accessToken");
    MapboxIsochrone.builder()
      .coordinates(testPoint)
      .addContoursMinutes(5,30,55)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_invalidAccessTokenExceptionThrown() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(StringStartsWith.startsWith("Using the Mapbox Isochrone API requires setting a valid access token."));
    MapboxIsochrone.builder()
      .accessToken("")
      .addContoursMinutes(5,30,55)
      .coordinates(testPoint)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_contoursAndColorsAmountMismatchExceptionThrown() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(StringStartsWith.startsWith("Number of color elements must match number of minute elements provided."));
    MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .addContoursMinutes(5,30,55)
      .addContoursColors("6706ce")
      .coordinates(testPoint)
      .profile(testProfile)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_noQueryExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: coordinates");
    MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(testProfile)
      .addContoursMinutes(5,30,55)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void
  build_invalidCoordinatesQueryExceptionThrown() throws ServicesException {
    thrown.expect(JsonSyntaxException.class);
    MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(testProfile)
      .coordinates(Point.fromJson("4444,4949"))
      .addContoursMinutes(5,30,55)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_noCountourMinutesOrMetersExceptionThrown() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("A query with at least one specified minute amount or meter value is required.");
    MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(testProfile)
      .coordinates(testPoint)
      .build();
  }

  @Test
  public void build_invalidCountourMinutesExceptionThrown() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("A query with at least one specified minute amount is required.");
    MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .profile(testProfile)
      .coordinates(testPoint)
      .addContoursMinutes()
      .build();
  }

  @Test
  public void build_invalidCountourMetersExceptionThrown() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("A query with at least one specified meter value is required.");
    MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .profile(testProfile)
            .coordinates(testPoint)
            .addContoursMeters()
            .build();
  }

  @Test
  public void build_optionalParameters() throws Exception {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .profile(testProfile)
      .addContoursMinutes(5,30,55)
      .baseUrl(mockUrl.toString())
      .build();

    assertNull(client.polygons());
    assertNull(client.contoursColors());
    assertNull(client.denoise());
    Response<FeatureCollection> response = client.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
  }

  @Test
  public void build_denoiseGetsAddedToListCorrectly() throws Exception {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .profile(testProfile)
      .denoise(.6f)
      .addContoursMinutes(5,30,55)
      .baseUrl(mockUrl.toString())
      .build();

    assertTrue(client.cloneCall().request().url().toString()
      .contains("denoise=0.6"));
  }

  @Test
  public void build_generalizeGetsAddedToListCorrectly() throws Exception {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .profile(testProfile)
      .denoise(.6f)
      .generalize(5f)
      .addContoursMinutes(5,30,55)
      .baseUrl(mockUrl.toString())
      .build();

    assertTrue(client.cloneCall().request().url().toString()
      .contains("generalize=5"));
  }

  @Test
  public void build_colorAndMinuteAmountMismatchThrowsException() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Number of color elements must match number of minute elements provided.");
    MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .profile(testProfile)
      .addContoursColors("4286f4")
      .addContoursMinutes(5,30,55)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_colorAndMeterAmountMismatchThrowsException() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Number of color elements must match number of meter elements provided.");
    MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursColors("4286f4")
            .addContoursMeters(5,30,55)
            .baseUrl(mockUrl.toString())
            .build();
  }

  @Test
  public void build_multipleColorsGetsAddedToListCorrectly() throws Exception {
    MapboxIsochrone client = MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .profile(testProfile)
      .addContoursColors("6706ce","04e813","4286f4")
      .addContoursMinutes(5,30,55)
      .baseUrl(mockUrl.toString())
      .build();


    String requestUrlString = client.cloneCall().request().url().toString();
    assertTrue(requestUrlString.contains("contours_colors=6706ce" + commaEquivalent
      + "04e813" + commaEquivalent + "4286f4"));
  }

  @Test
  public void build_multipleColorsGetsAddedToListCorrectlyWithMeters() throws Exception {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursColors("6706ce","04e813","4286f4")
            .addContoursMeters(5,30,55)
            .baseUrl(mockUrl.toString())
            .build();


    String requestUrlString = client.cloneCall().request().url().toString();
    assertTrue(requestUrlString.contains("contours_colors=6706ce" + commaEquivalent
            + "04e813" + commaEquivalent + "4286f4"));
  }

  @Test
  public void build_colorWithHexValuePoundSymbolExceptionThrown() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Make sure that none of the contour color HEX"
      + " values have a # in front of it. Provide a list of the HEX values "
      + "without any # symbols.");
    MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .profile(testProfile)
      .addContoursColors("#6706ce", "#04e813", "#4286f4")
      .addContoursMinutes(5, 25, 40)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_minutesOutOfOrderExceptionThrown() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("The minutes must be listed"
      + " in order from the lowest number to the highest number.");
    MapboxIsochrone.builder()
      .accessToken(ACCESS_TOKEN)
      .coordinates(testPoint)
      .profile(testProfile)
      .addContoursMinutes(10,52,30)
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void sanityUsingIntegerListForMeters() throws ServicesException, IOException {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .addContoursMeters(5,30,55)
            .profile(testProfile)
            .baseUrl(mockUrl.toString())
            .build();
    Response<FeatureCollection> response = client.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
    assertNotNull(response.body().features());
  }

  @Test
  public void cannotSpecifyContourMinutesAndMeters() throws ServicesException, IOException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Cannot specify both contoursMinutes and contoursMeters.");
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .addContoursMeters(15,35,75)
            .addContoursMinutes(5,30,55)
            .profile(testProfile)
            .baseUrl(mockUrl.toString())
            .build();
    Response<FeatureCollection> response = client.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
    assertNotNull(response.body().features());
  }

  @Test
  public void excludeMotorways() {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursMeters(5,30,55)
            .excludeMotorways(true)
            .baseUrl(mockUrl.toString())
            .build();

    assertTrue(client.cloneCall().request().url().toString()
            .contains("exclude=motorway"));
  }

  @Test
  public void excludeTolls() {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursMeters(5,30,55)
            .excludeTolls(true)
            .baseUrl(mockUrl.toString())
            .build();

    assertTrue(client.cloneCall().request().url().toString()
            .contains("exclude=toll"));
  }

  @Test
  public void excludeFerries() {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursMeters(5,30,55)
            .excludeFerries(true)
            .baseUrl(mockUrl.toString())
            .build();

    assertTrue(client.cloneCall().request().url().toString()
            .contains("exclude=ferry"));
  }

  @Test
  public void excludeUnpavedRoads() {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursMeters(5,30,55)
            .excludeUnpavedRoads(true)
            .baseUrl(mockUrl.toString())
            .build();

    assertTrue(client.cloneCall().request().url().toString()
            .contains("exclude=unpaved"));
  }

  @Test
  public void excludeCashOnlyTollRoads() {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursMeters(5,30,55)
            .excludeCashOnlyTollRoads(true)
            .baseUrl(mockUrl.toString())
            .build();

    assertTrue(client.cloneCall().request().url().toString()
            .contains("exclude=cash_only_tolls"));
  }

  @Test
  public void allExclusions() {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursMeters(5,30,55)
            .excludeMotorways(true)
            .excludeTolls(true)
            .excludeFerries(true)
            .excludeUnpavedRoads(true)
            .excludeCashOnlyTollRoads(true)
            .baseUrl(mockUrl.toString())
            .build();

    assertTrue(client.cloneCall().request().url().toString()
            .contains("exclude=ferry%2Cmotorway%2Ctoll%2Cunpaved%2Ccash_only_tolls"));
  }

  @Test
  public void exclusionsOmitted() {
    MapboxIsochrone.Builder builder = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursMeters(5,30,55)
            .excludeMotorways(true)
            .excludeTolls(true)
            .excludeFerries(true)
            .excludeUnpavedRoads(true)
            .excludeCashOnlyTollRoads(true)
            .baseUrl(mockUrl.toString());

    MapboxIsochrone client = builder.excludeMotorways(false)
            .excludeMotorways(false)
            .excludeTolls(false)
            .excludeFerries(false)
            .excludeUnpavedRoads(false)
            .excludeCashOnlyTollRoads(false)
            .build();

    assertFalse(client.cloneCall().request().url().toString()
            .contains("motorway"));
    assertFalse(client.cloneCall().request().url().toString()
            .contains("toll"));
    assertFalse(client.cloneCall().request().url().toString()
            .contains("ferry"));
    assertFalse(client.cloneCall().request().url().toString()
            .contains("unpaved"));
    assertFalse(client.cloneCall().request().url().toString()
            .contains("cash_only_tolls"));
  }

  @Test
  public void departureTest() {
    MapboxIsochrone client = MapboxIsochrone.builder()
            .accessToken(ACCESS_TOKEN)
            .coordinates(testPoint)
            .profile(testProfile)
            .addContoursMeters(5,30,55)
            .departAt("2000-11-21T13:33")
            .baseUrl(mockUrl.toString())
            .build();

    System.out.println(client.cloneCall().request().url().toString());

    assertTrue(client.cloneCall().request().url().toString()
            .contains("depart_at=2000-11-21T13%3A33"));
  }
}
