package com.mapbox.services.geocoding.v5.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The response object for a geocoder query.
 */
public class GeocodingResponse {

    private String type;
    private List<String> query;
    private List<GeocodingFeature> features;
    private String attribution;

    public GeocodingResponse() {
        this.query = new ArrayList<>();
        this.features = new ArrayList<>();
    }

    /**
     * Describes the GeoJSON type. Typically always "FeatureCollection".
     *
     * @return String with GeoJSON type.
     */
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * A place name for forward geocoding or a coordinate pair (longitude, latitude location) for
     * reverse geocoding.
     *
     * @return a List containing your search query.
     */
    public List<String> getQuery() {
        return this.query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    /**
     * The results, if any, from your query will be in the format {@link GeocodingFeature}.
     *
     * @return List of {@link GeocodingFeature}.
     */
    public List<GeocodingFeature> getFeatures() {
        return this.features;
    }

    public void setFeatures(List<GeocodingFeature> features) {
        this.features = features;
    }

    /**
     * Mapbox attribution.
     *
     * @return String with Mapbox attribution.
     */
    public String getAttribution() {
        return this.attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

}
