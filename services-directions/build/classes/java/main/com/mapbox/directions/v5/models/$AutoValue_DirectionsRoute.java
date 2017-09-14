
package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 abstract class $AutoValue_DirectionsRoute extends DirectionsRoute {

  private final Double distance;
  private final Double duration;
  private final String geometry;
  private final Double weight;
  private final String weightName;
  private final List<RouteLeg> legs;

  $AutoValue_DirectionsRoute(
      @Nullable Double distance,
      @Nullable Double duration,
      @Nullable String geometry,
      @Nullable Double weight,
      @Nullable String weightName,
      @Nullable List<RouteLeg> legs) {
    this.distance = distance;
    this.duration = duration;
    this.geometry = geometry;
    this.weight = weight;
    this.weightName = weightName;
    this.legs = legs;
  }

  @Nullable
  @Override
  public Double distance() {
    return distance;
  }

  @Nullable
  @Override
  public Double duration() {
    return duration;
  }

  @Nullable
  @Override
  public String geometry() {
    return geometry;
  }

  @Nullable
  @Override
  public Double weight() {
    return weight;
  }

  @Nullable
  @SerializedName(value = "weight_name")
  @Override
  public String weightName() {
    return weightName;
  }

  @Nullable
  @Override
  public List<RouteLeg> legs() {
    return legs;
  }

  @Override
  public String toString() {
    return "DirectionsRoute{"
        + "distance=" + distance + ", "
        + "duration=" + duration + ", "
        + "geometry=" + geometry + ", "
        + "weight=" + weight + ", "
        + "weightName=" + weightName + ", "
        + "legs=" + legs
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof DirectionsRoute) {
      DirectionsRoute that = (DirectionsRoute) o;
      return ((this.distance == null) ? (that.distance() == null) : this.distance.equals(that.distance()))
           && ((this.duration == null) ? (that.duration() == null) : this.duration.equals(that.duration()))
           && ((this.geometry == null) ? (that.geometry() == null) : this.geometry.equals(that.geometry()))
           && ((this.weight == null) ? (that.weight() == null) : this.weight.equals(that.weight()))
           && ((this.weightName == null) ? (that.weightName() == null) : this.weightName.equals(that.weightName()))
           && ((this.legs == null) ? (that.legs() == null) : this.legs.equals(that.legs()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= (distance == null) ? 0 : this.distance.hashCode();
    h *= 1000003;
    h ^= (duration == null) ? 0 : this.duration.hashCode();
    h *= 1000003;
    h ^= (geometry == null) ? 0 : this.geometry.hashCode();
    h *= 1000003;
    h ^= (weight == null) ? 0 : this.weight.hashCode();
    h *= 1000003;
    h ^= (weightName == null) ? 0 : this.weightName.hashCode();
    h *= 1000003;
    h ^= (legs == null) ? 0 : this.legs.hashCode();
    return h;
  }

  static final class Builder extends DirectionsRoute.Builder {
    private Double distance;
    private Double duration;
    private String geometry;
    private Double weight;
    private String weightName;
    private List<RouteLeg> legs;
    Builder() {
    }
    @Override
    public DirectionsRoute.Builder distance(@Nullable Double distance) {
      this.distance = distance;
      return this;
    }
    @Override
    public DirectionsRoute.Builder duration(@Nullable Double duration) {
      this.duration = duration;
      return this;
    }
    @Override
    public DirectionsRoute.Builder geometry(@Nullable String geometry) {
      this.geometry = geometry;
      return this;
    }
    @Override
    public DirectionsRoute.Builder weight(@Nullable Double weight) {
      this.weight = weight;
      return this;
    }
    @Override
    public DirectionsRoute.Builder weightName(@Nullable String weightName) {
      this.weightName = weightName;
      return this;
    }
    @Override
    public DirectionsRoute.Builder legs(@Nullable List<RouteLeg> legs) {
      this.legs = legs;
      return this;
    }
    @Override
    public DirectionsRoute build() {
      return new AutoValue_DirectionsRoute(
          this.distance,
          this.duration,
          this.geometry,
          this.weight,
          this.weightName,
          this.legs);
    }
  }

}
