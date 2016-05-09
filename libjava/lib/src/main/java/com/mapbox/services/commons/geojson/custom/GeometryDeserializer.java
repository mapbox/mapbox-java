package com.mapbox.services.commons.geojson.custom;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.services.commons.geojson.Geometry;

import java.lang.reflect.Type;

/**
 * Required to handle the "Unable to invoke no-args constructor for interface {@link Geometry} error
 * that Gson shows when trying to deserialize a list of {@link Geometry}.
 */
public class GeometryDeserializer implements JsonDeserializer<Geometry> {

    @Override
    public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Find the actual class name from the type property in the JSON.
        String geometryType = json.getAsJsonObject().get("type").getAsString();
        try {
            // Use the current context to deserialize it
            Type classType = Class.forName("com.mapbox.services.commons.geojson." + geometryType);
            return context.deserialize(json, classType);
        } catch (ClassNotFoundException e) {
            // Unknown geometry
            throw new JsonParseException(e);
        }
    }

}
