package com.mapbox.services.mapmatching.v4.models;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

/**
 * Created by ivo on 17/05/16.
 */
public class MapMatchingResponse extends FeatureCollection {
    private String code;

    /**
     * Private constructor.
     *
     * @param features List of {@link Feature}.
     */
    private MapMatchingResponse(java.util.List<Feature> features) {
        super(features);
    }

    /**
     * @return a string depicting the state of the response
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code a string depicting the state of the response
     */
    public void setCode(String code) {
        this.code = code;
    }

}
