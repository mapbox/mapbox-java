package com.mapbox.services.utils;

import com.mapbox.services.commons.utils.MapboxUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by antonio on 4/8/16.
 */
public class MapboxUtilsTest {

    @Test
    public void testEmpty() {
        assertFalse(MapboxUtils.isAccessTokenValid(null));
        assertFalse(MapboxUtils.isAccessTokenValid(""));
    }

    @Test
    public void testRightPrefix() {
        assertTrue(MapboxUtils.isAccessTokenValid("pk.XXX"));
        assertTrue(MapboxUtils.isAccessTokenValid("sk.XXX"));
    }

    @Test
    public void testWrongPrefix() {
        assertFalse(MapboxUtils.isAccessTokenValid("XXX"));
    }

}
