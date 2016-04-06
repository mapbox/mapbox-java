package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

/**
 * Created by antonio on 1/30/16.
 */
public class Feature implements GeoJSON {

    private final String type = "Feature";
    private final Geometry geometry;
    private final JsonObject properties;
    private final String id;

    /*
     * Private constructor
     */

    private Feature(Geometry geometry, JsonObject properties, String id) {
        this.geometry = geometry;
        this.properties = properties;
        this.id = id;
    }

    /*
     * Getters
     */

    @Override
    public String getType() {
        return type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public JsonObject getProperties() {
        return properties;
    }

    public String getId() {
        return id;
    }

    /*
     * Factories
     */

    public static Feature fromGeometry(Geometry geometry) {
        return new Feature(geometry, null, null);
    }

    public static Feature fromGeometry(Geometry geometry, JsonObject properties) {
        return new Feature(geometry, properties, null);
    }

    public static Feature fromGeometry(Geometry geometry, JsonObject properties, String id) {
        return new Feature(geometry, properties, id);
    }

    /*
     * Gson interface
     */

    public static Feature fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
        return gson.create().fromJson(json, Feature.class);
    }

    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
