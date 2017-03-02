package com.mapbox.services.api.turf;

import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfInvariant;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;

public class TurfInvariantTest extends BaseTurf {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testInvariantGeojsonType1() throws TurfException {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Type and name required"));
    TurfInvariant.geojsonType(null, null, null);
  }

  @Test
  public void testInvariantGeojsonType2() throws TurfException {
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Type and name required"));
    TurfInvariant.geojsonType(null, null, "myfn");
  }

  @Test
  public void testInvariantGeojsonType3() throws TurfException {
    String json = "{ type: 'Point', coordinates: [0, 0] }";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to myfn: must be a Polygon, given Point"));
    TurfInvariant.geojsonType(Point.fromJson(json), "Polygon", "myfn");
  }

  @Test
  public void testInvariantGeojsonType4() {
    String json = "{ type: 'Point'`, coordinates: [0, 0] }";
    TurfInvariant.geojsonType(Point.fromJson(json), "Point", "myfn");
  }

  @Test
  public void testInvariantFeatureOf1() throws TurfException {
    String json = "{ type: 'Feature', geometry: { type: 'Point', coordinates: [0, 0] }, properties: {}}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith(".featureOf() requires a name"));
    TurfInvariant.featureOf(Feature.fromJson(json), "Polygon", null);
  }

  @Test
  public void testInvariantFeatureOf2() throws TurfException {
    String json = "{}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to foo, Feature with geometry required"));
    TurfInvariant.featureOf(Feature.fromJson(json), "Polygon", "foo");
  }

  @Test
  public void testInvariantFeatureOf3() throws TurfException {
    String json = "{ type: 'Feature', geometry: { type: 'Point', coordinates: [0, 0] }}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to myfn: must be a Polygon, given Point"));
    TurfInvariant.featureOf(Feature.fromJson(json), "Polygon", "myfn");
  }

  @Test
  public void testInvariantFeatureOf4() {
    String json = "{ type: 'Feature', geometry: { type: 'Point', coordinates: [0, 0]}, properties: {}}";
    TurfInvariant.featureOf(Feature.fromJson(json), "Point", "myfn");
  }

  @Test
  public void testInvariantCollectionOf1() throws TurfException {
    String json = "{type: 'FeatureCollection', features: [{ type: 'Feature', geometry: { type: 'Point', coordinates: "
      + "[0, 0]}, properties: {}}]}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to myfn: must be a Polygon, given Point"));
    TurfInvariant.collectionOf(FeatureCollection.fromJson(json), "Polygon", "myfn");
  }

  @Test
  public void testInvariantCollectionOf2() throws TurfException {
    String json = "{}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("collectionOf() requires a name"));
    TurfInvariant.collectionOf(FeatureCollection.fromJson(json), "Polygon", null);
  }

  @Test
  public void testInvariantCollectionOf3() throws TurfException {
    String json = "{}";
    thrown.expect(TurfException.class);
    thrown.expectMessage(startsWith("Invalid input to foo, FeatureCollection required"));
    TurfInvariant.collectionOf(FeatureCollection.fromJson(json), "Polygon", "foo");
  }

  @Test
  public void testInvariantCollectionOf4() {
    String json = "{type: 'FeatureCollection', features: [{ type: 'Feature', geometry: { type: 'Point', coordinates: "
      + "[0, 0]}, properties: {}}]}";
    TurfInvariant.collectionOf(FeatureCollection.fromJson(json), "Point", "myfn");
  }


  @Test
  public void testInvariantGetCoord() {
    String jsonPoint = "{type: 'Point', coordinates: [1, 2]}";
    String jsonFeature = "{type: 'Feature', geometry: {type: 'Point', coordinates: [1, 2]}}";

    assertEquals(TurfInvariant.getCoord(Point.fromJson(jsonPoint)), Position.fromCoordinates(1, 2));
    assertEquals(TurfInvariant.getCoord(Feature.fromJson(jsonFeature)), Position.fromCoordinates(1, 2));
  }


}