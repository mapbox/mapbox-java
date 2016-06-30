package com.mapbox.services.geocoding.v5.models;

import java.util.List;

/**
 * The response object for a geocoder query.
 */
public class GeocodingResponse extends CarmenFeatureCollection {

    protected GeocodingResponse(List<CarmenFeature> features) {
        super(features);
    }

}
