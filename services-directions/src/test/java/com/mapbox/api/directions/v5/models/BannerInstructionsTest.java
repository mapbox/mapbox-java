package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.mapbox.core.TestUtils;
import org.junit.Test;
import org.mockito.Mockito;

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
}
