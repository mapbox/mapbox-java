package com.mapbox.services.commons.geojson;

import java.util.List;

/**
 * A GeoJSON object with the type "FeatureCollection" is a feature object which represents a
 * collection of feature objects.
 *
 * @see <a href='geojson.org/geojson-spec.html#feature-collection-objects'>Official GeoJSON FeatureCollection Specifications</a>
 */
public class FeatureCollection extends BaseFeatureCollection {

    private final List<Feature> features;

    /**
     * Protected constructor.
     * Unlike other GeoJSON objects in this package, this constructor is protected to enable
     * the deserialization of the Map Matching service response.
     *
     * @param features List of {@link Feature}.
     */
    protected FeatureCollection(List<Feature> features) {
        this.features = features;
    }

    /**
     * Get the List containing all the features within collection.
     *
     * @return List of features within collection.
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * Create a {@link FeatureCollection} from a List of features.
     *
     * @param features List of {@link Feature}
     * @return new {@link FeatureCollection}
     */
    public static FeatureCollection fromFeatures(List<Feature> features) {
        return new FeatureCollection(features);
    }

}
