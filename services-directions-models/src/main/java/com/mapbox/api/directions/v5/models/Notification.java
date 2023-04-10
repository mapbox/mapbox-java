package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;

/**
 * Class containing information about route notification. See {@link RouteLeg#notifications()}.
 */
@AutoValue
public abstract class Notification extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_Notification.Builder();
  }

  /**
   * Notification type. Can be one of {@link DirectionsCriteria.NotificationsTypeCriteria}.
   *
   * @return notification type
   */
  @NonNull
  @DirectionsCriteria.NotificationsTypeCriteria
  public abstract String type();

  /**
   * Notification subtype. Can be one of {@link DirectionsCriteria.NotificationsSubtypeCriteria},
   * depending on {@link Notification#type()}.
   *
   * @return notification subtype
   */
  @Nullable
  @DirectionsCriteria.NotificationsSubtypeCriteria
  public abstract String subtype();

  /**
   * Leg-wise start index of the area that violates the request parameter.
   *
   * @return start index
   */
  @SerializedName("geometry_index_start")
  @Nullable
  public abstract Integer geometryIndexStart();

  /**
   * Leg-wise end index of the area that violates the request parameter.
   *
   * @return end index
   */
  @SerializedName("geometry_index_end")
  @Nullable
  public abstract Integer geometryIndexEnd();

  /**
   * Notification details specific to {@link Notification#type()}
   * and {@link Notification#subtype()}.
   *
   * @return notification details
   */
  @Nullable
  public abstract NotificationDetails details();

  /**
   * Convert the current {@link Notification} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Notification}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined
   *   in this {@link Notification}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Notification> typeAdapter(Gson gson) {
    return new AutoValue_Notification.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a Notification
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static Notification fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Notification.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Notification}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * Notification type. Can be one of {@link DirectionsCriteria.NotificationsTypeCriteria}.
     *
     * @param type notification type
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder type(
      @NonNull @DirectionsCriteria.NotificationsTypeCriteria String type
    );

    /**
     * Notification subtype. Can be one of {@link DirectionsCriteria.NotificationsSubtypeCriteria},
     * depending on {@link Notification.Builder#type()}.
     *
     * @param subtype notification subtype
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder subtype(
      @Nullable @DirectionsCriteria.NotificationsSubtypeCriteria String subtype
    );

    /**
     * Leg-wise start index of the area that violates the request parameter.
     *
     * @param geometryIndexStart start index
     * @return this builder for chaining options together
     */
    @SerializedName("geometry_index_start")
    @NonNull
    public abstract Builder geometryIndexStart(@Nullable Integer geometryIndexStart);

    /**
     * Leg-wise end index of the area that violates the request parameter.
     *
     * @param geometryIndexEnd end index
     * @return this builder for chaining options together
     */
    @SerializedName("geometry_index_end")
    @NonNull
    public abstract Builder geometryIndexEnd(@Nullable Integer geometryIndexEnd);

    /**
     * Notification details.
     *
     * @param details notification details
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder details(@Nullable NotificationDetails details);

    /**
     * Build a new {@link Notification} object.
     *
     * @return a new {@link Notification} using the provided values in this builder
     */
    @NonNull
    public abstract Notification build();
  }
}
