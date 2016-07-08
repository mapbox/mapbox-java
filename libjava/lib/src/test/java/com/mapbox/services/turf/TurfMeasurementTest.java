package com.mapbox.services.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by antonio on 5/12/16.
 */
public class TurfMeasurementTest extends BaseTurf {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testBearing() {
        Point pt1 = Point.fromCoordinates(Position.fromCoordinates(-75.4, 39.4));
        Point pt2 = Point.fromCoordinates(Position.fromCoordinates(-75.534, 39.123));
        assertNotEquals(TurfMeasurement.bearing(pt1, pt2), 0, DELTA);
    }

    @Test
    public void testDestination() throws TurfException {
        Point pt1 = Point.fromCoordinates(Position.fromCoordinates(-75.0, 39.0));
        double dist = 100;
        double bear = 180;
        assertNotNull(TurfMeasurement.destination(pt1, dist, bear, TurfConstants.UNIT_KILOMETERS));
    }

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

    @Test
    public void testLineDistanceLineString() throws IOException, TurfException {
        Feature route1 = Feature.fromJson(loadJsonFixture("turf-line-distance", "route1.geojson"));
        Feature route2 = Feature.fromJson(loadJsonFixture("turf-line-distance", "route2.geojson"));
        assertEquals(Math.round(TurfMeasurement.lineDistance(route1, "miles")), 202);
        assertTrue((TurfMeasurement.lineDistance(route2, "kilometers") - 742) < 1
                && (TurfMeasurement.lineDistance(route2, "kilometers") - 742) > (-1) );
    }

    @Test
    public void testLineDistanceWithGeometries() throws IOException, TurfException {
        Feature route1 = Feature.fromJson(loadJsonFixture("turf-line-distance", "route1.geojson"));
        Feature route2 = Feature.fromJson(loadJsonFixture("turf-line-distance", "route2.geojson"));
        assertEquals(Math.round(TurfMeasurement.lineDistance(route1.getGeometry(), "miles")), 202);
        assertTrue((TurfMeasurement.lineDistance(route2.getGeometry(), "kilometers") - 742) < 1
                && (TurfMeasurement.lineDistance(route2.getGeometry(), "kilometers") - 742) > (-1) );
    }

    @Test
    public void testLineDistancePolygon() throws IOException, TurfException {
        Feature feat = Feature.fromJson(loadJsonFixture("turf-line-distance", "polygon.geojson"));
        assertEquals(Math.round(1000 * TurfMeasurement.lineDistance(feat, "kilometers")), 5599);
    }

    @Test
    public void testLineDistanceMultiLineString() throws IOException, TurfException {
        Feature feat = Feature.fromJson(loadJsonFixture("turf-line-distance", "multilinestring.geojson"));
        assertEquals(Math.round(1000 * TurfMeasurement.lineDistance(feat, "kilometers")), 4705);
    }

    @Test
    public void testLineDistanceFeatureCollection() throws IOException, TurfException {
        FeatureCollection feat = FeatureCollection.fromJson(loadJsonFixture("turf-line-distance", "featurecollection.geojson"));
        assertEquals(Math.round(1000 * TurfMeasurement.lineDistance(feat, "kilometers")), 10304);
    }

}
