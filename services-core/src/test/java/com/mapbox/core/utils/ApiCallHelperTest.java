package com.mapbox.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.mapbox.core.constants.Constants;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class ApiCallHelperTest {

  @Test
  public void getHeaderUserAgent_formatsStringCorrectly() throws Exception {
    assertTrue(ApiCallHelper.getHeaderUserAgent(
      null).startsWith(Constants.HEADER_USER_AGENT));
    assertTrue(ApiCallHelper.getHeaderUserAgent(
      "AppName").startsWith("AppName"));
  }

  @Test
  public void getHeaderUserAgent_nonAsciiCharsRemoved() {

    String osName = "os.name";
    String osVersion = "os.version";
    String osArch = "os.arch";

    String userAgent = String.format(
      Locale.US, "%s %s/%s (%s)", Constants.HEADER_USER_AGENT, osName, osVersion, osArch);

    String userAgentWithExtraChars = ApiCallHelper.getHeaderUserAgent(null,
      osName + '\u00A9', // copyright
      osVersion + '\u00AE', // Registered Sign
      osArch + '\u2122'); // TM

    Assert.assertEquals(userAgent, userAgentWithExtraChars);
  }
}
