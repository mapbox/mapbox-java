package com.mapbox.services.commons;

import com.mapbox.services.commons.utils.MapboxUtils;

/**
 * Created by antonio on 4/7/16.
 */
public abstract class MapboxBuilder {

    public abstract MapboxBuilder setAccessToken(String accessToken);
    public abstract String getAccessToken();

    protected void validateAccessToken(String accessToken) throws ServicesException {
        if (!MapboxUtils.isAccessTokenValid(accessToken)) {
            throw new ServicesException(
                    "Using Mapbox Services requires setting a valid access token.");
        }
    }

    public abstract Object build() throws ServicesException;

}
