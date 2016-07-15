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
     * Takes two points and finds the geographic bearing between them.
     */
    public static double bearing(Point p1, Point p2) {
        double degrees2radians = Math.PI / 180;
        double radians2degrees = 180 / Math.PI;
        Position coordinates1 = p1.getCoordinates();
        Position coordinates2 = p2.getCoordinates();

        double lon1 = degrees2radians * coordinates1.getLongitude();
        double lon2 = degrees2radians * coordinates2.getLongitude();
        double lat1 = degrees2radians * coordinates1.getLatitude();
        double lat2 = degrees2radians * coordinates2.getLatitude();
        double a = Math.sin(lon2 - lon1) * Math.cos(lat2);
        double b = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);

        double bearing = radians2degrees * Math.atan2(a, b);
        return bearing;
    }

    /**
     * Takes a Point and calculates the location of a destination point given a distance in
     * degrees, radians, miles, or kilometers; and bearing in degrees. This uses the Haversine
     * formula to account for global curvature.
     */
    public static Point destination(Point point1, double distance, double bearing, String units) throws TurfException {
        double degrees2radians = Math.PI / 180;
        double radians2degrees = 180 / Math.PI;
        Position coordinates1 = point1.getCoordinates();
        double longitude1 = degrees2radians * coordinates1.getLongitude();
        double latitude1 = degrees2radians * coordinates1.getLatitude();
        double bearing_rad = degrees2radians * bearing;

        double radians = TurfHelpers.distanceToRadians(distance, units);

        double latitude2 = Math.asin(Math.sin(latitude1) * Math.cos(radians) +
                Math.cos(latitude1) * Math.sin(radians) * Math.cos(bearing_rad));
        double longitude2 = longitude1 + Math.atan2(Math.sin(bearing_rad) *
                        Math.sin(radians) * Math.cos(latitude1),
                Math.cos(radians) - Math.sin(latitude1) * Math.sin(latitude2));

        return Point.fromCoordinates(
                Position.fromCoordinates(radians2degrees * longitude2, radians2degrees * latitude2));
    }

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

    /**
     * Takes two {@link Position} and returns a position midway between them. The midpoint is
     * calculated geodesically, meaning the curvature of the earth is taken into account.
     *
     * @return midpoint
     */
    public static Position midpoint(Position from, Position to) throws TurfException {
        Point midpointResult = midpoint(Point.fromCoordinates(from), Point.fromCoordinates(to));
        return Position.fromCoordinates(midpointResult.getCoordinates().getLongitude(),
                midpointResult.getCoordinates().getLatitude());
    }

    /**
     * Takes two {@link Point} and returns a point midway between them. The midpoint is calculated
     * geodesically, meaning the curvature of the earth is taken into account.
     *
     * @return midpoint
     */
    public static Point midpoint(Point from, Point to) throws TurfException {
        double dist = distance(from, to, TurfConstants.UNIT_MILES);
        double heading = bearing(from, to);
        return destination(from, dist / 2, heading, TurfConstants.UNIT_MILES);
    }
}
