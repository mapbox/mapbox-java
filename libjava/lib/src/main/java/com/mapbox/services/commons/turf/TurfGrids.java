package com.mapbox.services.commons.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;

import java.util.ArrayList;

public class TurfGrids {

    /**
     * Takes a {@link FeatureCollection} of {@link Point} and a {@link FeatureCollection} of
     * {@link Polygon} and returns the points that fall within the polygons.
     *
     * @param points   input points.
     * @param polygons input polygons.
     * @return points that land within at least one polygon.
     * @throws TurfException
     * @since 2.0.0
     */
    public static FeatureCollection within(FeatureCollection points, FeatureCollection polygons) throws TurfException {
        ArrayList<Feature> features = new ArrayList<>();
        for (int i = 0; i < polygons.getFeatures().size(); i++) {
            for (int j = 0; j < points.getFeatures().size(); j++) {
                Point point = (Point) points.getFeatures().get(j).getGeometry();
                boolean isInside = TurfJoins.inside(point, (Polygon) polygons.getFeatures().get(i).getGeometry());
                if (isInside) {
                    features.add(Feature.fromGeometry(point));
                }
            }
        }
        return FeatureCollection.fromFeatures(features);
    }
}
