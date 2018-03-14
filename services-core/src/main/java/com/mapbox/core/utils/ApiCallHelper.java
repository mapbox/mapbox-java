package com.mapbox.core.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.core.constants.Constants;

import java.util.Locale;

/**
 * Static class with methods for assisting in making Mapbox API calls.
 *
 * @since 3.0.0
 */
public final class ApiCallHelper {

  private static final String ONLY_PRINTABLE_CHARS = "[^\\p{ASCII}]";

  private ApiCallHelper() {
    // Private constructor preventing instances of class
  }

  /**
   * Computes a full user agent header of the form:
   * {@code MapboxJava/1.2.0 Mac OS X/10.11.5 (x86_64)}.
   *
   * @param clientAppName Application Name
   * @return {@link String} representing the header user agent
   * @since 1.0.0
   */
  public static String getHeaderUserAgent(@Nullable String clientAppName) {
    String osName = System.getProperty("os.name");
    String osVersion = System.getProperty("os.version");
    String osArch = System.getProperty("os.arch");

    if (TextUtils.isEmpty(osName) || TextUtils.isEmpty(osVersion) || TextUtils.isEmpty(osArch)) {
      return Constants.HEADER_USER_AGENT;
    } else {
      return getHeaderUserAgent(clientAppName, osName, osVersion, osArch);
    }
  }

  /**
   * Computes a full user agent header of the form:
   * {@code MapboxJava/1.2.0 Mac OS X/10.11.5 (x86_64)}.
   *
   * @param clientAppName Application Name
   * @param osName OS name
   * @param osVersion OS version
   * @param osArch OS Achitecture
   * @return {@link String} representing the header user agent
   * @since 1.0.0
   */
  public static String getHeaderUserAgent(@Nullable String clientAppName,
                                           @NonNull String osName, @NonNull String osVersion, @NonNull String osArch) {

      osName = osName.replaceAll(ONLY_PRINTABLE_CHARS, "");
      osVersion = osVersion.replaceAll(ONLY_PRINTABLE_CHARS, "");
      osArch = osArch.replaceAll(ONLY_PRINTABLE_CHARS, "");
      String baseUa = String.format(
        Locale.US, "%s %s/%s (%s)", Constants.HEADER_USER_AGENT, osName, osVersion, osArch);

      return TextUtils.isEmpty(clientAppName) ? baseUa : String.format(Locale.US, "%s %s",
        clientAppName, baseUa);
  }
}
