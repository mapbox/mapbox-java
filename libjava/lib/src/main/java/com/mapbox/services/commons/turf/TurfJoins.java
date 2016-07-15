package com.mapbox.services.commons.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

public class TurfJoins {

    public static boolean inside(Position position, Polygon polygon) throws TurfException {
        return inside(Point.fromCoordinates(position), polygon);
    }

    public static boolean inside(Point point, Polygon polygon) throws TurfException {
        boolean insidePoly = false;

        Position pt = TurfInvariant.getCoord(point);
        List<List<Position>> poly = polygon.getCoordinates();
        // normalize to multipolygon
        //if (polygon.getType().equals("Polygon")) {
            List<List<List<Position>>> polys = new ArrayList<>();
            polys.add(poly);

            boolean inHole = false;
            for (int i = 0; i < polys.size() && !insidePoly; i++) {
                // check if it is in the outer ring first
                if (inRing(pt, polys.get(i).get(0))) {

                    int k = 1;
                    // check for the point in any of the holes
                    while (k < polys.get(i).size() && !inHole) {
                        if (inRing(pt, polys.get(i).get(k))) {
                            inHole = true;
                        }
                        k++;
                    }
                    if (!inHole) insidePoly = true;
                }
            }
        //}
        return insidePoly;
    }

    // pt is [x,y] and ring is [[x,y], [x,y],..]
    private static boolean inRing(Position pt, List<Position> ring) {
        boolean isInside = false;
        boolean intersect;
        double xi, yi, xj, yj;
        for (int i = 0, j = ring.size() - 1; i < ring.size(); j = i++) {
            xi = ring.get(i).getLongitude();
            yi = ring.get(i).getLatitude();
            xj = ring.get(j).getLongitude();
            yj = ring.get(j).getLatitude();
            intersect = ((yi > pt.getLatitude()) != (yj > pt.getLatitude())) &&
                    (pt.getLongitude() < (xj - xi) * (pt.getLatitude() - yi) / (yj - yi) + xi);
            if (intersect) isInside = !isInside;
        }
        return isInside;
    }

    /*
    // pt is [x,y] and ring is [[x,y], [x,y],..]
    function inRing(pt, ring) {
        var isInside = false;
        for (var i = 0, j = ring.length - 1; i < ring.length; j = i++) {
            var xi = ring[i][0], yi = ring[i][1];
            var xj = ring[j][0], yj = ring[j][1];
            var intersect = ((yi > pt[1]) != = (yj > pt[1])) &&
                    (pt[0] < (xj - xi) * (pt[1] - yi) / (yj - yi) + xi);
            if (intersect) isInside = !isInside;
        }
        return isInside;
    }
     */

}
