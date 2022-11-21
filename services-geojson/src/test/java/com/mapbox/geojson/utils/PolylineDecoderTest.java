package com.mapbox.geojson.utils;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.TestUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PolylineDecoderTest extends TestUtils {

  @Test
  public void testHasNext() {
    String geometry = "_p~iF~ps|U_ulLnnqC_mqNvxq`@";

    InputStream inputStream = new ByteArrayInputStream(geometry.getBytes());
    PolylineDecoder polylineDecoder = new PolylineDecoder(inputStream, 5);

    assertTrue(polylineDecoder.hasNext());
    polylineDecoder.next();
    assertTrue(polylineDecoder.hasNext());
    polylineDecoder.next();
    assertTrue(polylineDecoder.hasNext());
    polylineDecoder.next();
    assertFalse(polylineDecoder.hasNext());

    polylineDecoder.close();
  }

  @Test
  public void testNextValues() {
    String geometry = "_p~iF~ps|U_ulLnnqC_mqNvxq`@";

    InputStream inputStream = new ByteArrayInputStream(geometry.getBytes());
    PolylineDecoder polylineDecoder = new PolylineDecoder(inputStream, 5);

    Point point = polylineDecoder.next();
    assertEquals(38.5, point.latitude(), 0.0);
    assertEquals(-120.2, point.longitude(), 0.0);
    point = polylineDecoder.next();
    assertEquals(40.7, point.latitude(), 0.0);
    assertEquals(-120.95, point.longitude(), 0.0);
    point = polylineDecoder.next();
    assertEquals(43.252, point.latitude(), 0.0);
    assertEquals(-126.453, point.longitude(), 0.0);
  }

  @Test
  public void testCurrentValue() {
    String geometry = "_p~iF~ps|U_ulLnnqC_mqNvxq`@";

    InputStream inputStream = new ByteArrayInputStream(geometry.getBytes());
    PolylineDecoder polylineDecoder = new PolylineDecoder(inputStream, 5);

    assertNull(polylineDecoder.getCurrent());
    assertEquals(polylineDecoder.next(), polylineDecoder.getCurrent());
    assertEquals(polylineDecoder.next(), polylineDecoder.getCurrent());
    assertEquals(polylineDecoder.next(), polylineDecoder.getCurrent());
  }
}
