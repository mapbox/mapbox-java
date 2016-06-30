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
 * Custom deserializer that assumes a Polyline string.
 * <p/>
 * In case of a json object, null is returned to support the geometry=false option
 * (empty object is returned instead of null)
 *
 * @see com.mapbox.services.mapmatching.v4.MapboxMapMatching.Builder
 */
public class GeometryDeserializer implements JsonDeserializer<Geometry> {

    @Override
    public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            return null;
        } else {
            return LineString.fromPolyline(json.getAsString(), Constants.OSRM_PRECISION_V4);
        }
    }

}
