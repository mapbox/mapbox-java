package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeoJson;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.Point;

/**
 * Also called Assertions, these methods enforce expectations of a certain type or calculate various
 * shapes from given points.
 *
 * @see <a href="http://turfjs.org/docs/">Turf documentation</a>
 * @since 1.2.0
 */
public final class TurfAssertions {

  private TurfAssertions() {
    // Private constructor preventing initialization of this class
  }

  /**
   * Unwrap a coordinate {@link Point} from a Feature with a Point geometry.
   *
   * @param obj any value
   * @return a coordinate
   * @see <a href="http://turfjs.org/docs/#getcoord">Turf getCoord documentation</a>
   * @since 1.2.0
   * @deprecated use {@link TurfMeta#getCoord(Feature)}
   */
  @Deprecated
  public static Point getCoord(Feature obj) {
    return TurfMeta.getCoord(obj);
  }

  /**
   * Enforce expectations about types of GeoJson objects for Turf.
   *
   * @param value any GeoJson object
   * @param type  expected GeoJson type
   * @param name  name of calling function
   * @see <a href="http://turfjs.org/docs/#geojsontype">Turf geojsonType documentation</a>
   * @since 1.2.0
   */
  public static void geojsonType(GeoJson value, String type, String name) {
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
   * @see <a href="http://turfjs.org/docs/#featureof">Turf featureOf documentation</a>
   * @since 1.2.0
   */
  public static void featureOf(Feature feature, String type, String name) {
    if (TextUtils.isEmpty(name)) {
      throw new TurfException(".featureOf() requires a name");
    }
    if (feature == null || !feature.type().equals("Feature") || feature.geometry() == null) {
      throw new TurfException(String.format(
        "Invalid input to %s, Feature with geometry required", name));
    }
    if (feature.geometry() == null || !feature.geometry().type().equals(type)) {
      throw new TurfException(String.format(
        "Invalid input to %s: must be a %s, given %s", name, type, feature.geometry().type()));
    }
  }

  /**
   * Enforce expectations about types of {@link FeatureCollection} inputs for Turf. Internally
   * this uses {@link Feature#type()}} to judge geometry types.
   *
   * @param featureCollection for which features will be judged
   * @param type              expected GeoJson type
   * @param name              name of calling function
   * @see <a href="http://turfjs.org/docs/#collectionof">Turf collectionOf documentation</a>
   * @since 1.2.0
   */
  public static void collectionOf(FeatureCollection featureCollection, String type, String name) {
    if (TextUtils.isEmpty(name)) {
      throw new TurfException("collectionOf() requires a name");
    }
    if (featureCollection == null || !featureCollection.type().equals("FeatureCollection")
      || featureCollection.features() == null) {
      throw new TurfException(String.format(
        "Invalid input to %s, FeatureCollection required", name));
    }
    for (Feature feature : featureCollection.features()) {
      if (feature == null || !feature.type().equals("Feature") || feature.geometry() == null) {
        throw new TurfException(String.format(
          "Invalid input to %s, Feature with geometry required", name));
      }
      if (feature.geometry() == null || !feature.geometry().type().equals(type)) {
        throw new TurfException(String.format(
          "Invalid input to %s: must be a %s, given %s", name, type, feature.geometry().type()));
      }
    }
  }
}
