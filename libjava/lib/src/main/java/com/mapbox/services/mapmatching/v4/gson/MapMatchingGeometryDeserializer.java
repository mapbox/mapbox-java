package com.mapbox.services.mapmatching.v4.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.LineString;

import java.lang.reflect.Type;

/**
 * A custom deserializer that assumes a Polyline string.
 * <p>
 * In case of a json object, null is returned to support the {@code geometry=false} option
 * (empty object is returned instead of null)
 *
 * @since 1.2.0
 */
public class MapMatchingGeometryDeserializer implements JsonDeserializer<Geometry> {

    /**
     * A custom deserializer that assumes a Polyline string.
     * <p>
     * In case of a json object, null is returned to support the {@code geometry=false} option
     * (empty object is returned instead of null)
     *
     * @param json    The Json data being deserialized.
     * @param typeOfT The type of the Object to deserialize to.
     * @param context Context for deserialization.
     * @return Deserialized Geometry.
     * @throws JsonParseException This exception is raised if there is a serious issue that occurs
     *                            during parsing of a Json string.
     * @since 1.2.0
     */
    @Override
    public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            return null;
        } else {
            return LineString.fromPolyline(json.getAsString(), Constants.OSRM_PRECISION_V4);
        }
    }
}
