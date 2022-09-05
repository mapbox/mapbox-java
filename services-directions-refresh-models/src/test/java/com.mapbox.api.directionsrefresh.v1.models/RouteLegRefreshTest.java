package com.mapbox.api.directionsrefresh.v1.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.Closure;
import com.mapbox.api.directions.v5.models.Congestion;
import com.mapbox.api.directions.v5.models.Incident;
import com.mapbox.api.directions.v5.models.LegAnnotation;
import com.mapbox.api.directions.v5.models.MaxSpeed;
import com.mapbox.api.directions.v5.models.SpeedLimit;
import com.mapbox.core.TestUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
      .incidents(
        Arrays.asList(
          Incident.builder()
            .id("incident_id")
            .build()
        )
      )
      .closures(
        Arrays.asList(
          Closure.builder()
            .geometryIndexStart(1)
            .geometryIndexEnd(2)
            .build()
          )
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
    List<Integer> trafficTendency = Arrays.asList(
      DirectionsCriteria.TRAFFIC_TENDENCY_UNKNOWN,
      DirectionsCriteria.TRAFFIC_TENDENCY_CONSTANT_CONGESTION,
      DirectionsCriteria.TRAFFIC_TENDENCY_INCREASING_CONGESTION,
      DirectionsCriteria.TRAFFIC_TENDENCY_DECREASING_CONGESTION,
      DirectionsCriteria.TRAFFIC_TENDENCY_RAPIDLY_INCREASING_CONGESTION,
      DirectionsCriteria.TRAFFIC_TENDENCY_RAPIDLY_DECREASING_CONGESTION
    );

    List<Incident> incidents = Arrays.asList(
      Incident.builder()
        .id("incident_1")
        .creationTime("creation_time")
        .closed(true)
        .congestion(
          Congestion.builder()
            .value(80)
            .build()
        )
        .alertcCodes(Arrays.asList(1, 2, 5))
        .countryCodeAlpha3("USA")
        .countryCodeAlpha2("US")
        .startTime("start_time")
        .endTime("end_time")
        .geometryIndexStart(4)
        .geometryIndexEnd(50)
        .impact(Incident.IMPACT_MAJOR)
        .numLanesBlocked(2)
        .description("somedescription")
        .type(Incident.INCIDENT_OTHER_NEWS)
        .subType("subType")
        .subTypeDescription("someSubTypeDescription")
        .lanesBlocked(Arrays.asList("lane_blocked_1"))
        .build(),
      Incident.builder()
        .id("incident_2")
        .type(Incident.INCIDENT_ACCIDENT)
        .build()
    );

    List<Closure> closures = Arrays.asList(
      Closure.builder()
        .geometryIndexStart(2)
        .geometryIndexEnd(4)
        .build(),
      Closure.builder()
        .geometryIndexStart(10)
        .geometryIndexEnd(20)
        .build()
    );

    RouteLegRefresh routeLegRefresh = RouteLegRefresh.builder()
      .annotation(
        LegAnnotation.builder()
          .congestion(congestions)
          .distance(distances)
          .duration(durations)
          .maxspeed(maxSpeeds)
          .speed(speeds)
          .trafficTendency(trafficTendency)
          .build()
      )
      .incidents(incidents)
      .closures(closures)
      .build();

    String json = routeLegRefresh.toJson();
    RouteLegRefresh fromJson = RouteLegRefresh.fromJson(json);

    assertNotNull(routeLegRefresh.annotation());
    assertNotNull(routeLegRefresh.incidents());
    assertNotNull(routeLegRefresh.closures());
    assertEquals(routeLegRefresh, fromJson);
  }

  @Test
  public void trafficTendencyFromJson() {
    String json = "{\"traffic_tendency\": [0,1,2,3,4,5]}";
    LegAnnotation expectedAnnotations = LegAnnotation.builder()
      .trafficTendency(Arrays.asList(
        DirectionsCriteria.TRAFFIC_TENDENCY_UNKNOWN,
        DirectionsCriteria.TRAFFIC_TENDENCY_CONSTANT_CONGESTION,
        DirectionsCriteria.TRAFFIC_TENDENCY_INCREASING_CONGESTION,
        DirectionsCriteria.TRAFFIC_TENDENCY_DECREASING_CONGESTION,
        DirectionsCriteria.TRAFFIC_TENDENCY_RAPIDLY_INCREASING_CONGESTION,
        DirectionsCriteria.TRAFFIC_TENDENCY_RAPIDLY_DECREASING_CONGESTION
      ))
      .build();

    LegAnnotation fromJson = LegAnnotation.fromJson(json);

    assertEquals(expectedAnnotations, fromJson);
  }
}
