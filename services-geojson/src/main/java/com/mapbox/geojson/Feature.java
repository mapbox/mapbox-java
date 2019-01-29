package com.mapbox.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;

import java.io.IOException;

/**
 * This defines a GeoJson Feature object which represents a spatially bound thing. Every Feature
 * object is a GeoJson object no matter where it occurs in a GeoJson text. A Feature object will
 * always have a "TYPE" member with the value "Feature".
 * <p>
 * A Feature object has a member with the name "geometry". The value of the geometry member SHALL be
 * either a Geometry object or, in the case that the Feature is unlocated, a JSON null value.
 * <p>
 * A Feature object has a member with the name "properties". The value of the properties member is
 * an object (any JSON object or a JSON null value).
 * <p>
 * If a Feature has a commonly used identifier, that identifier SHOULD be included as a member of
 * the Feature object through the {@link #id()} method, and the value of this member is either a
 * JSON string or number.
 * <p>
 * An example of a serialized feature is given below:
 * <pre>
 * {
 *   "TYPE": "Feature",
 *   "geometry": {
 *     "TYPE": "Point",
 *     "coordinates": [102.0, 0.5]
 *   },
 *   "properties": {
 *     "prop0": "value0"
 *   }
 * </pre>
 *
 * @since 1.0.0
 */
public final class Feature implements GeoJson {

  private static final String TYPE = "Feature";

  private final String type;

  @JsonAdapter(BoundingBoxTypeAdapter.class)
  private final BoundingBox bbox;

  private final String id;

  private final Geometry geometry;

  private final JsonObject properties;

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a Feature object from scratch it is better to use one of the other provided static
   * factory methods such as {@link #fromGeometry(Geometry)}.
   *
   * @param json a formatted valid JSON string defining a GeoJson Feature
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static Feature fromJson(@NonNull String json) {

    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());

    Feature feature = gson.create().fromJson(json, Feature.class);

    // Even thought properties are Nullable,
    // Feature object will be created with properties set to an empty object,
    // so that addProperties() would work
    if (feature.properties() != null) {
      return feature;
    }
    return new Feature(TYPE, feature.bbox(),
      feature.id(), feature.geometry(), new JsonObject());
  }

  /**
   * Create a new instance of this class by giving the feature a {@link Geometry}.
   *
   * @param geometry a single geometry which makes up this feature object
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static Feature fromGeometry(@Nullable Geometry geometry) {
    return new Feature(TYPE, null, null, geometry, new JsonObject());
  }

  /**
   * Create a new instance of this class by giving the feature a {@link Geometry}. You can also pass
   * in a double array defining a bounding box.
   *
   * @param geometry a single geometry which makes up this feature object
   * @param bbox     optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static Feature fromGeometry(@Nullable Geometry geometry, @Nullable BoundingBox bbox) {
    return new Feature(TYPE, bbox, null, geometry, new JsonObject());
  }

  /**
   * Create a new instance of this class by giving the feature a {@link Geometry} and optionally a
   * set of properties.
   *
   * @param geometry   a single geometry which makes up this feature object
   * @param properties a {@link JsonObject} containing the feature properties
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static Feature fromGeometry(@Nullable Geometry geometry, @Nullable JsonObject properties) {
    return new Feature(TYPE, null, null, geometry,
      properties == null ? new JsonObject() : properties);
  }

  /**
   * Create a new instance of this class by giving the feature a {@link Geometry}, optionally a
   * set of properties, and optionally pass in a bbox.
   *
   * @param geometry   a single geometry which makes up this feature object
   * @param bbox       optionally include a bbox definition as a double array
   * @param properties a {@link JsonObject} containing the feature properties
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static Feature fromGeometry(@Nullable Geometry geometry, @Nullable JsonObject properties,
                                     @Nullable BoundingBox bbox) {
    return new Feature(TYPE, bbox, null, geometry,
      properties == null ? new JsonObject() : properties);
  }

  /**
   * Create a new instance of this class by giving the feature a {@link Geometry}, optionally a
   * set of properties, and a String which represents the objects id.
   *
   * @param geometry   a single geometry which makes up this feature object
   * @param properties a {@link JsonObject} containing the feature properties
   * @param id         common identifier of this feature
   * @return {@link Feature}
   * @since 1.0.0
   */
  public static Feature fromGeometry(@Nullable Geometry geometry, @Nullable JsonObject properties,
                                     @Nullable String id) {
    return new Feature(TYPE, null, id, geometry,
      properties == null ? new JsonObject() : properties);
  }

