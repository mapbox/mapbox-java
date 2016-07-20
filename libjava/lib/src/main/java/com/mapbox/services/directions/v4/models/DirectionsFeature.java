package com.mapbox.services.directions.v4.models;

/**
 * Defines a <a href="http://geojson.org/geojson-spec.html#feature-objects">GeoJSON Feature Object</a>
 * with a Point geometry type.
 *
 * @since 1.0.0
 */
@Deprecated
public class DirectionsFeature {

    private String type;
    private FeatureGeometry geometry;
    private FeatureProperties properties;

    /**
     * The type of the GeoJSON object.
     *
     * @return string type.
     * @since 1.0.0
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type of the GeoJSON object.
     * @since 1.0.0
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * {@link FeatureGeometry} object contains type and the coordinates.
     *
     * @return {@link FeatureGeometry} object.
     * @since 1.0.0
     */
    public FeatureGeometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry {@link FeatureGeometry} object contains type and the coordinates.
     * @since 1.0.0
     */
    public void setGeometry(FeatureGeometry geometry) {
        this.geometry = geometry;
    }

    /**
     * Properties describing the point. This includes, at a minimum, a name.
     *
     * @return {@link FeatureProperties} object.
     * @since 1.0.0
     */
    public FeatureProperties getProperties() {
        return properties;
    }

    /**
     * @param properties Properties describing the point. This includes, at a minimum, a name.
     * @since 1.0.0
     */
    public void setProperties(FeatureProperties properties) {
        this.properties = properties;
    }
}
