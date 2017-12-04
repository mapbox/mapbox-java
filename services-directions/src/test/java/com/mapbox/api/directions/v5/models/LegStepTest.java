package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class LegStepTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    List<VoiceInstructions> voiceInstructions = new ArrayList<>();
    List<BannerInstructions> bannerInstructions = new ArrayList<>();

    LegStep legStep = LegStep.builder()
      .voiceInstructions(voiceInstructions)
      .bannerInstructions(bannerInstructions)
      .distance(0)
      .duration(0)
      .geometry("abc")
      .mode("driving")
      .drivingSide("right")
      .weight(1.0)
      .maneuver(Mockito.mock(StepManeuver.class))
      .build();
    assertNotNull(legStep);
  }
}
