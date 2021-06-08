package com.mapbox.api.directions.v5.utils;

import com.mapbox.geojson.Point;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_UNRESTRICTED;

public class FormatUtilsTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void join_1() {
    List<Integer> input = new ArrayList<>();
    input.add(1);
    input.add(5);
    input.add(7);
    String expected = "1;5;7";

    String actual = FormatUtils.join(";", input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void join_2() {
    List<Integer> input = new ArrayList<>();
    input.add(1);
    input.add(5);
    input.add(7);
    String expected = "1,5,7";

    String actual = FormatUtils.join(",", input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void join_3() {
    List<Boolean> input = new ArrayList<>();
    input.add(false);
    input.add(false);
    input.add(null);
    input.add(true);
    input.add(false);
    input.add(null);
    input.add(null);
    String expected = "false;false;;true;false;;";

    String actual = FormatUtils.join(";", input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void join_4() {
    List<String> names = new ArrayList<>();
    names.add(null);
    names.add(null);
    names.add("abc");
    names.add(null);
    names.add("def");
    names.add(null);
    names.add(null);
    String expected = ";;abc;;def;;";

    String actual = FormatUtils.join(";", names);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void join_empty() {
    List<Boolean> input = new ArrayList<>();

    String actual = FormatUtils.join(";", input);

    Assert.assertNull(actual);
  }

  @Test
  public void formatRadiuses() {
    List<Double> names = new ArrayList<>();
    names.add(5.1);
    names.add(3.3);
    names.add(Double.POSITIVE_INFINITY);
    String expected = "5.1;3.3;unlimited";

    String actual = FormatUtils.formatRadiuses(names);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void formatRadiuses_withNull() {
    List<Double> names = new ArrayList<>();
    names.add(5.1);
    names.add(null);
    names.add(Double.POSITIVE_INFINITY);
    String expected = "5.1;;unlimited";

    String actual = FormatUtils.formatRadiuses(names);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void formatRadiuses_0() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Radiuses need to be greater than 0 or Double.POSITIVE_INFINITY."
    );

    List<Double> names = new ArrayList<>();
    names.add(0.0);
    names.add(3.3);
    names.add(Double.POSITIVE_INFINITY);
    String expected = "5.1;3.3;unlimited";

    String actual = FormatUtils.formatRadiuses(names);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void formatRadiuses_negative() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(
      "Radiuses need to be greater than 0 or Double.POSITIVE_INFINITY."
    );

    List<Double> names = new ArrayList<>();
    names.add(-5.1);
    names.add(3.3);
    names.add(Double.POSITIVE_INFINITY);
    String expected = "5.1;3.3;unlimited";

    String actual = FormatUtils.formatRadiuses(names);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void formatBearings() {
    List<Double> bearing1 = new ArrayList<>();
    bearing1.add(1.1);
    bearing1.add(2.2);

    List<Double> bearing2 = new ArrayList<>();
    bearing2.add(7.7);
    bearing2.add(8.8);

    List<List<Double>> bearings = new ArrayList<>();
    bearings.add(null);
    bearings.add(null);
    bearings.add(bearing1);
    bearings.add(bearing2);
    bearings.add(null);

    String expected = ";;1.1,2.2;7.7,8.8;";

    String actual = FormatUtils.formatBearings(bearings);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void formatApproaches() {
    List<String> approaches = new ArrayList<>();
    approaches.add(APPROACH_CURB);
    approaches.add(null);
    approaches.add(null);
    approaches.add(APPROACH_UNRESTRICTED);
    approaches.add(APPROACH_UNRESTRICTED);
    approaches.add(null);
    approaches.add(null);
    approaches.add(APPROACH_CURB);
    approaches.add(null);
    String expected = "curb;;;unrestricted;unrestricted;;;curb;";

    String actual = FormatUtils.formatApproaches(approaches);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void formatPointsList() {
    List<Point> targets = new ArrayList<>();
    targets.add(null);
    targets.add(null);
    targets.add(Point.fromLngLat(5.55, 7.77));
    targets.add(Point.fromLngLat(1.22, 3.44));
    targets.add(null);
    targets.add(Point.fromLngLat(1.55, 8.99));
    String expected = ";;5.55,7.77;1.22,3.44;;1.55,8.99";

    String actual = FormatUtils.formatPointsList(targets);

    Assert.assertEquals(expected, actual);
  }
}