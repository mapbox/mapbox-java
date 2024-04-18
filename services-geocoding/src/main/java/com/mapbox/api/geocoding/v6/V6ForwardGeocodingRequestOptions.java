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
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Request parameters used to refine the results of a V6 forward geocoding query.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#forward-geocoding">Forward geocoding</a>
 */
@AutoValue
public abstract class V6ForwardGeocodingRequestOptions implements V6RequestOptions {

  @SerializedName("q")
  @Nullable
  abstract String query();

  @SerializedName("address_line1")
  @Nullable
  abstract String addressLine1();

  @SerializedName("address_number")
  @Nullable
  abstract String addressNumber();

  @SerializedName("street")
  @Nullable
  abstract String street();

  @SerializedName("block")
  @Nullable
  abstract String block();

  @SerializedName("place")
  @Nullable
  abstract String place();

  @SerializedName("region")
  @Nullable
  abstract String region();

  @SerializedName("postcode")
  @Nullable
  abstract String postcode();

  @SerializedName("locality")
  @Nullable
  abstract String locality();

  @SerializedName("neighborhood")
  @Nullable
  abstract String neighborhood();

  @SerializedName("autocomplete")
  @Nullable
  abstract Boolean autocomplete();

  @SerializedName("bbox")
  @Nullable
  abstract List<Double> bbox();

  String apiFormattedBbox() {
    final List<Double> bbox = bbox();
    if (bbox != null && bbox.size() == 4) {
      return String.format(Locale.US, "%s,%s,%s,%s",
        TextUtils.formatCoordinate(bbox.get(0)),
        TextUtils.formatCoordinate(bbox.get(1)),
        TextUtils.formatCoordinate(bbox.get(2)),
        TextUtils.formatCoordinate(bbox.get(3))
      );
    } else {
      return null;
    }
  }

  @SerializedName("country")
  @Nullable
  abstract String country();

  @SerializedName("language")
  @Nullable
  abstract String language();

  @SerializedName("limit")
  @Nullable
  abstract Integer limit();

  @SerializedName("proximity")
  @Nullable
  abstract String proximity();

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
   * Creates a new {@link V6ForwardGeocodingRequestOptions.Builder} object with query parameter.
   *
   * @param query The feature you're trying to look up. This could be an address, a city name, etc.
   * @return Builder object.
   * @throws ServicesException if query is empty
   */
  public static V6ForwardGeocodingRequestOptions.Builder builder(@NonNull String query) {
    if (query.isEmpty()) {
      throw new ServicesException("Search query can't be empty");
    }
    return new $AutoValue_V6ForwardGeocodingRequestOptions.Builder()
      .query(query);
  }

