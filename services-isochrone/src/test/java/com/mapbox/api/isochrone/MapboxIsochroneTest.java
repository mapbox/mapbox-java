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
  public void build_noCountourMinutesExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: contoursMinutes");
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
}
