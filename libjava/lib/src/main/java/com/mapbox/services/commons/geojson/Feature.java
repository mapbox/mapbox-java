package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

/**
 * A GeoJSON object with the type "Feature" is a feature object.
 *
 * @see <a href=geojson.org/geojson-spec.html#feature-objects>Official GeoJSON Feature Specifications</a>
 */
public class Feature implements GeoJSON {

    private final String type = "Feature";
    private final Geometry geometry;
    private final JsonObject properties;
    private final String id;

    /**
     * Private constructor.
     *
     * @param geometry   {@link Geometry} object.
     * @param properties of this feature as JSON.
     * @param id         common identifier of this feature.
     */
    private Feature(Geometry geometry, JsonObject properties, String id) {
        this.geometry = geometry;
        this.properties = properties;
        this.id = id;
    }

    // ******************************* Getters *******************************

    /**
     * Should always be "Feature".
     *
     * @return String "Feature".
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Get the features {@link Geometry}.
     *
     * @return {@link Geometry} of the feature or null if not set.
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Returns the optional properties of this feature as JSON.
     *
     * @return the properties of this feature
     */
    public JsonObject getProperties() {
        return properties;
    }

    /**
     * The optional, common identifier of this feature.
     *
     * @return The common identifier of this feature, if set.
     */
    public String getId() {
        return id;
    }

    // ****************************** Factories ******************************

    /**
     * Create a feature from geometry.
     *
     * @param geometry {@link Geometry} object.
     */
    public static Feature fromGeometry(Geometry geometry) {
        return new Feature(geometry, null, null);
    }

    /**
     * Create a feature from geometry.
     *
     * @param geometry   {@link Geometry} object.
     * @param properties of this feature as JSON.
     */
    public static Feature fromGeometry(Geometry geometry, JsonObject properties) {
        return new Feature(geometry, properties, null);
    }

    /**
     * Create a feature from geometry.
     *
     * @param geometry   {@link Geometry} object.
     * @param properties of this feature as JSON.
     * @param id         common identifier of this feature.
     */
    public static Feature fromGeometry(Geometry geometry, JsonObject properties, String id) {
        return new Feature(geometry, properties, id);
    }

    // **************************** Gson Interface ***************************

    /**
     * Create a GeoJSON feature object from JSON.
     *
     * @param json String of JSON making up a feature.
     * @return {@link Feature} GeoJSON object.
     */
    public static Feature fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
        return gson.create().fromJson(json, Feature.class);
    }

    /**
     * Convert feature into JSON.
     *
     * @return String containing feature JSON.
     */
    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
