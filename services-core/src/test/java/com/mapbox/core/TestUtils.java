package com.mapbox.core;

import com.google.gson.JsonParser;

import org.hamcrest.Matchers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestUtils {

  public static final double DELTA = 1E-10;
  public static final String ACCESS_TOKEN = "pk.XXX";

  public void compareJson(String expectedJson, String actualJson) {
    JsonParser parser = new JsonParser();
    assertThat(parser.parse(actualJson), Matchers.equalTo(parser.parse(expectedJson)));
  }

  public void checkEqual(double expected, double actual, double delta) {
    assertEquals(expected, actual, delta);
  }

  protected String loadJsonFixture(String filename) throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(filename);
    Scanner scanner = new Scanner(inputStream, UTF_8.name()).useDelimiter("\\A");
    return scanner.hasNext() ? scanner.next() : "";
  }

  public static <T extends Serializable> byte[] serialize(T obj)
    throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(obj);
    oos.close();
    return baos.toByteArray();
  }

  public static <T extends Serializable> T deserialize(byte[] bytes, Class<T> cl)
    throws IOException, ClassNotFoundException {
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    ObjectInputStream ois = new ObjectInputStream(bais);
    Object object = ois.readObject();
    return cl.cast(object);
  }

  /**
   * Comes from Google Utils Test Case
   */
  public static void expectNearNumber(double expected, double actual, double epsilon) {
    assertTrue(String.format("Expected %f to be near %f", actual, expected),
      Math.abs(expected - actual) <= epsilon);
  }
}
