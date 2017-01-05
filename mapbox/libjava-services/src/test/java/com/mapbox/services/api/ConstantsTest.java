package com.mapbox.services.api;

import com.mapbox.services.Constants;
import com.mapbox.services.api.MapboxService;

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
