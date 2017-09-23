package com.mapbox.staticmap.v1.models;

import com.mapbox.geojson.Point;
import com.mapbox.staticmap.v1.StaticMapCriteria;
import org.hamcrest.junit.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.awt.Color;

public class StaticMarkerAnnotationTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    StaticMarkerAnnotation staticMarkerAnnotation = StaticMarkerAnnotation.builder()
      .lnglat(Point .fromLngLat(1.0,2.0))
      .name(StaticMapCriteria.MEDIUM_PIN)
      .build();
    assertTrue(staticMarkerAnnotation.url().contains("pin-m(1.000000,2.000000)"));
  }

  @Test
  public void url_withOnlyLabelFormattedCorrectly() throws Exception {
    StaticMarkerAnnotation staticMarkerAnnotation = StaticMarkerAnnotation.builder()
      .lnglat(Point .fromLngLat(1.0,2.0))
      .name(StaticMapCriteria.MEDIUM_PIN)
      .label("abc")
      .build();
    assertTrue(staticMarkerAnnotation.url().contains("pin-m-abc(1.000000,2.000000)"));
  }

  @Test
  public void url_withLabelAndColorFormattedCorrectly() throws Exception {
    StaticMarkerAnnotation staticMarkerAnnotation = StaticMarkerAnnotation.builder()
      .lnglat(Point .fromLngLat(1.0,2.0))
      .name(StaticMapCriteria.MEDIUM_PIN)
      .label("abc")
      .color(Color.BLUE)
      .build();
    assertTrue(staticMarkerAnnotation.url().contains("pin-m-abc+0000ff(1.000000,2.000000)"));
  }

  @Test
  public void url_withColorOnlyFormattedCorrectly() throws Exception {
    StaticMarkerAnnotation staticMarkerAnnotation = StaticMarkerAnnotation.builder()
      .lnglat(Point .fromLngLat(1.0,2.0))
      .name(StaticMapCriteria.MEDIUM_PIN)
      .color(Color.BLUE)
      .build();
    assertTrue(staticMarkerAnnotation.url().contains("pin-m-0000ff(1.000000,2.000000)"));
  }

  @Test
  public void build_throwsExceptionWhenNoCoordinateIsGiven() throws Exception {

  }

  //
