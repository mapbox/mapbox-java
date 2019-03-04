package com.mapbox.geojson;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.gson.GeometryGeoJson;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeometryTest extends TestUtils {

  private static final String SAMPLE_GEOMETRY_COLLECTION = "sample-geometrycollection.json";

  @Test
  public void fromJson() throws IOException {
    final String json =
            "    { \"type\": \"GeometryCollection\"," +
                    "            \"bbox\": [120, 40, -120, -40]," +
                    "      \"geometries\": [" +
                    "      { \"type\": \"Point\"," +
                    "              \"bbox\": [110, 30, -110, -30]," +
                    "        \"coordinates\": [100, 0]}," +
                    "      { \"type\": \"LineString\"," +
                    "              \"bbox\": [110, 30, -110, -30]," +
                    "        \"coordinates\": [[101, 0], [102, 1]]}]}";
    Geometry geo = GeometryGeoJson.fromJson(json);
    assertEquals(geo.type(), "GeometryCollection");
  }

  @Test
  public void pointFromJson() throws IOException {
    Geometry geometry = GeometryGeoJson.fromJson("{\"coordinates\": [2,3],"
            + "\"type\":\"Point\",\"bbox\":[1.0,2.0,3.0,4.0]}");

    assertNotNull(geometry);
    assertNotNull(geometry.bbox());
    assertEquals(1.0, geometry.bbox().southwest().longitude(), DELTA);
    assertEquals(2.0, geometry.bbox().southwest().latitude(), DELTA);
    assertEquals(3.0, geometry.bbox().northeast().longitude(), DELTA);
    assertEquals(4.0, geometry.bbox().northeast().latitude(), DELTA);
    assertNotNull(((Point)geometry).coordinates());
    assertEquals(2, ((Point)geometry).longitude(), DELTA);
    assertEquals(3, ((Point)geometry).latitude(), DELTA);
  }

  @Test
  public void pointToJson() throws IOException {
    Geometry geometry = Point.fromLngLat(2, 3,
            BoundingBox.fromLngLats(1, 2, 3, 4));
    String pointStr = geometry.toJson();
    compareJson("{\"coordinates\": [2,3],"
            + "\"type\":\"Point\",\"bbox\":[1.0,2.0,3.0,4.0]}",
            pointStr);
  }

  @Test
  public void lineStringFromJson() throws Exception {
    Geometry lineString = GeometryGeoJson.fromJson("{\"coordinates\":[[1,2],[2,3],[3,4]],"
            + "\"type\":\"LineString\",\"bbox\":[1.0,2.0,3.0,4.0]}");

    assertNotNull(lineString);
    assertNotNull(lineString.bbox());
    assertEquals(1.0, lineString.bbox().southwest().longitude(), DELTA);
    assertEquals(2.0, lineString.bbox().southwest().latitude(), DELTA);
    assertEquals(3.0, lineString.bbox().northeast().longitude(), DELTA);
    assertEquals(4.0, lineString.bbox().northeast().latitude(), DELTA);
    assertNotNull(((LineString)lineString).coordinates());
    assertEquals(1, ((LineString)lineString).coordinates().get(0).longitude(), DELTA);
    assertEquals(2, ((LineString)lineString).coordinates().get(0).latitude(), DELTA);
    assertEquals(2, ((LineString)lineString).coordinates().get(1).longitude(), DELTA);
    assertEquals(3, ((LineString)lineString).coordinates().get(1).latitude(), DELTA);
    assertEquals(3, ((LineString)lineString).coordinates().get(2).longitude(), DELTA);
    assertEquals(4, ((LineString)lineString).coordinates().get(2).latitude(), DELTA);
  }

  @Test
  public void lineStringToJson() throws Exception {

    Geometry geometry = LineString.fromLngLats(
            Arrays.asList(Point.fromLngLat(1, 2),
                    Point.fromLngLat(2, 3),
                    Point.fromLngLat(3, 4)),
            BoundingBox.fromLngLats(1, 2, 3, 4));
    String geometryJsonStr = geometry.toJson();
    String expectedJsonString = "{\"coordinates\":[[1,2],[2,3],[3,4]],"
            + "\"type\":\"LineString\",\"bbox\":[1.0,2.0,3.0,4.0]}";
    compareJson(expectedJsonString, geometryJsonStr);
  }
}
