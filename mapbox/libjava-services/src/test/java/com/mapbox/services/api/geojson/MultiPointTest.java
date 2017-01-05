package com.mapbox.services.api.geojson;

import com.mapbox.services.commons.geojson.MultiPoint;
import com.mapbox.services.commons.models.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MultiPointTest extends BaseGeoJSON {

  @Test
  public void fromJson() {
    MultiPoint geo = MultiPoint.fromJson(BaseGeoJSON.SAMPLE_MULTIPOINT);
    assertEquals(geo.getType(), "MultiPoint");
    assertEquals(geo.getCoordinates().get(0).getLongitude(), 100.0, 0.0);
    assertEquals(geo.getCoordinates().get(0).getLatitude(), 0.0, 0.0);
    assertFalse(geo.getCoordinates().get(0).hasAltitude());
  }

  @Test
  public void toJson() {
    MultiPoint geo = MultiPoint.fromJson(BaseGeoJSON.SAMPLE_MULTIPOINT);
    compareJson(BaseGeoJSON.SAMPLE_MULTIPOINT, geo.toJson());
  }

  @Test
  public void checksEqualityFromCoordinates() {
    MultiPoint aMultiPoint = MultiPoint.fromCoordinates(new double[][]{
        {100.0, 0.0}, {101.0, 1.0}
    });

    String multiPointCoordinates = obtainLiteralCoordinatesFrom(aMultiPoint);

    assertEquals("Points: \n"
            + "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n"
            + "Position [longitude=101.0, latitude=1.0, altitude=NaN]\n",
        multiPointCoordinates);
  }

  @Test
  public void checksJsonEqualityFromCoordinates() {
    MultiPoint aMultiPoint = MultiPoint.fromCoordinates(new double[][]{
        {100.0, 0.0}, {101.0, 1.0}
    });

    String multiPointJsonCoordinates = aMultiPoint.toJson();

    compareJson("{ \"type\": \"MultiPoint\",\n"
        + "\"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]\n"
        + "}", multiPointJsonCoordinates);
  }

  private String obtainLiteralCoordinatesFrom(MultiPoint multiPoint) {
    List<Position> multiPointCoordinates = multiPoint.getCoordinates();
    StringBuilder literalCoordinates = new StringBuilder();
    literalCoordinates.append("Points: \n");
    for (Position point : multiPointCoordinates) {
      literalCoordinates.append(point.toString());
      literalCoordinates.append("\n");
    }
    return literalCoordinates.toString();
  }

}
