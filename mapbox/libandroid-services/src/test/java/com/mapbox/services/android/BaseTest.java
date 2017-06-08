package com.mapbox.services.android;

import com.google.gson.JsonParser;

import static org.junit.Assert.assertEquals;

public class BaseTest {

  protected static final double DELTA = 1E-10;

  public void compareJson(String json1, String json2) {
    JsonParser parser = new JsonParser();
    assertEquals(parser.parse(json1), parser.parse(json2));
  }
}
