package com.mapbox.services.commons.geojson;

/**
 * Created by antonio on 1/30/16.
 */
public interface Geometry<T> extends com.mapbox.services.commons.geojson.GeoJSON {

    T getCoordinates();

}
