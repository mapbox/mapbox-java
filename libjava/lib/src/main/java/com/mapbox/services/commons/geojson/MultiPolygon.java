package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Created by antonio on 1/30/16.
 */
public class MultiPolygon implements com.mapbox.services.commons.geojson.Geometry<List<List<List<Position>>>> {

    private final String type = "MultiPolygon";
    private final List<List<List<Position>>> coordinates;

    /*
     * Private constructor
     */

    private MultiPolygon(List<List<List<Position>>> coordinates) {
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
    public List<List<List<Position>>> getCoordinates() {
        return coordinates;
    }

    /*
     * Factories
     */

    public static MultiPolygon fromCoordinates(List<List<List<Position>>> coordinates) {
        return new MultiPolygon(coordinates);
    }

    /*
     * Gson interface
     */

    public static MultiPolygon fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, MultiPolygon.class);
    }

    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
