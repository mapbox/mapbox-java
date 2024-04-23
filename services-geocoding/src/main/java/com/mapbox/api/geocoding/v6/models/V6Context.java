package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * V6 response's piece of data which breaks out the complete geographical hierarchy
 * for a given address or place. It is both a reliable way to access the named values of each
 * component part of an address, plus contains feature-specific data such as the Wikidata id and
 * 3-letter alpha country code.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding/#the-context-object">The Context Object</a>
 */
@AutoValue
public abstract class V6Context extends V6JsonObject {

  /**
   * Address context element.
   *
   * @return address context element
   */
  @SerializedName("address")
  @Nullable
  public abstract V6ContextAddress address();

  /**
   * Street context element.
   *
   * @return street context element
   */
  @SerializedName("street")
  @Nullable
  public abstract V6ContextElement street();

  /**
   * Neighborhood context element.
   *
   * @return neighborhood context element
   */
  @SerializedName("neighborhood")
  @Nullable
  public abstract V6ContextElement neighborhood();

  /**
   * Place context element.
   *
   * @return place context element
   */
  @SerializedName("place")
  @Nullable
  public abstract V6ContextElement place();

  /**
   * Postcode context element.
   *
   * @return postcode context element
   */
  @SerializedName("postcode")
  @Nullable
  public abstract V6ContextElement postcode();

  /**
   * Region context element.
   *
   * @return region context element
   */
  @SerializedName("region")
  @Nullable
  public abstract V6ContextElement region();

  /**
   * Country context element.
   *
   * @return country context element
   */
  @SerializedName("country")
  @Nullable
  public abstract V6ContextElement country();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<V6Context> typeAdapter(Gson gson) {
    return new AutoValue_V6Context.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  abstract static class Builder extends BaseBuilder<Builder> {

    abstract Builder address(V6ContextAddress address);

    abstract Builder street(V6ContextElement street);

    abstract Builder neighborhood(V6ContextElement neighborhood);

    abstract Builder place(V6ContextElement place);

    abstract Builder postcode(V6ContextElement postcode);

    abstract Builder region(V6ContextElement region);

    abstract Builder country(V6ContextElement country);

    abstract V6Context build();
  }
}
