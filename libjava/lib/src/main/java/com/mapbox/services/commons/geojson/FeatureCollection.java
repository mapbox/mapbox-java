package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * A GeoJSON object with the type "FeatureCollection" is a feature object which represents a
 * collection of feature objects.
 *
 * @see <a href='geojson.org/geojson-spec.html#feature-collection-objects'>Official GeoJSON FeatureCollection Specifications</a>
 */
public class FeatureCollection implements GeoJSON {

    private final String type = "FeatureCollection";
    private final List<com.mapbox.services.commons.geojson.Feature> features;

    /**
     * Private constructor.
     *
     * @param features List of {@link Feature}.
     */
    private FeatureCollection(List<com.mapbox.services.commons.geojson.Feature> features) {
        this.features = features;
    }

    /*
     * Getters
     */

    /**
     * Should always be "FeatureCollection".
     *
     * @return String "FeatureCollection".
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Get the List containing all the features within collection.
     *
     * @return List of features within collection.
     */
    public List<com.mapbox.services.commons.geojson.Feature> getFeatures() {
        return features;
    }

    /*
     * Factories
     */

    /**
     * Create a {@link FeatureCollection} from a List of features.
     *
     * @param features List of {@link Feature}
     * @return new {@link FeatureCollection}
     */
    public static FeatureCollection fromFeatures(List<com.mapbox.services.commons.geojson.Feature> features) {
        return new FeatureCollection(features);
    }

    /*
     * Gson interface
     */

    /**
     * Create a GeoJSON feature collection object from JSON.
     *
     * @param json String of JSON making up a feature collection.
     * @return {@link FeatureCollection} GeoJSON object.
     */
    public static FeatureCollection fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionDeserializer());
        gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
        return gson.create().fromJson(json, FeatureCollection.class);
    }

    /**
     * Convert feature collection into JSON.
     *
     * @return String containing feature collection JSON.
     */
    @Override
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Position.class, new PositionSerializer());
        return gson.create().toJson(this);
    }

}
