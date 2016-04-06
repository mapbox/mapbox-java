package com.mapbox.services.geocoding.v5.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 1/30/16.
 */
public class GeocodingFeature {

    private String id;
    private String type;
    private String text;
    @SerializedName("place_name") private String placeName;
    private double relevance;
    private List<Double> bbox;
    private List<Double> center;
    private FeatureGeometry geometry;
    private List<com.mapbox.services.geocoding.v5.models.FeatureContext> context;

    /*
     * We leave properties as a generic object because at this moment Carmen makes no
     * specifications nor guarantees about the properties of each feature object. Feature
     * properties are passed directly from indexes and may vary by feature and datasource.
     */

    public Object properties;

    public GeocodingFeature() {
        this.bbox = new ArrayList<>();
        this.center = new ArrayList<>();
        this.context = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getRelevance() {
        return this.relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    public List<Double> getBbox() {
        return this.bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public List<Double> getCenter() {
        return this.center;
    }

    public void setCenter(List<Double> center) {
        this.center = center;
    }

    public FeatureGeometry getGeometry() {
        return this.geometry;
    }

    public void setGeometry(FeatureGeometry geometry) {
        this.geometry = geometry;
    }

    public List<com.mapbox.services.geocoding.v5.models.FeatureContext> getContext() {
        return this.context;
    }

    public void setContext(List<com.mapbox.services.geocoding.v5.models.FeatureContext> context) {
        this.context = context;
    }

    public Object getProperties() {
        return this.properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    /*
     * Additional API
     */

    public double getLongitude() {
        return getCenter().get(0);
    }

    public double getLatitude() {
        return getCenter().get(1);
    }

    @Override
    public String toString() {
        return getPlaceName();
    }

}
