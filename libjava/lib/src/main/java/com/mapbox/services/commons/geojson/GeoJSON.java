package com.mapbox.services.commons.geojson;

/**
 * Interface implemented by all GeoJSON objects, contains common fields.
 */
public interface GeoJSON {

    String getType();
    String toJson();

}
