package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class VoiceInstructions {

  public static Builder builder() {
    return new AutoValue_VoiceInstructions.Builder();
  }

  @Nullable
  public abstract Double distanceAlongGeometry();

  @Nullable
  public abstract String announcement();

  @Nullable
  public abstract String ssmlAnnouncement();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<VoiceInstructions> typeAdapter(Gson gson) {
    return new AutoValue_VoiceInstructions.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder distanceAlongGeometry(Double distanceAlongGeometry);

    public abstract Builder announcement(String announcement);

    public abstract Builder ssmlAnnouncement(String ssmlAnnouncement);

    public abstract VoiceInstructions build();
  }
}
