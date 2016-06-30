package com.mapbox.services.geocoding.v5.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.lang.reflect.Type;

/**
 * Created by antonio on 6/30/16.
 */
public class CarmenGeometryDeserializer implements JsonDeserializer<Geometry> {

    @Override
    public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) json;
        String geometryType = jsonObject.get("type").getAsString();
        if (geometryType.equals("Point")) {
            JsonArray coordinates = jsonObject.getAsJsonArray("coordinates");
            return Point.fromCoordinates(Position.fromCoordinates(
                    coordinates.get(0).getAsDouble(),
                    coordinates.get(1).getAsDouble()));
        } else {
            throw new JsonParseException("Unexpected geometry found: " + geometryType);
        }
    }

}