//
//  @Test(expected = ServicesException.class)
//  public void requiresOneAlphabetCharLabelIfPresent() throws ServicesException {
//    new StaticMarkerAnnotation.Builder()
//      .setName(StaticMapCriteria.PIN_SMALL)
//      .setLat(2.0)
//      .setLon(2.0)
//      .setLabel("aa")
//      .build();
//  }
//
//  @Test(expected = ServicesException.class)
//  public void requiresZeroToNinetynineNumericCharLabelIfPresent() throws ServicesException {
//    new StaticMarkerAnnotation.Builder()
//      .setName(Constants.PIN_SMALL)
//      .setLat(2.0)
//      .setLon(2.0)
//      .setLabel("100")
//      .build();
//  }
//
//  @Test
//  public void checksMarkerAlphabetCharLabel() throws ServicesException {
//    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
//      .setLat(2.0)
//      .setLon(1.0)
//      .setName(Constants.PIN_SMALL)
//      .setLabel("a")
//      .build();
//
//    assertTrue(staticMarkerAnnotation.getMarker().contains("pin-s-a(1.000000,2.000000)"));
//  }
//
//  @Test
//  public void checksMarkerCaseInsensitiveAlphabetCharLabel() throws ServicesException {
//    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
//      .setLat(2.0)
//      .setLon(1.0)
//      .setName(Constants.PIN_SMALL)
//      .setLabel("A")
//      .build();
//
//    assertTrue(staticMarkerAnnotation.getMarker().contains("pin-s-a(1.000000,2.000000)"));
//  }
//
//  @Test
//  public void checksMarkerNumericCharLabel() throws ServicesException {
//    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
//      .setLat(2.0)
//      .setLon(1.0)
//      .setName(Constants.PIN_SMALL)
//      .setLabel("33")
//      .build();
//
//    assertTrue(staticMarkerAnnotation.getMarker().contains("pin-s-33(1.000000,2.000000)"));
//  }
//
//  @Test
//  public void checksMarkerMapboxMakiIconCharLabel() throws ServicesException {
//    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
//      .setLat(2.0)
//      .setLon(1.0)
//      .setName(Constants.PIN_SMALL)
//      .setLabel("bakery")
//      .build();
//
//    assertTrue(staticMarkerAnnotation.getMarker().contains("pin-s-bakery(1.000000,2.000000)"));
//  }
//
//  @Test
//  public void requiresValidColorCodeIfPresent() throws ServicesException {
//    thrown.expect(ServicesException.class);
//    thrown.expectMessage(startsWith("You need to pass 3- or 6-digit hexadecimal color code."));
//
//    new StaticMarkerAnnotation.Builder()
//      .setName(Constants.PIN_SMALL)
//      .setLat(2.0)
//      .setLon(2.0)
//      .setColor("34564")
//      .build();
//  }
//
//  @Test
//  public void checksMarkerWithThreeDigitColorCode() throws ServicesException {
//    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
//      .setLat(2.0)
//      .setLon(1.0)
//      .setName(Constants.PIN_SMALL)
//      .setColor("333")
//      .build();
//
//    assertTrue(staticMarkerAnnotation.getMarker().contains("pin-s+333(1.000000,2.000000)"));
//  }
//
//  @Test
//  public void checksMarkerWithSixDigitHexColorCode() throws ServicesException {
//    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
//      .setLat(2.0)
//      .setLon(1.0)
//      .setName(Constants.PIN_SMALL)
//      .setColor("666666")
//      .build();
//
//    assertTrue(staticMarkerAnnotation.getMarker().contains("pin-s+666666(1.000000,2.000000)"));
//  }
//
//  @Test
//  public void markerPositionWorking() throws ServicesException {
//    Position position = Position.fromCoordinates(1.0, 2.0);
//
//    StaticMarkerAnnotation staticMarkerAnnotation
//      = new StaticMarkerAnnotation.Builder().setName(Constants.PIN_SMALL).setPosition(position).build();
//    assertTrue(staticMarkerAnnotation.getMarker().contains("(1.000000,2.000000)"));
//  }
//
//  @Test
//  public void singleMarkerInUrl() {
//    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
//      .setName(Constants.PIN_SMALL)
//      .setPosition(Position.fromCoordinates(1.0, 2.0))
//      .build();
//
//    MapboxStaticImage image = new MapboxStaticImage.Builder()
//      .setAccessToken("pk.")
//      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
//      .setStaticMarkerAnnotations(staticMarkerAnnotation)
//      .setPosition(Position.fromCoordinates(1.0, 2.0))
//      .setWidth(100).setHeight(200)
//      .build();
//
//    assertTrue(image.getUrl().toString().contains("pin-s(1.000000,2.000000)"));
//  }
//
//  @Test
//  public void doubleMarkersInUrl() {
//    StaticMarkerAnnotation staticMarkerAnnotation1 = new StaticMarkerAnnotation.Builder()
//      .setName(Constants.PIN_SMALL)
//      .setPosition(Position.fromCoordinates(1.0, 2.0))
//      .build();
//
//    StaticMarkerAnnotation staticMarkerAnnotation2 = new StaticMarkerAnnotation.Builder()
//      .setName(Constants.PIN_MEDIUM)
//      .setPosition(Position.fromCoordinates(5.0, 6.0))
//      .build();
//
//    MapboxStaticImage image = new MapboxStaticImage.Builder()
//      .setAccessToken("pk.")
//      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
//      .setStaticMarkerAnnotations(staticMarkerAnnotation1, staticMarkerAnnotation2)
//      .setPosition(Position.fromCoordinates(1.0, 2.0))
//      .setWidth(100).setHeight(200)
//      .build();
//
//    assertTrue(image.getUrl().toString().contains("pin-s(1.000000,2.000000),pin-m(5.000000,6.000000)"));
//  }
//
//  @Test
//  public void customMarkerIcon() {
//    StaticMarkerAnnotation staticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
//      .setUrl("https://www.mapbox.com/img/rocket.png")
//      .setPosition(Position.fromCoordinates(1.0, 2.0))
//      .build();
//
//    assertTrue(staticMarkerAnnotation.getMarker().contains("url-https://www.mapbox.com/img/rocket.png(1.0,2.0)"));
//  }
}
