package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BannerComponentTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    BannerComponents bannerComponents = BannerComponents.builder()
      .text("test")
      .type("text")
      .build();
    assertNotNull(bannerComponents);
  }

  @Test
  public void testSerializable() throws Exception {
    BannerComponents bannerComponents = BannerComponents.builder()
      .imageBaseUrl("www.test.com")
      .text("test")
      .type("icon")
      .build();
    byte[] serialized = TestUtils.serialize(bannerComponents);
    assertEquals(bannerComponents, deserialize(serialized, BannerComponents.class));
  }
}
