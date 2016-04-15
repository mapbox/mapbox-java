package com.mapbox.services.directions.v4.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the geometry of a {@link DirectionsFeature}.
 */
public class FeatureGeometry {

    private String type;
    private List<Double> coordinates;

    public FeatureGeometry() {
        coordinates = new ArrayList<>();
    }

    /**
     * Gives GeoJSON geometry type.
     *
     * @return string naming GeoJSON geometry type.
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gives the coordinate of the point. Longitude will always be first (index 0) in list and
     * latitude will be second (index 1).
     *
     * @return List of Double objects containing a point with longitude and latitude values.
     */
    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
