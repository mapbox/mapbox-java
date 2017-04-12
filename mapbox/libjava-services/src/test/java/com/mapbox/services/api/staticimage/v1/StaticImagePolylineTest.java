package com.mapbox.services.api.staticimage.v1;

import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.staticimage.v1.models.Polyline;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertTrue;

public class StaticImagePolylineTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSanity() throws ServicesException {
    Polyline polyline = new Polyline.Builder()
      .setPolyline("%7DrpeFxbnjVsFwdAvr@cHgFor@jEmAlFmEMwM_FuItCkOi@wc@bg@wBSgM")
      .build();

    assertTrue(polyline.getPath().contains("path(%7DrpeFxbnjVsFwdAvr@cHgFor@jEmAlFmEMwM_FuItCkOi@wc@bg@wBSgM)"));
  }

  @Test
  public void requirePolyline() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith(
      "Creating a path overlay requires a valid polyline string."));

    new Polyline.Builder().setFillColor("000").build();
  }

  @Test
  public void singlePathInUrl() {
    List<Position> coords = new ArrayList<>();
    coords.add(Position.fromCoordinates(1.0, 2.0));
    coords.add(Position.fromCoordinates(5.0, 6.0));

    Polyline polyline = new Polyline.Builder()
      .setPolyline(PolylineUtils.encode(coords, Constants.PRECISION_5))
      .build();

    MapboxStaticImage image = new MapboxStaticImage.Builder()
      .setAccessToken("pk.")
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setPolylines(polyline)
      .setAuto(true)
      .setWidth(100).setHeight(200)
      .build();

    assertTrue(image.getUrl().toString().contains("path(_seK_ibE_glW_glW)"));
  }
}
