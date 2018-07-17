package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LegAnnotationTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    LegAnnotation annotation = LegAnnotation.builder()
      .congestion(new ArrayList<String>())
      .distance(new ArrayList<Double>())
      .duration(new ArrayList<Double>())
      .speed(new ArrayList<Double>())
      .build();
    assertNotNull(annotation);
  }

  @Test
  public void testSerializable() throws Exception {
    List<Double> distance = new ArrayList<>();
    distance.add(20d);
    distance.add(40d);
    distance.add(60d);
    LegAnnotation annotation = LegAnnotation.builder()
      .congestion(new ArrayList<String>())
      .distance(distance)
      .duration(new ArrayList<Double>())
      .speed(new ArrayList<Double>())
      .build();
    byte[] serialized = TestUtils.serialize(annotation);
    assertEquals(annotation, deserialize(serialized, LegAnnotation.class));
  }

  @Test
  public void testToFromJson1() {

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

    LegAnnotation annotation = LegAnnotation.builder()
      .congestion(new ArrayList<String>())
      .distance(distanceList)
      .duration(durationList)
      .speed(speedList)
      .congestion(congestionList)
      .build();


    String jsonString = annotation.toJson();
    LegAnnotation annotationFromJson = LegAnnotation.fromJson(jsonString);

    assertEquals(annotation, annotationFromJson);
  }
}
