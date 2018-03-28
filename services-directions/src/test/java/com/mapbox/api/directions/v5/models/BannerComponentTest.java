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

  @Test
  public void compareTo_lessThan() {
    BannerComponents bannerComponents1 = BannerComponents.builder()
      .text("test")
      .type("text")
      .abbreviationPriority(2)
      .build();

    BannerComponents bannerComponents2 = BannerComponents.builder()
      .text("test")
      .type("text")
      .abbreviationPriority(3)
      .build();

    assertEquals(-1, bannerComponents1.compareTo(bannerComponents2));
  }

  @Test
  public void compareTo_greaterThan() {
    BannerComponents bannerComponents1 = BannerComponents.builder()
      .text("test")
      .type("text")
      .abbreviationPriority(2)
      .build();

    BannerComponents bannerComponents2 = BannerComponents.builder()
      .text("test")
      .type("text")
      .abbreviationPriority(3)
      .build();

    assertEquals(1, bannerComponents2.compareTo(bannerComponents1));
  }

  @Test
  public void compareTo_equals() {
    BannerComponents bannerComponents1 = BannerComponents.builder()
      .text("test")
      .type("text")
      .abbreviationPriority(3)
      .build();

    BannerComponents bannerComponents2 = BannerComponents.builder()
      .text("test")
      .type("text")
      .abbreviationPriority(3)
      .build();

    assertEquals(0, bannerComponents2.compareTo(bannerComponents1));
  }

  @Test
  public void compareTo_firstIsNull() {
    BannerComponents bannerComponents1 = BannerComponents.builder()
      .text("test")
      .type("text")
      .build();

    BannerComponents bannerComponents2 = BannerComponents.builder()
      .text("test")
      .type("text")
      .abbreviationPriority(3)
      .build();

    assertEquals(1, bannerComponents1.compareTo(bannerComponents2));
  }

  @Test
  public void compareTo_secondIsNull() {
    BannerComponents bannerComponents1 = BannerComponents.builder()
      .text("test")
      .type("text")
      .abbreviationPriority(3)
      .build();

    BannerComponents bannerComponents2 = BannerComponents.builder()
      .text("test")
      .type("text")
      .build();

    assertEquals(-1, bannerComponents1.compareTo(bannerComponents2));
  }

  @Test
  public void compareTo_bothAreNull() {
    BannerComponents bannerComponents1 = BannerComponents.builder()
      .text("test")
      .type("text")
      .build();

    BannerComponents bannerComponents2 = BannerComponents.builder()
      .text("test")
      .type("text")
      .build();

    assertEquals(0, bannerComponents1.compareTo(bannerComponents2));
  }
}
