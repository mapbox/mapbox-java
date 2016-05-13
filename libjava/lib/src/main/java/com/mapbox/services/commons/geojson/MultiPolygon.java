package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * A MultiPolygon is a type of {@link Geometry}.
 *
 * @see <a href='http://geojson.org/geojson-spec.html#multipolygon'>Official GeoJSON MultiPolygon Specifications</a>
 */
public class MultiPolygon implements Geometry<List<List<List<Position>>>> {

    private final String type = "MultiPolygon";
    private List<List<List<Position>>> coordinates;

    /**
     * Private constructor.
     *
     * @param coordinates List of {@link Position} making up the MultiPolygon.
     */
    private MultiPolygon(List<List<List<Position>>> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Should always be "MultiPolygon".
     *
     * @return String "MultiPolygon".
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Get the list of {@link Position} making up the MultiPolygon.
     *
     * @return List of {@link Position}.
     */
    @Override
    public List<List<List<Position>>> getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(List<List<List<Position>>> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Creates a {@link MultiPolygon} from a list of coordinates.
     *
     * @param coordinates List of {@link Position} coordinates.
     * @return {@link MultiPolygon}.
     */
    public static MultiPolygon fromCoordinates(List<List<List<Position>>> coordinates) {
        return new MultiPolygon(coordinates);
    }

    /**
     * Create a GeoJSON MultiPolygon object from JSON.
     *
     * @param json String of JSON making up a MultiPolygon.
     * @return {@link MultiPolygon} GeoJSON object.
     */
    public static MultiPolygon fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, MultiPolygon.class);
    }

    /**
     * Convert feature into JSON.
     *
     * @return String containing MultiPolygon JSON.
     */
    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }
}
