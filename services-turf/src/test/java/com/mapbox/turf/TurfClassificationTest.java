package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.services.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TurfClassificationTest extends TestUtils {

  private static final String PT = "turf-classification/pt.json";
  private static final String PTS = "turf-classification/pts.json";

  @Test
  public void testLineDistanceWithGeometries() throws IOException, TurfException {
    Point pt = (Point) Feature.fromJson(loadJsonFixture(PT)).geometry();
    FeatureCollection pts = FeatureCollection.fromJson(loadJsonFixture(PTS));

    List<Point> pointList = new ArrayList<>();
    for (Feature feature : pts.features()) {
      pointList.add((Point) (feature.geometry()));
    }
    Point closestPt = TurfClassification.nearest(pt, pointList);

    Assert.assertNotNull(closestPt);
    Assert.assertEquals(closestPt.type(), "Point");
    Assert.assertEquals(closestPt.longitude(), -75.33, DELTA);
    Assert.assertEquals(closestPt.latitude(), 39.44, DELTA);
  }
}