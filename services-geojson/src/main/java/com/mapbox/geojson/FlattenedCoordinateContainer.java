package com.mapbox.geojson;

import androidx.annotation.Keep;

@Keep
interface FlattenedCoordinateContainer<T, P> extends CoordinateContainer<T> {
  P flattenCoordinates();
}
