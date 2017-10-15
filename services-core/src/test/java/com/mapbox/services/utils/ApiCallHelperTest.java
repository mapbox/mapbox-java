package com.mapbox.services.utils;

import com.mapbox.services.constants.Constants;
import org.junit.Assert;
import org.junit.Test;

public class ApiCallHelperTest {

  @Test
  public void getHeaderUserAgent_formatsStringCorrectly() throws Exception {
    Assert.assertTrue(ApiCallHelper.getHeaderUserAgent(
      null).startsWith(Constants.HEADER_USER_AGENT));
    Assert.assertTrue(ApiCallHelper.getHeaderUserAgent(
      "AppName").startsWith("AppName"));
  }
}
