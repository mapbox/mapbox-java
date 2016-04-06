package com.mapbox.services.commons.utils;

import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Encoded Polyline Algorithm Format. Some parts from:
 * https://github.com/mapbox/polyline/blob/master/src/polyline.js
 * https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
 */
public class PolylineUtils {

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

}
