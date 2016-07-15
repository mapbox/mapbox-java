package com.mapbox.services.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfJoins;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TurfJoinsTest extends BaseTurf {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testJoinsBadType() throws TurfException {
        ArrayList<Position> pointList = new ArrayList<>();
        pointList.add(Position.fromCoordinates(0, 0));
        pointList.add(Position.fromCoordinates(0, 100));
        pointList.add(Position.fromCoordinates(100, 100));
        pointList.add(Position.fromCoordinates(100, 0));
        pointList.add(Position.fromCoordinates(0, 0));
        List<List<Position>> coordinates = new ArrayList<>();
        coordinates.add(pointList);
        Polygon poly = Polygon.fromCoordinates(coordinates);


        // TODO
        thrown.expect(TurfException.class);
        thrown.expectMessage(startsWith("A coordinate, feature, or point geometry is required"));
        //TurfJoins.inside(poly, poly);


/*
    test('bad type', function (t) {
    var poly = polygon([[[0,0], [0,100], [100,100], [100,0], [0,0]]]);

  t.throws(function() {
      inside(poly, poly);
  }, /A coordinate, feature, or point geometry is required/);

  t.end();
});
*/
    }

    @Test
    public void testJoinsFeatureCollectionSimple() throws TurfException {
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

        assertTrue(TurfJoins.inside(ptIn, poly)); // point inside simple polygon
        assertFalse(TurfJoins.inside(ptOut, poly)); // point outside simple polygon
    }

    @Test
    public void testJoinsFeatureCollectionConcave() throws TurfException {
        ArrayList<Position> pointList = new ArrayList<>();
        pointList.add(Position.fromCoordinates(0, 0));
        pointList.add(Position.fromCoordinates(50, 50));
        pointList.add(Position.fromCoordinates(0, 100));
        pointList.add(Position.fromCoordinates(100, 100));
        pointList.add(Position.fromCoordinates(100, 0));
        pointList.add(Position.fromCoordinates(0, 0));
        List<List<Position>> coordinates = new ArrayList<>();
        coordinates.add(pointList);
        Polygon concavePoly = Polygon.fromCoordinates(coordinates);

        Point ptConcaveIn = Point.fromCoordinates(Position.fromCoordinates(75, 75));
        Point ptConcaveOut = Point.fromCoordinates(Position.fromCoordinates(25, 50));

        // test for a concave polygon
        assertTrue(TurfJoins.inside(ptConcaveIn, concavePoly)); // point inside concave polygon
        assertFalse(TurfJoins.inside(ptConcaveOut, concavePoly)); // point outside concave polygon
    }


    @Test
    public void testJoinsPolyWithHole() throws IOException, TurfException {

        Point ptInHole = Point.fromCoordinates(Position.fromCoordinates(-86.69208526611328, 36.20373274711739));
        Point ptInPoly = Point.fromCoordinates(Position.fromCoordinates(-86.72229766845702, 36.20258997094334));
        Point ptOutsidePoly = Point.fromCoordinates(Position.fromCoordinates(-86.75079345703125, 36.18527313913089));
        Polygon polyHole = Polygon.fromJson(loadJsonFixture("turf-inside", "poly-with-hole.geojson"));

        assertFalse(TurfJoins.inside(ptInHole, polyHole));
        assertTrue(TurfJoins.inside(ptInPoly, polyHole));
        assertFalse(TurfJoins.inside(ptOutsidePoly, polyHole));
    }

    @Test
    public void testJoinsMultipolygonWithHole() throws IOException, TurfException {

        Point ptInHole = Point.fromCoordinates(Position.fromCoordinates(-86.69208526611328, 36.20373274711739));
        Point ptInPoly = Point.fromCoordinates(Position.fromCoordinates(-86.72229766845702, 36.20258997094334));
        Point ptInPoly2 = Point.fromCoordinates(Position.fromCoordinates(-86.75079345703125, 36.18527313913089));
        Point ptOutsidePoly = Point.fromCoordinates(Position.fromCoordinates(-86.75302505493164, 36.23015046460186));
        Polygon multiPolyHole = Polygon.fromJson(loadJsonFixture("turf-inside", "multipoly-with-hole.geojson"));

        assertFalse(TurfJoins.inside(ptInHole, multiPolyHole));
        assertTrue(TurfJoins.inside(ptInPoly, multiPolyHole));
        assertTrue(TurfJoins.inside(ptInPoly2, multiPolyHole));
        assertTrue(TurfJoins.inside(ptInPoly, multiPolyHole));
        assertFalse(TurfJoins.inside(ptOutsidePoly, multiPolyHole));
    }
}
