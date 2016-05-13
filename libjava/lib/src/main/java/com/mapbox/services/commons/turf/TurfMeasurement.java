package com.mapbox.services.commons.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Created by antonio on 5/12/16.
 */
public class TurfMeasurement {

    /**
     * Calculates the distance between two points in kilometers.
     * This uses the Haversine formula to account for global curvature.
     */
    public static double distance(Point point1, Point point2) throws TurfException {
        return distance(point1, point2, TurfConstants.UNIT_DEFAULT);
    }

    /**
     * Calculates the distance between two points in degress, radians, miles, or kilometers.
     * This uses the Haversine formula to account for global curvature.
     */
    public static double distance(Point point1, Point point2, String units) throws TurfException {
        double degrees2radians = Math.PI / 180;
        Position coordinates1 = point1.getCoordinates();
        Position coordinates2 = point2.getCoordinates();
        double dLat = degrees2radians * (coordinates2.getLatitude() - coordinates1.getLatitude());
        double dLon = degrees2radians * (coordinates2.getLongitude() - coordinates1.getLongitude());
        double lat1 = degrees2radians * coordinates1.getLatitude();
        double lat2 = degrees2radians * coordinates2.getLatitude();

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);

        return TurfHelpers.radiansToDistance(2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)), units);
    }

    /**
     * Takes a line and measures its length in the specified units.
     */
    public static double lineDistance(FeatureCollection line, String units) throws TurfException {
        double d = 0;
        for (int i = 0; i < line.getFeatures().size(); i++) {
            d += lineDistance(line.getFeatures().get(i), units);
        }
        return d;
    }

    public static double lineDistance(Feature line, String units) throws TurfException {
        return lineDistance(line.getGeometry(), units);
    }

    public static double lineDistance(Geometry line, String units) throws TurfException {
        double d;

        if (line.getType().equals("LineString")) {
            List<Position> coordinates = (List<Position>) line.getCoordinates();
            return length(coordinates, units);
        } else if (line.getType().equals("Polygon") || line.getType().equals("MultiLineString")) {
            List<List<Position>> coordinates = (List<List<Position>>) line.getCoordinates();
            d = 0;
            for (int i = 0; i < coordinates.size(); i++) {
                d += length(coordinates.get(i), units);
            }
            return d;
        } else if (line.getType().equals("MultiPolygon")) {
            List<List<List<Position>>> coordinates = (List<List<List<Position>>>) line.getCoordinates();
            d = 0;
            for (int i = 0; i < coordinates.size(); i++) {
                for (int j = 0; j < coordinates.get(i).size(); j++) {
                    d += length(coordinates.get(i).get(j), units);
                }
            }
            return d;
        } else {
            throw new TurfException("Input must be a LineString, MultiLineString, " +
                    "Polygon, or MultiPolygon Feature or Geometry (or a FeatureCollection " +
                    "containing only those types)");
        }
    }

    private static double length(List<Position> coords, String units) throws TurfException {
        double travelled = 0;
        Point prevCoords = Point.fromCoordinates(coords.get(0));
        Point curCoords = Point.fromCoordinates(coords.get(0));
        Point temp;
        for (int i = 1; i < coords.size(); i++) {
            curCoords.setCoordinates(coords.get(i));
            travelled += distance(prevCoords, curCoords, units);
            temp = prevCoords;
            prevCoords = curCoords;
            curCoords = temp;
        }
        return travelled;
    }

}
