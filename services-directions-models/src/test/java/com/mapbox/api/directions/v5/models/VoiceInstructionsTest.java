package com.mapbox.api.directions.v5.models;

import com.mapbox.geojson.TestUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VoiceInstructionsTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    VoiceInstructions voiceInstructions = VoiceInstructions.builder()
      .announcement("announcement")
      .distanceAlongGeometry(200d)
      .ssmlAnnouncement("ssmlAnnouncement").build();
    assertNotNull(voiceInstructions);
  }

  @Test
  public void testSerializable() throws Exception {
    VoiceInstructions voiceInstructions = VoiceInstructions.builder()
      .announcement("announcement")
      .distanceAlongGeometry(200d)
      .ssmlAnnouncement("ssmlAnnouncement").build();
    byte[] serialized = TestUtils.serialize(voiceInstructions);
    assertEquals(voiceInstructions, deserialize(serialized, VoiceInstructions.class));
  }

  @Test
  public void testToFromJson() {
    String ssmlAnnouncement =
      "<speak><amazon:effect name=\\\"drc\\\"><prosody rate=\\\"1.08\\\">In a quarter mile, "
        + "take the ramp on the right towards <say-as interpret-as=\\\"address\\\">I-495</say-as>: "
        + "Baltimore</prosody></amazon:effect></speak>";

    VoiceInstructions voiceInstructions = VoiceInstructions.builder()
      .distanceAlongGeometry(375.7)
      .announcement("In a quarter mile, take the ramp on the right towards I 495: Baltimore")
      .ssmlAnnouncement(ssmlAnnouncement)
      .build();

    String jsonString = voiceInstructions.toJson();
    VoiceInstructions voiceInstructionsFromJson = VoiceInstructions.fromJson(jsonString);

    assertEquals(voiceInstructions, voiceInstructionsFromJson);
  }
}
