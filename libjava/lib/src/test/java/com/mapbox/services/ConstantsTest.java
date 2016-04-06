package com.mapbox.services;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by antonio on 1/30/16.
 */
public class ConstantsTest {

    @Test
    public void testApiSecure() {
        assertTrue(Constants.BASE_API_URL.startsWith("https"));
    }
}
