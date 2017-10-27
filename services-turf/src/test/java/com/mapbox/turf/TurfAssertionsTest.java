package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.services.TestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;

public class TurfAssertionsTest extends TestUtils {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testInvariantGeojsonType1() throws TurfException {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Type and name required"));
    TurfAssertions.geojsonType(null, null, null);
  }

  @Test
  public void testInvariantGeojsonType2() throws TurfException {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Type and name required"));
    TurfAssertions.geojsonType(null, null, "myfn");
  }

  @Test
  public void testInvariantGeojsonType3() throws TurfException {
    String json = "{ type: 'Point', coordinates: [0, 0] }";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to myfn: must be a Polygon, given Point"));
    TurfAssertions.geojsonType(Point.fromJson(json), "Polygon", "myfn");
  }

  @Test
  public void testInvariantGeojsonType4() {
    String json = "{ type: 'Point', coordinates: [0, 0] }";
    TurfAssertions.geojsonType(Point.fromJson(json), "Point", "myfn");
  }

  @Test
  public void testInvariantFeatureOf1() throws TurfException {
    String json = "{ type: 'Feature', geometry: { type: 'Point', coordinates: [0, 0] }, "
      + "properties: {}}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith(".featureOf() requires a name"));
    TurfAssertions.featureOf(Feature.fromJson(json), "Polygon", null);
  }

  @Test
  public void testInvariantFeatureOf2() throws TurfException {
    String json = "{}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to foo, Feature with geometry required"));
    TurfAssertions.featureOf(Feature.fromJson(json), "Polygon", "foo");
  }

  @Test
  public void testInvariantFeatureOf3() throws TurfException {
    String json = "{ type: 'Feature', geometry: { type: 'Point', coordinates: [0, 0] }}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to myfn: must be a Polygon, given Point"));
    TurfAssertions.featureOf(Feature.fromJson(json), "Polygon", "myfn");
  }

  @Test
  public void testInvariantFeatureOf4() {
    String json = "{ type: 'Feature', geometry: { type: 'Point', coordinates: [0, 0]}, "
      + "properties: {}}";
    TurfAssertions.featureOf(Feature.fromJson(json), "Point", "myfn");
  }

  @Test
  public void testInvariantCollectionOf1() throws TurfException {
    String json = "{type: 'FeatureCollection', features: [{ type: 'Feature', geometry: { "
      + "type: 'Point', coordinates: [0, 0]}, properties: {}}]}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to myfn: must be a Polygon, given Point"));
    TurfAssertions.collectionOf(FeatureCollection.fromJson(json), "Polygon", "myfn");
  }

  @Test
  public void testInvariantCollectionOf2() throws TurfException {
    String json = "{}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("collectionOf() requires a name"));
    TurfAssertions.collectionOf(FeatureCollection.fromJson(json), "Polygon", null);
  }

  @Test
  public void testInvariantCollectionOf3() throws TurfException {
    String json = "{}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to foo, FeatureCollection required"));
    TurfAssertions.collectionOf(FeatureCollection.fromJson(json), "Polygon", "foo");
  }

  @Test
  public void testInvariantCollectionOf4() {
    String json = "{type: 'FeatureCollection', features: [{ type: 'Feature', geometry: { "
      + "type: 'Point', coordinates: [0, 0]}, properties: {}}]}";
    TurfAssertions.collectionOf(FeatureCollection.fromJson(json), "Point", "myfn");
  }


  @Test
  public void testInvariantGetCoord() {
    String jsonFeature = "{type: 'Feature', geometry: {type: 'Point', coordinates: [1, 2]}}";
    assertEquals(TurfAssertions.getCoord(Feature.fromJson(jsonFeature)),
      Point.fromLngLat(1, 2));
  }
}