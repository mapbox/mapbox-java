package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

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
}
