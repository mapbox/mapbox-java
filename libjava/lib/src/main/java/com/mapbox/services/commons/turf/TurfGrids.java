package com.mapbox.services.commons.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;

import java.util.ArrayList;

/**
 * Created by antonio on 7/15/16.
 */
public class TurfGrids {

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
