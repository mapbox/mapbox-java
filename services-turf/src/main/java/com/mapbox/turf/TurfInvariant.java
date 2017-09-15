package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Point;
import com.mapbox.services.utils.TextUtils;

/**
 * Also called Assertions, these methods enforce expectations of a certain type or calculate various
 * shapes from given points.
 *
 * @see <a href="http://turfjs.org/docs/">Turf documentation</a>
 * @since 1.2.0
 */
public class TurfInvariant {

  /**
   * Unwrap a coordinate {@link Point} from a Feature with a Point geometry.
   *
   * @param obj any value
   * @return a coordinate
   * @throws TurfException signals that a Turf exception of some sort has occurred
   * @see <a href="http://turfjs.org/docs/#getcoord">Turf getCoord documentation</a>
   * @since 1.2.0
   */
  public static Point getCoord(Feature obj) throws TurfException {
    if (obj.geometry() instanceof Point) {
      return (Point) obj.geometry();
    }
    throw new TurfException("A feature with a Point geometry is required.");
  }

    /**
     * Enforce expectations about types of GeoJson objects for Turf.
     *
     * @param value any GeoJson object
     * @param type  expected GeoJson type
     * @param name  name of calling function
     * @throws TurfException signals that a Turf exception of some sort has occurred
     * @see <a href="http://turfjs.org/docs/#geojsontype">Turf geojsonType documentation</a>
     * @since 1.2.0
     */
  public static void geojsonType(GeoJson value, String type, String name) throws TurfException {
    if (TextUtils.isEmpty(type) || TextUtils.isEmpty(name)) {
      throw new TurfException("Type and name required");
    }
    if (value == null || !value.type().equals(type)) {
      throw new TurfException("Invalid input to " + name + ": must be a " + type
        + ", given " + (value != null ? value.type() : " null"));
    }
  }

  /**
   * Enforce expectations about types of {@link Feature} inputs for Turf. Internally this uses
   * {@link Feature#type()} to judge geometry types.
   *
   * @param feature with an expected geometry type
   * @param type    type expected GeoJson type
   * @param name    name of calling function
   * @throws TurfException signals that a Turf exception of some sort has occurred
   * @see <a href="http://turfjs.org/docs/#featureof">Turf featureOf documentation</a>
   * @since 1.2.0
   */
  public static void featureOf(Feature feature, String type, String name) throws TurfException {
    if (TextUtils.isEmpty(name)) {
      throw new TurfException(".featureOf() requires a name");
    }
    if (feature == null || !feature.type().equals("Feature") || feature.geometry() == null) {
      throw new TurfException("Invalid input to " + name + ", Feature with geometry required");
    }
    if (feature.geometry() == null || !feature.geometry().type().equals(type)) {
      throw new TurfException("Invalid input to " + name + ": must be a " + type
        + ", given " + feature.geometry().type());
    }
  }

  /**
   * Enforce expectations about types of {@link FeatureCollection} inputs for Turf. Internally
   * this uses {@link Feature#type()}} to judge geometry types.
   *
   * @param featureCollection for which features will be judged
   * @param type              expected GeoJson type
   * @param name              name of calling function
   * @throws TurfException signals that a Turf exception of some sort has occurred
   * @see <a href="http://turfjs.org/docs/#collectionof">Turf collectionOf documentation</a>
   * @since 1.2.0
   */
  public static void collectionOf(FeatureCollection featureCollection, String type, String name)
    throws TurfException {
    if (TextUtils.isEmpty(name)) {
      throw new TurfException("collectionOf() requires a name");
    }
    if (featureCollection == null || !featureCollection.type().equals("FeatureCollection")
      || featureCollection.features() == null) {
      throw new TurfException("Invalid input to " + name + ", FeatureCollection required");
    }
    for (Feature feature : featureCollection.features()) {
      if (feature == null || !feature.type().equals("Feature") || feature.geometry() == null) {
        throw new TurfException("Invalid input to " + name + ", Feature with geometry required");
      }
      if (feature.geometry() == null || !feature.geometry().type().equals(type)) {
        throw new TurfException("Invalid input to " + name + ": must be a " + type
          + ", given " + feature.geometry().type());
      }
    }
  }
}
