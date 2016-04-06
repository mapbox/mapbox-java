package com.mapbox.services.directions.v5.models;

import com.mapbox.services.commons.models.Position;

/**
 * Created by antonio on 4/4/16.
 */
public class DirectionsWaypoint {

    private String name;
    private double[] location;

    /*
     * Getters
     */

    public String getName() {
        return name;
    }

    public double[] getLocation() {
        return location;
    }

    public Position asPosition() {
        return Position.fromCoordinates(location[0], location[1]);
    }
}
