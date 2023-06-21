package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Element of the {@link V6Context} which holds complete geographical hierarchy for a given address
 * or place.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#the-context-object">The Context Object</a>
 */
@AutoValue
public abstract class V6ContextElement extends V6JsonObject {

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
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<V6ContextElement> typeAdapter(Gson gson) {
    return new AutoValue_V6ContextElement.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  abstract static class Builder extends BaseBuilder<Builder> {

    public abstract Builder mapboxId(String mapboxId);

    public abstract Builder name(String name);

    abstract V6ContextElement build();
  }
}
