package com.mapbox.services.geocoding.v5.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the features geometry, useful for placing it on your map view.
 */
public class FeatureGeometry {

    private String type;
    private List<Double> coordinates;

    public FeatureGeometry() {
        this.coordinates = new ArrayList<>();
    }

    /**
     * Gives the GeoJSON geometry type. Typically will be a "point".
     *
     * @return String with GeoJSON geometry type.
     */
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * List of feature coordinates that can be used to mark/draw the result on your map view.
     *
     * @return List containing longitude, latitude pair.
     */
    public List<Double> getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

}
