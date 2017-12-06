package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
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
}
