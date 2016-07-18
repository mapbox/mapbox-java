package com.mapbox.services.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.MultiPolygon;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfJoins;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TurfJoinsTest extends BaseTurf {

    @Test
    public void testFeatureCollection() throws TurfException {
        // Test for a simple polygon
        ArrayList<Position> pointList = new ArrayList<>();
        pointList.add(Position.fromCoordinates(0, 0));
        pointList.add(Position.fromCoordinates(0, 100));
        pointList.add(Position.fromCoordinates(100, 100));
        pointList.add(Position.fromCoordinates(100, 0));
        pointList.add(Position.fromCoordinates(0, 0));
        List<List<Position>> coordinates = new ArrayList<>();
        coordinates.add(pointList);
        Polygon poly = Polygon.fromCoordinates(coordinates);

        Point ptIn = Point.fromCoordinates(Position.fromCoordinates(50, 50));
        Point ptOut = Point.fromCoordinates(Position.fromCoordinates(140, 150));

        assertTrue(TurfJoins.inside(ptIn, poly));
        assertFalse(TurfJoins.inside(ptOut, poly));

        // Test for a concave polygon
        pointList = new ArrayList<>();
        pointList.add(Position.fromCoordinates(0, 0));
        pointList.add(Position.fromCoordinates(50, 50));
        pointList.add(Position.fromCoordinates(0, 100));
        pointList.add(Position.fromCoordinates(100, 100));
        pointList.add(Position.fromCoordinates(100, 0));
        pointList.add(Position.fromCoordinates(0, 0));
        coordinates = new ArrayList<>();
        coordinates.add(pointList);
        Polygon concavePoly = Polygon.fromCoordinates(coordinates);

        ptIn = Point.fromCoordinates(Position.fromCoordinates(75, 75));
        ptOut = Point.fromCoordinates(Position.fromCoordinates(25, 50));

        assertTrue(TurfJoins.inside(ptIn, concavePoly));
        assertFalse(TurfJoins.inside(ptOut, concavePoly));
    }

    @Test
    public void testPolyWithHole() throws TurfException, IOException {
        Point ptInHole = Point.fromCoordinates(Position.fromCoordinates(-86.69208526611328, 36.20373274711739));
        Point ptInPoly = Point.fromCoordinates(Position.fromCoordinates(-86.72229766845702, 36.20258997094334));
        Point ptOutsidePoly = Point.fromCoordinates(Position.fromCoordinates(-86.75079345703125, 36.18527313913089));
        Feature polyHole = Feature.fromJson(loadJsonFixture("turf-inside", "poly-with-hole.geojson"));

        assertFalse(TurfJoins.inside(ptInHole, (Polygon)polyHole.getGeometry()));
        assertTrue(TurfJoins.inside(ptInPoly, (Polygon)polyHole.getGeometry()));
        assertFalse(TurfJoins.inside(ptOutsidePoly, (Polygon)polyHole.getGeometry()));
    }

    @Test
    public void testMultipolygonWithHole() throws TurfException, IOException {
        Point ptInHole = Point.fromCoordinates(Position.fromCoordinates(-86.69208526611328, 36.20373274711739));
        Point ptInPoly = Point.fromCoordinates(Position.fromCoordinates(-86.72229766845702, 36.20258997094334));
        Point ptInPoly2 = Point.fromCoordinates(Position.fromCoordinates(-86.75079345703125, 36.18527313913089));
        Point ptOutsidePoly = Point.fromCoordinates(Position.fromCoordinates(-86.75302505493164, 36.23015046460186));

        Feature multiPolyHole = Feature.fromJson(loadJsonFixture("turf-inside", "multipoly-with-hole.geojson"));
        assertFalse(TurfJoins.inside(ptInHole, (MultiPolygon)multiPolyHole.getGeometry()));
        assertTrue(TurfJoins.inside(ptInPoly, (MultiPolygon)multiPolyHole.getGeometry()));
        assertTrue(TurfJoins.inside(ptInPoly2, (MultiPolygon)multiPolyHole.getGeometry()));
        assertFalse(TurfJoins.inside(ptOutsidePoly, (MultiPolygon)multiPolyHole.getGeometry()));
    }

    /*
     * Custom test
     */

    @Test
    public void testInputPositions() throws IOException, TurfException {
        Position ptInPoly = Position.fromCoordinates(-86.72229766845702, 36.20258997094334);
        Position ptOutsidePoly = Position.fromCoordinates(-86.75079345703125, 36.18527313913089);
        Feature polyHole = Feature.fromJson(loadJsonFixture("turf-inside", "poly-with-hole.geojson"));

        Polygon polygon = (Polygon) polyHole.getGeometry();
        List<List<Position>> polygonPosition = polygon.getCoordinates();

        assertTrue(TurfJoins.inside(ptInPoly, polygonPosition.get(0)));
        assertFalse(TurfJoins.inside(ptOutsidePoly, polygonPosition.get(0)));


    }
}
