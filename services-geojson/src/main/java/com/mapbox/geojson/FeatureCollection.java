package com.mapbox.geojson;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;

import java.io.IOException;
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
@Keep
public final class FeatureCollection implements GeoJson {

  private static final String TYPE = "FeatureCollection";

  private final String type;

  @JsonAdapter(BoundingBoxTypeAdapter.class)
  private final BoundingBox bbox;

  private final List<Feature> features;

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
    gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
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
    return new FeatureCollection(TYPE, null, Arrays.asList(features));
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
    return new FeatureCollection(TYPE, null, features);
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
    return new FeatureCollection(TYPE, bbox, Arrays.asList(features));
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
    return new FeatureCollection(TYPE, bbox, features);
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
    List<Feature> featureList = Arrays.asList(feature);
    return new FeatureCollection(TYPE, null, featureList);
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
    List<Feature> featureList = Arrays.asList(feature);
    return new FeatureCollection(TYPE, bbox, featureList);
  }

  FeatureCollection(String type, @Nullable BoundingBox bbox, @Nullable List<Feature> features) {
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
    this.bbox = bbox;
    this.features = features;
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
    return type;
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
  public BoundingBox bbox() {
    return bbox;
  }

  /**
   * This provides the list of feature making up this Feature Collection. Note that if the
   * FeatureCollection was created through {@link #fromJson(String)} this list could be null.
   * Otherwise, the list can't be null but the size of the list can equal 0.
   *
   * @return a list of {@link Feature}s which make up this Feature Collection
   * @since 1.0.0
   */
  @Nullable
  public List<Feature> features() {
    return features;
  }

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
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
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
    return new FeatureCollection.GsonTypeAdapter(gson);
  }

  @Override
  public String toString() {
    return "FeatureCollection{"
            + "type=" + type + ", "
            + "bbox=" + bbox + ", "
            + "features=" + features
            + "}";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof FeatureCollection) {
      FeatureCollection that = (FeatureCollection) obj;
      return (this.type.equals(that.type()))
              && ((this.bbox == null) ? (that.bbox() == null) : this.bbox.equals(that.bbox()))
              && ((this.features == null)
                   ? (that.features() == null) : this.features.equals(that.features()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode *= 1000003;
    hashCode ^= type.hashCode();
    hashCode *= 1000003;
    hashCode ^= (bbox == null) ? 0 : bbox.hashCode();
    hashCode *= 1000003;
    hashCode ^= (features == null) ? 0 : features.hashCode();
    return hashCode;
  }

  /**
   * TypeAdapter to serialize/deserialize FeatureCollection objects.
   *
   * @since 4.6.0
   */
  static final class GsonTypeAdapter extends TypeAdapter<FeatureCollection> {
    private volatile TypeAdapter<String> stringAdapter;
    private volatile TypeAdapter<BoundingBox> boundingBoxAdapter;
    private volatile TypeAdapter<List<Feature>> listFeatureAdapter;
    private final Gson gson;

    GsonTypeAdapter(Gson gson) {
      this.gson = gson;
    }

    @Override
    public void write(JsonWriter jsonWriter, FeatureCollection object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("type");
      if (object.type() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> stringAdapter = this.stringAdapter;
        if (stringAdapter == null) {
          stringAdapter = gson.getAdapter(String.class);
          this.stringAdapter = stringAdapter;
        }
        stringAdapter.write(jsonWriter, object.type());
      }
      jsonWriter.name("bbox");
      if (object.bbox() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<BoundingBox> boundingBoxTypeAdapter = this.boundingBoxAdapter;
        if (boundingBoxTypeAdapter == null) {
          boundingBoxTypeAdapter = gson.getAdapter(BoundingBox.class);
          this.boundingBoxAdapter = boundingBoxTypeAdapter;
        }
        boundingBoxTypeAdapter.write(jsonWriter, object.bbox());
      }
      jsonWriter.name("features");
      if (object.features() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<Feature>> listFeatureAdapter = this.listFeatureAdapter;
        if (listFeatureAdapter == null) {
          TypeToken typeToken = TypeToken.getParameterized(List.class, Feature.class);
          listFeatureAdapter =
            (TypeAdapter<List<Feature>>) gson.getAdapter(typeToken);
          this.listFeatureAdapter = listFeatureAdapter;
        }
        listFeatureAdapter.write(jsonWriter, object.features());
      }
      jsonWriter.endObject();
    }

    @Override
    public FeatureCollection read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String type = null;
      BoundingBox bbox = null;
      List<Feature> features = null;
      while (jsonReader.hasNext()) {
        String name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (name) {
          case "type":
            TypeAdapter<String> stringAdapter = this.stringAdapter;
            if (stringAdapter == null) {
              stringAdapter = gson.getAdapter(String.class);
              this.stringAdapter = stringAdapter;
            }
            type = stringAdapter.read(jsonReader);
            break;

          case "bbox":
            TypeAdapter<BoundingBox> boundingBoxAdapter = this.boundingBoxAdapter;
            if (boundingBoxAdapter == null) {
              boundingBoxAdapter = gson.getAdapter(BoundingBox.class);
              this.boundingBoxAdapter = boundingBoxAdapter;
            }
            bbox = boundingBoxAdapter.read(jsonReader);
            break;

          case "features":
            TypeAdapter<List<Feature>> listFeatureAdapter = this.listFeatureAdapter;
            if (listFeatureAdapter == null) {
              TypeToken typeToken = TypeToken.getParameterized(List.class, Feature.class);
              listFeatureAdapter =
                (TypeAdapter<List<Feature>>) gson.getAdapter(typeToken);
              this.listFeatureAdapter = listFeatureAdapter;
            }
            features = listFeatureAdapter.read(jsonReader);
            break;

          default:
            jsonReader.skipValue();

        }
      }
      jsonReader.endObject();
      return new FeatureCollection(type, bbox, features);
    }
  }
}
