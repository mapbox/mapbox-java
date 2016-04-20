package com.mapbox.services.commons.geojson.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mapbox.services.commons.models.Position;

import java.lang.reflect.Type;

/**
 * Required to handle the special case where the altitude might be a Double.NaN, which isn't a valid
 * double value as per JSON specification.
 */
public class PositionSerializer implements JsonSerializer<Position> {

    @Override
    public JsonElement serialize(Position src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray rawCoordinates = new JsonArray();
        rawCoordinates.add(new JsonPrimitive(src.getLongitude()));
        rawCoordinates.add(new JsonPrimitive(src.getLatitude()));

        // Includes altitude
        if (src.hasAltitude()) {
            rawCoordinates.add(new JsonPrimitive(src.getAltitude()));
        }

        return rawCoordinates;
    }

}
