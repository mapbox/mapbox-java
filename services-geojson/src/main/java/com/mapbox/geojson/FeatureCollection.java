package com.mapbox.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.GeometryDeserializer;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.PointSerializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This represents a GeoJson Feature Collection which holds a list of {@link Feature} objects (when
 * serialized the feature list becomes a JSON array).
 * <p>
 * Note that the feature list could potentially be empty. Features within the list must follow the
 * specifications defined inside the {@link Feature} class.
 * <p>
 * An example of a Feature Collections given below:
 * <pre>
 * {
 *   "TYPE": "FeatureCollection",
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
public abstract class FeatureCollection implements GeoJson {

  @Expose
  @SerializedName("type")
  private static final String TYPE = "FeatureCollection";

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a FeatureCollection object from scratch it is better to use one of the other provided
   * static factory methods such as {@link #fromFeatures(List)}.
   *
   * @param json a formatted valid JSON string defining a GeoJson Feature Collection
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static FeatureCollection fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
    return gson.create().fromJson(json, FeatureCollection.class);
  }

  /**
   * Create a new instance of this class by giving the feature collection an array of
   * {@link Feature}s. The array of features itself isn't null but it can be empty and have a length
   * of 0.
   *
   * @param features an array of features
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static FeatureCollection fromFeatures(@NonNull Feature[] features) {
    return new AutoValue_FeatureCollection(null, Arrays.asList(features));
  }

  /**
   * Create a new instance of this class by giving the feature collection a list of
   * {@link Feature}s. The list of features itself isn't null but it can empty and have a size of 0.
   *
   * @param features a list of features
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static FeatureCollection fromFeatures(@NonNull List<Feature> features) {
    return new AutoValue_FeatureCollection(null, features);
  }

  /**
   * Create a new instance of this class by giving the feature collection a single {@link Feature}.
   *
   * @param feature a single feature
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static FeatureCollection fromFeature(@NonNull Feature feature) {
    List<Feature> featureList = new ArrayList<>();
    featureList.add(feature);
    return new AutoValue_FeatureCollection(null, featureList);
  }

  /**
   * Create a new instance of this class by giving the feature collection an array of
   * {@link Feature}s. The array of features itself isn't null but it can be empty and have a length
   * of 0.
   *
   * @param features an array of features
   * @param bbox     optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static FeatureCollection fromFeatures(@NonNull Feature[] features,
                                               @Nullable BoundingBox bbox) {
    return new AutoValue_FeatureCollection(bbox, Arrays.asList(features));
  }

  /**
   * Create a new instance of this class by giving the feature collection a list of
   * {@link Feature}s. The list of features itself isn't null but it can be empty and have a size of
   * 0.
   *
   * @param features a list of features
   * @param bbox     optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static FeatureCollection fromFeatures(@NonNull List<Feature> features,
                                               @Nullable BoundingBox bbox) {
    return new AutoValue_FeatureCollection(bbox, features);
  }

  /**
   * Create a new instance of this class by giving the feature collection a single {@link Feature}.
   *
   * @param feature a single feature
   * @param bbox    optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static FeatureCollection fromFeature(@NonNull Feature feature,
                                              @Nullable BoundingBox bbox) {
    List<Feature> featureList = new ArrayList<>();
    featureList.add(feature);
    return new AutoValue_FeatureCollection(bbox, featureList);
  }

  /**
   * This describes the type of GeoJson this object is, thus this will always return
   * {@link FeatureCollection}.
   *
   * @return a String which describes the TYPE of GeoJson, for this object it will always return
   *   {@code FeatureCollection}
   * @since 1.0.0
   */
  @NonNull
  @Override
  public String type() {
    return TYPE;
  }

  /**
   * A Feature Collection might have a member named {@code bbox} to include information on the
   * coordinate range for it's {@link Feature}s. The value of the bbox member MUST be a list of
   * size 2*n where n is the number of dimensions represented in the contained feature geometries,
   * with all axes of the most southwesterly point followed by all axes of the more northeasterly
   * point. The axes order of a bbox follows the axes order of geometries.
   *
   * @return a list of double coordinate values describing a bounding box
   * @since 3.0.0
   */
  @Nullable
  @Override
  public abstract BoundingBox bbox();

  /**
   * This provides the list of feature making up this Feature Collection. Note that if the
   * FeatureCollection was created through {@link #fromJson(String)} this list could be null.
   * Otherwise, the list can't be null but the size of the list can equal 0.
   *
   * @return a list of {@link Feature}s which make up this Feature Collection
   * @since 1.0.0
   */
  @Nullable
  public abstract List<Feature> features();

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Feature Collection
   * @since 1.0.0
   */
  @Override
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer());
    gson.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
    gson.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    return gson.create().toJson(this);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<FeatureCollection> typeAdapter(Gson gson) {
    return new AutoValue_FeatureCollection.GsonTypeAdapter(gson);
  }
}
