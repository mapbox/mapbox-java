package com.mapbox.turf;

import com.google.gson.JsonParser;

import org.hamcrest.Matchers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestUtils {

  public static final double DELTA = 1E-10;
  public static final String ACCESS_TOKEN = "pk.XXX";

  public void compareJson(String expectedJson, String actualJson) {
    JsonParser parser = new JsonParser();
    assertThat(parser.parse(actualJson), Matchers.equalTo(parser.parse(expectedJson)));
  }

  protected String loadJsonFixture(String filename) {
    try {
      String filepath = "src/test/resources/" + filename;
      byte[] encoded = Files.readAllBytes(Paths.get(filepath));
      return new String(encoded, UTF_8);
    } catch (IOException e) {
      fail("Unable to load " + filename);
      return "";
    }
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

  protected List<String> getResourceFolderFileNames (String folder) {
    ClassLoader loader = getClass().getClassLoader();
    URL url = loader.getResource(folder);
    String path = url.getPath();
    List<String> names = new ArrayList<>();
    for (File file : new File(path).listFiles()) {
      names.add(file.getName());
    }
    return names;
  }
}
