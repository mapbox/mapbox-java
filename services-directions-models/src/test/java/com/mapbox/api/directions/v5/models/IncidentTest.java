package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IncidentTest extends TestUtils {

  @Test
  public void sanity() {
    assertNotNull(buildDefaultIncident());
  }

  @Test
  public void testSerializable() throws Exception {
    Incident incident = buildDefaultIncident();
    byte[] serialized = TestUtils.serialize(incident);
    assertEquals(incident, deserialize(serialized, Incident.class));
  }

  @Test
  public void testFromJson() {
    String incidentJsonString = "{"
        + "\"incident_type\": \"road_closure\","
        + "\"start_time\": \"2020-08-12T14:18:20Z\","
        + "\"end_time\": \"2020-08-12T14:46:40Z\","
        + "\"creation_time\": \"2020-08-12T14:16:40Z\","
        + "\"id\": 5"
        + "}";
    Incident incident = Incident.fromJson(incidentJsonString);

    Assert.assertEquals("road_closure", incident.incidentType());
    Assert.assertEquals("2020-08-12T14:16:40Z", incident.creationTime());
    Assert.assertEquals("2020-08-12T14:18:20Z", incident.startTime());
    Assert.assertEquals("2020-08-12T14:46:40Z", incident.endTime());
    Assert.assertEquals(5, incident.id(), 0);

    String jsonStr = incident.toJson();

    compareJson(incidentJsonString, jsonStr);
  }

  @Test
  public void testToFromJson() {
    Incident incident = buildDefaultIncident();

    String jsonString = incident.toJson();
    Incident incidentFromJson = Incident.fromJson(jsonString);

    Assert.assertEquals(incident, incidentFromJson);
  }

  private Incident buildDefaultIncident() {
    return Incident.builder()
        .startTime(DEFAULT_START_TIME)
        .endTime(DEFAULT_END_TIME)
        .creationTime(DEFAULT_CREATION_TIME)
        .incidentType(DEFAULT_TYPE)
        .id(DEFAULT_ID)
        .build();
  }

  private static final String DEFAULT_START_TIME = "2020-08-12T14:20:40Z";
  private static final String DEFAULT_END_TIME = "2020-08-12T14:15:40Z";
  private static final String DEFAULT_CREATION_TIME = "2020-08-12T14:10:40Z";
  private static final String DEFAULT_TYPE = "test";
  private static final Integer DEFAULT_ID = 111;
}
