package com.mapbox.services.geocoding.v5.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Query results, if any, will be in a GeocodingFeature format.
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

    public Object properties;

    public GeocodingFeature() {
        this.bbox = new ArrayList<>();
        this.center = new ArrayList<>();
        this.context = new ArrayList<>();
    }

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
     * Determines the type of the GeoJSON object.
     *
     * @return String with GeoJSON object type.
     */
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Text representing the feature (e.g. "Austin").
     *
     * @return String with feature text.
     */
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Human-readable text representing the full result hierarchy
     * (e.g. "Austin, Texas, United States").
     *
     * @return String with human-readable text.
     */
    public String getPlaceName() {
        return this.placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     * Results are returned in order of relevance. The relevance property is based on how much of
     * the query matched text in the result. You can use the relevance property to remove rough
     * results if you require a response that matches your whole query.
     *
     * @return double value between 0 and 1.
     */
    public double getRelevance() {
        return this.relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    /**
     * List giving a bounding box in the order [minx,miny,maxx,maxy]. Use to fit the entire
     * feature within the map view.
     *
     * @return bounding box List.
     */
    public List<Double> getBbox() {
        return this.bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    /**
     * Center location of the feature.
     *
     * @return Double List with the order longitude, latitude.
     */
    public List<Double> getCenter() {
        return this.center;
    }

    public void setCenter(List<Double> center) {
        this.center = center;
    }

    /**
     * Feature geometry can be a point, polygon, multipolygon, or linestring.
     *
     * @return {@link FeatureGeometry} object.
     */
    public FeatureGeometry getGeometry() {
        return this.geometry;
    }

    public void setGeometry(FeatureGeometry geometry) {
        this.geometry = geometry;
    }

    /**
     * List representing a hierarchy of feature parents. Each parent includes id, and text.
     *
     * @return List of {@link FeatureContext}.
     * @see <a href=https://mapbox.com/api-documentation/#geocoding>Geocoder Documentation</a>
     */
    public List<com.mapbox.services.geocoding.v5.models.FeatureContext> getContext() {
        return this.context;
    }

    public void setContext(List<com.mapbox.services.geocoding.v5.models.FeatureContext> context) {
        this.context = context;
    }

    /**
     * We leave properties as a generic object because at this moment Carmen makes no
     * specifications nor guarantees about the properties of each feature object. Feature
     * properties are passed directly from indexes and may vary by feature and datasource.
     *
     * @return an Object with feature properties.
     */
    public Object getProperties() {
        return this.properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    /**
     * Get the feature center longitude.
     *
     * @return double value of feature center longitude.
     */
    public double getLongitude() {
        return getCenter().get(0);
    }

    /**
     * Get the feature center latitude.
     *
     * @return double value of feature center latitude.
     */
    public double getLatitude() {
        return getCenter().get(1);
    }

    /**
     * Human-readable text representing the full result hierarchy
     * (e.g. "Austin, Texas, United States").
     *
     * @return String with human-readable text.
     */
    @Override
    public String toString() {
        return getPlaceName();
    }

}
