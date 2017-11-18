package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class DirectionsError {

  @Nullable
  public abstract String code();

  @Nullable
  public abstract String message();

  public static Builder builder() {
    return new AutoValue_DirectionsError.Builder();
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<DirectionsError> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsError.GsonTypeAdapter(gson);
  }


  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder code(String code);

    public abstract Builder message(String message);

    public abstract DirectionsError build();
  }
}
