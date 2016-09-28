package com.mapbox.services.commons;

import com.mapbox.services.commons.utils.MapboxUtils;

/**
 * Abstract class containing Mapbox specific methods.
 */
public abstract class MapboxBuilder {

  protected String clientAppName;

  public abstract MapboxBuilder setAccessToken(String accessToken);

  public abstract String getAccessToken();

  /**
   * Set the App Name to identify
   *
   * @param appName base package name or other simple string identifier
   * @param <T>     The concrete implementation of MapboxBuilder
   * @return the current MapboxBuilder instance
   */
  public abstract <T extends MapboxBuilder> T setClientAppName(String appName);

  public String getClientAppName() {
    return clientAppName;
  }

  /**
   * Method to validate a Mapbox Access token.
   *
   * @param accessToken A string containing a Mapbox Access Token
   * @throws ServicesException Generic Exception for all things Mapbox.
   * @since 1.0.0
   */
  protected void validateAccessToken(String accessToken) throws ServicesException {
    if (!MapboxUtils.isAccessTokenValid(accessToken)) {
      throw new ServicesException(
              "Using Mapbox Services requires setting a valid access token.");
    }
  }

  /**
   * The builder.
   *
   * @return {@link MapboxBuilder}
   * @throws ServicesException Generic Exception for all things Mapbox.
   * @since 1.0.0
   */
  public abstract Object build() throws ServicesException;

}
