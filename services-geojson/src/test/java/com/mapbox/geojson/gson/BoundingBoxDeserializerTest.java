package com.mapbox.geojson.gson;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.exception.GeoJsonException;

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
    compareJson(fixtureJson, point.toJson());
  }

  @Test
  public void bboxDeserializer_deserializeThreeDimensionalArray() {
    String fixtureJson
      = "{\"type\":\"LineString\",\"bbox\": "
      + "[100.0,0.0,-100.0,105.0,1.0,0.0],\"coordinates\":[[100.0, 0.0],[101.0, 1.0]]}";
    LineString lineString = LineString.fromJson(fixtureJson);
    compareJson(fixtureJson, lineString.toJson());
  }

  @Test
  public void bboxDeserializer_deserializeTwoDimensionalArray() {
    String fixtureJson
      = "{\"type\":\"Point\",\"bbox\":[1,2,3,4],\"coordinates\":[100,0]}";
    Point point = Point.fromJson(fixtureJson);
    compareJson(fixtureJson, point.toJson());
  }
}