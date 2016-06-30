package com.mapbox.services.geocoding.v5.models;

import com.mapbox.services.commons.geojson.BaseFeatureCollection;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import java.util.List;

/**
 * Created by antonio on 6/30/16.
 */
public class CarmenFeatureCollection extends BaseFeatureCollection {

    private List<String> query;
    private String attribution;
    private final List<CarmenFeature> features;

    /**
     * Protected constructor.
     * Unlike other GeoJSON objects, this constructor is protected to enable the deserialization
     * of the Geocoding service responses.
     *
     * @param features List of {@link Feature}.
     */
    protected CarmenFeatureCollection(List<CarmenFeature> features) {
        this.features = features;
    }

    /**
     * A place name for forward geocoding or a coordinate pair (longitude, latitude location) for
     * reverse geocoding.
     *
     * @return a List containing your search query.
     */
    public List<String> getQuery() {
        return this.query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    /**
     * Mapbox attribution.
     *
     * @return String with Mapbox attribution.
     */
    public String getAttribution() {
        return this.attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    /**
     * Get the List containing all the features within collection.
     *
     * @return List of features within collection.
     */
    public List<CarmenFeature> getFeatures() {
        return features;
    }

    /**
     * Create a {@link FeatureCollection} from a List of features.
     *
     * @param features List of {@link Feature}
     * @return new {@link FeatureCollection}
     */
    public static CarmenFeatureCollection fromFeatures(List<CarmenFeature> features) {
        return new CarmenFeatureCollection(features);
    }

}
