package com.mapbox.api.geocoding.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.core.TestUtils;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import retrofit2.Response;

public class CarmenContextTest extends TestUtils {

  private static final String GEOCODING_FIXTURE = "geocoding.json";
  private static final String GEOCODING_BATCH_FIXTURE = "geocoding_batch.json";
  private static final String REVERSE_GEOCODE_FIXTURE = "geocoding_reverse.json";
  private static final String GEOCODE_WITH_BBOX_FIXTURE = "bbox_geocoding_result.json";
  private static final String GEOCODE_LANGUAGE_FIXTURE = "language_geocoding_result.json";

  private MockWebServer server;
  private HttpUrl mockUrl;

  @Test
  public void sanity() throws Exception {
    CarmenContext carmenContext = CarmenContext.builder().build();
    assertNotNull(carmenContext);
  }

  @Test
  public void id_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).context().get(0).id()
      .equals("neighborhood.291451"));
  }

  @Test
  public void text_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).context().get(0).text()
      .equals("Downtown"));
  }

  @Test
  public void shortCode_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("texas")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).context().get(0).shortCode()
      .equals("us"));
  }

  @Test
  public void wikidata_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("texas")
      .baseUrl(mockUrl.toString())
      .build();
    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
    assertTrue(response.body().features().get(0).context().get(0).wikidata()
      .equals("Q30"));
  }

//  @Test
//  public void category_returnsCorrectString() throws Exception {
  // TODO find a fixture with category
//    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
//      .accessToken(ACCESS_TOKEN)
//      .query("1600 pennsylvania ave nw")
//      .baseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
//    System.out.println(response.body().features().get(0).context().get(0).category());
//    assertTrue(response.body().features().get(0).context().get(0).category()
//      .equals("Q30"));
//  }

//  @Test
//  public void maki_returnsCorrectString() throws Exception {
//    // TODO find a fixture with category
//    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
//      .accessToken(ACCESS_TOKEN)
//      .query("texas")
//      .baseUrl(mockUrl.toString())
//      .build();
//    Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
//    System.out.println(response.body().features().get(0).context().get(0).maki());
//    assertTrue(response.body().features().get(0).context().get(0).maki()
//      .equals("Q30"));
//  }

  @Test
  public void testSerializable() throws Exception {
    CarmenContext carmenContext = CarmenContext.builder()
      .id("123")
      .shortCode("shortCode")
      .build();
    byte[] bytes = serialize(carmenContext);
    assertEquals(carmenContext, deserialize(bytes, CarmenContext.class));
  }
}
