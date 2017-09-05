package com.mapbox.services.commons.geojson;

public interface Geometry<T> extends GeoJSON {

  T coordinates();

}
