package com.mapbox.services.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMisc;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by antonio on 7/8/16.
 */
public class TurfMiscTest extends BaseTurf {

    @Test
    public void testTurfLineSliceLine1() throws IOException, TurfException {
        Point start = Point.fromCoordinates(Position.fromCoordinates(-97.79617309570312, 22.254624939561698));
        Point stop = Point.fromCoordinates(Position.fromCoordinates(-97.72750854492188, 22.057641623615734));

        Feature line1 = Feature.fromJson(loadJsonFixture("turf-line-slice", "line1.geojson"));

        LineString sliced = TurfMisc.lineSlice(start, stop, line1);
        assertNotNull(sliced);
    }

    @Test
    public void testTurfLineSliceRawGeometry() throws IOException, TurfException {
        Point start = Point.fromCoordinates(Position.fromCoordinates(-97.79617309570312,22.254624939561698));
        Point stop = Point.fromCoordinates(Position.fromCoordinates(-97.72750854492188,22.057641623615734));

        Feature line1 = Feature.fromJson(loadJsonFixture("turf-line-slice", "line1.geojson"));

        LineString sliced = TurfMisc.lineSlice(start, stop, (LineString)line1.getGeometry());
        assertNotNull(sliced);
    }

    @Test
    public void testTurfLineSliceLine2() throws TurfException {
        Point start = Point.fromCoordinates(Position.fromCoordinates(0,0.1));
        Point stop = Point.fromCoordinates(Position.fromCoordinates(.9,.8));

        ArrayList<Position> coordinates = new ArrayList<>();
        coordinates.add(Position.fromCoordinates(0, 0));
        coordinates.add(Position.fromCoordinates(1, 1));
        LineString line2 = LineString.fromCoordinates(coordinates);

        LineString sliced = TurfMisc.lineSlice(start, stop, line2);
        assertNotNull(sliced);
    }

    @Test
    public void testTurfLineSliceRoute1() throws IOException, TurfException {
        Point start = Point.fromCoordinates(Position.fromCoordinates(-79.0850830078125,37.60117623656667));
        Point stop = Point.fromCoordinates(Position.fromCoordinates(-77.7667236328125,38.65119833229951));

        Feature route1 = Feature.fromJson(loadJsonFixture("turf-line-slice", "route1.geojson"));

        LineString sliced = TurfMisc.lineSlice(start, stop, route1);
        assertNotNull(sliced);
    }

    @Test
    public void testTurfLineSliceRoute2() throws IOException, TurfException {
        Point start = Point.fromCoordinates(Position.fromCoordinates(-112.60660171508789,45.96021963947196));
        Point stop = Point.fromCoordinates(Position.fromCoordinates(-111.97265625,48.84302835299516));

        Feature route2 = Feature.fromJson(loadJsonFixture("turf-line-slice", "route2.geojson"));

        LineString sliced = TurfMisc.lineSlice(start, stop, route2);
        assertNotNull(sliced);
    }

    @Test
    public void testTurfLineSliceVertical() throws IOException, TurfException {
        Point start = Point.fromCoordinates(Position.fromCoordinates(-121.25447809696198, 38.70582415504791));
        Point stop = Point.fromCoordinates(Position.fromCoordinates(-121.25447809696198, 38.70634324369764));

        Feature vertical = Feature.fromJson(loadJsonFixture("turf-line-slice", "vertical.geojson"));

        LineString sliced = TurfMisc.lineSlice(start, stop, vertical);
        assertNotNull(sliced);

        // No duplicated coords
        assertEquals(sliced.getCoordinates().size(), 2);

        // Vertical slice does not collapse to 1st coord
        assertNotEquals(
                sliced.getCoordinates().get(0).getCoordinates(),
                sliced.getCoordinates().get(1).getCoordinates());
    }
}
