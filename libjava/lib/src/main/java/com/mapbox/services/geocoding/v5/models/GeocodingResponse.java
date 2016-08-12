package com.mapbox.services.geocoding.v5.models;

import java.util.List;

/**
 * The response object for a geocoder query.
 *
 * @since 1.0.0
 */
public class GeocodingResponse extends CarmenFeatureCollection {

    public GeocodingResponse(List<CarmenFeature> features) {
        super(features);
    }

}
