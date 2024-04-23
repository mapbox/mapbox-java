package com.mapbox.api.geocoding.v6;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.geocoding.v6.models.V6FeatureType;
import com.mapbox.api.geocoding.v6.models.V6Worldview;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Request parameters used to refine the results of a V6 a reverse geocoding query.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding/#reverse-geocoding">Reverse geocoding</a>
 */
@AutoValue
public abstract class V6ReverseGeocodingRequestOptions implements V6RequestOptions {

  @SerializedName("longitude")
  @NonNull
  abstract Double longitude();

  @SerializedName("latitude")
  @NonNull
  abstract Double latitude();

  @SerializedName("country")
  @Nullable
  abstract String country();

  @SerializedName("language")
  @Nullable
  abstract String language();

  @SerializedName("limit")
  @Nullable
  abstract Integer limit();

  @SerializedName("types")
  @Nullable
  abstract List<String> types();

  @Nullable
  String apiFormattedTypes() {
    final List<String> types = types();
    if (types != null) {
      return TextUtils.join(",", types.toArray());
    } else {
      return null;
    }
  }

  @SerializedName("worldview")
  @Nullable
  abstract String worldview();

  /**
   * Creates a new {@link Builder} object.
   *
   * @param point geographic coordinate for the location being queried
   * @return Builder object.
   */
  public static Builder builder(@NonNull Point point) {
    return new $AutoValue_V6ReverseGeocodingRequestOptions.Builder()
      .longitude(point.longitude())
      .latitude(point.latitude());
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<V6ReverseGeocodingRequestOptions> typeAdapter(Gson gson) {
    return new AutoValue_V6ReverseGeocodingRequestOptions.GsonTypeAdapter(gson);
  }

  @NonNull
  String toJson() {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .create();
    return gson.toJson(this, V6ReverseGeocodingRequestOptions.class);
  }

  /**
   * This builder is used to create a new instance that holds request options
   * for the reverse geocoding request.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    abstract Builder longitude(@NonNull Double longitude);

    abstract Builder latitude(@NonNull Double latitude);

    /**
     * Limit results to one or more country.
     * Permitted values are
     * <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166 alpha 2 country codes</a>.
     *
     * @param country limit geocoding results to one or more country
     * @return this builder for chaining options together
     */
    public Builder country(@NonNull String... country) {
      return country(TextUtils.join(",", country));
    }

    abstract Builder country(String country);

    /**
     * Set the language of the text supplied in responses. Also affects result scoring,
     * with results matching the user's query in the requested language being preferred over
     * results that match in another language. For example, an autocomplete query for things
     * that start with Frank might return Frankfurt as the first result with an English (en)
     * language parameter, but Frankreich ("France") with a German (de) language parameter.
     *
     * Options are <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tags</a>
     * comprised of a mandatory
     * <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1 language code</a>
     * and, optionally, one or more IETF subtags for country or script.
     *
     * @param language language of the text supplied in responses
     * @return this builder for chaining options together
     */
    public Builder language(Locale language) {
      return language(language.getLanguage());
    }

    /**
     * Set the language of the text supplied in responses. Also affects result scoring,
     * with results matching the user's query in the requested language being preferred over
     * results that match in another language. For example, an autocomplete query for things
     * that start with Frank might return Frankfurt as the first result with an English (en)
     * language parameter, but Frankreich ("France") with a German (de) language parameter.
     *
     * Options are <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tags</a>
     * comprised of a mandatory
     * <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1 language code</a>
     * and, optionally, one or more IETF subtags for country or script.
     *
     * @param language language of the text supplied in responses
     * @return this builder for chaining options together
     */
    public abstract Builder language(String language);

    /**
     * Specify the maximum number of results to return.
     * The default is 1 and the maximum supported is 5.
     *
     * The default behavior in reverse geocoding is to return at most one feature at each
     * of the multiple levels of the administrative hierarchy
     * (for example, one address, one region, one country).
     *
     * Increasing the limit allows returning multiple features of the same type,
     * but only for one type (for example, multiple address results). Consequently, setting
     * limit to a higher-than-default value requires specifying exactly one types parameter.
     *
     * @param limit the number of returned results
     * @return this builder for chaining options together
     */
    public abstract Builder limit(@IntRange(from = 1, to = 5) Integer limit);

    /**
     * Filter results to include only a subset (one or more) of the available feature types.
     * Only values defined in {@link V6FeatureType.FeatureType} are allowed.
     *
     * @param types filter results to include only a subset of the available feature types
     * @return this builder for chaining options together
     * @see <a href="https://docs.mapbox.com/api/search/geocoding/#geographic-feature-types">Geographic Feature Types</a>
     */
    public Builder types(@NonNull @V6FeatureType.FeatureType String... types) {
      return types(Arrays.asList(types));
    }

    abstract Builder types(List<String> types);

    /**
     * Returns features that are defined differently by audiences that belong to various regional,
     * cultural, or political groups.
     *
     * Available worldviews are defined in {@link com.mapbox.api.geocoding.v6.models.V6Worldview}.
     * If worldview is not set, the us worldview boundaries are returned by default.
     *
     * @param worldview worldview code
     * @return this builder for chaining options together
     * @see <a href="https://docs.mapbox.com/api/search/geocoding/#worldviews">Worldviews</a>
     */
    public abstract Builder worldview(@V6Worldview.Worldview @NonNull String worldview);

    /**
     * Build a new {@link V6ReverseGeocodingRequestOptions} object.
     *
     * @return a new {@link V6ReverseGeocodingRequestOptions}
     */
    public abstract V6ReverseGeocodingRequestOptions build();
  }
}
