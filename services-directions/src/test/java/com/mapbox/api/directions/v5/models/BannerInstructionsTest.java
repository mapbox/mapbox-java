package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.mapbox.core.TestUtils;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

public class BannerInstructionsTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    BannerInstructions bannerInstructions = BannerInstructions.builder()
      .distanceAlongGeometry(100d)
      .primary(mock(BannerText.class))
      .secondary(mock(BannerText.class))
      .build();
    assertNotNull(bannerInstructions);
  }

  @Test
  public void testSerializable() throws Exception {
    BannerInstructions bannerInstructions = BannerInstructions.builder()
      .distanceAlongGeometry(100d)
      .primary(BannerText.builder().build())
      .secondary(BannerText.builder().build())
      .build();
    byte[] serialized = TestUtils.serialize(bannerInstructions);
    assertEquals(bannerInstructions, deserialize(serialized, BannerInstructions.class));
  }

  @Test
  public void testToFromJson() {

    List<BannerComponents> components =
      Arrays.asList(BannerComponents.builder()
        .type("text")
        .text("You have arrived")
        .build());

    BannerInstructions bannerInstructions = BannerInstructions.builder()
      .distanceAlongGeometry(79.3)
      .primary(
        BannerText.builder()
          .text("You have arrived")
          .type("arrive")
          .modifier("straight")
          .components(components)
          .build())
      .build();

    String jsonString = bannerInstructions.toJson();
    BannerInstructions bannerInstructionsFromJson = BannerInstructions.fromJson(jsonString);

    Assert.assertEquals(bannerInstructions, bannerInstructionsFromJson);
  }
}
