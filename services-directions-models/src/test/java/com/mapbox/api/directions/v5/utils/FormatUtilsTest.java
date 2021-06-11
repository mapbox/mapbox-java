package com.mapbox.api.directions.v5.utils;

import com.mapbox.geojson.Point;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_UNRESTRICTED;

public class FormatUtilsTest {

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
  public void join_empty() {
    List<Boolean> input = new ArrayList<>();
    String expected = "";

    String actual = FormatUtils.join(";", input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void formatCoordinate() {
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
  public void formatDistributions() {
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
  public void formatWaypointNames() {
    List<String> names = new ArrayList<>();
    names.add(null);
    names.add(null);
    names.add("abc");
    names.add(null);
    names.add("def");
    names.add(null);
    names.add(null);
    String expected = ";;abc;;def;;";

    String actual = FormatUtils.formatWaypointNames(names);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void formatCoordinates() {
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