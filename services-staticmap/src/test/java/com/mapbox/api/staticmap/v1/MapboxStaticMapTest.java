package com.mapbox.api.staticmap.v1;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.mapbox.api.staticmap.v1.models.StaticMarkerAnnotation;
import com.mapbox.api.staticmap.v1.models.StaticPolylineAnnotation;
import com.mapbox.core.TestUtils;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ColorUtils;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MapboxStaticMapTest extends TestUtils {

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
  public void build_throwsExceptionWhenStyleIdNotSet() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("You need to set a map style.");
    MapboxStaticMap.builder().accessToken(ACCESS_TOKEN).styleId("").build();
  }

  @Test
  public void accessToken_exceptionGetsThrownWhenMissing() throws Exception {
    thrown.expect(ServicesException.class);
    thrown.expectMessage("Using Mapbox Services requires setting a valid access token.");
    MapboxStaticMap.builder().build();
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
    LineString lineString
      = LineString.fromJson("{\"type\":\"LineString\",\"coordinates\":[[100,0],[101,1]]}");

    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .retina(true)
      .cameraAuto(true)
      .geoJson(lineString)
      .build();
    assertThat(staticMap.url().toString(),
      containsString("geojson(%7B%22type%22:%22LineString%22,%22coordinates%22:"
        + "[[100.0,0.0],[101.0,1.0]]%7D)"));
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
    assertTrue(staticMap.url().toString().contains("pin-m-a(-71.041500,42.366200)"));
  }

  @Test
  public void staticMarkerAnnotations_MultipleMarkersGetAddedToUrlCorrectly() throws Exception {
    List<StaticMarkerAnnotation> markers = new ArrayList<>();

    markers.add(StaticMarkerAnnotation.builder()
      .name(StaticMapCriteria.MEDIUM_PIN).lnglat(Point.fromLngLat(-71.0415, 42.3662))
      .label("a").build());

    markers.add(StaticMarkerAnnotation.builder()
      .name(StaticMapCriteria.MEDIUM_PIN).lnglat(Point.fromLngLat(-71.0842, 42.3943))
      .color(
        Color.ORANGE.getRed(),
        Color.ORANGE.getGreen(),
        Color.ORANGE.getBlue())
      .label("a").build());
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .retina(true)
      .cameraAuto(true)
      .staticMarkerAnnotations(markers)
      .build();
    assertTrue(staticMap.url().toString().contains("pin-m-a(-71.041500,42.366200),"
      + "pin-m-a+FFC800(-71.084200,42.394300)"));
  }

  @Test
  public void staticPolylineAnnotations_getsAddedToUrlCorrectly() throws Exception {
    List<StaticPolylineAnnotation> polylines = new ArrayList<>();

    polylines.add(StaticPolylineAnnotation.builder()
      .polyline("abcdefg")
      .fillColor(
        Color.BLUE.getRed(),
        Color.BLUE.getGreen(),
        Color.BLUE.getBlue())
      .fillOpacity(0.1f).build());

    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .retina(true)
      .cameraAuto(true)
      .staticPolylineAnnotations(polylines)
      .build();
    assertTrue(staticMap.url().toString().contains("/path+0000FF-0.1(abcdefg)/"));
  }

  @Test
  public void precision_doesAdjustCoordinateValues() throws Exception {
    MapboxStaticMap staticMap = MapboxStaticMap.builder()
      .accessToken(ACCESS_TOKEN)
      .retina(true)
      .cameraPoint(Point.fromLngLat(2.123456789, 2.123456789))
      .precision(4)
      .build();

    assertTrue(staticMap.url().toString().contains("/2.1234,2.1234,0.0000,0.0000,0.0000/"));
  }
}
