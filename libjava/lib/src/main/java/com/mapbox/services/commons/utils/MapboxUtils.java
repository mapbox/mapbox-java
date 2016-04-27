package com.mapbox.services.commons.utils;

/**
 * Misc utils around Mapbox services.
 */
public class MapboxUtils {

    /**
     * Checks that the provided access token is not empty or null, and that it starts with
     * the right prefixes. Note that this method does not check Mapbox servers to verify that
     * it actually belongs to an account.
     *
     * @param accessToken
     * @return true if the provided access token is valid, false otherwise.
     */
    public static boolean isAccessTokenValid(String accessToken) {
        if (TextUtils.isEmpty(accessToken)) return false;
        if (!accessToken.startsWith("pk.") && !accessToken.startsWith("sk.")) return false;
        return true;
    }

}
