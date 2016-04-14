package com.mapbox.services.commons.geojson;

/**
 * TODO
 * @param <T>
 */
public interface Geometry<T> extends com.mapbox.services.commons.geojson.GeoJSON {

    T getCoordinates();

}
