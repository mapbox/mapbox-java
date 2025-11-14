package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

/*
 * Toll cost information.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
@SuppressWarnings({"checkstyle:javadoctype", "checkstyle:javadocmethod"})
public abstract class TollCost extends DirectionsJsonObject {

  /*
   * Create a new instance of this class by using the `Builder` class.
   *
   * return this class's `Builder` for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_TollCost.Builder();
  }

  /*
   * Toll cost currency in ISO 4217 format. Refers to numbers in `CostPerVehicleSize`.
   *
   * return toll cost currency
   */
  @Nullable
  public abstract String currency();

  /*
   * Payment methods for this toll road.
   *
   * return payment methods
   */
  @Nullable
  @SerializedName("payment_methods")
  public abstract PaymentMethods paymentMethods();

  /*
   * Convert the current `TollCost` to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified `TollCost`.
   *
   * return a `Builder` with the same values set to match the ones defined in this `TollCost`
   */
  public abstract Builder toBuilder();

  /*
   * Gson type adapter for parsing Gson to this class.
   *
   * param gson the built `Gson` object
   * return the type adapter for this class
   */
  public static TypeAdapter<TollCost> typeAdapter(Gson gson) {
    return new AutoValue_TollCost.GsonTypeAdapter(gson);
  }

  /*
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * param json a formatted valid JSON string defining a TollCost
   * return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static TollCost fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, TollCost.class);
  }

  /*
   * This builder can be used to set the values describing the `TollCost`.
   */
  @AutoValue.Builder
  @SuppressWarnings({"checkstyle:javadoctype", "checkstyle:javadocmethod"})
  public abstract static class Builder extends DirectionsJsonObject.Builder<TollCost.Builder> {

    /*
     * Toll cost currency. Refers to numbers in `CostPerVehicleSize`.
     *
     * param currency toll cost currency
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder currency(@Nullable String currency);

    /*
     * Payment methods for this toll road.
     *
     * param paymentMethods payment methods
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder paymentMethods(@Nullable PaymentMethods paymentMethods);

    /*
     * Build a new `TollCost` object.
     *
     * return a new `TollCost` using the provided values in this builder
     */
    @NonNull
    public abstract TollCost build();
  }
}
