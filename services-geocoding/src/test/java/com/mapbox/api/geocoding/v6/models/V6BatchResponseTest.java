package com.mapbox.api.geocoding.v6.models;

import com.google.gson.JsonElement;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class V6BatchResponseTest extends TestUtils {

  @Test
  public void responseParsingTest() throws IOException {
    final String rawResponse = loadJsonFixture("v6/batch_response_non_empty_valid.json");

    final V6BatchResponse response = V6BatchResponse.fromJson(rawResponse);
    assertNotNull(response);

    final List<V6Response> responses = response.responses();
    assertNotNull(responses);
    assertEquals(3, responses.size());

    final V6Response firstResponse = responses.get(0);
    assertEquals(2, responses.get(0).features().size());

    final List<V6Feature> features = firstResponse.features();
    assertEquals("FeatureCollection", firstResponse.type());
    assertEquals("Test attribution", firstResponse.attribution());
    assertEquals(2, features.size());

    final V6Feature feature = features.get(0);
    assertEquals(Point.fromLngLat(-77.03655, 38.89768), feature.geometry());
    assertEquals("dXJuOm1ieGFkcjo2YzdhYjM4Yi05YzM4LTQ3ZDItODFkMS1jYzZlYjg5YzliMWM", feature.id());
    assertEquals("Feature", feature.type());

    final V6Properties properties = feature.properties();
    assertNotNull(properties);

    assertEquals(
      "dXJuOm1ieGFkcjo2YzdhYjM4Yi05YzM4LTQ3ZDItODFkMS1jYzZlYjg5YzliMWM",
      properties.mapboxId()
    );
    assertEquals("address", properties.featureType());
    assertEquals("1600 Pennsylvania Avenue Northwest", properties.name());
    assertEquals(
      "Washington, District of Columbia 20500, United States",
      properties.placeFormatted()
    );

    assertEquals(
      "Washington, D.C.",
      properties.namePreferred()
    );

    assertEquals(
      "1600 Pennsylvania Avenue Northwest, DC 20005, USA",
      properties.fullAddress()
    );

    assertEquals(Arrays.asList(-81.0, 31.0, -71.0, 41.0), properties.bbox());

    final V6Coordinates coordinates = ModelDataFactory.createV6Coordinates(
      -77.03655, 38.89768, "rooftop"
    );
    assertEquals(coordinates, properties.coordinates());
    assertEquals(Point.fromLngLat(-77.03655, 38.89768), coordinates.point());

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

    final V6ContextElement neighborhoodContext = context.neighborhood();
    assertNotNull(neighborhoodContext);

    final Map<String, JsonElement> unrecognized =
      neighborhoodContext.getUnrecognizedJsonProperties();
    assertNotNull(unrecognized);
    assertNotNull(unrecognized.get("translations"));

    assertEquals(1, responses.get(1).features().size());
    assertEquals(0, responses.get(2).features().size());
  }

  @Test
  public void emptyResponseParsingTest() throws IOException {
    final String rawResponse = loadJsonFixture("v6/batch_response_empty.json");

    final V6BatchResponse response = V6BatchResponse.fromJson(rawResponse);
    assertNotNull(response);

    final List<V6Response> responses = response.responses();
    assertNotNull(responses);
    assertEquals(0, responses.size());
  }
}
