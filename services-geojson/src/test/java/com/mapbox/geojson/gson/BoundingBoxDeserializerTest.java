package com.mapbox.geojson.gson;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.services.TestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BoundingBoxDeserializerTest extends TestUtils {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void bboxDeserializer_handlesOnlyOneAltitudeCorrectly() throws Exception {
    thrown.expect(GeoJsonException.class);
    thrown.expectMessage("The value of the bbox member MUST be an array of length");
    String fixtureJson
      = "{\"type\":\"Point\",\"bbox\":[1.0,2.0,3.0,4.0,5.0],\"coordinates\":[100,0]}";
    Point point = Point.fromJson(fixtureJson);
    compareJson(point.toJson(), fixtureJson);
  }
}