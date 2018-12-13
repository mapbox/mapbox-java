package com.mapbox.geojson.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class PointDeserializerTest extends TestUtils {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void deserialize_sanity() throws Exception {
    String jsonString = "[100.0, 0.0]";
    GsonBuilder gsonBuilder = new GsonBuilder()
      .registerTypeAdapter(Point.class, new PointDeserializer());
    Point point = gsonBuilder.create().fromJson(jsonString, Point.class);

    assertEquals(100, point.longitude(), DELTA);
    assertEquals(0, point.latitude(), DELTA);
  }

  @Test
  public void point_doesNotDeserializeObject() throws Exception {
    thrown.expect(NullPointerException.class);

    String jsonString = "{ \"coordinates\": [100.0, 0.0, 200.0]}";
    GsonBuilder gsonBuilder = new GsonBuilder()
      .registerTypeAdapter(Point.class, new PointDeserializer());
    gsonBuilder.create().fromJson(jsonString, Point.class);
  }

  @Test
  public void point_deserializeArray() throws Exception {
    String jsonString = "[100.0, 0.0, 200.0]";
    GsonBuilder gsonBuilder = new GsonBuilder()
      .registerTypeAdapter(Point.class, new PointDeserializer());
    Point point = gsonBuilder.create().fromJson(jsonString, Point.class);

    assertEquals(100, point.longitude(), DELTA);
    assertEquals(0, point.latitude(), DELTA);
    assertEquals(200, point.altitude(), DELTA);
  }

  @Test
  public void point_deserializeArrayOfArrays() throws Exception {
    String jsonString = "[[50.0, 50.0, 100.0], [100.0, 100.0, 200.0]]";
    GsonBuilder gsonBuilder = new GsonBuilder()
      .registerTypeAdapter(Point.class, new PointDeserializer());
    List<Point> points = gsonBuilder.create().fromJson(jsonString,
      new TypeToken<List<Point>>() {}.getType());

    assertEquals(50, points.get(0).longitude(), DELTA);
    assertEquals(50, points.get(0).latitude(), DELTA);
    assertEquals(100, points.get(0).altitude(), DELTA);


    assertEquals(100, points.get(1).longitude(), DELTA);
    assertEquals(100, points.get(1).latitude(), DELTA);
    assertEquals(200, points.get(1).altitude(), DELTA);
  }

  @Test
  public void point_deserializeArrayOfArraysOfArrays() throws Exception {
    String jsonString = "[[[50.0, 50.0, 100.0], [100.0, 100.0, 200.0]]]";
    GsonBuilder gsonBuilder = new GsonBuilder()
      .registerTypeAdapter(Point.class, new PointDeserializer());

    Type type =
      new TypeToken<List<List<Point>>>() {}.getType();
   List<List<Point>> points = gsonBuilder.create().fromJson(jsonString,
      type);

    assertEquals(50, points.get(0).get(0).longitude(), DELTA);
    assertEquals(50, points.get(0).get(0).latitude(), DELTA);
    assertEquals(100, points.get(0).get(0).altitude(), DELTA);


    assertEquals(100, points.get(0).get(1).longitude(), DELTA);
    assertEquals(100, points.get(0).get(1).latitude(), DELTA);
    assertEquals(200, points.get(0).get(1).altitude(), DELTA);
  }
}
