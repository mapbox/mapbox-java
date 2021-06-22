package com.mapbox.api.directions.v5.utils;

import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_CURB;
import static com.mapbox.api.directions.v5.DirectionsCriteria.APPROACH_UNRESTRICTED;
import static org.junit.Assert.assertEquals;
import com.mapbox.api.directions.v5.models.Bearing;
import com.mapbox.geojson.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class FormatUtilsTest {

  @Test
  public void join_1() {
    List<Integer> input = new ArrayList<>();
    input.add(1);
    input.add(5);
    input.add(7);
    String expected = "1;5;7";

    String actual = FormatUtils.join(";", input);

    assertEquals(expected, actual);
  }

  @Test
  public void join_2() {
    List<Integer> input = new ArrayList<>();
    input.add(1);
    input.add(5);
    input.add(7);
    String expected = "1,5,7";

    String actual = FormatUtils.join(",", input);

    assertEquals(expected, actual);
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

    assertEquals(expected, actual);
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

    assertEquals(expected, actual);
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

    assertEquals(expected, actual);
  }

  @Test
  public void formatRadiuses_withNull() {
    List<Double> names = new ArrayList<>();
    names.add(5.1);
    names.add(null);
    names.add(Double.POSITIVE_INFINITY);
    String expected = "5.1;;unlimited";

    String actual = FormatUtils.formatRadiuses(names);

    assertEquals(expected, actual);
  }

  @Test
  public void formatBearings() {
    List<Bearing> bearings = new ArrayList<>();
    bearings.add(null);
    bearings.add(null);
    bearings.add(Bearing.builder().angle(1.1).degrees(2.2).build());
    bearings.add(Bearing.builder().angle(7.7).degrees(8.8).build());
    bearings.add(null);

    String expected = ";;1.1,2.2;7.7,8.8;";

    String actual = FormatUtils.formatBearings(bearings);

    assertEquals(expected, actual);
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

    assertEquals(expected, actual);
  }
}