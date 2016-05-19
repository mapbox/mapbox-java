package com.mapbox.services.commons.utils;

import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Polyline utils
 */
public class PolylineUtils {

    /*
     * Encoded Polyline Algorithm Format. Some parts from:
     * https://github.com/mapbox/polyline/blob/master/src/polyline.js
     * https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
     */

    /**
     * Decodes an encoded path string into a sequence of Positions.
     */
    public static List<Position> decode(final String encodedPath, int precision) {
        int len = encodedPath.length();

        // OSRM uses precision=6, the default Polyline spec divides by 1E5, capping at precision=5
        double factor = Math.pow(10, precision);

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<Position> path = new ArrayList<>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(Position.fromCoordinates(lng / factor, lat / factor));
        }

        return path;
    }

    /**
     * Encodes a sequence of Positions into an encoded path string.
     */
    public static String encode(final List<Position> path, int precision) {
        long lastLat = 0;
        long lastLng = 0;

        final StringBuffer result = new StringBuffer();

        // OSRM uses precision=6, the default Polyline spec divides by 1E5, capping at precision=5
        double factor = Math.pow(10, precision);

        for (final Position point : path) {
            long lat = Math.round(point.getLatitude() * factor);
            long lng = Math.round(point.getLongitude() * factor);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encode(dLat, result);
            encode(dLng, result);

            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }

    private static void encode(long v, StringBuffer result) {
        v = v < 0 ? ~(v << 1) : v << 1;
        while (v >= 0x20) {
            result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
            v >>= 5;
        }
        result.append(Character.toChars((int) (v + 63)));
    }

    /*
     * Polyline simplification method. It's a direct port of simplify.js to Java.
     * See: https://github.com/mourner/simplify-js/blob/master/simplify.js
     */

    /**
     * Square distance between 2 points
     */
    private static double getSqDist(Position p1, Position p2) {
        double dx = p1.getLongitude() - p2.getLongitude();
        double dy = p1.getLatitude() - p2.getLatitude();
        return dx * dx + dy * dy;
    }

    /**
     * Square distance from a point to a segment
     */
    private static double getSqSegDist(Position p, Position p1, Position p2) {
        double x = p1.getLongitude();
        double y = p1.getLatitude();
        double dx = p2.getLongitude() - x;
        double dy = p2.getLatitude() - y;

        if (dx != 0 || dy != 0) {
            double t = ((p.getLongitude() - x) * dx + (p.getLatitude() - y) * dy) / (dx * dx + dy * dy);
            if (t > 1) {
                x = p2.getLongitude();
                y = p2.getLatitude();

            } else if (t > 0) {
                x += dx * t;
                y += dy * t;
            }
        }

        dx = p.getLongitude() - x;
        dy = p.getLatitude() - y;

        return dx * dx + dy * dy;
    }

    /**
     * Basic distance-based simplification
     */
    private static Position[] simplifyRadialDist(Position[] points, double sqTolerance) {
        Position prevPoint = points[0];
        ArrayList<Position> newPoints = new ArrayList<>();
        newPoints.add(prevPoint);
        Position point = null;

        for (int i = 1, len = points.length; i < len; i++) {
            point = points[i];

            if (getSqDist(point, prevPoint) > sqTolerance) {
                newPoints.add(point);
                prevPoint = point;
            }
        }

        if (prevPoint != point) newPoints.add(point);

        return newPoints.toArray(new Position[newPoints.size()]);
    }

    private static List<Position> simplifyDPStep(Position[] points, int first, int last, double sqTolerance, List<Position> simplified) {
        double maxSqDist = sqTolerance;
        int index = 0;

        ArrayList<Position> stepList = new ArrayList<>();

        for (int i = first + 1; i < last; i++) {
            double sqDist = getSqSegDist(points[i], points[first], points[last]);
            if (sqDist > maxSqDist) {
                index = i;
                maxSqDist = sqDist;
            }
        }

        if (maxSqDist > sqTolerance) {
            if (index - first > 1) {
                stepList.addAll(simplifyDPStep(points, first, index, sqTolerance, simplified));
            }

            stepList.add(points[index]);

            if (last - index > 1) {
                stepList.addAll(simplifyDPStep(points, index, last, sqTolerance, simplified));
            }
        }

        return stepList;
    }

    /**
     * Simplification using Ramer-Douglas-Peucker algorithm
     */
    private static Position[] simplifyDouglasPeucker(Position[] points, double sqTolerance) {
        int last = points.length - 1;

        ArrayList<Position> simplified = new ArrayList<>();
        simplified.add(points[0]);
        simplified.addAll(simplifyDPStep(points, 0, last, sqTolerance, simplified));
        simplified.add(points[last]);
        return simplified.toArray(new Position[simplified.size()]);
    }

    /**
     * Both algorithms combined for awesome performance
     */
    public static Position[] simplify(Position[] points) {
        return simplify(points, 1.0);
    }

    public static Position[] simplify(Position[] points, double tolerance) {
        return simplify(points, tolerance, false);
    }

    public static Position[] simplify(Position[] points, double tolerance, boolean highestQuality) {
        if (points.length <= 2) return points;

        double sqTolerance = tolerance * tolerance;

        points = highestQuality ? points : simplifyRadialDist(points, sqTolerance);
        points = simplifyDouglasPeucker(points, sqTolerance);

        return points;
    }
}
