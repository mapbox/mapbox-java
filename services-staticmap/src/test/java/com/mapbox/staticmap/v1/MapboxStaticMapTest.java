package com.mapbox.staticmap.v1;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mapbox.geojson.Point;
import com.mapbox.services.BaseTest;
import com.mapbox.services.exceptions.ServicesException;
import com.mapbox.services.utils.TextUtils;
import com.mapbox.staticmap.v1.models.StaticMarkerAnnotation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MapboxStaticMapTest extends BaseTest {

  private static final String ACCESS_TOKEN = "pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .build();
    assertNotNull(staticMap);
    assertTrue(!TextUtils.isEmpty(staticMap.url().toString()));
  }

  @Test
  public void accessToken_exceptionGetsThrownWhenMissing() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Using Mapbox Services requires setting a valid access token.");
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .build();
    assertNotNull(staticMap);
  }

  @Test
  public void baseUrl_settingShowsUpInTheRequestUrl() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .baseUrl("https://foobar.com")
      .build();
    assertTrue(staticMap.url().toString().startsWith("https://foobar.com/styles/v1/mapbox/"));
  }

  @Test
  public void user_getsSetInAppropriatePlace() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .user("foobar")
      .build();
    assertTrue(staticMap.url().toString().contains("/styles/v1/foobar/"));
  }

  @Test
  public void styleId_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .styleId(StaticMapCriteria.DARK_STYLE)
      .build();
    assertTrue(staticMap.url().toString().contains("styles/v1/mapbox/dark-v9/static/"));
  }

  @Test
  public void logo_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .logo(false)
      .build();
    assertTrue(staticMap.url().toString().contains("&logo=false"));
  }

  @Test
  public void attribution_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .attribution(false)
      .build();
    assertTrue(staticMap.url().toString().contains("&attribution=false"));
  }

  @Test
  public void retina_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .retina(true)
      .build();
    assertTrue(staticMap.url().toString().contains("250x250@2x"));
  }

  @Test
  public void cameraPoint_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .cameraPoint(Point.fromLngLat(-71.0614, 42.3548))
      .cameraZoom(14)
      .build();
    assertTrue(staticMap.url().toString().contains("-71.061400,42.354800"));
  }

  @Test
  public void cameraZoom_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .cameraPoint(Point.fromLngLat(-71.0614, 42.3548))
      .cameraZoom(12)
      .build();
    assertTrue(staticMap.url().toString().contains("12.000000"));
  }

  @Test
  public void cameraBearing_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .cameraPoint(Point.fromLngLat(-71.0614, 42.3548))
      .cameraZoom(12)
      .cameraBearing(45)
      .build();
    assertTrue(staticMap.url().toString().contains("45.000000"));
  }

  @Test
  public void cameraPitch_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .cameraPoint(Point.fromLngLat(-71.0614, 42.3548))
      .cameraZoom(12)
      .cameraBearing(45)
      .cameraPitch(30)
      .build();
    assertTrue(staticMap.url().toString().contains("30.000000"));
  }

  @Test
  public void cameraAuto_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .cameraAuto(true)
      .build();
    assertTrue(staticMap.url().toString().contains("/auto/250x250"));
  }

  @Test
  public void beforeLayer_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .beforeLayer("airport-label")
      .build();
    assertTrue(staticMap.url().toString().contains("&before_layer=airport-label"));

  }

  @Test
  public void width_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .width(1000)
      .build();
    assertTrue(staticMap.url().toString().contains("/1000x250?"));
  }

  @Test
  public void height_getsAddedToUrlCorrectly() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .height(500)
      .build();
    assertTrue(staticMap.url().toString().contains("/250x500?"));
  }

  @Test
  public void geoJson_getsAddedToUrlCorrectly() throws Exception {

  }

  @Test
  public void staticMarkerAnnotations_getsAddedToUrlCorrectly() throws Exception {
    StaticMarkerAnnotation marker = StaticMarkerAnnotation.builder()
      .name(StaticMapCriteria.MEDIUM_PIN).lnglat(Point.fromLngLat(-71.0415, 42.3662))
      .label("a").build();
    List<StaticMarkerAnnotation> markers = new ArrayList<>();
    markers.add(marker);

    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .retina(true)
      .cameraAuto(true)
      .staticMarkerAnnotations(markers)
      .build();
    System.out.println(staticMap.url());
  }

  @Test
  public void staticMarkerAnnotations_MultipleMarkersGetAddedToUrlCorrectly() throws Exception {
    List<StaticMarkerAnnotation> markers = new ArrayList<>();

    markers.add(StaticMarkerAnnotation.builder()
      .name(StaticMapCriteria.MEDIUM_PIN).lnglat(Point.fromLngLat(-71.0415, 42.3662))
      .label("a").build());

    markers.add(StaticMarkerAnnotation.builder()
      .name(StaticMapCriteria.MEDIUM_PIN).lnglat(Point.fromLngLat(-71.0842, 42.3943))
      .color(Color.ORANGE).label("a").build());


    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .retina(true)
      .cameraAuto(true)
      .staticMarkerAnnotations(markers)
      .build();
    System.out.println(staticMap.url());
  }

  @Test
  public void staticPolylineAnnotations_getsAddedToUrlCorrectly() throws Exception {

  }

  @Test
  public void precision_doesAdjustCoordinateValues() throws Exception {

  }
}
