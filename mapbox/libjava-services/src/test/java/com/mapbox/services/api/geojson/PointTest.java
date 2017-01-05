package com.mapbox.services.api.geojson;

import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PointTest extends BaseGeoJSON {

  @Test
  public void fromJson() {
    Point geo = Point.fromJson(BaseGeoJSON.SAMPLE_POINT);
    assertEquals(geo.getType(), "Point");
    assertEquals(geo.getCoordinates().getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().hasAltitude());
  }

  @Test
  public void toJson() {
    Point geo = Point.fromJson(BaseGeoJSON.SAMPLE_POINT);
    compareJson(BaseGeoJSON.SAMPLE_POINT, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    Point aPoint = Point.fromCoordinates(new double[]{100.0, 0.0});

    String pointCoordinates = obtainLiteralCoordinatesFrom(aPoint);

    assertEquals("Point: \n"
        + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n", pointCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    Point aPoint = Point.fromCoordinates(new double[]{100.0, 0.0});

    String pointJsonCoordinates = aPoint.toJson();

    compareJson("{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }", pointJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(Point point) {
    Position thePoint = point.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Point: \n");

    literalCoordinates.append(thePoint.toString());
    literalCoordinates.append("\n");

    return literalCoordinates.toString();
  }

}

