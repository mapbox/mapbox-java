package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * A MultiLineString is a type of {@link Geometry}.
 *
 * @see <a href='http://geojson.org/geojson-spec.html#multilinestringn'>Official GeoJSON MultiLineString Specifications</a>
 */
public class MultiLineString implements com.mapbox.services.commons.geojson.Geometry<List<List<Position>>> {

    private final String type = "MultiLineString";
    private final List<List<Position>> coordinates;

    /*
     * Private constructor
     */

    /**
     * Private constructor.
     *
     * @param coordinates List of {@link Position} making up the MultiLineString.
     */
    private MultiLineString(List<List<Position>> coordinates) {
        this.coordinates = coordinates;
    }

    /*
     * Getters
     */

    /**
     * Should always be "MultiLineString".
     *
     * @return String "MultiLineString".
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Get the list of {@link Position} making up the MultiLineString.
     *
     * @return List of {@link Position}.
     */
    @Override
    public List<List<Position>> getCoordinates() {
        return coordinates;
    }

    /*
     * Factories
     */

    /**
     * Creates a {@link MultiLineString} from a list of coordinates.
     *
     * @param coordinates List of {@link Position} coordinates.
     * @return {@link MultiLineString}.
     */
    public static MultiLineString fromCoordinates(List<List<Position>> coordinates) {
        return new MultiLineString(coordinates);
    }

    /*
     * Gson interface
     */

    /**
     * Create a GeoJSON MultiLineString object from JSON.
     *
     * @param json String of JSON making up a MultiLineString.
     * @return {@link MultiLineString} GeoJSON object.
     */
    public static MultiLineString fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, MultiLineString.class);
    }

    /**
     * Convert feature into JSON.
     *
     * @return String containing MultiLineString JSON.
     */
    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }
    
}
