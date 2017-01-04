package com.mapbox.services;

import com.mapbox.services.commons.MapboxService;

import org.junit.Test;

import static org.junit.Assert.*;

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
