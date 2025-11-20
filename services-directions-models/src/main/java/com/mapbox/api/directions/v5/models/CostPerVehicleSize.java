package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterConfig;

/*
 * Payment method for the toll road. See `TollCost`, `PaymentsMethods`.
 */
@GsonTypeAdapterConfig(useBuilderOnRead = false)
@AutoValue
@SuppressWarnings({"checkstyle:javadoctype", "checkstyle:javadocmethod"})
public abstract class CostPerVehicleSize extends DirectionsJsonObject {

  /*
   * Create a new instance of this class by using the `Builder` class.
   *
   * return this class's `Builder` for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_CostPerVehicleSize.Builder();
  }

  /*
   * Returns the toll cost for a small sized vehicle.
   * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
   * A toll cost of -1 indicates that the underlying data required
   * to compute the toll cost is not available.
   *
   * return toll cost
   */
  @Nullable
  public abstract Double small();

  /*
   * Returns the toll cost for a standard sized vehicle.
   * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
   * A toll cost of -1 indicates that the underlying data required
   * to compute the toll cost is not available.
   *
   * return toll cost
   */
  @Nullable
  public abstract Double standard();

  /*
   * Returns the toll cost for a middle sized vehicle.
   * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
   * A toll cost of -1 indicates that the underlying data required
   * to compute the toll cost is not available.
   *
   * return toll cost
   */
  @Nullable
  public abstract Double middle();

  /*
   * Returns the toll cost for a large sized vehicle.
   * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
   * A toll cost of -1 indicates that the underlying data required
   * to compute the toll cost is not available.
   *
   * return toll cost
   */
  @Nullable
  public abstract Double large();

  /*
   * Returns the toll cost for a jumbo sized vehicle.
   * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
   * A toll cost of -1 indicates that the underlying data required
   * to compute the toll cost is not available.
   *
   * return toll cost
   */
  @Nullable
  public abstract Double jumbo();

  /*
   * Convert the current `CostPerVehicleSize` to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified `CostPerVehicleSize`.
   *
   * return a `Builder` with the same values set to match
   *        the ones defined in this `CostPerVehicleSize`
   */
  public abstract Builder toBuilder();

  /*
   * Gson type adapter for parsing Gson to this class.
   *
   * param gson the built `Gson` object
   * return the type adapter for this class
   */
  public static TypeAdapter<CostPerVehicleSize> typeAdapter(Gson gson) {
    return new AutoValue_CostPerVehicleSize.GsonTypeAdapter(gson);
  }

  /*
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * param json a formatted valid JSON string defining a CostPerVehicleSize
   * return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static CostPerVehicleSize fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, CostPerVehicleSize.class);
  }

  /*
   * This builder can be used to set the values describing the `CostPerVehicleSize`.
   */
  @AutoValue.Builder
  @SuppressWarnings({"checkstyle:javadoctype", "checkstyle:javadocmethod"})
  public abstract static class Builder
          extends DirectionsJsonObject.Builder<CostPerVehicleSize.Builder> {

    /*
     * Toll cost for a small sized vehicle.
     * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
     * A toll cost of -1 indicates that the underlying data required
     * to compute the toll cost is not available.
     *
     * param small toll cost
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder small(@Nullable Double small);

    /*
     * Toll cost for a standard sized vehicle.
     * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
     * A toll cost of -1 indicates that the underlying data required
     * to compute the toll cost is not available.
     *
     * param standard toll cost
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder standard(@Nullable Double standard);

    /*
     * Toll cost for a middle sized vehicle.
     * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
     * A toll cost of -1 indicates that the underlying data required
     * to compute the toll cost is not available.
     *
     * param middle toll cost
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder middle(@Nullable Double middle);

    /*
     * Toll cost for a large sized vehicle.
     * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
     * A toll cost of -1 indicates that the underlying data required
     * to compute the toll cost is not available.
     *
     * param large toll cost
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder large(@Nullable Double large);

    /*
     * Toll cost for a jumbo sized vehicle.
     * A toll cost of 0 is valid and simply means, "no toll costs are incurred for this route".
     * A toll cost of -1 indicates that the underlying data required
     * to compute the toll cost is not available.
     *
     * param jumbo toll cost
     * return this builder for chaining options together
     */
    @NonNull
    public abstract Builder jumbo(@Nullable Double jumbo);

    /*
     * Build a new `CostPerVehicleSize` object.
     *
     * return a new `CostPerVehicleSize` using the provided values in this builder
     */
    @NonNull
    public abstract CostPerVehicleSize build();
  }
}
