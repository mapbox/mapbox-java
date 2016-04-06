package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * Created by antonio on 1/30/16.
 */
public class MultiPoint implements com.mapbox.services.commons.geojson.Geometry<List<Position>> {

    private final String type = "MultiPoint";
    private final List<Position> coordinates;

    /*
     * Private constructor
     */

    private MultiPoint(List<Position> coordinates) {
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
    public List<Position> getCoordinates() {
        return coordinates;
    }

    /*
     * Factories
     */

    public static MultiPoint fromCoordinates(List<Position> coordinates) {
        return new MultiPoint(coordinates);
    }

    /*
     * Gson interface
     */

    public static MultiPoint fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, MultiPoint.class);
    }

    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
