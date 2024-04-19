package com.mapbox.api.geocoding.v6.models;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class V6ResponseTest extends TestUtils {

  @Test
  public void responseParsingTest() throws IOException {
    final String rawResponse = loadJsonFixture("v6/response_non_empty_valid.json");

    final V6Response response = V6Response.fromJson(rawResponse);
    assertNotNull(response);

    final List<V6Feature> features = response.features();
    assertEquals("FeatureCollection", response.type());
    assertEquals("Test attribution", response.attribution());
    assertEquals(2, features.size());

    final V6Feature feature = features.get(0);
    assertEquals(Point.fromLngLat(-77.03394, 38.899929), feature.geometry());
    assertEquals("dXJuOm1ieGFkcjowMmVhNDY5OS0zY2IxLTRkOTctOGJhMi0yNWRkMDA3NDIzNGQ", feature.id());
    assertEquals("Feature", feature.type());

    final V6Properties properties = feature.properties();
    assertNotNull(properties);

    assertEquals(
      "dXJuOm1ieGFkcjowMmVhNDY5OS0zY2IxLTRkOTctOGJhMi0yNWRkMDA3NDIzNGQ",
      properties.mapboxId()
    );
    assertEquals("address", properties.featureType());
    assertEquals("740 15th Street Northwest", properties.name());
    assertEquals(
      "Washington, District of Columbia 20005, United States",
      properties.placeFormatted()
    );

    assertEquals(
      "Washington, D.C.",
      properties.namePreferred()
    );

    assertEquals(
      "740 15th Street Northwest, DC 20005, USA",
      properties.fullAddress()
    );

    final V6Coordinates coordinates = ModelDataFactory.createV6Coordinates(
      -77.03394, 38.899929, "rooftop"
    );
    assertEquals(coordinates, properties.coordinates());
    assertEquals(Point.fromLngLat(-77.03394, 38.899929), coordinates.point());

    assertEquals(Arrays.asList(-80.0, 35.0, -70.0, 40.0), properties.bbox());

    final V6MatchCode matchCode = ModelDataFactory.createV6MatchCode(
      "matched",
      "matched",
      "not_applicable",
      "matched",
      "matched",
      "matched",
      "inferred",
      "exact"
    );

    assertEquals(matchCode, properties.matchCode());

    final V6Context context = properties.context();
    assertNotNull(context);
  }

  @Test
  public void emptyResponseParsingTest() throws IOException {
    final String rawResponse = loadJsonFixture("v6/response_empty.json");

    final V6Response response = V6Response.fromJson(rawResponse);
    assertNotNull(response);

    final List<V6Feature> features = response.features();
    assertEquals(0, features.size());
  }
}
