package com.mapbox.services.geocoding.v5.models;

/**
 * Feature context breaks down the places address and gives you more information about each part.
 * In the case of an address it can give you the type id for each part and its name.
 *
 * @see <a href=https://mapbox.com/api-documentation/#geocoding>Geocoder Documentation</a>
 */
public class FeatureContext {

    private String id;
    private String text;

    /**
     * Feature IDs are formatted like {type}.{id}. {type} is one of the following, "country",
     * "region", "postcode", "place", "locality", "neighborhood", "address", or poi. Additional
     * feature types may be added in the future, but the current types will stay the same. The
     * numeric part of a feature ID may change between data updates.
     *
     * @return String with format {type}.{id}.
     */
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Human-readable String describing a specific type.
     *
     * @return String with specific type name.
     */
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
