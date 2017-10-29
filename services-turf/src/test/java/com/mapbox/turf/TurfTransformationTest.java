package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.services.TestUtils;
import org.junit.Test;

public class TurfTransformationTest extends TestUtils {

  private static final String CIRCLE_IN = "turf-transformation/circle_in.json";
  private static final String CIRCLE_OUT = "turf-transformation/circle_out.json";

  @Test
  public void name() throws Exception {
    Feature feature = Feature.fromJson(loadJsonFixture(CIRCLE_IN));
    Polygon polygon = TurfTransformation.circle((Point) feature.geometry(),
      feature.getNumberProperty("radius").doubleValue());

    FeatureCollection featureCollection = FeatureCollection.fromJson(loadJsonFixture(CIRCLE_OUT));
    compareJson(featureCollection.features().get(1).geometry().toJson(), polygon.toJson());
  }
}