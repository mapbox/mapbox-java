package com.mapbox.api.directions.v5.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

public class BannerTextTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    BannerText bannerText = BannerText.builder().text("test").build();
    assertNotNull(bannerText);
  }

  @Test
  public void testSerializable() throws Exception {
    BannerText bannerText = BannerText.builder().text("test").build();
    byte[] serialized = TestUtils.serialize(bannerText);
    assertEquals(bannerText, deserialize(serialized, BannerText.class));
  }
}
