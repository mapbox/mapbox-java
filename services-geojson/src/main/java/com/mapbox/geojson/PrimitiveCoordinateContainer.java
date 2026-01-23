package com.mapbox.geojson;

interface PrimitiveCoordinateContainer<T, P> extends CoordinateContainer<T> {
  P coordinatesPrimitives();
}
