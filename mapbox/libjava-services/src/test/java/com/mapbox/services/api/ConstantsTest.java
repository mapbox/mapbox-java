package com.mapbox.services.api;

import com.mapbox.services.Constants;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConstantsTest {

  @Test
  public void testApiSecure() {
    assertTrue(Constants.BASE_API_URL.startsWith("https"));
  }

  @Test
  public void testUserAgent() {
    assertTrue(MapboxService.getHeaderUserAgent(null).startsWith(Constants.HEADER_USER_AGENT));
    assertTrue(MapboxService.getHeaderUserAgent("AppName").startsWith("AppName"));
  }
}
