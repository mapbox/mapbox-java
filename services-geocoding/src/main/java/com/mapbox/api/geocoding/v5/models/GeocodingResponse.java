package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * This is the initial object which gets returned when the geocoding request receives a result.
 * Since each result is a {@link CarmenFeature}, the response simply returns a list of those
 * features.
 *
 * @since 1.0.0
 */
public class GeocodingResponse implements Serializable {

  private static final String TYPE = "FeatureCollection";

  private final String type;

  private final List<String> query;

  private final List<CarmenFeature> features;

  private final String attribution;

  GeocodingResponse(
          String type,
          List<String> query,
          List<CarmenFeature> features,
          String attribution) {
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
    if (query == null) {
      throw new NullPointerException("Null query");
    }
    this.query = query;
    if (features == null) {
      throw new NullPointerException("Null features");
    }
    this.features = features;
    if (attribution == null) {
      throw new NullPointerException("Null attribution");
    }
    this.attribution = attribution;
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a GeoJson Geocoding Response
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  @NonNull
  public static GeocodingResponse fromJson(@NonNull String json) {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter())
      .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
      .create();
    return gson.fromJson(json, GeocodingResponse.class);
  }

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  @NonNull
  public static Builder builder() {
    return new GeocodingResponse.Builder()
      .type(TYPE);
  }

  /**
   * A geocoding response will always be an extension of a {@link FeatureCollection} containing
   * additional information.
   *
   * @return the type of GeoJSON this is
   * @since 1.0.0
   */
  @NonNull
  public String type()  {
    return type;
  }

  /**
   * A list of space and punctuation-separated strings from the original query.
   *
   * @return a list containing the original query
   * @since 1.0.0
   */
  @NonNull
  public List<String> query()  {
    return query;
  }

  /**
   * A list of the CarmenFeatures which contain the results and are ordered from most relevant to
   * least.
   *
   * @return a list of {@link CarmenFeature}s which each represent an individual result from the
   *   query
   * @since 1.0.0
   */
  @NonNull
  public List<CarmenFeature> features()  {
    return features;
  }

  /**
   * A string attributing the results of the Mapbox Geocoding API to Mapbox and links to Mapbox's
   * terms of service and data sources.
   *
   * @return information about Mapbox's terms of service and the data sources
   * @since 1.0.0
   */
  @NonNull
  public String attribution()  {
    return attribution;
  }

  /**
   * Convert the current {@link GeocodingResponse} to its builder holding the currently assigned
   * values. This allows you to modify a single variable and then rebuild the object resulting in
   * an updated and modified {@link GeocodingResponse}.
   *
   * @return a {@link GeocodingResponse.Builder} with the same values set to match the ones defined
   *   in this {@link GeocodingResponse}
   * @since 3.0.0
   */
  @NonNull
  public Builder toBuilder()  {
    return new Builder(this);
  }


  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Geocoding Response
   * @since 1.0.0
   */
  @NonNull
  public String toJson() {
    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter())
      .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
      .create();
    return gson.toJson(this, GeocodingResponse.class);
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<GeocodingResponse> typeAdapter(Gson gson) {
    return new GeocodingResponse.GsonTypeAdapter(gson);
  }

