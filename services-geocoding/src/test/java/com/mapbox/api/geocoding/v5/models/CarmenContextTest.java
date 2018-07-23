package com.mapbox.api.geocoding.v5.models;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.mapbox.api.geocoding.v5.GeocodingTestUtils;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.core.TestUtils;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import org.hamcrest.Matchers;
import org.junit.Test;
import retrofit2.Response;

public class CarmenContextTest extends GeocodingTestUtils {

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
    GeocodingResponse response = mapboxGeocoding.executeCall().body();
    assertNotNull(response);
    assertThat(response.features().get(0).context().get(0).id(), equalTo("neighborhood.291451"));
  }

  @Test
  public void text_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    GeocodingResponse response = mapboxGeocoding.executeCall().body();
    assertNotNull(response);
    assertThat(response.features().get(0).context().get(0).text(), equalTo("Downtown"));
  }

  @Test
  public void shortCode_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("1600 pennsylvania ave nw")
      .baseUrl(mockUrl.toString())
      .build();
    GeocodingResponse response = mapboxGeocoding.executeCall().body();
    assertNotNull(response);
    assertThat(response.features().get(0).context().get(3).shortCode(), equalTo("US-DC"));
  }

  @Test
  public void wikidata_returnsCorrectString() throws Exception {
    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
      .accessToken(ACCESS_TOKEN)
      .query("texas")
      .baseUrl(mockUrl.toString())
      .build();
    GeocodingResponse response = mapboxGeocoding.executeCall().body();
    assertNotNull(response);
    assertThat(response.features().get(0).context().get(2).wikidata(), equalTo("Q148"));
  }

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
