package com.mapbox.api.directions.v5.models;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LegAnnotationTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    LegAnnotation annotation = LegAnnotation.builder()
      .congestionNumeric(new ArrayList<Integer>())
      .congestion(new ArrayList<String>())
      .distance(new ArrayList<Double>())
      .duration(new ArrayList<Double>())
      .speed(new ArrayList<Double>())
      .unrecognizedJsonProperties(new HashMap<>())
      .build();
    assertNotNull(annotation);
  }

  @Test
  public void testSerializable() throws Exception {
    Map<String, JsonElement> unrecognizedProperties = new HashMap<>();
    unrecognizedProperties.put("aaa", new JsonPrimitive("bbb"));
    List<Double> distance = new ArrayList<>();
    distance.add(20d);
    distance.add(40d);
    distance.add(60d);
    LegAnnotation annotation = LegAnnotation.builder()
      .congestionNumeric(new ArrayList<Integer>())
      .congestion(new ArrayList<String>())
      .distance(distance)
      .duration(new ArrayList<Double>())
      .speed(new ArrayList<Double>())
      .unrecognizedJsonProperties(unrecognizedProperties)
      .build();
    byte[] serialized = TestUtils.serialize(annotation);
    assertEquals(annotation, deserialize(serialized, LegAnnotation.class));
  }

  @Test
  public void testToFromJson1() {
    Map<String, JsonElement> unrecognizedProperties = new HashMap<>();
    unrecognizedProperties.put("aaa", new JsonPrimitive("bbb"));

    List<Double> distanceList = Arrays.asList(
      4.294596842089401,
      5.051172053200946,
      5.533254065167979,
      6.576513793849532,
      7.4449640160938015,
      8.468757534990829,
      15.202780313562714,
      7.056346577326572);

    List<Double> durationList = Arrays.asList(
      1.0,
      1.2,
      2.0,
      1.6,
      1.8,
      2.0,
      3.6,
      1.7
    );

    List<Double> speedList = Arrays.asList(
      4.3,
      4.2,
      2.8,
      4.1,
      4.1,
      4.2,
      4.2,
      4.2);

    List<String> congestionList = Arrays.asList(
      "low",
      "moderate",
      "moderate",
      "moderate",
      "heavy",
      "heavy",
      "heavy",
      "heavy");

    List<Integer> congestionNumericList = Arrays.asList(
        0,
        4,
        4,
        4,
        null,
        0,
        0,
        0);

    List<Integer> freeflowSpeed = Arrays.asList(
      0,
      1,
      2,
      3,
      4,
      null,
      0,
      9
    );

    List<Integer> currentSpeed = Arrays.asList(
      0,
      4,
      2,
      7,
      4,
      null,
      0,
      2
    );

    LegAnnotation annotation = LegAnnotation.builder()
      .congestionNumeric(congestionNumericList)
      .distance(distanceList)
      .duration(durationList)
      .speed(speedList)
      .congestion(congestionList)
      .freeflowSpeed(freeflowSpeed)
      .currentSpeed(currentSpeed)
      .unrecognizedJsonProperties(unrecognizedProperties)
      .build();


    String jsonString = annotation.toJson();
    LegAnnotation annotationFromJson = LegAnnotation.fromJson(jsonString);

    assertEquals(annotation, annotationFromJson);
  }

  @Test
  public void testExampleResponse() throws IOException {
    String responseString = loadJsonFixture("directions_freeflow_speed_and_current_speed_annotations.json");
    DirectionsResponse response = DirectionsResponse.fromJson(responseString);

    List<Integer> freeflowSpeed = response.routes().get(0).legs().get(0).annotation().freeflowSpeed();
    assertEquals(341, freeflowSpeed.size());
    assertEquals((Integer) 11, freeflowSpeed.get(0));
    assertEquals(null, freeflowSpeed.get(24));
    assertEquals((Integer) 27, freeflowSpeed.get(113));
    assertEquals((Integer) 8, freeflowSpeed.get(340));

    List<Integer> currentSpeed = response.routes().get(0).legs().get(0).annotation().currentSpeed();
    assertEquals(341, currentSpeed.size());
    assertEquals(null, currentSpeed.get(0));
    assertEquals((Integer) 5, currentSpeed.get(33));
    assertEquals((Integer) 9, currentSpeed.get(317));
    assertEquals(null, currentSpeed.get(340));
  }

  protected String loadJsonFixture(String filename) throws IOException {
    InputStream inputStream = getResourceInputSteam(filename);
    Scanner scanner = new Scanner(inputStream, UTF_8.name()).useDelimiter("\\A");
    return scanner.hasNext() ? scanner.next() : "";
  }

  private InputStream getResourceInputSteam(String filename) {
    ClassLoader classLoader = getClass().getClassLoader();
    return classLoader.getResourceAsStream(filename);
  }
}
