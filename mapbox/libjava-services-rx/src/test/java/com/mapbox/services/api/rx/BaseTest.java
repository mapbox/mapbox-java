package com.mapbox.services.api.rx;

import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class BaseTest {

  protected static final double DELTA = 1E-10;

  public void compareJson(String json1, String json2) {
    JsonParser parser = new JsonParser();
    assertEquals(parser.parse(json1), parser.parse(json2));
  }

  protected String loadJsonFixture(String filename) throws IOException {
    byte[] content = Files.readAllBytes(Paths.get(filename));
    return new String(content, Charset.forName("utf-8"));
  }

  protected String loadJsonFixture(String folder, String filename) throws IOException {
    byte[] content = Files.readAllBytes(Paths.get("src/test/fixtures/" + folder + "/" + filename));
    return new String(content, Charset.forName("utf-8"));
  }

}
