package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LegStepTest extends TestUtils {

  private LegStep legStep;

  @Before
  public void setUp() throws Exception {
    List<VoiceInstructions> voiceInstructions = new ArrayList<>();
    List<BannerInstructions> bannerInstructions = new ArrayList<>();

    legStep = LegStep.builder()
      .voiceInstructions(voiceInstructions)
      .bannerInstructions(bannerInstructions)
      .distance(0)
      .duration(0)
      .geometry("abc")
      .mode("driving")
      .drivingSide("right")
      .weight(1.0)
      .maneuver(StepManeuver.builder().rawLocation(new double[] {1.0, 2.0}).build())
      .build();
  }

  @Test
  public void sanity() throws Exception {
    assertNotNull(legStep);
  }

  @Test
  public void testSerializable() throws Exception {
    byte[] serialized = TestUtils.serialize(legStep);
    assertEquals(legStep, deserialize(serialized, LegStep.class));
  }
}
