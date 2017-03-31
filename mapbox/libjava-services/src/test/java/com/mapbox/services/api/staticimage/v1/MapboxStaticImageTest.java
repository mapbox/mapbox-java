package com.mapbox.services.api.staticimage.v1;

import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.staticimage.v1.models.Marker;
import com.mapbox.services.commons.models.Position;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import okhttp3.HttpUrl;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MapboxStaticImageTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSanity() throws ServicesException {
    MapboxStaticImage client = new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLat(1.0).setLon(2.0)
      .setZoom(10)
      .setWidth(100).setHeight(200)
      .build();
    HttpUrl url = client.getUrl();
    assertFalse(url.toString().isEmpty());
  }

  @Test
  public void requireAccessToken() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token."));
    new MapboxStaticImage.Builder()
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLat(1.0).setLon(2.0)
      .setZoom(10)
      .setWidth(100).setHeight(200)
      .build();
  }

  @Test
  public void requireStyleId() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set a map style."));
    new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setLat(1.0).setLon(2.0)
      .setZoom(10)
      .setWidth(100).setHeight(200)
      .build();
  }

  @Test
  public void requireLon() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set the map lon/lat coordinates."));
    new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLat(1.0)
      .setZoom(10)
      .setWidth(100).setHeight(200)
      .build();
  }

  @Test
  public void requireLat() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set the map lon/lat coordinates."));
    new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLon(2.0)
      .setZoom(10)
      .setWidth(100).setHeight(200)
      .build();
  }

  @Test
  public void requireZoom() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set the map zoom level."));
    new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLat(1.0).setLon(2.0)
      .setWidth(100).setHeight(200)
      .build();
  }

  @Test
  public void requireWidth() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set a valid image width (between 1 and 1280)."));
    new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLat(1.0).setLon(2.0)
      .setZoom(10)
      .setHeight(200)
      .build();
  }

  @Test
  public void requireHeight() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set a valid image height (between 1 and 1280)."));
    new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLat(1.0).setLon(2.0)
      .setZoom(10)
      .setWidth(100)
      .build();
  }

  @Test
  public void requireHeightInRange() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set a valid image height (between 1 and 1280)."));
    new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLat(1.0).setLon(2.0)
      .setZoom(10)
      .setWidth(100)
      .setHeight(5000)
      .build();
  }

  @Test
  public void testPrecision() throws ServicesException {
    MapboxStaticImage client = new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLocation(Position.fromCoordinates(1.23456789, -98.76))
      .setZoom(10).setBearing(345.67890123456789).setPitch(0.000000005)
      .setWidth(100).setHeight(200)
      .setPrecision(5)
      .build();
    HttpUrl url = client.getUrl();
    assertEquals(
      url.toString(),
      "https://api.mapbox.com/styles/v1/mapbox/streets-v9/static/1.23456,-98.76000,10.00000,345.67890,0.00000/100x200?access_token=pk.");
  }

  @Test
  public void requireMarkerName() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set a marker name."));

    ArrayList<Marker> markers = new ArrayList<>();
    Marker m = new Marker();
    m.setName("");
    markers.add(m);

    new MapboxStaticImage.Builder()
            .setAccessToken("pk.")
            .setStyleId(Constants.MAPBOX_STYLE_STREETS)
            .setLon(2.0)
            .setLat(2.0)
            .setZoom(10)
            .setWidth(100).setHeight(200)
            .setMarker(markers)
            .build();
  }

  @Test
  public void requireMarkerLat() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set the map lon/lat coordinates."));

    ArrayList<Marker> markers = new ArrayList<>();
    Marker marker = new Marker();
    marker.setName("pin-s");
    marker.setLon(2.0);
    markers.add(marker);

    new MapboxStaticImage.Builder()
            .setAccessToken("pk.")
            .setStyleId(Constants.MAPBOX_STYLE_STREETS)
            .setLon(2.0)
            .setLat(2.0)
            .setZoom(10)
            .setWidth(100).setHeight(200)
            .setMarker(markers)
            .build();
  }

  @Test
  public void requireMarkerLon() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to set the map lon/lat coordinates."));

    ArrayList<Marker> markers = new ArrayList<>();
    Marker marker = new Marker();
    marker.setName("pin-s");
    marker.setLat(2.0);
    markers.add(marker);

    new MapboxStaticImage.Builder()
            .setAccessToken("pk.")
            .setStyleId(Constants.MAPBOX_STYLE_STREETS)
            .setLon(2.0)
            .setLat(2.0)
            .setZoom(10)
            .setWidth(100).setHeight(200)
            .setMarker(markers)
            .build();
  }

  @Test
  public void requireValidMarkerColor() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("You need to pass 3- or 6-digit hexadecimal color code."));

    ArrayList<Marker> markers = new ArrayList<>();
    Marker marker = new Marker();
    marker.setName("pin-s");
    marker.setLat(2.0);
    marker.setLon(2.0);
    marker.setColor("34564");
    markers.add(marker);

    new MapboxStaticImage.Builder()
            .setAccessToken("pk.")
            .setStyleId(Constants.MAPBOX_STYLE_STREETS)
            .setLon(2.0)
            .setLat(2.0)
            .setZoom(10)
            .setWidth(100).setHeight(200)
            .setMarker(markers)
            .build();
  }

  @Test
  public void testAnnotationPathSegmentSingleMarker() throws ServicesException {

    ArrayList<Marker> markers = new ArrayList<>();
    markers.add(getMarker());

    MapboxStaticImage client = new MapboxStaticImage.Builder()
            .setAccessToken("pk.")
            .setStyleId(Constants.MAPBOX_STYLE_STREETS)
            .setMarker(markers)
            .setLocation(Position.fromCoordinates(1.23456789,37.75965))
            .setZoom(10).setBearing(345.67890123456789).setPitch(0.000000005)
            .setWidth(100).setHeight(200)
            .setPrecision(5)
            .build();
    HttpUrl url = client.getUrl();
    assertEquals(
            url.toString(),
            "https://api.mapbox.com/styles/v1/mapbox/streets-v9/static/pin-s-a+9ed4bd(1.23456,37.75965)/1.23456,37.75965,10.00000,345.67890,0.00000/100x200?access_token=pk.");
  }

  @Test
  public void testAnnotationPathSegmentMultipleMarkers() throws ServicesException {

    ArrayList<Marker> markers = new ArrayList<>();
    markers.add(getMarker());
    markers.add(getMarker());

    MapboxStaticImage client = new MapboxStaticImage.Builder()
            .setAccessToken("pk.")
            .setStyleId(Constants.MAPBOX_STYLE_STREETS)
            .setMarker(markers)
            .setLocation(Position.fromCoordinates(1.23456789,37.75965))
            .setZoom(10).setBearing(345.67890123456789).setPitch(0.000000005)
            .setWidth(100).setHeight(200)
            .setPrecision(5)
            .build();
    HttpUrl url = client.getUrl();
    assertEquals(
            url.toString(),
            "https://api.mapbox.com/styles/v1/mapbox/streets-v9/static/pin-s-a+9ed4bd(1.23456,37.75965),pin-s-a+9ed4bd(1.23456,37.75965)/1.23456,37.75965,10.00000,345.67890,0.00000/100x200?access_token=pk.");
  }

  private Marker getMarker(){

    Marker marker = new Marker();
    marker.setName("pin-s");
    marker.setLat(37.75965);
    marker.setLon(1.23456789);
    marker.setPrecision(5);
    marker.setLabel("a");
    marker.setColor("9ed4bd");

    return marker;
  }

}
