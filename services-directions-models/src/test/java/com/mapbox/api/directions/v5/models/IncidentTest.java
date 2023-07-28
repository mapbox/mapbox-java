package com.mapbox.api.directions.v5.models;

import com.google.common.collect.Lists;
import com.google.gson.JsonPrimitive;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class IncidentTest extends TestUtils {

  @Test
  public void sanity(){
    assertNotNull(getDefault());
  }

  @Test
  public void serialize() throws Exception {
    Incident incident = getFilledIncident();
    byte[] serialized = TestUtils.serialize(incident);
    assertEquals(incident, deserialize(serialized, Incident.class));
  }

  @Test
  public void toAndFromJson() {
    Incident source = getFilledIncident();

    String json = source.toJson();
    Incident serialized = Incident.fromJson(json);

    assertEquals(source, serialized);
  }

  @Test
  public void deserializeFromJson(){
    String json = "{" +
      "\"id\": \"15985415522454461962\"," +
      "\"type\": \"construction\"," +
      "\"description\": \"Zwischen Eching und Oberpfaffenhofen - Bauarbeiten auf Abschnitt. " +
      "Rechter Fahrstreifen gesperrt.\"," +
      "\"long_description\": \"Zwischen Eching und Oberpfaffenhofen - Bauarbeiten auf Abschnitt. " +
      "Rechter Fahrstreifen gesperrt.\"," +
      "\"creation_time\": \"2020-10-08T11:34:14Z\"," +
      "\"start_time\": \"2020-10-06T12:52:02Z\"," +
      "\"end_time\": \"2020-11-27T16:00:00Z\"," +
      "\"impact\": \"minor\"," +
      "\"sub_type\": \"CONSTRUCTION\"," +
      "\"sub_type_description\": \"construction\"," +
      "\"alertc_codes\": [" +
      "501," +
      "803" +
      "]," +
      "\"traffic_codes\": {" +
      "\"jartic_cause_code\": 400," +
      "\"jartic_regulation_code\": 600," +
      "\"unknown_code\": 700" +
      "}," +
      "\"lanes_blocked\": []," +
      "\"num_lanes_blocked\": null," +
      "\"iso_3166_1_alpha2\": US," +
      "\"iso_3166_1_alpha3\": USA," +
      "\"geometry_index_start\": 805," +
      "\"geometry_index_end\": 896," +
      "\"affected_road_names\": [\"S6/E 28/Obwodnica Trójmiasta\"]" +
      "}";

    Incident fromJson = Incident.fromJson(json);

    assertNotNull(fromJson);
    assertEquals(fromJson.id(), "15985415522454461962");
    assertEquals(fromJson.type(), "construction");
    assertEquals(fromJson.description(), "Zwischen Eching und Oberpfaffenhofen - Bauarbeiten " +
      "auf Abschnitt. Rechter Fahrstreifen gesperrt.");
    assertEquals(fromJson.longDescription(), "Zwischen Eching und Oberpfaffenhofen - " +
      "Bauarbeiten auf Abschnitt. Rechter Fahrstreifen gesperrt.");
    assertEquals(fromJson.creationTime(), "2020-10-08T11:34:14Z");
    assertEquals(fromJson.startTime(), "2020-10-06T12:52:02Z");
    assertEquals(fromJson.impact(), "minor");
    assertEquals(fromJson.subType(), "CONSTRUCTION");
    assertEquals(fromJson.subTypeDescription(), "construction");
    assertEquals(fromJson.alertcCodes().size(), 2);
    assertEquals(400, (int) fromJson.trafficCodes().jarticCauseCode());
    assertEquals(600, (int) fromJson.trafficCodes().jarticRegulationCode());
    assertEquals(700, fromJson.trafficCodes().getUnrecognizedJsonProperties().get("unknown_code").getAsInt());
    assertEquals(fromJson.geometryIndexStart().longValue(), 805L);
    assertEquals(fromJson.geometryIndexEnd().longValue(), 896L);
    assertEquals(fromJson.countryCodeAlpha2(), "US");
    assertEquals(fromJson.countryCodeAlpha3(), "USA");
    assertEquals(fromJson.lanesBlocked().size(), 0);
    assertEquals(Lists.newArrayList("S6/E 28/Obwodnica Trójmiasta"), fromJson.affectedRoadNames());
    assertNull(fromJson.numLanesBlocked());
  }

  private Incident getDefault() {
    return Incident.builder()
      .id("id")
      .build();
  }

  private Incident getFilledIncident() {
    return Incident.builder()
      .id("some_id")
      .alertcCodes(Lists.newArrayList(431, 2123, 934))
      .trafficCodes(
        TrafficCodes.builder()
          .jarticCauseCode(400)
          .jarticRegulationCode(600)
          .unrecognizedJsonProperties(
            Collections.singletonMap("unknown_code", new JsonPrimitive((700)))
          )
          .build()
      )
      .closed(true)
      .congestion(Congestion.builder().value(10).build())
      .creationTime("2020-11-18T11:34:14Z")
      .startTime("2021-11-18T11:32:14Z")
      .endTime("2021-11-18T12:12:08Z")
      .description("description")
      .longDescription("long description")
      .geometryIndexStart(1)
      .geometryIndexEnd(943)
      .impact(Incident.IMPACT_MAJOR)
      .subType("sub type")
      .subTypeDescription("sub type desc")
      .type(Incident.INCIDENT_DISABLED_VEHICLE)
      .lanesBlocked(Lists.<String>newArrayList())
      .numLanesBlocked(null)
      .countryCodeAlpha2("US")
      .countryCodeAlpha3("USA")
      .affectedRoadNames(Lists.newArrayList("test1", "test2"))
      .build();
  }
}
