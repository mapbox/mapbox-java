package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Created by antonio on 1/30/16.
 */
public class FeatureCollection implements GeoJSON {

    private final String type = "FeatureCollection";
    private final List<com.mapbox.services.commons.geojson.Feature> features;

    /*
     * Private constructor
     */

    private FeatureCollection(List<com.mapbox.services.commons.geojson.Feature> features) {
        this.features = features;
    }

    /*
     * Getters
     */

    @Override
    public String getType() {
        return type;
    }

    public List<com.mapbox.services.commons.geojson.Feature> getFeatures() {
        return features;
    }

    /*
     * Factories
     */

    public static FeatureCollection fromFeatures(List<com.mapbox.services.commons.geojson.Feature> features) {
        return new FeatureCollection(features);
    }

        /*
     * Gson interface
     */

    public static FeatureCollection fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
        return gson.create().fromJson(json, FeatureCollection.class);
    }

    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
