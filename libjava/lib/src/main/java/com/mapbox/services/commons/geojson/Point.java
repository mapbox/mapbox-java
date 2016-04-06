package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

/**
 * Created by antonio on 1/30/16.
 */
public class Point implements com.mapbox.services.commons.geojson.Geometry<Position> {

    private final String type = "Point";
    private final Position coordinates;

    /*
     * Private constructor
     */

    private Point(Position coordinates) {
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
    public Position getCoordinates() {
        return coordinates;
    }

    /*
     * Factories
     */

    public static Point fromCoordinates(Position coordinates) {
        return new Point(coordinates);
    }

    /*
     * Gson interface
     */

    public static Point fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, Point.class);
    }

    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
