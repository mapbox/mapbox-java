package com.mapbox.services.models;

import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by antonio on 7/14/16.
 */
public class PositionTest {

    @Test
    public void testEquals() {
        assertNotEquals(Position.fromCoordinates(1.2, 3.4), null);
        assertNotEquals(Position.fromCoordinates(1.2, 3.4), Position.fromCoordinates(1.2, 0.0));
        assertNotEquals(Position.fromCoordinates(1.2, 3.4), Position.fromCoordinates(0.0, 3.4));
        assertNotEquals(Position.fromCoordinates(1.2, 3.4), Position.fromCoordinates(1.2, 3.4, 0.0));

        assertEquals(Position.fromCoordinates(4.3, 2.1), Position.fromCoordinates(4.3, 2.1));
        assertEquals(Position.fromCoordinates(6.5, 4.3, 2.1), Position.fromCoordinates(6.5, 4.3, 2.1));
    }
}
