package com.mapbox.geojson.utils;

import static com.mapbox.geojson.utils.PolylineUtils.decode;
import static com.mapbox.geojson.utils.PolylineUtils.encode;
import static com.mapbox.geojson.utils.PolylineUtils.simplify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PolylineUtilsTest extends TestUtils {

  private static final String TEST_LINE
    = "_cqeFf~cjVf@p@fA}AtAoB`ArAx@hA`GbIvDiFv@gAh@t@X\\|@z@`@Z\\Xf@Vf@VpA\\tATJ@NBBkC";

  @Test
  public void testDecodePath() {
    List<Point> latLngs = decode(TEST_LINE, Constants.PRECISION_5);

    int expectedLength = 21;
    assertEquals("Wrong length.", expectedLength, latLngs.size());

    Point lastPoint = latLngs.get(expectedLength - 1);
    expectNearNumber(37.76953, lastPoint.latitude(), 1e-6);
    expectNearNumber(-122.41488, lastPoint.longitude(), 1e-6);
  }

  @Test
  public void testEncodePath() {
    List<Point> path = decode(TEST_LINE, Constants.PRECISION_5);
    String encoded = encode(path, Constants.PRECISION_5);
    assertEquals(TEST_LINE, encoded);
  }

  @Test
  public void decode_neverReturnsNullButRatherAnEmptyList() throws Exception {
    List<Point> path = decode("", Constants.PRECISION_5);
    assertNotNull(path);
    assertEquals(0, path.size());
  }

  @Test
  public void encode_neverReturnsNull() throws Exception {
    String encodedString = encode(new ArrayList<Point>(), Constants.PRECISION_6);
    assertNotNull(encodedString);
  }

  @Test
  public void simplify_neverReturnsNullButRatherAnEmptyList() throws Exception {
    List<Point> simplifiedPath = simplify(new ArrayList<Point>(), Constants.PRECISION_6);
    assertNotNull(simplifiedPath);
  }
}
