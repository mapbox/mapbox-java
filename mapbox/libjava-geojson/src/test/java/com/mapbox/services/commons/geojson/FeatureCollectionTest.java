package com.mapbox.services.commons.geojson;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FeatureCollectionTest extends BaseTest {

  private static final String SAMPLE_FEATURECOLLECTION = "sample-featurecollection.json";

  @Test
  public void sanity() throws Exception {
    List<Feature> features = new ArrayList<>();
    features.add(Feature.fromGeometry(null));
    features.add(Feature.fromGeometry(null));
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
    assertNotNull(featureCollection);
  }

  @Test
  public void bbox_nullWhenNotSet() throws Exception {
    List<Feature> features = new ArrayList<>();
    features.add(Feature.fromGeometry(null));
    features.add(Feature.fromGeometry(null));
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
    assertNull(featureCollection.bbox());
  }

  @Test
  public void bbox_doesNotSerializeWhenNotPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    Feature feature = Feature.fromGeometry(lineString);

    List<Feature> features = new ArrayList<>();
    features.add(feature);
    features.add(feature);
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
    compareJson(featureCollection.toJson(),
      "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\","
        + "\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[1,2],[2,3]]},"
        + "\"properties\":{}},{\"type\":\"Feature\","
        + "\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[1,2],[2,3]]},"
        + "\"properties\":{}}]}");
  }

  @Test
  public void bbox_returnsCorrectBbox() throws Exception {
    List<Feature> features = new ArrayList<>();
    features.add(Feature.fromGeometry(null));
    features.add(Feature.fromGeometry(null));
    double[] bbox = new double[] {1.0, 2.0, 3.0, 4.0};
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features, bbox);
    assertNotNull(featureCollection.bbox());
    assertEquals(4, featureCollection.bbox().length);
    assertEquals(1.0, featureCollection.bbox()[0], DELTA);
    assertEquals(2.0, featureCollection.bbox()[1], DELTA);
    assertEquals(3.0, featureCollection.bbox()[2], DELTA);
    assertEquals(4.0, featureCollection.bbox()[3], DELTA);
  }

  @Test
  public void bbox_doesSerializeWhenPresent() throws Exception {
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 2.0));
    points.add(Point.fromLngLat(2.0, 3.0));
    LineString lineString = LineString.fromLngLats(points);
    Feature feature = Feature.fromGeometry(lineString);

    List<Feature> features = new ArrayList<>();
    features.add(feature);
    features.add(feature);
    double[] bbox = new double[] {1.0, 2.0, 3.0, 4.0};
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features, bbox);
    compareJson(featureCollection.toJson(),
      "{\"type\":\"FeatureCollection\",\"bbox\":[1.0,2.0,3.0,4.0],"
        + "\"features\":[{\"type\":\"Feature\","
        + "\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[1,2],[2,3]]},\"properties\":{}},"
        + "{\"type\":\"Feature\","
        + "\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[1,2],[2,3]]},\"properties\":{}}"
        + "]}");
  }

  @Test
  public void fromJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_FEATURECOLLECTION);
    FeatureCollection geo = FeatureCollection.fromJson(json);
    assertEquals(geo.type(), "FeatureCollection");
    assertEquals(geo.features().size(), 3);
    assertEquals(geo.features().get(0).type(), "Feature");
    assertEquals(geo.features().get(0).geometry().type(), "Point");
    assertEquals(geo.features().get(1).type(), "Feature");
    assertEquals(geo.features().get(1).geometry().type(), "LineString");
    assertEquals(geo.features().get(2).type(), "Feature");
    assertEquals(geo.features().get(2).geometry().type(), "Polygon");
  }

  @Test
  public void toJson() throws IOException {
    final String json = loadJsonFixture(SAMPLE_FEATURECOLLECTION);
    FeatureCollection geo = FeatureCollection.fromJson(json);
    compareJson(json, geo.toJson());
  }
}