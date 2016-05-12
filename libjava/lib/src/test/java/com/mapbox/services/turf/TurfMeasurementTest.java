package com.mapbox.services.turf;

import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;

/**
 * Created by antonio on 5/12/16.
 */
public class TurfMeasurementTest {

    private final static double DELTA = 1E-10;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDistance() throws TurfException {
        Point pt1 = Point.fromCoordinates(Position.fromCoordinates(-75.343, 39.984));
        Point pt2 = Point.fromCoordinates(Position.fromCoordinates(-75.534, 39.123));

        // Common cases
        assertEquals(TurfMeasurement.distance(pt1, pt2, TurfConstants.UNIT_MILES), 60.37218405837491, DELTA);
        assertEquals(TurfMeasurement.distance(pt1, pt2, TurfConstants.UNIT_NAUTICAL_MILES), 52.461979624130436, DELTA);
        assertEquals(TurfMeasurement.distance(pt1, pt2, TurfConstants.UNIT_KILOMETERS), 97.15957803131901, DELTA);
        assertEquals(TurfMeasurement.distance(pt1, pt2, TurfConstants.UNIT_RADIANS), 0.015245501024842149, DELTA);
        assertEquals(TurfMeasurement.distance(pt1, pt2, TurfConstants.UNIT_DEGREES), 0.8735028650863799, DELTA);

        // This also works
        assertEquals(TurfMeasurement.distance(pt1, pt2, "kilometres"), 97.15957803131901, DELTA);

        // Default is kilometers
        assertEquals(TurfMeasurement.distance(pt1, pt2), 97.15957803131901, DELTA);

        // Bad unit
        thrown.expect(TurfException.class);
        thrown.expectMessage(startsWith("Invalid unit."));
        TurfMeasurement.distance(pt1, pt2, "blah");
    }
}
