package com.mapbox.services.mapmatching.v4.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Created by ivo on 17/05/16.
 */
public class MapMatchingResponse extends FeatureCollection {

    private static final String KEY_MATCHED_POINTS = "matchedPoints";

    private String code;

    protected MapMatchingResponse(List<Feature> features) {
        super(features);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Convenience method to obtain the list of matched points.
     */
    public Position[] getMatchedPoints() {
        return getMatchedPoints(0);
    }

    /**
     * Convenience method to obtain the list of matched points for other matches. When the matching
     * algorithm cannot decide the correct match between two points, it will omit that line and
     * create several sub-matches, each as a feature. The higher the number of features, the more
     * likely that the input traces are poorly aligned to the road network.
     */
    public Position[] getMatchedPoints(int submatch) {
        JsonObject properties = getFeatures().get(submatch).getProperties();
        JsonArray points = properties.getAsJsonArray(KEY_MATCHED_POINTS);

        Position[] positions = new Position[points.size()];
        for (int i = 0; i < points.size(); i++) {
            positions[i] = Position.fromCoordinates(
                    points.get(i).getAsJsonArray().get(0).getAsDouble(),
                    points.get(i).getAsJsonArray().get(1).getAsDouble());
        }

        return positions;
    }

}
