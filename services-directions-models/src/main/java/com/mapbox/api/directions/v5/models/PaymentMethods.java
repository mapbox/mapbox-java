package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

/*
 * Payment methods for the toll road.
 */
@AutoValue
@SuppressWarnings({"checkstyle:javadoctype", "checkstyle:javadocmethod"})
public abstract class PaymentMethods extends DirectionsJsonObject {

  /*
   * Create a new instance of this class by using the `Builder` class.
   *
   * return this class's `Builder` for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_PaymentMethods.Builder();
  }

  /*
   * Information about payment by etc.
   *
   * return etc payment method
   */
  @Nullable
  public abstract CostPerVehicleSize etc();

  /*
   * Information about payment by cash.
   *
   * return cash payment method
   */
  @Nullable
  public abstract CostPerVehicleSize cash();

  /*
   * Convert the current `PaymentMethods` to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified `PaymentMethods`.
   *
   * return a `Builder` with the same values set to match the ones defined in this `PaymentMethods`
   */
  public abstract Builder toBuilder();

  /*
   * Gson type adapter for parsing Gson to this class.
   *
   * param gson the built `Gson` object
   * return the type adapter for this class
   */
  public static TypeAdapter<PaymentMethods> typeAdapter(Gson gson) {
    return new AutoValue_PaymentMethods.GsonTypeAdapter(gson);
  }

  /*
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * param json a formatted valid JSON string defining a PaymentMethods
   * return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static PaymentMethods fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, PaymentMethods.class);
  }

  /*
   * This builder can be used to set the values describing the `PaymentMethods`.
   */
  @AutoValue.Builder
  @SuppressWarnings({"checkstyle:javadoctype", "checkstyle:javadocmethod"})
  public abstract static class Builder
          extends DirectionsJsonObject.Builder<PaymentMethods.Builder> {

    /*
     * Information about payment by etc.
     *
     * param etc payment method
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder etc(@Nullable CostPerVehicleSize etc);

    /*
     * Information about payment by cash.
     *
     * param cash payment method
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder cash(@Nullable CostPerVehicleSize cash);

    /*
     * Build a new `PaymentMethods` object.
     *
     * return a new `PaymentMethods` using the provided values in this builder
     */
    @NonNull
    public abstract PaymentMethods build();
  }
}
