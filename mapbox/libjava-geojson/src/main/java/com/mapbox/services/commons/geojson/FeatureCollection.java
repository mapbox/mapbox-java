package com.mapbox.services.commons.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * This represents a GeoJSON Feature Collection which holds a list of {@link Feature} objects (when
 * serialized the feature list becomes a JSON array).
 * <p>
 * Note that the feature list could potentially be empty. Features within the list must follow the
 * specifications defined inside the {@link Feature} class.
 * <p>
 * An example of a Feature Collections given below:
 * <pre>
 * {
 *   "type": "FeatureCollection",
 *   "bbox": [100.0, 0.0, -100.0, 105.0, 1.0, 0.0],
 *   "features": [
 *     //...
 *   ]
 * }
 * </pre>
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class FeatureCollection implements GeoJSON, Serializable {

  private static final String type = "FeatureCollection";

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a FeatureCollection object from scratch it is better to use one of the other provided
   * static factory methods such as {@link #fromFeatures(List)}.
   *
   * @param json a formatted valid JSON string defining a GeoJSON Feature Collection
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 1.0.0
   */
  public static FeatureCollection fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    return gson.create().fromJson(json, FeatureCollection.class);
  }

  /**
   * Create a new instance of this class by giving the feature collection an array of
   * {@link Feature}s. The array of features itself isn't null but it can empty and have a length of
   * 0.
   *
   * @param features an array of features
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 1.0.0
   */
  public static FeatureCollection fromFeatures(@NonNull Feature[] features) {
    return fromFeatures(Arrays.asList(features), null);
  }

  /**
   * Create a new instance of this class by giving the feature collection an array of
   * {@link Feature}s. The array of features itself isn't null but it can empty and have a length of
   * 0.
   *
   * @param features an array of features
   * @param bbox     optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 3.0.0
   */
  public static FeatureCollection fromFeatures(@NonNull Feature[] features,
                                               @Nullable double[] bbox) {
    return fromFeatures(Arrays.asList(features), bbox);
  }


  public static FeatureCollection fromFeatures(@NonNull List<Feature> features) {
    return fromFeatures(features, null);
  }

  public static FeatureCollection fromFeatures(@NonNull List<Feature> features,
                                               @Nullable double[] bbox) {
    return new AutoValue_FeatureCollection(features, bbox);
  }


  public abstract List<Feature> features();

  @NonNull
  @Override
  public String type() {
    return type;
  }

  @Nullable
  @Override
  public abstract double[] bbox();

  public String toJson() {
    return new Gson().toJson(this);
  }
}
