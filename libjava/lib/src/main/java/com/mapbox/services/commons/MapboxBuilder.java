package com.mapbox.services.commons;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by antonio on 4/7/16.
 */
public abstract class MapboxBuilder {

    public abstract MapboxBuilder setAccessToken(String accessToken);
    public abstract String getAccessToken();

    protected void validateAccessToken(String accessToken) throws ServicesException {
        if (StringUtils.isEmpty(accessToken) ||
            (!accessToken.startsWith("pk.") && !accessToken.startsWith("sk."))) {
            throw new ServicesException(
                    "Using Mapbox Services requires setting a valid access token.");
        }
    }

    public abstract MapboxService build() throws ServicesException;

}
