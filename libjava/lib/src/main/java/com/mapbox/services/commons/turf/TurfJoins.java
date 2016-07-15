package com.mapbox.services.commons.turf;

import com.mapbox.services.commons.geojson.MultiPolygon;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

public class TurfJoins {

    public static boolean inside(Point point, Polygon polygon) throws TurfException {
        // This API needs to get better
        List<List<Position>> coordinates = polygon.getCoordinates();
        ArrayList<List<List<Position>>> multiCoordinates = new ArrayList<>();
        multiCoordinates.add(coordinates);
        return inside(point, MultiPolygon.fromCoordinates(multiCoordinates));
    }

    public static boolean inside(Point point, MultiPolygon polygon) throws TurfException {
        Position pt = TurfInvariant.getCoord(point);
        List<List<List<Position>>> polys = polygon.getCoordinates();

        boolean insidePoly = false;
        for (int i = 0; i < polys.size() && !insidePoly; i++) {
            // check if it is in the outer ring first
            if (inRing(pt, polys.get(i).get(0))) {
                boolean inHole = false;
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

        return insidePoly;
    };

    // pt is [x,y] and ring is [[x,y], [x,y],..]
    public static boolean inRing(Position pt, List<Position> ring) {
        boolean isInside = false;

        for (int i = 0, j = ring.size() - 1; i < ring.size(); j = i++) {
            double xi = ring.get(i).getLongitude();
            double yi = ring.get(i).getLatitude();
            double xj = ring.get(j).getLongitude();
            double yj = ring.get(j).getLatitude();
            boolean intersect = (
                    (yi > pt.getLatitude()) != (yj > pt.getLatitude())) &&
                    (pt.getLongitude() < (xj - xi) * (pt.getLatitude() - yi) / (yj - yi) + xi);
            if (intersect) isInside = !isInside;
        }

        return isInside;
    }

}
