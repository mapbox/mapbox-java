package com.mapbox.geojson.shifter;

import com.google.gson.JsonParser;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.FlattenListOfPoints;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ShifterTest {

  static class TestCoordinateShifter implements CoordinateShifter {
    @Override
    public List<Double> shiftLonLat(double lon, double lat) {
      return Arrays.asList(lon + 3, lat + 5);
    }

    @Override
    public double[] shift(double lon, double lat) {
      return new double[]{lon + 3, lat + 5};
    }

    @Override
    public List<Double> shiftLonLatAlt(double lon, double lat, double altitude) {
      return Arrays.asList(lon + 3, lat + 5, altitude + 8);
    }

    @Override
    public double[] shift(double lon, double lat, double altitude) {
      if (Double.isNaN(altitude)) {
        return shift(lon, lat);
      } else {
        return new double[]{lon, lat, altitude};
      }
    }

    @Override
    public List<Double> unshiftPoint(Point shiftedPoint) {
      return Arrays.asList(shiftedPoint.longitude() - 3,
              shiftedPoint.latitude() - 5,
              shiftedPoint.altitude() - 8);
    }

    @Override
    public List<Double> unshiftPoint(List<Double> coordinates) {
      if (coordinates.size() > 2) {
        return Arrays.asList(coordinates.get(0) - 3,
                coordinates.get(1) - 5,
                coordinates.get(2) - 8);
      }
      return Arrays.asList(coordinates.get(0) - 3,
              coordinates.get(1) - 5);
    }

    @Override
    public double[] unshiftPointArray(double[] coordinates) {
      if (coordinates.length > 2) {
        return new double[]{coordinates[0] - 3,
                coordinates[1] - 5,
                coordinates[2] - 8
        };
      }
      return new double[]{coordinates[0] - 3,
              coordinates[1] - 5};
    }
  }

  @Test
  public void basic_coordinate_shifter_manager_creation() throws Exception {
    CoordinateShifterManager coordinateShifterManager = new CoordinateShifterManager();
    assertNotNull(coordinateShifterManager);
  }

  @Test
  public void default_shifter(){
    assertTrue(CoordinateShifterManager.isUsingDefaultShifter());

    CoordinateShifter shifter = new TestCoordinateShifter();
    CoordinateShifterManager.setCoordinateShifter(shifter);
    assertFalse(CoordinateShifterManager.isUsingDefaultShifter());

    CoordinateShifterManager.setCoordinateShifter(null);
    assertTrue(CoordinateShifterManager.isUsingDefaultShifter());
  }

  @Test
  public void point_basic_shift() throws Exception {

    Point southwest = Point.fromLngLat(2.0, 2.0);
    Point northeast = Point.fromLngLat(4.0, 4.0);

    CoordinateShifter shifter = new TestCoordinateShifter();

    // Manually shifted
    List<Double> shifted = shifter.shiftLonLat(southwest.longitude(), southwest.latitude());
    Point southwestManualShifted = Point.fromLngLat(shifted.get(0), shifted.get(1));
    shifted = shifter.shiftLonLat(northeast.longitude(), northeast.latitude());
    Point northeastManualShifted = Point.fromLngLat(shifted.get(0), shifted.get(1));

    CoordinateShifterManager.setCoordinateShifter(shifter);

    // Autoshifted
    Point southwestShifted = Point.fromLngLat(southwest.longitude(), southwest.latitude());
    Point northeastShifted = Point.fromLngLat(northeast.longitude(), northeast.latitude());

    assertEquals(southwestManualShifted, southwestShifted);
    assertEquals(northeastManualShifted, northeastShifted);

    CoordinateShifterManager.setCoordinateShifter(null);
  }

  @Test
  public void bbox_basic_shift() throws Exception {

    Point southwest = Point.fromLngLat(2.0, 2.0);
    Point northeast = Point.fromLngLat(4.0, 4.0);

    CoordinateShifter shifter = new TestCoordinateShifter();

    // Manually shifted
    List<Double> shifted = shifter.shiftLonLat(southwest.longitude(), southwest.latitude());
    Point southwestManualShifted = Point.fromLngLat(shifted.get(0), shifted.get(1));
    shifted = shifter.shiftLonLat(northeast.longitude(), northeast.latitude());
    Point northeastManualShifted = Point.fromLngLat(shifted.get(0), shifted.get(1));

    CoordinateShifterManager.setCoordinateShifter(shifter);

    BoundingBox boundingBoxFromDouble = BoundingBox.fromLngLats(2.0, 2.0, 4.0, 4.0);

    BoundingBox boundingBoxFromPoints =
            BoundingBox.fromPoints(Point.fromLngLat(2.0, 2.0),
                                   Point.fromLngLat(4.0, 4.0));


    assertEquals(boundingBoxFromDouble, boundingBoxFromPoints);
    assertEquals(southwestManualShifted, boundingBoxFromPoints.southwest());
    assertEquals(northeastManualShifted, boundingBoxFromPoints.northeast());

    CoordinateShifterManager.setCoordinateShifter(null);
  }

  @Test
  public void bbox_basic_shift_primitive() throws Exception {

    Point southwest = Point.fromLngLat(2.0, 2.0);
    Point northeast = Point.fromLngLat(4.0, 4.0);

    CoordinateShifter shifter = new TestCoordinateShifter();

    // Manually shifted
    double[] shifted = shifter.shift(southwest.longitude(), southwest.latitude());
    Point southwestManualShifted = Point.fromLngLat(shifted[0], shifted[1]);
    shifted = shifter.shift(northeast.longitude(), northeast.latitude());
    Point northeastManualShifted = Point.fromLngLat(shifted[0], shifted[1]);

    CoordinateShifterManager.setCoordinateShifter(shifter);

    BoundingBox boundingBoxFromDouble = BoundingBox.fromLngLats(2.0, 2.0, 4.0, 4.0);

    BoundingBox boundingBoxFromPoints =
            BoundingBox.fromPoints(Point.fromLngLat(2.0, 2.0),
                                   Point.fromLngLat(4.0, 4.0));


    assertEquals(boundingBoxFromDouble, boundingBoxFromPoints);
    assertEquals(southwestManualShifted, boundingBoxFromPoints.southwest());
    assertEquals(northeastManualShifted, boundingBoxFromPoints.northeast());

    CoordinateShifterManager.setCoordinateShifter(null);
  }

  @Test
  public void point_toJson() throws Exception {

    // set shifter
    CoordinateShifterManager.setCoordinateShifter(new TestCoordinateShifter());
    Point point1 = Point.fromLngLat(2.0, 3.0);
    String point1JsonString = point1.toJson();
    Point point2 = Point.fromJson(point1JsonString);

    assertEquals(point1, point2);

    CoordinateShifterManager.setCoordinateShifter(null);
  }

  @Test
  public void point_fromJson() throws Exception {

    // set shifter
    CoordinateShifterManager.setCoordinateShifter(new TestCoordinateShifter());

    Point point1 = Point.fromLngLat(1.0, 2.0);
    String jsonString = "{\"type\":\"Point\",\"coordinates\":[1.0, 2.0]}";
    Point point2 = Point.fromJson(jsonString);

    assertEquals(point1, point2);

    CoordinateShifterManager.setCoordinateShifter(null);
  }

  @Test
  public void linestring_basic_shift_with_bbox() {
    // set shifter
    CoordinateShifterManager.setCoordinateShifter(new TestCoordinateShifter());

    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(1.0, 1.0));
    points.add(Point.fromLngLat(2.0, 2.0));
    points.add(Point.fromLngLat(3.0, 3.0));
    BoundingBox bbox = BoundingBox.fromLngLats(1.0, 2.0, 3.0, 4.0);
    LineString lineString = LineString.fromLngLats(points, bbox);

    String jsonString = lineString.toJson();
    compareJson("{\"coordinates\":[[1,1],[2,2],[3,3]],"
                    + "\"type\":\"LineString\",\"bbox\":[1.0,2.0,3.0,4.0]}",
            jsonString);

    double[] flattenLngLatPoints= new double[]{1.0, 1.0, 2.0, 2.0, 3.0, 3.0};
    LineString lineString2 = LineString.fromFlattenArrayOfPoints(flattenLngLatPoints, bbox);
    String jsonString2 = lineString2.toJson();
    compareJson("{\"coordinates\":[[1,1],[2,2],[3,3]],"
                    + "\"type\":\"LineString\",\"bbox\":[1.0,2.0,3.0,4.0]}",
            jsonString2);

    CoordinateShifterManager.setCoordinateShifter(null);
  }

  public void compareJson(String expectedJson, String actualJson) {
    JsonParser parser = new JsonParser();
    assertThat(parser.parse(actualJson), Matchers.equalTo(parser.parse(expectedJson)));
  }

}
