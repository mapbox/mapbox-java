package com.mapbox.services.api.models;

import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PositionTest {

  @Test
  public void testEquals() {
    assertNotEquals(Position.fromCoordinates(1.2, 3.4), null);
    assertNotEquals(Position.fromCoordinates(1.2, 3.4), Position.fromCoordinates(1.2, 0.0));
    assertNotEquals(Position.fromCoordinates(1.2, 3.4), Position.fromCoordinates(0.0, 3.4));
    assertNotEquals(Position.fromCoordinates(1.2, 3.4), Position.fromCoordinates(1.2, 3.4, 0.0));

    assertEquals(Position.fromCoordinates(4.3, 2.1), Position.fromCoordinates(4.3, 2.1));
    assertEquals(Position.fromCoordinates(6.5, 4.3, 2.1), Position.fromCoordinates(6.5, 4.3, 2.1));
  }

  @Test
  public void checksEqualityFromCoordinatesLongitudeLatitude() {
    Position position = Position.fromCoordinates(new double[] {100.0, 0.0});

    String positionCoordinates = position.toString();

    assertEquals("Position [longitude=100.0, latitude=0.0, altitude=NaN]", positionCoordinates);
  }

  @Test
  public void checksEqualityFromCoordinatesLongitudeLatitudeAltitude() {
    Position position = Position.fromCoordinates(new double[] {100.0, 0.0, 3.1});

    String positionCoordinates = position.toString();

    assertEquals("Position [longitude=100.0, latitude=0.0, altitude=3.1]", positionCoordinates);
  }
}
