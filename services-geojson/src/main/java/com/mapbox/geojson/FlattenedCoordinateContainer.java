package com.mapbox.geojson;

interface FlattenedCoordinateContainer<T, P> extends CoordinateContainer<T> {
  P flattenCoordinates();
}
