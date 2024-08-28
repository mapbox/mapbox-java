package com.mapbox.api.geocoding.v6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.mapbox.core.exceptions.ServicesException;

/**
 * Structured Input is a type of Forward geocoding search that allows you to define the
 * feature type of each element of the search query by type. This can increase the accuracy of
 * results for well-formed datasets.
 *
 * Structured input replaces string query parameter in the forward geocoding.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding/#forward-geocoding-with-structured-input">Structured input</a>
 */
@AutoValue
public abstract class V6StructuredInputQuery {

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

  @SerializedName("country")
  @Nullable
  abstract String country();

  /**
   * Creates a new {@link V6StructuredInputQuery.Builder} object.
   * @return Builder object.
   */
  public static V6StructuredInputQuery.Builder builder() {
    return new AutoValue_V6StructuredInputQuery.Builder();
  }

  /**
   * This builder is used to create a new instance that holds structured input request options.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * A string including address_number and street. These values can be provided as separate
     * parameters address_number and street listed below.
     *
     * @param addressLine1 structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder addressLine1(@NonNull String addressLine1);

    /**
     * The number associated with the house.
     *
     * @param addressNumber structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder addressNumber(@NonNull String addressNumber);

    /**
     * The name of the street in the address.
     *
     * @param street structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder street(@NonNull String street);

    /**
     * In some countries like Japan, the block is a component in the address.
     *
     * @param block structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder block(@NonNull String block);

    /**
     * Typically these are cities, villages, municipalities, etc.
     * They are usually features used in postal addressing, and are suitable for display in ambient
     * end-user applications where current-location context is needed
     * (for example, in weather displays).
     *
     * @param place structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder place(@NonNull String place);

    /**
     * Top-level sub-national administrative features, such as states in the United States
     * or provinces in Canada or China.
     *
     * @param region structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder region(@NonNull String region);

    /**
     * Postal codes used in country-specific national addressing systems.
     *
     * @param postcode structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder postcode(@NonNull String postcode);

    /**
     * Official sub-city features present in countries where such an additional administrative
     * layer is used in postal addressing, or where such features are commonly referred to in local
     * parlance. Examples include city districts in Brazil and Chile and arrondissements in France.
     *
     * @param locality structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder locality(@NonNull String locality);

    /**
     * Colloquial sub-city features often referred to in local parlance. Unlike locality features,
     * these typically lack official status and may lack universally agreed-upon boundaries.
     *
     * @param neighborhood structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder neighborhood(@NonNull String neighborhood);

    /**
     * Generally recognized countries or, in some cases like Hong Kong, an area of quasi-national
     * administrative status that has a designated country code under <a href="https://www.iso.org/iso-3166-country-codes.html">ISO 3166-1</a>.
     *
     * @param country structured input component.
     * @return this builder for chaining options together
     */
    public abstract Builder country(@NonNull String country);

    abstract V6StructuredInputQuery autoBuild();

    /**
     * Build a new {@link V6StructuredInputQuery} object.
     *
     * @return a new {@link V6StructuredInputQuery} using the provided values in this builder
     * @throws ServicesException if all components are null.
     */
    public V6StructuredInputQuery build() {
      final V6StructuredInputQuery query = autoBuild();

      if (query.addressLine1() == null
        && query.addressNumber() == null
        && query.street() == null
        && query.block() == null
        && query.place() == null
        && query.region() == null
        && query.postcode() == null
        && query.locality() == null
        && query.neighborhood() == null
        && query.country() == null
      ) {
        throw new ServicesException("At least one component must be non null");
      }

      return query;
    }
  }
}
