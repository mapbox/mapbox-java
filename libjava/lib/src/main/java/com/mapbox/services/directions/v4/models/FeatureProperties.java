package com.mapbox.services.directions.v4.models;

/**
 * Properties describing a {@link DirectionsFeature}.
 */
@Deprecated
public class FeatureProperties {

    private String name;

    /**
     * Gives the name of the closest street to the {@link DirectionFeature} point.
     *
     * @return String name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
