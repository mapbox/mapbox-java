package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LineStringTest extends BaseGeoJSON {

  @Test
  public void fromJson() {
    LineString geo = LineString.fromJson(BaseGeoJSON.SAMPLE_LINESTRING);
    assertEquals(geo.getType(), "LineString");
    assertEquals(geo.getCoordinates().get(0).getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().get(0).getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().get(0).hasAltitude());
  }

  @Test
  public void toJson() {
    LineString geo = LineString.fromJson(BaseGeoJSON.SAMPLE_LINESTRING);
    compareJson(BaseGeoJSON.SAMPLE_LINESTRING, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    LineString aLine = LineString.fromCoordinates(new double[][]{
        {100.0, 0.0}, {101.0, 1.0}
    });

    String lineCoordinates = obtainLiteralCoordinatesFrom(aLine);

    assertEquals("Line: \n"
            + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n"
            + "Position [longitude=101.0, latitude=1.0, altitude=NaN]\n",
        lineCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    LineString aLine = LineString.fromCoordinates(new double[][]{
        {100.0, 0.0}, {101.0, 1.0}
    });

    String lineJsonCoordinates = aLine.toJson();

    compareJson("{ \"type\": \"LineString\",\n"
            + "\"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]\n}",
        lineJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(LineString line) {
    List<Position> lineCoordinates = line.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Line: \n");
    for (Position point : lineCoordinates) {
      literalCoordinates.append(point.toString());
      literalCoordinates.append("\n");
    }
    return literalCoordinates.toString();
  }

}
