package com.mapbox.services.api.staticimage.v1;


import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.staticimage.v1.models.StaticMarkerAnnotation;
import com.mapbox.services.commons.models.Position;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertTrue;

public class StaticMarkerAnnotationTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSanity() throws ServicesException {
    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
      .setLat(2.0)
      .setLon(1.0)
      .setName(Constants.PIN_SMALL)
      .build();

    assertTrue(staticMarkerAnnotation.getMarker().contains("pin-s-+(1.000000,2.000000)"));
  }

  @Test
  public void requireMarkerName() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith(
      "You need to set a marker name using one of the three Mapbox Service Constant names"));

    new StaticMarkerAnnotation.Builder().setLat(2.0).setLon(1.0).build();
  }

  @Test
  public void requireCorrectMarkerName() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith(
      "You need to set a marker name using one of the three Mapbox Service Constant names"));

    new StaticMarkerAnnotation.Builder().setLat(2.0).setLon(1.0).setName("foobar").build();
  }

  @Test
  public void requireMarkerLat() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith(
      "You need to give the marker either lon/lat coordinates or a Position object."));

    new StaticMarkerAnnotation.Builder().setName(Constants.PIN_SMALL).setLon(1.0).build();
  }

  @Test
  public void requireMarkerLon() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith(
      "You need to give the marker either lon/lat coordinates or a Position object."));

    new StaticMarkerAnnotation.Builder().setName(Constants.PIN_SMALL).setLat(2.0).build();
  }

  @Test
  public void requireValidMarkerColor() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to pass 3- or 6-digit hexadecimal color code."));

    new StaticMarkerAnnotation.Builder()
      .setName(Constants.PIN_SMALL)
      .setLat(2.0)
      .setLon(2.0)
      .setColor("34564")
      .build();
  }

  @Test
  public void markerPositionWorking() throws ServicesException {
    Position position = Position.fromCoordinates(1.0, 2.0);

    StaticMarkerAnnotation staticMarkerAnnotation
      = new StaticMarkerAnnotation.Builder().setName(Constants.PIN_SMALL).setPosition(position).build();
    assertTrue(staticMarkerAnnotation.getMarker().contains("(1.000000,2.000000)"));
  }

  @Test
  public void singleMarkerInUrl() {
    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
      .setName(Constants.PIN_SMALL)
      .setPosition(Position.fromCoordinates(1.0, 2.0))
      .build();

    MapboxStaticImage image = new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setStaticMarkerAnnotations(staticMarkerAnnotation)
      .setPosition(Position.fromCoordinates(1.0, 2.0))
      .setWidth(100).setHeight(200)
      .build();

    assertTrue(image.getUrl().toString().contains("pin-s-+(1.000000,2.000000)"));
  }

  @Test
  public void doubleMarkersInUrl() {
    StaticMarkerAnnotation staticMarkerAnnotation1 = new StaticMarkerAnnotation.Builder()
      .setName(Constants.PIN_SMALL)
      .setPosition(Position.fromCoordinates(1.0, 2.0))
      .build();

    StaticMarkerAnnotation staticMarkerAnnotation2 = new StaticMarkerAnnotation.Builder()
      .setName(Constants.PIN_MEDIUM)
      .setPosition(Position.fromCoordinates(5.0, 6.0))
      .build();

    MapboxStaticImage image = new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setStaticMarkerAnnotations(staticMarkerAnnotation1, staticMarkerAnnotation2)
      .setPosition(Position.fromCoordinates(1.0, 2.0))
      .setWidth(100).setHeight(200)
      .build();

    assertTrue(image.getUrl().toString().contains("pin-s-+(1.000000,2.000000),pin-m-+(5.000000,6.000000)"));
  }

  @Test
  public void customMarkerIcon() {
    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
      .setUrl("https://www.mapbox.com/img/rocket.png")
      .setPosition(Position.fromCoordinates(1.0, 2.0))
      .build();

    assertTrue(staticMarkerAnnotation.getMarker().contains("url-https://www.mapbox.com/img/rocket.png(1.0,2.0)"));
  }
}