  /**
   * Create a new instance of this class by giving the feature a {@link Geometry}, optionally a
   * set of properties, and a String which represents the objects id.
   *
   * @param geometry   a single geometry which makes up this feature object
   * @param properties a {@link JsonObject} containing the feature properties
   * @param bbox       optionally include a bbox definition as a double array
   * @param id         common identifier of this feature
   * @return {@link Feature}
   * @since 1.0.0
   */
  public static Feature fromGeometry(@Nullable Geometry geometry, @NonNull JsonObject properties,
                                     @Nullable String id, @Nullable BoundingBox bbox) {
    return new Feature(TYPE, bbox, id, geometry,
      properties == null ? new JsonObject() : properties);
  }

  Feature(String type, @Nullable BoundingBox bbox, @Nullable String id,
          @Nullable Geometry geometry, @Nullable JsonObject properties) {
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
    this.bbox = bbox;
    this.id = id;
    this.geometry = geometry;
    this.properties = properties;
  }

  /**
   * This describes the TYPE of GeoJson geometry this object is, thus this will always return
   * {@link Feature}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code Feature}
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
   * A feature may have a commonly used identifier which is either a unique String or number.
   *
   * @return a String containing this features unique identification or null if one wasn't given
   *   during creation.
   * @since 1.0.0
   */
  @Nullable
  public String id() {
    return id;
  }

  /**
   * The geometry which makes up this feature. A Geometry object represents points, curves, and
   * surfaces in coordinate space. One of the seven geometries provided inside this library can be
   * passed in through one of the static factory methods.
   *
   * @return a single defined {@link Geometry} which makes this feature spatially aware
   * @since 1.0.0
   */
  @Nullable
  public Geometry geometry() {
    return geometry;
  }