  @Override
  public String toString() {
    return "GeocodingResponse{"
            + "type=" + type + ", "
            + "query=" + query + ", "
            + "features=" + features + ", "
            + "attribution=" + attribution
            + "}";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!(obj instanceof GeocodingResponse)) {
      return false;
    }
    GeocodingResponse that = (GeocodingResponse) obj;
    return Objects.equals(type, that.type)
            && Objects.equals(query, that.query)
            && Objects.equals(features, that.features)
            && Objects.equals(attribution, that.attribution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, query, features, attribution);
  }

  /**
   * This builder can be used to set the values describing the {@link GeocodingResponse}.
   *
   * @since 3.0.0
   */
  @SuppressWarnings("unused")
  public static class Builder {

    private String type;
    private List<String> query;
    private List<CarmenFeature> features;
    private String attribution;

    Builder() {
    }

    private Builder(GeocodingResponse source) {
      this.type = source.type();
      this.query = source.query();
      this.features = source.features();
      this.attribution = source.attribution();
    }

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
    Builder type(@NonNull String type) {
      if (type == null) {
        throw new NullPointerException("Null type");
      }
      this.type = type;
      return this;
    }

    /**
     * A list of space and punctuation-separated strings from the original query.
     *
     * @param query a list containing the original query
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder query(@NonNull List<String> query) {
      if (query == null) {
        throw new NullPointerException("Null query");
      }
      this.query = query;
      return this;
    }

    /**
     * A list of the CarmenFeatures which contain the results and are ordered from most relevant to
     * least.
     *
     * @param features a list of {@link CarmenFeature}s which each represent an individual result
     *                 from the query
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder features(@NonNull List<CarmenFeature> features) {
      if (features == null) {
        throw new NullPointerException("Null features");
      }
      this.features = features;
      return this;
    }

    /**
     * A string attributing the results of the Mapbox Geocoding API to Mapbox and links to Mapbox's
     * terms of service and data sources.
     *
     * @param attribution information about Mapbox's terms of service and the data sources
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder attribution(@NonNull String attribution) {
      if (attribution == null) {
        throw new NullPointerException("Null attribution");
      }
      this.attribution = attribution;
      return this;
    }

    /**
     * Build a new {@link GeocodingResponse} object.
     *
     * @return a new {@link GeocodingResponse} using the provided values in this builder
     * @since 3.0.0
     */
    public GeocodingResponse build()  {
      String missing = "";
      if (this.type == null) {
        missing += " type";
      }
      if (this.query == null) {
        missing += " query";
      }
      if (this.features == null) {
        missing += " features";
      }
      if (this.attribution == null) {
        missing += " attribution";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new GeocodingResponse(
              this.type,
              this.query,
              this.features,
              this.attribution);
    }
  }

  /**
   * @since 4.9.0
   */
  private static final class GsonTypeAdapter extends GeocodingTypeAdapter<GeocodingResponse> {
    private volatile TypeAdapter<List<String>> listOfStringAdapter;
    private volatile TypeAdapter<List<CarmenFeature>> listOfCarmenFeatureAdapter;

    GsonTypeAdapter(Gson gson) {
      super(gson);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, GeocodingResponse object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      writeString("type", object.type(), jsonWriter);

      jsonWriter.name("query");
      if (object.query() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<String>> listOfStringAdapter = this.listOfStringAdapter;
        if (listOfStringAdapter == null) {
          listOfStringAdapter =
            (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class,
              String.class));
          this.listOfStringAdapter = listOfStringAdapter;
        }
        listOfStringAdapter.write(jsonWriter, object.query());
      }

      jsonWriter.name("features");
      if (object.features() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<CarmenFeature>> listOfCarmenFeatureAdapter =
          this.listOfCarmenFeatureAdapter;
        if (listOfCarmenFeatureAdapter == null) {
          listOfCarmenFeatureAdapter =
            (TypeAdapter<List<CarmenFeature>>) gson
              .getAdapter(TypeToken.getParameterized(List.class, CarmenFeature.class));
          this.listOfCarmenFeatureAdapter = listOfCarmenFeatureAdapter;
        }
        listOfCarmenFeatureAdapter.write(jsonWriter, object.features());
      }

      writeString("attribution", object.attribution(), jsonWriter);

      jsonWriter.endObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GeocodingResponse read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String type = null;
      List<String> query = null;
      List<CarmenFeature> features = null;
      String attribution = null;
      while (jsonReader.hasNext()) {
        String name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (name) {
          case "type":
            type = readString(jsonReader);
            break;

          case "query":
            TypeAdapter<List<String>> listOfStringAdapter = this.listOfStringAdapter;
            if (listOfStringAdapter == null) {
              listOfStringAdapter =
                (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class,
                  String.class));
              this.listOfStringAdapter = listOfStringAdapter;
            }
            query = listOfStringAdapter.read(jsonReader);
            break;

          case "features":
            TypeAdapter<List<CarmenFeature>> listOfCarmenFeatureAdapter =
                    this.listOfCarmenFeatureAdapter;
            if (listOfCarmenFeatureAdapter == null) {
              listOfCarmenFeatureAdapter =
                (TypeAdapter<List<CarmenFeature>>) gson
                        .getAdapter(TypeToken.getParameterized(List.class, CarmenFeature.class));
              this.listOfCarmenFeatureAdapter = listOfCarmenFeatureAdapter;
            }
            features = listOfCarmenFeatureAdapter.read(jsonReader);
            break;

          case "attribution":
            attribution = readString(jsonReader);
            break;

          default:
            jsonReader.skipValue();
        }
      }
      jsonReader.endObject();
      return new GeocodingResponse(type, query, features, attribution);
    }
  }
}
