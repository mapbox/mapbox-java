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
public class GeometryCollection implements com.mapbox.services.commons.geojson.GeoJSON {

    private final String type = "GeometryCollection";
    private final List<com.mapbox.services.commons.geojson.Geometry> geometries;

    /*
     * Private constructor
     */

    public GeometryCollection(List<com.mapbox.services.commons.geojson.Geometry> geometries) {
        this.geometries = geometries;
    }

    /*
     * Getters
     */

    @Override
    public String getType() {
        return type;
    }

    public List<com.mapbox.services.commons.geojson.Geometry> getGeometries() {
        return geometries;
    }

    /*
     * Factories
     */

    public static GeometryCollection fromGeometries(List<com.mapbox.services.commons.geojson.Geometry> geometries) {
        return new GeometryCollection(geometries);
    }

    /*
     * Gson interface
     */

    public static GeometryCollection fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
        return gson.create().fromJson(json, GeometryCollection.class);
    }

    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
