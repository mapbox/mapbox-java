package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A Polygon is a type of {@link Geometry}.
 *
 * @see <a href='http://geojson.org/geojson-spec.html#polygon'>Official GeoJSON Polygon Specifications</a>
 */
public class Polygon implements Geometry<List<List<Position>>> {

    private final String type = "Polygon";
    private List<List<Position>> coordinates;

    /**
     * Private constructor.
     *
     * @param coordinates List of {@link Position} making up the Polygon.
     */
    private Polygon(List<List<Position>> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Should always be "Polygon".
     *
     * @return String "Polygon".
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Get the list of {@link Position} making up the Polygon.
     *
     * @return List of {@link Position}.
     */
    @Override
    public List<List<Position>> getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(List<List<Position>> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Creates a {@link Polygon} from a list of coordinates.
     *
     * @param coordinates List of {@link Position} coordinates.
     * @return {@link Polygon}.
     */
    public static Polygon fromCoordinates(List<List<Position>> coordinates) {
        return new Polygon(coordinates);
    }

    public static Polygon fromCoordinates(double[][][] coordinates) {
        List<List<Position>> coordinatesList = new ArrayList<>();
        for (int i = 0; i < coordinates.length; i++) {
            List<Position> innerList = new ArrayList<>();
            for (int j = 0; j < coordinates[i].length; j++) {
                innerList.add(Position.fromCoordinates(
                        coordinates[i][j][0],
                        coordinates[i][j][1]));
            }
            coordinatesList.add(innerList);
        }

        return fromCoordinates(coordinatesList);
    }

    /**
     * Create a GeoJSON Polygon object from JSON.
     *
     * @param json String of JSON making up a Polygon.
     * @return {@link Polygon} GeoJSON object.
     */
    public static Polygon fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        return gson.create().fromJson(json, Polygon.class);
    }

    /**
     * Convert feature into JSON.
     *
     * @return String containing Polygon JSON.
     */
    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }
}
