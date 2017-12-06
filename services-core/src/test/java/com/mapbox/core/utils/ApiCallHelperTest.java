package com.mapbox.core.utils;

import static org.junit.Assert.assertTrue;

import com.mapbox.core.constants.Constants;
import org.junit.Test;

public class ApiCallHelperTest {

  @Test
  public void getHeaderUserAgent_formatsStringCorrectly() throws Exception {
    assertTrue(ApiCallHelper.getHeaderUserAgent(
      null).startsWith(Constants.HEADER_USER_AGENT));
    assertTrue(ApiCallHelper.getHeaderUserAgent(
      "AppName").startsWith("AppName"));
  }
}
