package com.mapbox.geojson.utils;

import com.mapbox.core.constants.Constants;
import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PolylineUtilsTest extends TestUtils {

  private static final String TEST_LINE = "_cqeFf~cjVf@p@fA}AtAoB`ArAx@hA`GbIvDiFv@gAh@t@X\\|@z@`@Z\\Xf@Vf@VpA\\tATJ@NBBkC";

  @Test
  public void testDecodePath() {
    List<Point> latLngs = PolylineUtils.decode(TEST_LINE, Constants.PRECISION_5);

    int expectedLength = 21;
    Assert.assertEquals("Wrong length.", expectedLength, latLngs.size());

    Point lastPoint = latLngs.get(expectedLength - 1);
    expectNearNumber(37.76953, lastPoint.latitude(), 1e-6);
    expectNearNumber(-122.41488, lastPoint.longitude(), 1e-6);
  }

  @Test
  public void testEncodePath() {
    List<Point> path = PolylineUtils.decode(TEST_LINE, Constants.PRECISION_5);
    String encoded = PolylineUtils.encode(path, Constants.PRECISION_5);
    Assert.assertEquals(TEST_LINE, encoded);
  }
}
