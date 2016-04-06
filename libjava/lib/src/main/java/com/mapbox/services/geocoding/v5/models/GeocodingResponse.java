package com.mapbox.services.geocoding.v5.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 1/30/16.
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getQuery() {
        return this.query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    public List<GeocodingFeature> getFeatures() {
        return this.features;
    }

    public void setFeatures(List<GeocodingFeature> features) {
        this.features = features;
    }

    public String getAttribution() {
        return this.attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }
    
}
