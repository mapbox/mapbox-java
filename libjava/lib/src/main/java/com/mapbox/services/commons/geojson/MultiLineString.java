package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Created by antonio on 1/30/16.
 */
public class MultiLineString implements com.mapbox.services.commons.geojson.Geometry<List<List<Position>>> {

    private final String type = "MultiLineString";
    private final List<List<Position>> coordinates;

    /*
     * Private constructor
     */

    private MultiLineString(List<List<Position>> coordinates) {
        this.coordinates = coordinates;
    }

    /*
     * Getters
     */

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<List<Position>> getCoordinates() {
        return coordinates;
    }

    /*
     * Factories
     */

    public static MultiLineString fromCoordinates(List<List<Position>> coordinates) {
        return new MultiLineString(coordinates);
    }

    /*
     * Gson interface
     */

    public static MultiLineString fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, MultiLineString.class);
    }

    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }
    
}
