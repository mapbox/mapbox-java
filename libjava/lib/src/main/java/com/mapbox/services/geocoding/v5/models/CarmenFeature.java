package com.mapbox.services.geocoding.v5.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * The Features key in the geocoding API response contains the majority of information you'll want
 * to use.
 *
 * @see <a href="https://github.com/mapbox/carmen/blob/master/carmen-geojson.md">Carmen Geojson information</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#geocoding">Mapbox geocoder documentation</a>
 * @see <a href='geojson.org/geojson-spec.html#feature-objects'>Official GeoJSON Feature Specifications</a>
 * @since 1.0.0
 */
public class CarmenFeature extends Feature {

    private String text;
    @SerializedName("place_name")
    private String placeName;
    private double[] bbox;
    private String address;
    private double[] center;
    private List<CarmenContext> context;

    private double relevance;

    /**
     * Private constructor.
     */
    private CarmenFeature(Geometry geometry, JsonObject properties, String id) {
        super(geometry, properties, id);
    }

    /**
     * @return Text representing the feature (e.g. "Austin").
     * @since 1.0.0
     */
    public String getText() {
        return text;
    }

    /**
     * @return Human-readable text representing the full result hierarchy (e.g. "Austin, Texas, United States").
     * @since 1.0.0
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * @return Array bounding box of the form [minx, miny, maxx, maxy].
     * @since 1.0.0
     */
    public double[] getBbox() {
        return bbox;
    }

    /**
     * @return Where applicable. Contains the housenumber for the returned feature
     * @since 1.0.0
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return Array of the form [lon, lat].
     * @since 1.0.0
     */
    public double[] getCenter() {
        return center;
    }

    /**
     * @return Array representing a hierarchy of parents. Each parent includes id, text keys.
     * @since 1.0.0
     */
    public List<CarmenContext> getContext() {
        return context;
    }

    /**
     * You can use the relevance property to remove rough results if you require a response that
     * matches your whole query.
     *
     * @return double value between 0 and 1.
     * @since 1.0.0
     */
    public double getRelevance() {
        return relevance;
    }

    /**
     * Util to transform center into a Position object
     *
     * @return a {@link Position} representing the center.
     * @since 1.0.0
     */
    public Position asPosition() {
        return Position.fromCoordinates(center[0], center[1]);
    }

    /**
     * Human-readable text representing the full result hierarchy
     * (e.g. "Austin, Texas, United States").
     *
     * @return String with human-readable text.
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return getPlaceName();
    }
}
