
package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 abstract class $AutoValue_DirectionsWaypoint extends DirectionsWaypoint {

  private final String name;
  private final double[] rawLocation;

  $AutoValue_DirectionsWaypoint(
      @Nullable String name,
      @Nullable double[] rawLocation) {
    this.name = name;
    this.rawLocation = rawLocation;
  }

  @Nullable
  @Override
  public String name() {
    return name;
  }

  @Nullable
  @SerializedName(value = "location")
  @SuppressWarnings(value = {"mutable"})
  @Override
  double[] rawLocation() {
    return rawLocation;
  }

  @Override
  public String toString() {
    return "DirectionsWaypoint{"
        + "name=" + name + ", "
        + "rawLocation=" + Arrays.toString(rawLocation)
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof DirectionsWaypoint) {
      DirectionsWaypoint that = (DirectionsWaypoint) o;
      return ((this.name == null) ? (that.name() == null) : this.name.equals(that.name()))
           && (Arrays.equals(this.rawLocation, (that instanceof $AutoValue_DirectionsWaypoint) ? (($AutoValue_DirectionsWaypoint) that).rawLocation : that.rawLocation()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= (name == null) ? 0 : this.name.hashCode();
    h *= 1000003;
    h ^= Arrays.hashCode(this.rawLocation);
    return h;
  }

  static final class Builder extends DirectionsWaypoint.Builder {
    private String name;
    private double[] rawLocation;
    Builder() {
    }
    @Override
    public DirectionsWaypoint.Builder name(@Nullable String name) {
      this.name = name;
      return this;
    }
    @Override
    public DirectionsWaypoint.Builder rawLocation(@Nullable double[] rawLocation) {
      this.rawLocation = rawLocation;
      return this;
    }
    @Override
    public DirectionsWaypoint build() {
      return new AutoValue_DirectionsWaypoint(
          this.name,
          this.rawLocation);
    }
  }

}
