package com.mapbox.services.commons.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.GeoJSON;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;

/**
 * Created by antonio on 7/14/16.
 */
public class TurfInvariant {

    /**
     * Unwrap a coordinate from a Feature with a Point geometry, a Point
     * geometry, or a single coordinate.
     *
     * @param {*} obj any value
     * @returns {Array<number>} a coordinate
     */
    public static Position getCoord(Feature obj) throws TurfException {
        if (obj.getGeometry().getClass().equals(Point.class)) {
            return getCoord((Point) obj.getGeometry());
        }

        throw new TurfException("A coordinate, feature, or point geometry is required");
    }

    public static Position getCoord(Point obj) throws TurfException {
        if (obj != null) {
            return obj.getCoordinates();
        }

        throw new TurfException("A coordinate, feature, or point geometry is required");
    }

    /**
     * Enforce expectations about types of GeoJSON objects for Turf.
     *
     * @alias geojsonType
     * @param {GeoJSON} value any GeoJSON object
     * @param {string} type expected GeoJSON type
     * @param {string} name name of calling function
     * @throws {Error} if value is not the expected type.
     */
    public static void geojsonType(GeoJSON value, String type, String name) throws TurfException {
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(name)) {
           throw new TurfException("Type and name required");
        }

        if (value == null || !value.getType().equals(type)) {
            throw new TurfException("Invalid input to " + name + ": must be a " + type
                    + ", given " + value.getType());
        }
    }

    /**
     * Enforce expectations about types of {@link Feature} inputs for Turf.
     * Internally this uses {@link Feature#getType()} to judge geometry types.
     *
     * @alias featureOf
     * @param {Feature} feature a feature with an expected geometry type
     * @param {string} type expected GeoJSON type
     * @param {string} name name of calling function
     * @throws {Error} error if value is not the expected type.
     */
    public static void featureOf(Feature feature, String type, String name) throws TurfException {
        if (TextUtils.isEmpty(name)) {
            throw new TurfException(".featureOf() requires a name");
        }

        if (feature == null || !feature.getType().equals("Feature") || feature.getGeometry() == null) {
            throw new TurfException("Invalid input to " + name + ", Feature with geometry required");
        }

        if (feature.getGeometry() == null || !feature.getGeometry().getType().equals(type)) {
            throw new TurfException("Invalid input to " + name + ": must be a " + type
                    + ", given " + feature.getGeometry().getType());
        }
    }

    /**
     * Enforce expectations about types of {@link FeatureCollection} inputs for Turf.
     * Internally this uses {@link Feature#getType()}} to judge geometry types.
     *
     * @alias collectionOf
     * @param {FeatureCollection} featurecollection a featurecollection for which features will be judged
     * @param {string} type expected GeoJSON type
     * @param {string} name name of calling function
     * @throws {Error} if value is not the expected type.
     */
    public static void collectionOf(FeatureCollection featurecollection, String type, String name) throws TurfException {
        if (TextUtils.isEmpty(name)) {
            throw new TurfException("collectionOf() requires a name");
        }

        if (featurecollection == null || !featurecollection.getType().equals("FeatureCollection")
                || featurecollection.getFeatures() == null) {
            throw new TurfException("Invalid input to " + name + ", FeatureCollection required");
        }

        for (Feature feature : featurecollection.getFeatures()) {
            if (feature == null || !feature.getType().equals("Feature") || feature.getGeometry() == null) {
                throw new TurfException("Invalid input to " + name + ", Feature with geometry required");
            }

            if (feature.getGeometry() == null || !feature.getGeometry().getType().equals(type)) {
                throw new TurfException("Invalid input to " + name + ": must be a " + type
                        + ", given " + feature.getGeometry().getType());
            }
        }
    }

}
