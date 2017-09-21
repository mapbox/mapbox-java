package com.mapbox.services.commons.geojson;

import java.util.Arrays;
import java.util.List;

/**
 * A GeoJSON object with the type "FeatureCollection" is a feature object which represents a
 * collection of feature objects.
 *
 * @see <a href='geojson.org/geojson-spec.html#feature-collection-objects'>Official GeoJSON FeatureCollection Specifications</a>
 * @since 1.0.0
 */
public class FeatureCollection extends BaseFeatureCollection {

  private final List<Feature> features;

  /**
   * Protected constructor.
   * Unlike other GeoJSON objects in this package, this constructor is protected to enable
   * the deserialization of the Map Matching service response.
   *
   * @param features List of {@link Feature}.
   * @since 1.0.0
   */
  protected FeatureCollection(List<Feature> features) {
    this.features = features;
  }

  /**
   * Get the List containing all the features within collection.
   *
   * @return List of features within collection.
   * @since 1.0.0
   */
  public List<Feature> getFeatures() {
    return features;
  }

  /**
   * Create a {@link FeatureCollection} from a List of features.
   *
   * @param features List of {@link Feature}
   * @return new {@link FeatureCollection}
   * @since 1.0.0
   */
  public static FeatureCollection fromFeatures(List<Feature> features) {
    return new FeatureCollection(features);
  }

  /**
   * Create a {@link FeatureCollection} from an array of features.
   *
   * @param features Array of {@link Feature}
   * @return new {@link FeatureCollection}
   * @since 1.0.0
   */
  public static FeatureCollection fromFeatures(Feature[] features) {
    return new FeatureCollection(Arrays.asList(features));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    FeatureCollection that = (FeatureCollection) o;

    return features != null ? features.equals(that.features) : that.features == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (features != null ? features.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return this.toJson();
  }
}
