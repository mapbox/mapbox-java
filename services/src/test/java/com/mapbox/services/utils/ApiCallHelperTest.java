package com.mapbox.services.utils;

import org.junit.Assert;
import org.junit.Test;

public class ApiCallHelperTest {

  @Test
  public void getHeaderUserAgent_formatsStringCorrectly() throws Exception {
    String userAgent = ApiCallHelper.getHeaderUserAgent("MapboxJava");
    Assert.assertTrue("MapboxJava/1.2.0 Mac OS X/10.11.5 (x86_64)".equals(userAgent));
  }
}