  /**
   * This contains the JSON object which holds the feature properties. The value of the properties
   * member is a {@link JsonObject} and might be empty if no properties are provided.
   *
   * @return a {@link JsonObject} which holds this features current properties
   * @since 1.0.0
   */
  @Nullable
  public JsonObject properties() {
    return properties;
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Feature
   * @since 1.0.0
   */
  @Override
  public String toJson() {

    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(GeoJsonAdapterFactory.create())
            .registerTypeAdapterFactory(GeometryAdapterFactory.create())
            .create();


    // Empty properties -> should not appear in json string
    Feature feature = this;
    if (properties().size() == 0) {
      feature = new Feature(TYPE, bbox(), id(), geometry(), null);
    }

    return gson.toJson(feature);
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<Feature> typeAdapter(Gson gson) {
    return new Feature.GsonTypeAdapter(gson);
  }

  /**
   * Convenience method to add a String member.
   *
   * @param key   name of the member
   * @param value the String value associated with the member
   * @since 1.0.0
   */
  public void addStringProperty(String key, String value) {
    properties().addProperty(key, value);
  }

  /**
   * Convenience method to add a Number member.
   *
   * @param key   name of the member
   * @param value the Number value associated with the member
   * @since 1.0.0
   */
  public void addNumberProperty(String key, Number value) {
    properties().addProperty(key, value);
  }

  /**
   * Convenience method to add a Boolean member.
   *
   * @param key   name of the member
   * @param value the Boolean value associated with the member
   * @since 1.0.0
   */
  public void addBooleanProperty(String key, Boolean value) {
    properties().addProperty(key, value);
  }

  /**
   * Convenience method to add a Character member.
   *
   * @param key   name of the member
   * @param value the Character value associated with the member
   * @since 1.0.0
   */
  public void addCharacterProperty(String key, Character value) {
    properties().addProperty(key, value);
  }

  /**
   * Convenience method to add a JsonElement member.
   *
   * @param key   name of the member
   * @param value the JsonElement value associated with the member
   * @since 1.0.0
   */
  public void addProperty(String key, JsonElement value) {
    properties().add(key, value);
  }

  /**
   * Convenience method to get a String member.
   *
   * @param key name of the member
   * @return the value of the member, null if it doesn't exist
   * @since 1.0.0
   */
  public String getStringProperty(String key) {
    return properties().get(key).getAsString();
  }

  /**
   * Convenience method to get a Number member.
   *
   * @param key name of the member
   * @return the value of the member, null if it doesn't exist
   * @since 1.0.0
   */
  public Number getNumberProperty(String key) {
    return properties().get(key).getAsNumber();
  }

  /**
   * Convenience method to get a Boolean member.
   *
   * @param key name of the member
   * @return the value of the member, null if it doesn't exist
   * @since 1.0.0
   */
  public Boolean getBooleanProperty(String key) {
    return properties().get(key).getAsBoolean();
  }

  /**
   * Convenience method to get a Character member.
   *
   * @param key name of the member
   * @return the value of the member, null if it doesn't exist
   * @since 1.0.0
   */
  public Character getCharacterProperty(String key) {
    return properties().get(key).getAsCharacter();
  }

  /**
   * Convenience method to get a JsonElement member.
   *
   * @param key name of the member
   * @return the value of the member, null if it doesn't exist
   * @since 1.0.0
   */
  public JsonElement getProperty(String key) {
    return properties().get(key);
  }

  /**
   * Removes the property from the object properties.
   *
   * @param key name of the member
   * @return Removed {@code property} from the key string passed in through the parameter.
   * @since 1.0.0
   */
  public JsonElement removeProperty(String key) {
    return properties().remove(key);
  }

  /**
   * Convenience method to check if a member with the specified name is present in this object.
   *
   * @param key name of the member
   * @return true if there is the member has the specified name, false otherwise.
   * @since 1.0.0
   */
  public boolean hasProperty(String key) {
    return properties().has(key);
  }

  /**
   * Convenience method to check for a member by name as well as non-null value.
   *
   * @param key name of the member
   * @return true if member is present with non-null value, false otherwise.
   * @since 1.3.0
   */
  public boolean hasNonNullValueForProperty(String key) {
    return hasProperty(key) && !getProperty(key).isJsonNull();
  }

  @Override
  public String toString() {
    return "Feature{"
            + "type=" + type + ", "
            + "bbox=" + bbox + ", "
            + "id=" + id + ", "
            + "geometry=" + geometry + ", "
            + "properties=" + properties
            + "}";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Feature) {
      Feature that = (Feature) obj;
      return (this.type.equals(that.type()))
              && ((this.bbox == null) ? (that.bbox() == null) : this.bbox.equals(that.bbox()))
              && ((this.id == null) ? (that.id() == null) : this.id.equals(that.id()))
              && ((this.geometry == null)
                   ? (that.geometry() == null) : this.geometry.equals(that.geometry()))
              && ((this.properties == null)
                   ? (that.properties() == null) : this.properties.equals(that.properties()));
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
    hashCode ^= (id == null) ? 0 : id.hashCode();
    hashCode *= 1000003;
    hashCode ^= (geometry == null) ? 0 : geometry.hashCode();
    hashCode *= 1000003;
    hashCode ^= (properties == null) ? 0 : properties.hashCode();
    return hashCode;
  }

  /**
   * TypeAdapter to serialize/deserialize Feature objects.
   *
   * @since 4.6.0
   */
  static final class GsonTypeAdapter extends TypeAdapter<Feature> {
    private volatile TypeAdapter<String> stringTypeAdapter;
    private volatile TypeAdapter<BoundingBox> boundingBoxTypeAdapter;
    private volatile TypeAdapter<Geometry> geometryTypeAdapter;
    private volatile TypeAdapter<JsonObject> jsonObjectTypeAdapter;
    private final Gson gson;

    GsonTypeAdapter(Gson gson) {
      this.gson = gson;
    }

    @Override
    public void write(JsonWriter jsonWriter, Feature object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("type");
      if (object.type() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> stringTypeAdapter = this.stringTypeAdapter;
        if (stringTypeAdapter == null) {
          stringTypeAdapter = gson.getAdapter(String.class);
          this.stringTypeAdapter = stringTypeAdapter;
        }
        stringTypeAdapter.write(jsonWriter, object.type());
      }
      jsonWriter.name("bbox");
      if (object.bbox() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<BoundingBox> boundingBoxTypeAdapter = this.boundingBoxTypeAdapter;
        if (boundingBoxTypeAdapter == null) {
          boundingBoxTypeAdapter = gson.getAdapter(BoundingBox.class);
          this.boundingBoxTypeAdapter = boundingBoxTypeAdapter;
        }
        boundingBoxTypeAdapter.write(jsonWriter, object.bbox());
      }
      jsonWriter.name("id");
      if (object.id() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> stringTypeAdapter = this.stringTypeAdapter;
        if (stringTypeAdapter == null) {
          stringTypeAdapter = gson.getAdapter(String.class);
          this.stringTypeAdapter = stringTypeAdapter;
        }
        stringTypeAdapter.write(jsonWriter, object.id());
      }
      jsonWriter.name("geometry");
      if (object.geometry() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<Geometry> geometryTypeAdapter = this.geometryTypeAdapter;
        if (geometryTypeAdapter == null) {
          geometryTypeAdapter = gson.getAdapter(Geometry.class);
          this.geometryTypeAdapter = geometryTypeAdapter;
        }
        geometryTypeAdapter.write(jsonWriter, object.geometry());
      }
      jsonWriter.name("properties");
      if (object.properties() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<JsonObject> jsonObjectTypeAdapter = this.jsonObjectTypeAdapter;
        if (jsonObjectTypeAdapter == null) {
          jsonObjectTypeAdapter = gson.getAdapter(JsonObject.class);
          this.jsonObjectTypeAdapter = jsonObjectTypeAdapter;
        }
        jsonObjectTypeAdapter.write(jsonWriter, object.properties());
      }
      jsonWriter.endObject();
    }

    @Override
    public Feature read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String type = null;
      BoundingBox bbox = null;
      String id = null;
      Geometry geometry = null;
      JsonObject properties = null;
      while (jsonReader.hasNext()) {
        String name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (name) {
          case "type":
            TypeAdapter<String> strTypeAdapter = this.stringTypeAdapter;
            if (strTypeAdapter == null) {
              strTypeAdapter = gson.getAdapter(String.class);
              this.stringTypeAdapter = strTypeAdapter;
            }
            type = strTypeAdapter.read(jsonReader);
            break;

          case "bbox":
            TypeAdapter<BoundingBox> boundingBoxTypeAdapter = this.boundingBoxTypeAdapter;
            if (boundingBoxTypeAdapter == null) {
              boundingBoxTypeAdapter = gson.getAdapter(BoundingBox.class);
              this.boundingBoxTypeAdapter = boundingBoxTypeAdapter;
            }
            bbox = boundingBoxTypeAdapter.read(jsonReader);
            break;

          case "id":
            strTypeAdapter = this.stringTypeAdapter;
            if (strTypeAdapter == null) {
              strTypeAdapter = gson.getAdapter(String.class);
              this.stringTypeAdapter = strTypeAdapter;
            }
            id = strTypeAdapter.read(jsonReader);
            break;

          case "geometry":
            TypeAdapter<Geometry> geometryTypeAdapter = this.geometryTypeAdapter;
            if (geometryTypeAdapter == null) {
              geometryTypeAdapter = gson.getAdapter(Geometry.class);
              this.geometryTypeAdapter = geometryTypeAdapter;
            }
            geometry = geometryTypeAdapter.read(jsonReader);
            break;

          case "properties":
            TypeAdapter<JsonObject> jsonObjectTypeAdapter = this.jsonObjectTypeAdapter;
            if (jsonObjectTypeAdapter == null) {
              jsonObjectTypeAdapter = gson.getAdapter(JsonObject.class);
              this.jsonObjectTypeAdapter = jsonObjectTypeAdapter;
            }
            properties = jsonObjectTypeAdapter.read(jsonReader);
            break;

          default:
            jsonReader.skipValue();

        }
      }
      jsonReader.endObject();
      return new Feature(type, bbox, id, geometry, properties);
    }
  }
}
