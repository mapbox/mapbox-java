package com.mapbox.services.geocoding.v5.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Created by antonio on 6/30/16.
 */
public class CarmenFeature extends Feature {

    private String text;
    @SerializedName("place_name") private String placeName;
    private double[] bbox;
    private String address;
    private double[] center;
    private List<CarmenContext> context;

    private double relevance;

    /**
     * Private constructor.
     */
    private CarmenFeature(Geometry geometry, JsonObject properties, String id) {
        super(geometry, properties, id);
    }

    /**
     * Text representing the feature (e.g. "Austin").
     */
    public String getText() {
        return text;
    }

    /**
     * Human-readable text representing the full result hierarchy (e.g. "Austin, Texas, United States").
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * Array bounding box of the form [minx, miny, maxx, maxy].
     */
    public double[] getBbox() {
        return bbox;
    }

    /**
     * Where applicable. Contains the housenumber for the returned feature
     */
    public String getAddress() {
        return address;
    }

    /**
     * Array of the form [lon, lat].
     */
    public double[] getCenter() {
        return center;
    }

    /**
     * Array representing a hierarchy of parents. Each parent includes id, text keys.
     */
    public List<CarmenContext> getContext() {
        return context;
    }

    public double getRelevance() {
        return relevance;
    }

    /**
     * Util to transform center into a Position object
     */

    public Position asPosition() {
        return Position.fromCoordinates(center[0], center[1]);
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
