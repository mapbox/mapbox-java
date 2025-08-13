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
 * Class containing information about notification details specific to
 * {@link Notification#type()} and {@link Notification#subtype()}.
 */
@AutoValue
public abstract class NotificationDetails extends DirectionsJsonObject {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_NotificationDetails.Builder();
  }

  /**
   * If {@link Notification#type()} is {@link DirectionsCriteria#NOTIFICATION_TYPE_VIOLATION}
   * and {@link Notification#subtype()} is one of:
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_HEIGHT},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WIDTH},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WEIGHT},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_UNPAVED},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_POINT_EXCLUSION},
   * it is the requested value which was violated.
   *
   * @return requested value
   */
  @SerializedName("requested_value")
  @Nullable
  public abstract String requestedValue();

  /**
   * If {@link Notification#type()} is {@link DirectionsCriteria#NOTIFICATION_TYPE_VIOLATION}
   * and {@link Notification#subtype()} is one of:
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_HEIGHT},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WIDTH},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WEIGHT},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_UNPAVED},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_POINT_EXCLUSION},
   * it is the actual value associated with the property of the road
   * (as opposed to {@link NotificationDetails#requestedValue()}).
   *
   * @return actual value
   */
  @SerializedName("actual_value")
  @Nullable
  public abstract String actualValue();

  /**
   * If {@link Notification#type()} is {@link DirectionsCriteria#NOTIFICATION_TYPE_VIOLATION}
   * and {@link Notification#subtype()} is one of:
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_HEIGHT},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WIDTH},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WEIGHT},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_UNPAVED},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_POINT_EXCLUSION},
   * it is unit of measure associated with
   * {@link NotificationDetails#requestedValue()} and {@link NotificationDetails#actualValue()}.
   *
   * @return unit
   */
  @Nullable
  public abstract String unit();

  /**
   * If {@link Notification#type()} is {@link DirectionsCriteria#NOTIFICATION_TYPE_VIOLATION}
   * and {@link Notification#subtype()} is one of:
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_HEIGHT},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WIDTH},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WEIGHT},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_UNPAVED},
   * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_POINT_EXCLUSION},
   * it is the message of the notification.
   *
   * @return message
   */
  @Nullable
  public abstract String message();

  /**
   * Convert the current {@link NotificationDetails} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link NotificationDetails}.
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
  public static TypeAdapter<NotificationDetails> typeAdapter(Gson gson) {
    return new AutoValue_NotificationDetails.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a NotificationDetails
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   */
  public static NotificationDetails fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, NotificationDetails.class);
  }

  /**
   * This builder can be used to set the values describing the {@link NotificationDetails}.
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * If {@link Notification#type()} is {@link DirectionsCriteria#NOTIFICATION_TYPE_VIOLATION}
     * and {@link Notification#subtype()} is one of:
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_HEIGHT},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WIDTH},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WEIGHT},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_UNPAVED},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_POINT_EXCLUSION},
     * it is the requested value which was violated.
     *
     * @param requestedValue requested value
     * @return this builder for chaining options together
     */
    @SerializedName("requested_value")
    @NonNull
    public abstract Builder requestedValue(@Nullable String requestedValue);

    /**
     * If {@link Notification#type()} is {@link DirectionsCriteria#NOTIFICATION_TYPE_VIOLATION}
     * and {@link Notification#subtype()} is one of:
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_HEIGHT},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WIDTH},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WEIGHT},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_UNPAVED},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_POINT_EXCLUSION},
     * it is the actual value associated with the property of the road
     * (as opposed to {@link NotificationDetails#requestedValue()}).
     *
     * @param actualValue actual value
     * @return this builder for chaining options together
     */
    @SerializedName("actual_value")
    @NonNull
    public abstract Builder actualValue(@Nullable String actualValue);

    /**
     * If {@link Notification#type()} is {@link DirectionsCriteria#NOTIFICATION_TYPE_VIOLATION}
     * and {@link Notification#subtype()} is one of:
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_HEIGHT},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WIDTH},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WEIGHT},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_UNPAVED},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_POINT_EXCLUSION},
     * it is unit of measure associated with
     * {@link NotificationDetails#requestedValue()} and {@link NotificationDetails#actualValue()}.
     *
     * @param unit unit
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder unit(@Nullable String unit);

    /**
     * If {@link Notification#type()} is {@link DirectionsCriteria#NOTIFICATION_TYPE_VIOLATION}
     * and {@link Notification#subtype()} is one of:
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_HEIGHT},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WIDTH},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_MAX_WEIGHT},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_UNPAVED},
     * {@link DirectionsCriteria#NOTIFICATION_SUBTYPE_POINT_EXCLUSION},
     * it is the message of the notification.
     *
     * @param message message
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder message(@Nullable String message);

    /**
     * Build a new {@link NotificationDetails} object.
     *
     * @return a new {@link NotificationDetails} using the provided values in this builder
     */
    @NonNull
    public abstract NotificationDetails build();
  }
}
