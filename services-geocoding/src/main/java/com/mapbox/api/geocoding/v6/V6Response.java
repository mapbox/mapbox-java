package com.mapbox.api.geocoding.v6;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.geocoding.v5.models.GeocodingAdapterFactory;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * This is the initial object which gets returned when the geocoding request receives a result.
 * Since each result is a {@link V6Feature}, the response simply returns a list of those
 * features.
 */
@AutoValue
public abstract class V6Response implements Serializable {

    /**
     * A geocoding response will always be an extension of a {@link FeatureCollection} containing
     * additional information.
     *
     * @return the type of GeoJSON this is
     */
    @NonNull
    public abstract String type();

    /**
     * A list of the V6Features which contain the results and are ordered from most relevant to
     * least.
     *
     * @return a list of {@link V6Feature}s which each represent an individual result from the query
     */
    @NonNull
    public abstract List<V6Feature> features();

    /**
     * A string attributing the results of the Mapbox Geocoding API to Mapbox and links to Mapbox's
     * terms of service and data sources.
     *
     * @return information about Mapbox's terms of service and the data sources
     */
    @NonNull
    public abstract String attribution();

    /**
     * Create a new instance of this class by passing in a formatted valid JSON String.
     *
     * @param json a formatted valid JSON string defining a GeoJson Geocoding Response
     * @return a new instance of this class defined by the values passed inside this static factory
     *   method
     */
    @NonNull
    public static V6Response fromJson(@NonNull String json) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(GeometryAdapterFactory.create())
            .registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter())
            .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
            .create();
        return gson.fromJson(json, V6Response.class);
    }

    /**
     * Gson TYPE adapter for parsing Gson to this class.
     *
     * @param gson the built {@link Gson} object
     * @return the TYPE adapter for this class
     */
    public static TypeAdapter<V6Response> typeAdapter(Gson gson) {
        return new AutoValue_V6Response.GsonTypeAdapter(gson);
    }
}
