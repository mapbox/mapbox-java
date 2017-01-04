package com.mapbox.services.commons.geojson;

/**
 * Interface implemented by all GeoJSON objects, contains common fields.
 *
 * @since 1.0.0
 */
public interface GeoJSON {

  String getType();

  String toJson();

}
