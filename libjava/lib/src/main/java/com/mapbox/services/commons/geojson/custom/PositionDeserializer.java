package com.mapbox.services.commons.geojson.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.services.commons.models.Position;

import java.lang.reflect.Type;

/**
 * Required to handle the "Expected BEGIN_OBJECT but was BEGIN_ARRAY" error that Gson would show
 * otherwise.
 */
public class PositionDeserializer implements JsonDeserializer<Position> {

    @Override
    public Position deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray rawCoordinates = json.getAsJsonArray();
        double longitude = rawCoordinates.get(0).getAsDouble();
        double latitude = rawCoordinates.get(1).getAsDouble();

        // Includes altitude
        if (rawCoordinates.size() > 2) {
            double altitude = rawCoordinates.get(2).getAsDouble();
            return Position.fromCoordinates(longitude, latitude, altitude);
        }

        // It doesn't have altitude
        return Position.fromCoordinates(longitude, latitude);
    }

}
