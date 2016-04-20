package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * A GeoJSON object with the type "GeometryCollection" is a geometry object which represents a
 * collection of geometry objects.
 *
 * @see <a href='geojson.org/geojson-spec.html#geometry-collection'>Official GeoJSON GeometryCollection Specifications</a>
 */
public class GeometryCollection implements com.mapbox.services.commons.geojson.GeoJSON {

    private final String type = "GeometryCollection";
    private final List<com.mapbox.services.commons.geojson.Geometry> geometries;

    /**
     * Private constructor.
     *
     * @param geometries List of {@link Geometry}.
     */
    public GeometryCollection(List<com.mapbox.services.commons.geojson.Geometry> geometries) {
        this.geometries = geometries;
    }

    /*
     * Getters
     */

    /**
     * Should always be "GeometryCollection".
     *
     * @return String "GeometryCollection".
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Get the List containing all the geometries within collection.
     *
     * @return List of geometries within collection.
     */
    public List<com.mapbox.services.commons.geojson.Geometry> getGeometries() {
        return geometries;
    }

    /*
     * Factories
     */

    /**
     * Create a {@link GeometryCollection} from a List of geometries.
     *
     * @param geometries List of {@link Geometry}
     * @return new {@link GeometryCollection}
     */
    public static GeometryCollection fromGeometries(List<com.mapbox.services.commons.geojson.Geometry> geometries) {
        return new GeometryCollection(geometries);
    }

    /*
     * Gson interface
     */

    /**
     * Create a GeoJSON geometry collection object from JSON.
     *
     * @param json String of JSON making up a geometry collection.
     * @return {@link GeometryCollection} GeoJSON object.
     */
    public static GeometryCollection fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
        return gson.create().fromJson(json, GeometryCollection.class);
    }

    /**
     * Convert geometry collection into JSON.
     *
     * @return String containing geometry collection JSON.
     */
    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
