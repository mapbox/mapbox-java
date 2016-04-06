package com.mapbox.services.directions.v4.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 11/6/15.
 */
@Deprecated
public class FeatureGeometry {

    private String type;
    private List<Double> coordinates;

    public FeatureGeometry() {
        coordinates = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
