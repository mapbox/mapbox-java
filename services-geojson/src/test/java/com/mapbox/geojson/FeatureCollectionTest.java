package com.mapbox.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeatureCollectionTest extends TestUtils {

  private static final String SAMPLE_FEATURECOLLECTION = "sample-featurecollection.json";
  private static final String SAMPLE_FEATURECOLLECTION_BBOX = "sample-feature-collection-with-bbox.json";

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
    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(features, bbox);
    assertNotNull(featureCollection.bbox());
    assertEquals(1.0, featureCollection.bbox().west(), DELTA);
    assertEquals(2.0, featureCollection.bbox().south(), DELTA);
    assertEquals(3.0, featureCollection.bbox().east(), DELTA);
    assertEquals(4.0, featureCollection.bbox().north(), DELTA);
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
    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
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
  public void passingInSingleFeature_doesHandleCorrectly() throws Exception {
    Point geometry = Point.fromLngLat(1.0, 2.0);
    Feature feature = Feature.fromGeometry(geometry);
    FeatureCollection geo = FeatureCollection.fromFeature(feature);
    assertNotNull(geo.features());
    assertEquals(1, geo.features().size());
    assertEquals(2.0, ((Point) geo.features().get(0).geometry()).coordinates().get(1), DELTA);
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
    final String json = loadJsonFixture(SAMPLE_FEATURECOLLECTION_BBOX);
    FeatureCollection geo = FeatureCollection.fromJson(json);
    compareJson(json, geo.toJson());
  }
}