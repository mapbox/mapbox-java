package com.mapbox.services.api.geocoding.v5.models;

import com.mapbox.services.commons.geojson.BaseFeatureCollection;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import java.util.List;

/**
 * The Geocoding API hierarchy starts with this class.
 *
 * @since 1.0.0
 */
public class CarmenFeatureCollection extends BaseFeatureCollection {

  private List<String> query;
  private String attribution;
  private List<CarmenFeature> features;

  public CarmenFeatureCollection() {
  }

  /**
   * Protected constructor.
   * Unlike other GeoJSON objects, this constructor is protected to enable the deserialization
   * of the Geocoding service responses.
   *
   * @param features List of {@link Feature}.
   * @since 1.0.0
   */
  protected CarmenFeatureCollection(List<CarmenFeature> features) {
    this.features = features;
  }

  /**
   * A place name for forward geocoding or a coordinate pair (longitude, latitude location) for
   * reverse geocoding.
   *
   * @return a List containing your search query.
   * @since 1.0.0
   */
  public List<String> getQuery() {
    return this.query;
  }

  /**
   * A place name for forward geocoding or a coordinate pair (longitude, latitude location)
   * for reverse geocoding.
   *
   * @param query The search terms used.
   * @since 1.0.0
   */
  public void setQuery(List<String> query) {
    this.query = query;
  }

  /**
   * Mapbox attribution.
   *
   * @return String with Mapbox attribution.
   * @since 1.0.0
   */
  public String getAttribution() {
    return this.attribution;
  }

  /**
   * @param attribution String with Mapbox attribution.
   * @since 1.0.0
   */
  public void setAttribution(String attribution) {
    this.attribution = attribution;
  }

  /**
   * Get the List containing all the features within collection.
   *
   * @return List of features within collection.
   * @since 1.0.0
   */
  public List<CarmenFeature> getFeatures() {
    return features;
  }

  /**
   * Create a {@link FeatureCollection} from a List of features.
   *
   * @param features List of {@link Feature}
   * @return new {@link FeatureCollection}
   * @since 1.0.0
   */
  public static CarmenFeatureCollection fromFeatures(List<CarmenFeature> features) {
    return new CarmenFeatureCollection(features);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CarmenFeatureCollection that = (CarmenFeatureCollection) o;

    if (getQuery() != null ? !getQuery().equals(that.getQuery()) : that.getQuery() != null) {
      return false;
    }
    if (getAttribution() != null ? !getAttribution().equals(that.getAttribution()) : that.getAttribution() != null) {
      return false;
    }
    return getFeatures() != null ? getFeatures().equals(that.getFeatures()) : that.getFeatures() == null;

  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (getQuery() != null ? getQuery().hashCode() : 0);
    result = 31 * result + (getAttribution() != null ? getAttribution().hashCode() : 0);
    result = 31 * result + (getFeatures() != null ? getFeatures().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CarmenFeatureCollection{"
      + "query=" + query
      + ", attribution='" + attribution + '\''
      + ", features=" + features
      + '}';
  }
}
