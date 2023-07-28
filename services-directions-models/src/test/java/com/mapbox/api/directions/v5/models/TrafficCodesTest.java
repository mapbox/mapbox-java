package com.mapbox.api.directions.v5.models;

import com.google.gson.JsonPrimitive;
import com.mapbox.core.TestUtils;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TrafficCodesTest extends TestUtils {

  @Test
  public void sanity() {
    assertNotNull(getDefault());
  }

  @Test
  public void serialize() throws Exception {
    TrafficCodes trafficCodes = getFilledTrafficCodes();
    byte[] serialized = TestUtils.serialize(trafficCodes);
    assertEquals(trafficCodes, deserialize(serialized, TrafficCodes.class));
  }

  @Test
  public void toAndFromJson() {
    TrafficCodes source = getFilledTrafficCodes();

    String json = source.toJson();
    TrafficCodes serialized = TrafficCodes.fromJson(json);

    assertEquals(source, serialized);
  }

  @Test
  public void deserializeFromJson() {
    String json = "{" +
      "\"jartic_cause_code\": 400," +
      "\"jartic_regulation_code\": 600," +
      "\"unknown_code\": 700" +
      "}";

    TrafficCodes fromJson = TrafficCodes.fromJson(json);

    assertNotNull(fromJson);
    assertEquals(400, (int) fromJson.jarticCauseCode());
    assertEquals(600, (int) fromJson.jarticRegulationCode());
    assertEquals(700, fromJson.getUnrecognizedJsonProperties().get("unknown_code").getAsInt());
  }

  @Test
  public void deserializeFromEmptyJson() {
    TrafficCodes fromJson = TrafficCodes.fromJson("{}");

    assertNotNull(fromJson);
    assertNull(fromJson.jarticCauseCode());
    assertNull(fromJson.jarticRegulationCode());
    assertNull(fromJson.getUnrecognizedJsonProperties());
  }

  private TrafficCodes getDefault() {
    return TrafficCodes.builder().build();
  }

  private TrafficCodes getFilledTrafficCodes() {
    return TrafficCodes.builder()
      .jarticCauseCode(400)
      .jarticRegulationCode(600)
      .unrecognizedJsonProperties(
        Collections.singletonMap("unknown_code", new JsonPrimitive((700)))
      )
      .build();
  }
}
