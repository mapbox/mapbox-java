package com.mapbox.geocoding.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.GeometryDeserializer;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.PointSerializer;

import java.util.List;

/**
 * This is the initial object which gets returned when the geocoding request receives a result.
 * Since each result is a {@link CarmenFeature}, the response simply returns a list of those
 * features.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class GeocodingResponse {

  private static final String TYPE = "FeatureCollection";

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_GeocodingResponse.Builder()
      .type(TYPE);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a GeoJson Geocoding Response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static GeocodingResponse fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    return gson.create().fromJson(json, GeocodingResponse.class);
  }

  /**
   * A geocoding response will always be an extension of a {@link FeatureCollection} containing
   * additional information.
   *
   * @return the type of GeoJSON this is
   * @since 1.0.0
   */
  @Nullable
  public abstract String type();

  /**
   * A Geocoding Response might have a member named {@code bbox} to include information on the
   * coordinate range for it's {@link CarmenFeature}s. The value of the bbox member MUST be a list
   * of size 2*n where n is the number of dimensions represented in the contained feature
   * geometries, with all axes of the most southwesterly point followed by all axes of the more
   * northeasterly point. The axes order of a bbox follows the axes order of geometries.
   *
   * @return a list of double coordinate values describing a bounding box
   * @since 3.0.0
   */
  @Nullable
  public abstract BoundingBox bbox();

  /**
   * A list of space and punctuation-separated strings from the original query.
   *
   * @return a list containing the original query
   * @since 1.0.0
   */
  @Nullable
  public abstract List<String> query();

  /**
   * A list of the CarmenFeatures which contain the results and are ordered from most relevant to
   * least.
   *
   * @return a list of {@link CarmenFeature}s which each represent an individual result from the
   *   query
   * @since 1.0.0
   */
  @Nullable
  public abstract List<CarmenFeature> features();

  /**
   * A string attributing the results of the Mapbox Geocoding API to Mapbox and links to Mapbox's
   * terms of service and data sources.
   *
   * @return information about Mapbox's terms of service and the data sources
   * @since 1.0.0
   */
  @Nullable
  public abstract String attribution();

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Geocoding Response
   * @since 1.0.0
   */
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer());
    return gson.create().toJson(this);
  }

  /**
   * Gson TYPE adapter for parsing Gson t«o this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<GeocodingResponse> typeAdapter(Gson gson) {
    return new AutoValue_GeocodingResponse.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link GeocodingResponse}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * This describes the TYPE of GeoJson geometry this object is, thus this will always return
     * {@link FeatureCollection}. Note that this isn't public since it should always be set to
     * "FeatureCollection"
     *
     * @param type a String which describes the TYPE of geometry, for this object it will always
     *             return {@code FeatureCollection}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    abstract Builder type(@NonNull String type);

    /**
     * A Geocoding Response might have a member named {@code bbox} to include information on the
     * coordinate range for it's {@link CarmenFeature}s. The value of the bbox member MUST be a list
     * of size 2*n where n is the number of dimensions represented in the contained feature
     * geometries, with all axes of the most southwesterly point followed by all axes of the more
     * northeasterly point. The axes order of a bbox follows the axes order of geometries.
     *
     * @param bbox a list of double coordinate values describing a bounding box
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @Nullable
    public abstract Builder bbox(@Nullable BoundingBox bbox);

    /**
     * A list of space and punctuation-separated strings from the original query.
     *
     * @param query a list containing the original query
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder query(@Nullable List<String> query);

    /**
     * A list of the CarmenFeatures which contain the results and are ordered from most relevant to
     * least.
     *
     * @param features a list of {@link CarmenFeature}s which each represent an individual result
     *                 from the query
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder features(@Nullable List<CarmenFeature> features);

    /**
     * A string attributing the results of the Mapbox Geocoding API to Mapbox and links to Mapbox's
     * terms of service and data sources.
     *
     * @param attribution information about Mapbox's terms of service and the data sources
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder attribution(@Nullable String attribution);

    /**
     * Build a new {@link GeocodingResponse} object.
     *
     * @return a new {@link GeocodingResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract GeocodingResponse build();
  }
}
