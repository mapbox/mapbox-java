package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;

import java.util.List;

/**
 * A LineString is a type of {@link Geometry}.
 *
 * @see <a href='geojson.org/geojson-spec.html#linestring'>Official GeoJSON LineString Specifications</a>
 */
public class LineString implements com.mapbox.services.commons.geojson.Geometry<List<Position>> {

    private final String type = "LineString";
    private final List<Position> coordinates;

    /**
     * Private constructor.
     *
     * @param coordinates List of {@link Position} making up the LineString.
     */
    private LineString(List<Position> coordinates) {
        this.coordinates = coordinates;
    }

    /*
     * Getters
     */

    /**
     * Should always be "LineString".
     *
     * @return String "LineString".
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Get the list of {@link Position} making up the LineString.
     *
     * @return List of {@link Position}.
     */
    @Override
    public List<Position> getCoordinates() {
        return coordinates;
    }

    /*
     * Factories
     */

    /**
     * creates a {@link LineString} from a list of coordinates.
     *
     * @param coordinates List of {@link Position} coordinates.
     * @return {@link LineString}.
     */
    public static LineString fromCoordinates(List<Position> coordinates) {
        return new LineString(coordinates);
    }

    /*
     * Gson interface
     */

    /**
     * Create a GeoJSON LineString object from JSON.
     *
     * @param json String of JSON making up a LineString.
     * @return {@link LineString} GeoJSON object.
     */
    public static LineString fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, LineString.class);
    }

    /**
     * Convert feature into JSON.
     *
     * @return String containing LineString JSON.
     */
    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

    /*
     * Polyline utils
     */

    /**
     * Convert a polyline into a LineString.
     *
     * @param polyline String describing a polyline.
     * @param precision 
     * @return
     */
    public static LineString fromPolyline(String polyline, int precision) {
        return new LineString(PolylineUtils.decode(polyline, precision));
    }

    public String toPolyline(int precision) {
        return PolylineUtils.encode(getCoordinates(), precision);
    }

}
