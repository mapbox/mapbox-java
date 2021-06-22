package com.mapbox.api.directions.v5.utils;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.Bearing;
import com.mapbox.geojson.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ParseUtilsTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void parseToIntegers() {
    String input = "1;4;5;7;8";
    List<Integer> expected = new ArrayList<>();
    expected.add(1);
    expected.add(4);
    expected.add(5);
    expected.add(7);
    expected.add(8);

    List<Integer> actual = ParseUtils.parseToIntegers(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToStrings_1() {
    String input = "distance;congestion";
    List<String> expected = new ArrayList<>();
    expected.add(DirectionsCriteria.ANNOTATION_DISTANCE);
    expected.add(DirectionsCriteria.ANNOTATION_CONGESTION);

    List<String> actual = ParseUtils.parseToStrings(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToStrings_2() {
    String input = "ab;;;cd;ef;;;gh;ij";
    List<String> expected = new ArrayList<>();
    expected.add("ab");
    expected.add(null);
    expected.add(null);
    expected.add("cd");
    expected.add("ef");
    expected.add(null);
    expected.add(null);
    expected.add("gh");
    expected.add("ij");

    List<String> actual = ParseUtils.parseToStrings(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToStrings_3() {
    String input = "distance,congestion";
    List<String> expected = new ArrayList<>();
    expected.add(DirectionsCriteria.ANNOTATION_DISTANCE);
    expected.add(DirectionsCriteria.ANNOTATION_CONGESTION);

    List<String> actual = ParseUtils.parseToStrings(input, ",");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToStrings_nullAtFirst() {
    String input = ";distance;congestion";
    List<String> expected = new ArrayList<>();
    expected.add(null);
    expected.add(DirectionsCriteria.ANNOTATION_DISTANCE);
    expected.add(DirectionsCriteria.ANNOTATION_CONGESTION);

    List<String> actual = ParseUtils.parseToStrings(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToStrings_nullAtLast() {
    String input = ";distance;congestion;";
    List<String> expected = new ArrayList<>();
    expected.add(null);
    expected.add(DirectionsCriteria.ANNOTATION_DISTANCE);
    expected.add(DirectionsCriteria.ANNOTATION_CONGESTION);
    expected.add(null);

    List<String> actual = ParseUtils.parseToStrings(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToStrings_nulls() {
    String input = ";;";
    List<String> expected = new ArrayList<>();
    expected.add(null);
    expected.add(null);
    expected.add(null);

    List<String> actual = ParseUtils.parseToStrings(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testParseToStrings() {
    String input = ",a,b";
    List<String> expected = new ArrayList<>();
    expected.add(null);
    expected.add("a");
    expected.add("b");

    List<String> actual = ParseUtils.parseToStrings(input, ",");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToPoints() {
    String input = "1.2,3.4;;;5.65,7.123;;;";
    List<Point> expected = new ArrayList<>();
    expected.add(Point.fromLngLat(1.2, 3.4));
    expected.add(null);
    expected.add(null);
    expected.add(Point.fromLngLat(5.65, 7.123));
    expected.add(null);
    expected.add(null);
    expected.add(null);

    List<Point> actual = ParseUtils.parseToPoints(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToDoubles() {
    String input = ";5.1;;7.4;;";
    List<Double> expected = new ArrayList<>();
    expected.add(null);
    expected.add(5.1);
    expected.add(null);
    expected.add(7.4);
    expected.add(null);
    expected.add(null);

    List<Double> actual = ParseUtils.parseToDoubles(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToListOfListOfDoubles() {
    String input = ";5.1,7.4;;";
    List<Bearing> expected = new ArrayList<>();
    expected.add(null);
    expected.add(Bearing.builder().angle(5.1).degrees(7.4).build());
    expected.add(null);
    expected.add(null);

    List<Bearing> actual = ParseUtils.parseBearings(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToBooleans() {
    String input = ";true;;;false;false;true;;;;";
    List<Boolean> expected = new ArrayList<>();
    expected.add(null);
    expected.add(true);
    expected.add(null);
    expected.add(null);
    expected.add(false);
    expected.add(false);
    expected.add(true);
    expected.add(null);
    expected.add(null);
    expected.add(null);
    expected.add(null);

    List<Boolean> actual = ParseUtils.parseToBooleans(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToBooleans_empty() {
    String input = "";
    List<Boolean> expected = new ArrayList<>();
    expected.add(null);

    List<Boolean> actual = ParseUtils.parseToBooleans(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseToBooleans_wrong_string() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Boolean value should be either true or false string but is abc"
    );

    String input = "true;abc";

    ParseUtils.parseToBooleans(input);
  }
}