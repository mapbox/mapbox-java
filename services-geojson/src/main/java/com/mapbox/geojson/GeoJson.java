package com.mapbox.geojson;

/**
 * Generic implementation for all GeoJson objects defining common traits that each GeoJson object
 * has. This logic is carried over to {@link Geometry} which is an interface which all seven GeoJson
 * geometries implement.
 *
 * @since 1.0.0
 */
public interface GeoJson {

  /**
   * This describes the type of GeoJson geometry, Feature, or FeatureCollection this object is.
   * Every GeoJson Object will have this defined once an instance is created and will never return
   * null.
   *
   * @return a String which describes the type of geometry, for this object it will always return
   *   {@code Feature}
   * @since 1.0.0
   */
  String type();

  /**
   * This takes the currently defined values found inside the GeoJson instance and converts it to a
   * GeoJson string.
   *
   * @return a JSON string which represents this Feature
   * @since 1.0.0
   */
  String toJson();

  /**
   * A GeoJson object MAY have a member named "bbox" to include information on the coordinate range
   * for its Geometries, Features, or FeatureCollections.  The value of the bbox member MUST be an
   * array of length 2*n where n is the number of dimensions represented in the contained
   * geometries, with all axes of the most southwesterly point followed by all axes of the more
   * northeasterly point.  The axes order of a bbox follows the axes order of geometries.
   *
   * @return a double array with the length 2*n where n is the number of dimensions represented in
   *   the contained geometries
   * @since 3.0.0
   */
  BoundingBox bbox();
}
