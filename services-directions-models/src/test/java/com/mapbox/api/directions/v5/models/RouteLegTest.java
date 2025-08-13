package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.api.directions.v5.DirectionsCriteria;
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

    List<Incident> incidents = new ArrayList<>();

    List<Closure> closures = new ArrayList<>();

    LegAnnotation annotation = LegAnnotation.builder()
      .congestion(new ArrayList<String>())
      .distance(distanceList)
      .duration(durationList)
      .speed(speedList)
      .congestion(congestionList)
      .build();

    List<Notification> notifications = Arrays.asList(
      Notification.builder()
        .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
        .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_MAX_HEIGHT)
        .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_STATIC)
        .geometryIndexStart(123)
        .geometryIndexEnd(145)
        .build()
    );

    RouteLeg routeLeg = RouteLeg.builder()
      .annotation(annotation)
      .distance(53.4)
      .duration(14.3)
      .steps(steps)
      .incidents(incidents)
      .closures(closures)
      .summary("")
      .notifications(notifications)
      .build();

    String jsonString = routeLeg.toJson();
    RouteLeg routeLegFromJson = RouteLeg.fromJson(jsonString);

    assertEquals(routeLeg, routeLegFromJson);
  }

  @Test
  public void testFromJson() {
    String routeLegJsonString = "{"
      + "\"distance\": 53.4,"
      + "\"duration\": 14.3,"
      + "\"summary\": \"route summary\","
      + "\"admins\": [{ \"iso_3166_1_alpha3\": \"USA\", \"iso_3166_1\": \"US\" }],"
      + "\"incidents\": [{ \"id\": \"15985415522454461962\" }],"
      + "\"closures\": [{ \"geometry_index_start\": 1,\"geometry_index_end\": 4}],"
      + "\"notifications\": [{ \"geometry_index_start\": 123,\"geometry_index_end\": 145, \"type\": \"violation\", \"refresh_type\": \"dynamic\", \"subtype\": \"maxHeight\"}]}";
    List<Admin> admins = new ArrayList<>();
    admins.add(Admin.builder().countryCode("US").countryCodeAlpha3("USA").build());

    List<Incident> incidents = new ArrayList<>();
    incidents.add(Incident.builder().id("15985415522454461962").build());

    List<Closure> closures = new ArrayList<>();
    closures.add(Closure.builder().geometryIndexStart(1).geometryIndexEnd(4).build());

    List<Notification> notifications = Arrays.asList(
      Notification.builder()
        .type(DirectionsCriteria.NOTIFICATION_TYPE_VIOLATION)
        .subtype(DirectionsCriteria.NOTIFICATION_SUBTYPE_MAX_HEIGHT)
        .refreshType(DirectionsCriteria.NOTIFICATION_REFRESH_TYPE_DYNAMIC)
        .geometryIndexStart(123)
        .geometryIndexEnd(145)
        .build()
    );

    RouteLeg routeLeg = RouteLeg.builder()
      .distance(53.4)
      .duration(14.3)
      .summary("route summary")
      .admins(admins)
      .incidents(incidents)
      .closures(closures)
      .notifications(notifications)
      .build();

    RouteLeg routeLegFromJson = RouteLeg.fromJson(routeLegJsonString);

    assertEquals(routeLeg, routeLegFromJson);
    assertEquals("US", routeLegFromJson.admins().get(0).countryCode());
    assertEquals("USA", routeLegFromJson.admins().get(0).countryCodeAlpha3());
  }
}
