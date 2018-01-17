package com.mapbox.core;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.hamcrest.Matchers;
import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

public class TestUtils extends Dispatcher {

  protected static final String FORWARD_GEOCODING = "geocoding.json";
  protected static final String FORWARD_VALID = "forward_valid.json";
  private static final String FORWARD_INVALID = "forward_invalid.json";
  private static final String FORWARD_VALID_ZH = "forward_valid_zh.json";

  public static final double DELTA = 1E-10;
  public static final String ACCESS_TOKEN = "pk.XXX";

  public MockWebServer server;
  public HttpUrl mockUrl;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.setDispatcher(this);
    server.start();
    mockUrl = server.url("");
  }


  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Override
  public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
    try {
      String response;
      if (request.getPath().contains("1600") && !request.getPath().contains("nw")) {
        response = loadJsonFixture(FORWARD_VALID);
      }else if (request.getPath().contains("nw")) {
        response = loadJsonFixture(FORWARD_GEOCODING);
      } else if (request.getPath().contains("sandy")) {
        response = loadJsonFixture(FORWARD_INVALID);
      } else {
        response = loadJsonFixture(FORWARD_VALID_ZH);
      }
      return new MockResponse().setBody(response);
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }
  }

  public void compareJson(String expectedJson, String actualJson) {
    JsonParser parser = new JsonParser();
    assertThat(parser.parse(actualJson), Matchers.equalTo(parser.parse(expectedJson)));
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
