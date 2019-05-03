package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Array representing a hierarchy of parents. Each parent includes id, text keys along with
 * (if avaliable) a wikidata, short_code, and Maki key.
 *
 * @see <a href="https://github.com/mapbox/carmen/blob/master/carmen-geojson.md">Carmen Geojson information</a>
 * @see <a href="https://www.mapbox.com/api-documentation/search/#geocoding">Mapbox geocoder documentation</a>
 * @since 1.0.0
 */
public class CarmenContext implements Serializable {

  private final String id;

  private final String text;

  private final Map<String, String> texts;

  private final String shortCode;

  private final String wikidata;

  private final String category;

  private final String maki;

  private final String language;
  private final List<String> languages;

  CarmenContext(
          @Nullable String id,
          @Nullable String text,
          @Nullable Map<String, String> texts,
          @Nullable String shortCode,
          @Nullable String wikidata,
          @Nullable String category,
          @Nullable String maki,
          @Nullable String language,
          @Nullable List<String> languages) {
    this.id = id;
    this.text = text;
    this.texts = texts;
    this.shortCode = shortCode;
    this.wikidata = wikidata;
    this.category = category;
    this.maki = maki;
    this.language = language;
    this.languages = languages;
  }

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new CarmenContext.Builder();
  }

  /**
   * Create a CarmenContext object from JSON.
   *
   * @param json string of JSON making up a carmen context
   * @return this class using the defined information in the provided JSON string
   * @since 3.0.0
   */
  @SuppressWarnings("unused")
  public static CarmenContext fromJson(@NonNull String json) {
    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
            .create();
    return gson.fromJson(json, CarmenContext.class);
  }

  /**
   * ID of the feature of the form {index}.{id} where index is the id/handle of the data-source
   * that contributed the result.
   *
   * @return string containing the ID
   * @since 1.0.0
   */
  @Nullable
  public String id() {
    return id;
  }

  /**
   * A string representing the feature.
   *
   * @return text representing the feature (e.g. "Austin")
   * @since 1.0.0
   */
  @Nullable
  public String text() {
    return text;
  }

  /**
   * A map representing language tag to text string mapping.
   * @return map representing language tag to text string mapping
   * @since 4.9.0
   */
  public Map<String, String> texts() {
    return texts;
  }

  /**
   * The ISO 3166-1 country and ISO 3166-2 region code for the returned feature.
   *
   * @return a String containing the country or region code
   * @since 1.0.0
   */
  @Nullable
  @SerializedName("short_code")
  public String shortCode() {
    return shortCode;
  }

  /**
   * The WikiData identifier for a country, region or place.
   *
   * @return a unique identifier WikiData uses to query and gather more information
   *         about this specific feature
   * @since 1.2.0
   */
  @Nullable
  public String wikidata() {
    return wikidata;
  }

  /**
   * provides the categories that define this features POI if applicable.
   *
   * @return comma-separated list of categories applicable to a poi
   * @since 1.0.0
   */
  @Nullable
  public String category() {
    return category;
  }

  /**
   * Suggested icon mapping from the most current version of the Maki project for a poi feature,
   * based on its category. Note that this doesn't actually return the image but rather the
   * identifier which can be used to download the correct image manually.
   *
   * @return string containing the recommended Maki icon
   * @since 1.2.0
   */
  @Nullable
  public String maki() {
    return maki;
  }

  /**
   * A string of the IETF primary language tag in the query.
   * @return  A string of the IETF primary language tag in the query.
   * @since 4.9.0
   */
  @Nullable
  public String language() {
    return language;
  }

  /**
   * This field is only returned when multiple languages are requested using the language
   * parameter, and will be present for each requested language.
   * @return List of requested language tags.
   * @since 4.9.0
   */
  @Nullable
  public List<String> languages() {
    return languages;
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<CarmenContext> typeAdapter(Gson gson) {
    return new GsonTypeAdapter(gson);
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a JSON
   * string.
   *
   * @return a JSON string which represents this CarmenContext
   * @since 3.0.0
   */
  @SuppressWarnings("unused")
  public String toJson() {
    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
            .create();
    return gson.toJson(this);
  }

  /**
   * Convert current instance values into another Builder to quickly change one or more values.
   *
   * @return a new instance of {@link CarmenContext} using the newly defined values
   * @since 3.0.0
   */
  @SuppressWarnings("unused")
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public String toString() {
    return "CarmenContext{"
            + "id='" + id + '\''
            + ", text='" + text + '\''
            + ", texts=" + texts
            + ", shortCode='" + shortCode + '\''
            + ", wikidata='" + wikidata + '\''
            + ", category='" + category + '\''
            + ", maki='" + maki + '\''
            + ", language='" + language + '\''
            + ", languages=" + languages
            + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!(obj instanceof CarmenContext)) {
      return false;
    }
    CarmenContext that = (CarmenContext) obj;
    return Objects.equals(id, that.id)
            && Objects.equals(text, that.text)
            && Objects.equals(texts, that.texts)
            && Objects.equals(shortCode, that.shortCode)
            && Objects.equals(wikidata, that.wikidata)
            && Objects.equals(category, that.category)
            && Objects.equals(maki, that.maki)
            && Objects.equals(language, that.language)
            && Objects.equals(languages, that.languages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, texts, shortCode, wikidata, category, maki, language, languages);
  }

  /**
   * This builder can be used to set the values describing the {@link CarmenFeature}.
   *
   * @since 3.0.0
   */
  @SuppressWarnings("unused")
  public static class Builder {

    private String id;

    private String text;

    private Map<String, String> texts;

    private String shortCode;

    private String wikidata;

    private String category;

    private String maki;

    private String language;

    private List<String> languages;

    Builder() {
    }

    private Builder(CarmenContext source) {
      this.id = source.id();
      this.text = source.text();
      this.texts = source.texts();
      this.shortCode = source.shortCode();
      this.wikidata = source.wikidata();
      this.category = source.category();
      this.maki = source.maki();
      this.language = source.language();
      this.languages = source.languages();
    }

    /**
     * ID of the feature of the form {index}.{id} where index is the id/handle of the data-source
     * that contributed the result.
     *
     * @param id string containing the ID
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder id(@Nullable String id) {
      this.id = id;
      return this;
    }

    /**
     * A string representing the feature.
     *
     * @param text representing the feature (e.g. "Austin")
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder text(String text) {
      this.text = text;
      return this;
    }

    /**
     * A string analogous to the text field that matches the query in the requested language.
     * This field is only returned when multiple languages are requested using the language
     * parameter, and will be present for each requested language.
     *
     * @param texts map with language tag to text string mapping
     * @return this builder for chaining options together
     * @since 4.9.0
     */
    public Builder text(Map<String, String> texts) {
      this.texts = texts;
      return this;
    }

    /**
     * The ISO 3166-1 country and ISO 3166-2 region code for the returned feature.
     *
     * @param shortCode a String containing the country or region code
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder shortCode(@Nullable String shortCode) {
      this.shortCode = shortCode;
      return this;
    }

    /**
     * The WikiData identifier for a country, region or place.
     *
     * @param wikidata a unique identifier WikiData uses to query and gather more information about
     *                 this specific feature
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder wikidata(@Nullable String wikidata) {
      this.wikidata = wikidata;
      return this;
    }

    /**
     * provides the categories that define this features POI if applicable.
     *
     * @param category comma-separated list of categories applicable to a poi
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder category(@Nullable String category) {
      this.category = category;
      return this;
    }

    /**
     * Suggested icon mapping from the most current version of the Maki project for a poi feature,
     * based on its category. Note that this doesn't actually return the image but rather the
     * identifier which can be used to download the correct image manually.
     *
     * @param maki string containing the recommended Maki icon
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder maki(@Nullable String maki) {
      this.maki = maki;
      return this;
    }

    /**
     * The language for the query.
     * @param language string containing the query's primary language
     * @return this builder for chaining options together
     * @since 4.9.0
     */
    public Builder language(@Nullable String language) {
      this.language = language;
      return this;
    }

    /**
     * This field is only returned when multiple languages are requested using
     * the language parameter, and will be present for each requested language.
     *
     * @param languages list of strings containing language tags
     * @return this builder for chaining options together
     * @since 4.9.0
     */
    public Builder languages(@Nullable List<String> languages) {
      this.languages = languages;
      return this;
    }

    /**
     * Build a new {@link CarmenContext} object.
     *
     * @return a new {@link CarmenContext} using the provided values in this builder
     * @since 3.0.0
     */
    public CarmenContext build() {
      return new CarmenContext(
              this.id,
              this.text,
              this.texts,
              this.shortCode,
              this.wikidata,
              this.category,
              this.maki,
              this.language,
              this.languages);
    }
  }

  /**
   * @since 4.9.0
   */
  private static final class GsonTypeAdapter extends GeocodingTypeAdapter<CarmenContext> {

    private GsonTypeAdapter(Gson gson) {
      super(gson);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, CarmenContext object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      writeString("id", object.id(), jsonWriter);
      writeString("text", object.text(), jsonWriter);
      writeLocalizedMap("text", object.texts(), jsonWriter);
      writeString("short_code", object.shortCode(), jsonWriter);
      writeString("wikidata", object.wikidata(), jsonWriter);
      writeString("category", object.category(), jsonWriter);
      writeString("maki", object.maki(), jsonWriter);
      writeString("language", object.language(), jsonWriter);
      writeLanguages(object.languages(), jsonWriter);

      jsonWriter.endObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CarmenContext read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String id = null;
      String text = null;
      Map<String, String> texts = null;
      String shortCode = null;
      String wikidata = null;
      String category = null;
      String maki = null;
      String language = null;
      List<String> languages = null;
      while (jsonReader.hasNext()) {
        String name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (name) {
          case "id":
            id = readString(jsonReader);
            break;

          case "text":
            text = readString(jsonReader);
            break;

          case "short_code":
            shortCode = readString(jsonReader);
            break;

          case "wikidata":
            wikidata = readString(jsonReader);
            break;

          case "category":
            category = readString(jsonReader);
            break;

          case "maki":
            maki = readString(jsonReader);
            break;

          case "language":
            language = readString(jsonReader);
            break;

          default:
            if (name.startsWith("language_")) {
              languages = readLanguages(languages, name.substring(9), jsonReader);

            } else if (name.startsWith("text_")) {
              texts = readLocalizedMap(texts, name.substring(5), jsonReader);

            } else {
              jsonReader.skipValue();
            }
        }
      }
      jsonReader.endObject();
      return new CarmenContext(id, text, texts, shortCode,
                               wikidata, category, maki, language, languages);
    }
  }
}

