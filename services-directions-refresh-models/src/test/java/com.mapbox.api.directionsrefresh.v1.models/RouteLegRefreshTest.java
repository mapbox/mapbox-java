package com.mapbox.api.directionsrefresh.v1.models;

import com.mapbox.api.directions.v5.models.LegAnnotation;
import com.mapbox.api.directions.v5.models.MaxSpeed;
import com.mapbox.api.directions.v5.models.SpeedLimit;
import com.mapbox.core.TestUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RouteLegRefreshTest extends TestUtils {

  @Test
  public void sanity() {
    RouteLegRefresh.builder()
      .annotation(
        LegAnnotation.builder()
          .congestion(Collections.<String>emptyList())
          .distance(Collections.<Double>emptyList())
          .maxspeed(Collections.<MaxSpeed>emptyList())
          .speed(Collections.<Double>emptyList())
          .duration(Collections.<Double>emptyList())
          .build()
      )
      .build();
  }

  @Test
  public void fromJson() {
    List<String> congestions = Arrays.asList(
      "congestion1",
      "congestion2",
      "congestion3"
    );
    List<MaxSpeed> maxSpeeds = Arrays.asList(
      MaxSpeed.builder().unknown(true).build(),
      MaxSpeed.builder().unit(SpeedLimit.KMPH).speed(20).build(),
      MaxSpeed.builder().unit(SpeedLimit.MPH).speed(45).build(),
      MaxSpeed.builder().none(true).build(),
      MaxSpeed.builder().none(false).build()
    );
    List<Double> distances = Arrays.asList(
      0.1,
      0.2,
      1.3,
      121212.0
    );
    List<Double> speeds = Arrays.asList(
      10.0,
      42.1,
      50.5,
      0.1
    );
    List<Double> durations = Arrays.asList(
      0.1,
      10.2
    );
    RouteLegRefresh routeLegRefresh = RouteLegRefresh.builder()
      .annotation(
        LegAnnotation.builder()
          .congestion(congestions)
          .distance(distances)
          .duration(durations)
          .maxspeed(maxSpeeds)
          .speed(speeds)
          .build()
      )
      .build();

    String json = routeLegRefresh.toJson();
    RouteLegRefresh fromJson = RouteLegRefresh.fromJson(json);

    assertNotNull(routeLegRefresh.annotation());
    assertEquals(routeLegRefresh, fromJson);
  }

}
