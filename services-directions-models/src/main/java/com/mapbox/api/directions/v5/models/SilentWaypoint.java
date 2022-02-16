package com.mapbox.api.directions.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
abstract public class SilentWaypoint extends DirectionsJsonObject {

  public static Builder builder() {
    return new AutoValue_SilentWaypoint.Builder();
  }

  @SerializedName("waypoint_index")
  public abstract int waypointIndex();
  @SerializedName("distance_from_start")
  public abstract double distanceFromStart();
  @SerializedName("geometry_index")
  public abstract int geometryIndex();

  public static TypeAdapter<SilentWaypoint> typeAdapter(Gson gson) {
    return new AutoValue_SilentWaypoint.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder waypointIndex(int waypointIndex);
    public abstract Builder distanceFromStart(double distanceFromStart);
    public abstract Builder geometryIndex(int geometryIndex);
    public abstract SilentWaypoint build();
  }
}
