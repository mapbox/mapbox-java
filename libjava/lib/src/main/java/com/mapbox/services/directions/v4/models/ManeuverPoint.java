package com.mapbox.services.directions.v4.models;

import java.util.ArrayList;
import java.util.List;

/**
 * If turn-by-turn directions were requested for route, each step gives the location in which it
 * should occur.
 *
 * @since 1.0.0
 */
public class ManeuverPoint {

    private String type;
    private List<Double> coordinates;

    /**
     * Builder
     *
     * @since 1.0.0
     */
    public ManeuverPoint() {
        coordinates = new ArrayList<>();
    }

    /**
     * Gives the GeoJSON geometry type.
     *
     * @return String naming type.
     * @since 1.0.0
     */
    public String getType() {
        return type;
    }

    /**
     * @param type String naming type.
     * @since 1.0.0
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gives the coordinate of the Point. Longitude will always be first (index 0) in list and
     * latitude will be second (index 1).
     *
     * @return List of Double objects containing a point with longitude and latitude values.
     * @since 1.0.0
     */
    public List<Double> getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates List of Double objects containing a point with longitude and latitude
     *                    values.
     * @since 1.0.0
     */
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
