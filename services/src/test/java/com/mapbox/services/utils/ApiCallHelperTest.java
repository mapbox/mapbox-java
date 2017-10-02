package com.mapbox.services.utils;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ApiCallHelperTest {

  @Test
  @Ignore
  public void getHeaderUserAgent_formatsStringCorrectly() throws Exception {
    String userAgent = ApiCallHelper.getHeaderUserAgent("MapboxJava");
    System.out.println(userAgent);
    Assert.assertTrue("MapboxJava/1.2.0 Mac OS X/10.11.5 (x86_64)".equals(userAgent));
  }
}
