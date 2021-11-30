package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;

import org.junit.Test;

public class ShieldSvgTest extends TestUtils {

  @Test
  public void sanity() {
    assertNotNull(getDefault());
  }

  @Test
  public void serializable() throws Exception {
    ShieldSvg shieldSVG = getDefault();
    byte[] serialized = TestUtils.serialize(shieldSVG);

    assertEquals(shieldSVG, deserialize(serialized, ShieldSvg.class));
  }

  @Test
  public void jsonComparingDefaultSVG() {
    ShieldSvg shieldSVG = getDefault();
    String json = shieldSVG.toJson();

    ShieldSvg fromJson = ShieldSvg.fromJson(json);

    assertEquals(shieldSVG, fromJson);
  }

  @Test
  public void jsonFromFixture() throws Exception {
    String json = loadJsonFixture("styles_svg.json");
    ShieldSvg svg = ShieldSvg.fromJson(json);

    String svgJson = svg.toJson();

    compareJson(json, svgJson);
  }

  private ShieldSvg getDefault() {
    String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" id=\"rectangle-yellow-2\" " +
        "width=\"60\" height=\"42\" viewBox=\"0 0 20 14\"><g>" +
        "<path d=\"M0,0 H20 V14 H0 Z\" fill=\"none\"/><path " +
        "d=\"M3,1 H17 C17,1 19,1 19,3 V11 C19,11 19,13 17,13 H3 C3,13 1,13 1,11 V3 C1,3 1,1 3,1\" " +
        "fill=\"none\" stroke=\"hsl(230, 18%, 13%)\" stroke-linejoin=\"round\" " +
        "stroke-miterlimit=\"4px\" stroke-width=\"2\"/><path " +
        "d=\"M3,1 H17 C17,1 19,1 19,3 V11 C19,11 19,13 17,13 H3 C3,13 1,13 1,11 V3 C1,3 1,1 3,1\" " +
        "fill=\"hsl(50, 100%, 70%)\"/><path d=\"M0,4 H20 V10 H0 Z\" fill=\"none\" " +
        "id=\"mapbox-text-placeholder\"/></g></svg>";

    return ShieldSvg.builder()
        .svg(svg)
        .build();
  }
}
