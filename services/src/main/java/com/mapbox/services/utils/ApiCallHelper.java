package com.mapbox.services.utils;

import com.mapbox.services.constants.Constants;

import java.util.Locale;

import static com.mapbox.services.utils.TextUtils.isEmpty;

public class ApiCallHelper {

  /**
   * Computes a full user agent header of the form:
   * {@code MapboxJava/1.2.0 Mac OS X/10.11.5 (x86_64)}.
   *
   * @param clientAppName Application Name
   * @return {@link String} representing the header user agent
   * @since 1.0.0
   */
  public static String getHeaderUserAgent(String clientAppName) {
    try {
      String osName = System.getProperty("os.name");
      String osVersion = System.getProperty("os.version");
      String osArch = System.getProperty("os.arch");

      if (isEmpty(osName) || isEmpty(osVersion) || isEmpty(osArch)) {
        return Constants.HEADER_USER_AGENT;
      } else {
        String baseUa = String.format(
          Locale.US, "%s %s/%s (%s)", Constants.HEADER_USER_AGENT, osName, osVersion, osArch);
        return isEmpty(clientAppName) ? baseUa : String.format(Locale.US, "%s %s", clientAppName, baseUa);
      }

    } catch (Exception exception) {
      return Constants.HEADER_USER_AGENT;
    }
  }
}
