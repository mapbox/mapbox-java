package com.mapbox.services.directions.v5.models;

import com.mapbox.services.commons.models.Position;

/**
 * An input coordinate snapped to the roads network.
 */
public class DirectionsWaypoint {

    private String name;
    private double[] location;

    /**
     * @return String with the name of the way the coordinate snapped to.
     */
    public String getName() {
        return name;
    }

    /**
     * @return double array of [longitude, latitude] for the snapped coordinate.
     */
    public double[] getLocation() {
        return location;
    }

    /**
     * converts double array {@link #getLocation()} to a {@link Position}. You'll typically want to
     * use this format instead of {@link #getLocation()} as it's easier to work with.
     *
     * @return {@link Position}.
     */
    public Position asPosition() {
        return Position.fromCoordinates(location[0], location[1]);
    }
}
