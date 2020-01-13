package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteLegTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    RouteLeg routeLeg = RouteLeg.builder().distance(100d).duration(200d).build();
    assertNotNull(routeLeg);
  }

  @Test
  public void testSerializable() throws Exception {
    RouteLeg routeLeg = RouteLeg.builder().distance(100d).duration(200d).build();
    byte[] serialized = TestUtils.serialize(routeLeg);
    assertEquals(routeLeg, deserialize(serialized, RouteLeg.class));
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

    List<LegStep> steps = new ArrayList<>();

    LegAnnotation annotation = LegAnnotation.builder()
      .congestion(new ArrayList<String>())
      .distance(distanceList)
      .duration(durationList)
      .speed(speedList)
      .congestion(congestionList)
      .build();

    RouteLeg routeLeg = RouteLeg.builder()
      .annotation(annotation)
      .distance(53.4)
      //.weight(14.3)
      .duration(14.3)
      .steps(steps)
      .summary("")
      .build();

    String jsonString = routeLeg.toJson();
    RouteLeg routeLegFromJson = RouteLeg.fromJson(jsonString);

    assertEquals(routeLeg, routeLegFromJson);
  }
}
