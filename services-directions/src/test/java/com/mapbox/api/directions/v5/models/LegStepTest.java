package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

  @Test
  public void testToFromJson3() {

    List<VoiceInstructions> voiceInstructions = Arrays.asList();
    List<BannerInstructions> bannerInstructions = Arrays.asList();

    List<StepIntersection> intersections =
      Arrays.asList(StepIntersection.builder()
      .in(0)
      .out(1)
      .entry(Arrays.asList(false, true, true))
      .bearings(Arrays.asList(120, 210, 300))
      .rawLocation(new double[]{13.424671, 52.508812})
      .build());

    legStep = LegStep.builder()
      .distance(236.9)
      .duration(59.1)
      .geometry("asn_Ie_}pAdKxG")
      .mode("driving")
      .drivingSide("right")
      .name("Adalbertstraße")
      .weight(59.1)
      .maneuver(StepManeuver.builder()
        .rawLocation(new double[] {13.424671, 52.508812})
        .bearingAfter(202.0)
        .bearingBefore(299.0)
        .type("turn")
        .modifier("left")
        .instruction("Turn left onto Adalbertstraße")
        .build())
      .intersections(intersections)
      .voiceInstructions(voiceInstructions)
      .bannerInstructions(bannerInstructions)
      .rotaryName("rotary name test")
      .destinations("destination test")
      .exits("exits test")
      .pronunciation("pronunciation test")
      .ref("ref test")
      .rotaryPronunciation("rotary pronunciation test")
      .build();

    String jsonString = legStep.toJson();
    LegStep legStepFromJson = LegStep.fromJson(jsonString);

    assertEquals(legStep, legStepFromJson);
  }
}
