package com.mapbox.services.commons.geojson;

/**
 * Interface implemented by all GeoJSON objects, contains common fields.
 *
 * @since 1.0.0
 */
public interface GeoJSON {

  String type();

  String toJson();

  /**
   * A GeoJSON object MAY have a member named "bbox" to include information on the coordinate range
   * for its Geometries, Features, or FeatureCollections.  The value of the bbox member MUST be an
   * array of length 2*n where n is the number of dimensions represented in the contained
   * geometries, with all axes of the most southwesterly point followed by all axes of the more
   * northeasterly point.  The axes order of a bbox follows the axes order of geometries.
   *
   * @return
   */
  double[] bbox();

}
