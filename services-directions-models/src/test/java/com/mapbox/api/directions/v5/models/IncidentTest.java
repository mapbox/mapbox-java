package com.mapbox.api.directions.v5.models;

import com.google.common.collect.Lists;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IncidentTest extends TestUtils {

  @Test
  public void sanity(){
    assertNotNull(getDefault());
  }

  @Test
  public void testSerializableObject() throws Exception {
    Incident incident = Incident.builder()
      .id("some_id")
      .alertcCodes(Lists.newArrayList(431, 2123, 934))
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
      .build();
    byte[] serialized = TestUtils.serialize(incident);
    assertEquals(incident, deserialize(serialized, Incident.class));
  }

  @Test
  public void testSerializableFromJson(){
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
      "\"lanes_blocked\": []," +
      "\"geometry_index_start\": 805," +
      "\"geometry_index_end\": 896" +
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
    assertEquals(fromJson.geometryIndexStart().longValue(), 805L);
    assertEquals(fromJson.geometryIndexEnd().longValue(), 896L);
  }

  private Incident getDefault() {
    return Incident.builder()
      .id("id")
      .build();
  }
}
