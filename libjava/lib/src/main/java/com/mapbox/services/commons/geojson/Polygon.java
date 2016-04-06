package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Created by antonio on 1/30/16.
 */
public class Polygon implements com.mapbox.services.commons.geojson.Geometry<List<List<Position>>> {

    private final String type = "Polygon";
    private final List<List<Position>> coordinates;

    /*
     * Private constructor
     */

    private Polygon(List<List<Position>> coordinates) {
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

    public static Polygon fromCoordinates(List<List<Position>> coordinates) {
        return new Polygon(coordinates);
    }

    /*
     * Gson interface
     */

    public static Polygon fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, Polygon.class);
    }

    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
