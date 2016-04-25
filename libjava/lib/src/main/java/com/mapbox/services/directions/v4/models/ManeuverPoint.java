package com.mapbox.services.directions.v4.models;

import java.util.ArrayList;
import java.util.List;

/**
 * If turn-by-turn directions were requested for route, each step gives the location in which it
 * should occur.
 */
@Deprecated
public class ManeuverPoint {

    private String type;
    private List<Double> coordinates;

    public ManeuverPoint() {
        coordinates = new ArrayList<>();
    }

    /**
     * Gives the GeoJSON geometry type.
     *
     * @return string naming type.
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gives the coordinate of the Point. Longitude will always be first (index 0) in list and
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
