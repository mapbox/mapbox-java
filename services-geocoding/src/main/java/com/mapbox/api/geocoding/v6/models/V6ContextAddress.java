package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Element of the {@link V6Context} which holds complete geographical hierarchy for a given address.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding/#the-context-object">The Context Object</a>
 */
@AutoValue
public abstract class V6ContextAddress extends V6JsonObject {

  /**
   * Element id. This id can be queried directly via a forward geocoding search to traverse into
   * a different geographical layer.
   *
   * @return element id
   */
  @Nullable
  @SerializedName("mapbox_id")
  public abstract String mapboxId();

  /**
   * Element name. Depending on geographic features stores information like street name.
   *
   * @return element name
   */
  @Nullable
  @SerializedName("name")
  public abstract String name();

  /**
   * Element address_number. The address number for the address this is holding.
   *
   * @return element address_number
   */
  @Nullable
  @SerializedName("address_number")
  public abstract String addressNumber();

  /**
   * Element street_name. The street name for the address this is holding.
   *
   * @return element name
   */
  @Nullable
  @SerializedName("street_name")
  public abstract String streetName();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<V6ContextAddress> typeAdapter(Gson gson) {
    return new AutoValue_V6ContextAddress.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  abstract static class Builder extends BaseBuilder<Builder> {

    public abstract Builder mapboxId(String mapboxId);

    public abstract Builder name(String name);

    public abstract Builder addressNumber(String addressNumber);

    public abstract Builder streetName(String streetName);

    abstract V6ContextAddress build();
  }
}
