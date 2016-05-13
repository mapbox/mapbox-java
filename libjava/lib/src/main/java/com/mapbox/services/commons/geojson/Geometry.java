package com.mapbox.services.commons.geojson;

/**
 * Interface implemented by all Geometry objects, contains common fields.
 * @param <T> the type of the coordinates, normally a list interface of positions.
 */
public interface Geometry<T> extends GeoJSON {

    T getCoordinates();
    void setCoordinates(T coordinates);

}
