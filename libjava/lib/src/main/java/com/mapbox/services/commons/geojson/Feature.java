package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

/**
 * A GeoJSON object with the type "Feature" is a feature object.
 *
 * @see <a href='geojson.org/geojson-spec.html#feature-objects'>Official GeoJSON Feature Specifications</a>
 */
public class Feature implements GeoJSON {

    private final String type = "Feature";
    private final Geometry geometry;
    private JsonObject properties;
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

    public void setProperties(JsonObject properties) {
        this.properties = properties;
    }

    /**
     * The optional, common identifier of this feature.
     *
     * @return The common identifier of this feature, if set.
     */
    public String getId() {
        return id;
    }

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

    /**
     * Convenience method to add a String member.
     * @param key name of the member
     * @param value the String value associated with the member
     */
    public void addStringProperty(String key, String value) {
        this.properties.addProperty(key, value);
    }

    /**
     * Convenience method to add a Number member.
     * @param key name of the member
     * @param value the Number value associated with the member
     */
    public void addNumberProperty(String key, Number value) {
        this.properties.addProperty(key, value);
    }

    /**
     * Convenience method to add a Boolean member.
     * @param key name of the member
     * @param value the Boolean value associated with the member
     */
    public void addBooleanProperty(String key, Boolean value) {
        this.properties.addProperty(key, value);
    }

    /**
     * Convenience method to add a Character member.
     * @param key name of the member
     * @param value the Character value associated with the member
     */
    public void addCharacterProperty(String key, Character value) {
        this.properties.addProperty(key, value);
    }

    /**
     * Convenience method to add a JsonElement member.
     * @param key name of the member
     * @param value the JsonElement value associated with the member
     */
    public void addProperty(String key, JsonElement value) {
        this.properties.add(key, value);
    }

    /**
     * Convenience method to get a String member.
     * @param key name of the member
     * @return the value of the member, null if it doesn't exist
     */
    public String getStringProperty(String key) {
        return this.properties.get(key).getAsString();
    }

    /**
     * Convenience method to get a Number member.
     * @param key name of the member
     * @return the value of the member, null if it doesn't exist
     */
    public Number getNumberProperty(String key) {
        return this.properties.get(key).getAsNumber();
    }

    /**
     * Convenience method to get a Boolean member.
     * @param key name of the member
     * @return the value of the member, null if it doesn't exist
     */
    public Boolean getBooleanProperty(String key) {
        return this.properties.get(key).getAsBoolean();
    }

    /**
     * Convenience method to get a Character member.
     * @param key name of the member
     * @return the value of the member, null if it doesn't exist
     */
    public Character getCharacterProperty(String key) {
        return this.properties.get(key).getAsCharacter();
    }

    /**
     * Convenience method to get a JsonElement member.
     * @param key name of the member
     * @return the value of the member, null if it doesn't exist
     */
    public JsonElement getProperty(String key) {
        return this.properties.get(key);
    }

    /**
     * Removes the property from the object properties
     * @param key name of the member
     * @return
     */
    public JsonElement removeProperty(String key) {
        return this.properties.remove(key);
    }

    /**
     * Convenience method to check if a member with the specified name is present in this object.
     * @param key
     * @return
     */
    public boolean hasProperty(String key) {
        return this.properties.has(key);
    }
}
