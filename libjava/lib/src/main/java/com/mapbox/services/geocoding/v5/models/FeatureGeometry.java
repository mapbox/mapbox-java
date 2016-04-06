package com.mapbox.services.geocoding.v5.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 1/30/16.
 */
public class FeatureGeometry {

    private String type;
    private List<Double> coordinates;

    public FeatureGeometry() {
        this.coordinates = new ArrayList<>();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

}
