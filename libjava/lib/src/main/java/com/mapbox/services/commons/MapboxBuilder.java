package com.mapbox.services.commons;

import com.mapbox.services.commons.utils.MapboxUtils;

/**
 * Abstract class containing Mapbox specific methods.
 */
public abstract class MapboxBuilder {

    public abstract MapboxBuilder setAccessToken(String accessToken);

    public abstract String getAccessToken();

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