  /**
   * Creates a new {@link V6ForwardGeocodingRequestOptions.Builder} object with
   * structured input query parameter.
   *
   * @param query structured input query.
   * @return Builder object.
   */
  public static V6ForwardGeocodingRequestOptions.Builder builder(
    @NonNull V6StructuredInputQuery query
  ) {
    return new $AutoValue_V6ForwardGeocodingRequestOptions.Builder()
      .addressLine1(query.addressLine1())
      .addressNumber(query.addressNumber())
      .street(query.street())
      .block(query.block())
      .place(query.place())
      .region(query.region())
      .postcode(query.postcode())
      .locality(query.locality())
      .neighborhood(query.neighborhood());
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<V6ForwardGeocodingRequestOptions> typeAdapter(Gson gson) {
    return new AutoValue_V6ForwardGeocodingRequestOptions.GsonTypeAdapter(gson);
  }

  @NonNull
  String toJson() {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .create();
    return gson.toJson(this, V6ForwardGeocodingRequestOptions.class);
  }

  /**
   * This builder is used to create a new instance that holds request options
   * for the forward geocoding request.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    abstract Builder query(@NonNull String query);

    // Structured input options
    abstract Builder addressLine1(String addressLine1);

    abstract Builder addressNumber(String addressNumber);

    abstract Builder street(String street);

    abstract Builder block(String block);

    abstract Builder place(String place);

    abstract Builder region(String region);

    abstract Builder postcode(String postcode);

    abstract Builder locality(String locality);

    abstract Builder neighborhood(String neighborhood);

    /**
     * Specify whether to return autocomplete results (true by default).
     * When autocomplete is enabled, results will be included that start with the requested string,
     * rather than just responses that match it exactly.
     * For example, a query for India might return both India and Indiana with autocomplete enabled,
     * but only India if it's disabled.
     * <p>
     * When autocomplete is enabled, each user keystroke counts as one request to the Geocoding API.
     * For example, a search for "Cali" would be reflected as four separate Geocoding API requests.
     * To reduce the total requests sent, you can configure your application to only call the
     * Geocoding API after a specific number of characters are typed.
     *
     * @param autocomplete optionally set whether to return autocomplete results
     * @return this builder for chaining options together
     */
    public abstract Builder autocomplete(@Nullable Boolean autocomplete);

    /**
     * Limit results to only those contained within the supplied bounding box.
     * The bounding box cannot cross the 180th meridian.
     *
     * @param boundingBox the bounding box
     * @return this builder for chaining options together
     */
    public Builder boundingBox(@NonNull BoundingBox boundingBox) {
      return bbox(
        Arrays.asList(
          boundingBox.southwest().longitude(),
          boundingBox.southwest().latitude(),
          boundingBox.northeast().longitude(),
          boundingBox.northeast().latitude()
        )
      );
    }

    abstract Builder bbox(List<Double> bbox);

    /**
     * Limit results to one or more country.
     * Each country can be represented as:
     * - <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166 alpha 2 country codes</a>
     * - Generally recognized country name
     * - In some cases like Hong Kong, an area of quasi-national administrative status
     * that has been given a designated country code under ISO 3166-1.
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
     * with results matching the user's query in the requested language being preferred over results
     * that match in another language. For example, an autocomplete query for things that start with
     * Frank might return Frankfurt as the first result with an English (en) language parameter,
     * but Frankreich ("France") with a German (de) language parameter.
     * <p>
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
     * with results matching the user's query in the requested language being preferred over results
     * that match in another language. For example, an autocomplete query for things that start with
     * Frank might return Frankfurt as the first result with an English (en) language parameter,
     * but Frankreich ("France") with a German (de) language parameter.
     * <p>
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
     * The default is 5 and the maximum supported is 10.
     *
     * @param limit the number of returned results
     * @return this builder for chaining options together
     */
    public abstract Builder limit(@IntRange(from = 1, to = 10) Integer limit);

    /**
     * Bias the response to favor results that are closer to the location that is retrieved
     * by reverse IP lookup.
     *
     * @return this builder for chaining options together
     */
    public Builder withIpAsProximity() {
      return proximity("ip");
    }

    /**
     * Bias the response to favor results that are closer to this location.
     *
     * @param proximity a point to bias the response
     * @return this builder for chaining options together
     */
    public Builder proximity(@NonNull Point proximity) {
      return proximity(String.format(
        Locale.US,
        "%s,%s",
        TextUtils.formatCoordinate(proximity.longitude()),
        TextUtils.formatCoordinate(proximity.latitude())
      ));
    }

    abstract Builder proximity(@NonNull String proximity);

    /**
     * Filter results to include only a subset (one or more) of the available feature types.
     * Only values defined in {@link V6FeatureType.FeatureType} are allowed.
     *
     * @param types filter results to include only a subset of the available feature types
     * @return this builder for chaining options together
     * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#geographic-feature-types">Geographic Feature Types</a>
     */
    public Builder types(@NonNull @V6FeatureType.FeatureType String... types) {
      return types(Arrays.asList(types));
    }

    abstract Builder types(List<String> types);

    /**
     * Returns features that are defined differently by audiences that belong to various regional,
     * cultural, or political groups.
     * <p>
     * Available worldviews are defined in {@link com.mapbox.api.geocoding.v6.models.V6Worldview}.
     * If worldview is not set, the us worldview boundaries are returned by default.
     *
     * @param worldview worldview code
     * @return this builder for chaining options together
     * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#worldviews">Worldviews</a>
     */
    public abstract Builder worldview(@V6Worldview.Worldview @NonNull String worldview);

    /**
     * Build a new {@link V6ForwardGeocodingRequestOptions} object.
     *
     * @return a new {@link V6ForwardGeocodingRequestOptions}
     */
    public abstract V6ForwardGeocodingRequestOptions build();
  }
}
