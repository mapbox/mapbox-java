package com.mapbox.services.mapmatching.v4.models;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import java.util.List;

/**
 * Created by ivo on 17/05/16.
 */
public class MapMatchingResponse extends FeatureCollection {

    private String code;

    protected MapMatchingResponse(List<Feature> features) {
        super(features);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
