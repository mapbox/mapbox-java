
package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 abstract class $AutoValue_StepIntersection extends StepIntersection {

  private final double[] rawLocation;
  private final List<Integer> bearings;
  private final List<String> classes;
  private final List<Boolean> entry;
  private final Integer in;
  private final Integer out;
  private final List<IntersectionLanes> lanes;

  $AutoValue_StepIntersection(
      @Nullable double[] rawLocation,
      @Nullable List<Integer> bearings,
      @Nullable List<String> classes,
      @Nullable List<Boolean> entry,
      @Nullable Integer in,
      @Nullable Integer out,
      @Nullable List<IntersectionLanes> lanes) {
    this.rawLocation = rawLocation;
    this.bearings = bearings;
    this.classes = classes;
    this.entry = entry;
    this.in = in;
    this.out = out;
    this.lanes = lanes;
  }

  @Nullable
  @SerializedName(value = "location")
  @SuppressWarnings(value = {"mutable"})
  @Override
  double[] rawLocation() {
    return rawLocation;
  }

  @Nullable
  @Override
  public List<Integer> bearings() {
    return bearings;
  }

  @Nullable
  @Override
  public List<String> classes() {
    return classes;
  }

  @Nullable
  @Override
  public List<Boolean> entry() {
    return entry;
  }

  @Nullable
  @Override
  public Integer in() {
    return in;
  }

  @Nullable
  @Override
  public Integer out() {
    return out;
  }

  @Nullable
  @Override
  public List<IntersectionLanes> lanes() {
    return lanes;
  }

  @Override
  public String toString() {
    return "StepIntersection{"
        + "rawLocation=" + Arrays.toString(rawLocation) + ", "
        + "bearings=" + bearings + ", "
        + "classes=" + classes + ", "
        + "entry=" + entry + ", "
        + "in=" + in + ", "
        + "out=" + out + ", "
        + "lanes=" + lanes
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof StepIntersection) {
      StepIntersection that = (StepIntersection) o;
      return (Arrays.equals(this.rawLocation, (that instanceof $AutoValue_StepIntersection) ? (($AutoValue_StepIntersection) that).rawLocation : that.rawLocation()))
           && ((this.bearings == null) ? (that.bearings() == null) : this.bearings.equals(that.bearings()))
           && ((this.classes == null) ? (that.classes() == null) : this.classes.equals(that.classes()))
           && ((this.entry == null) ? (that.entry() == null) : this.entry.equals(that.entry()))
           && ((this.in == null) ? (that.in() == null) : this.in.equals(that.in()))
           && ((this.out == null) ? (that.out() == null) : this.out.equals(that.out()))
           && ((this.lanes == null) ? (that.lanes() == null) : this.lanes.equals(that.lanes()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= Arrays.hashCode(this.rawLocation);
    h *= 1000003;
    h ^= (bearings == null) ? 0 : this.bearings.hashCode();
    h *= 1000003;
    h ^= (classes == null) ? 0 : this.classes.hashCode();
    h *= 1000003;
    h ^= (entry == null) ? 0 : this.entry.hashCode();
    h *= 1000003;
    h ^= (in == null) ? 0 : this.in.hashCode();
    h *= 1000003;
    h ^= (out == null) ? 0 : this.out.hashCode();
    h *= 1000003;
    h ^= (lanes == null) ? 0 : this.lanes.hashCode();
    return h;
  }

  static final class Builder extends StepIntersection.Builder {
    private double[] rawLocation;
    private List<Integer> bearings;
    private List<String> classes;
    private List<Boolean> entry;
    private Integer in;
    private Integer out;
    private List<IntersectionLanes> lanes;
    Builder() {
    }
    @Override
    public StepIntersection.Builder rawLocation(@Nullable double[] rawLocation) {
      this.rawLocation = rawLocation;
      return this;
    }
    @Override
    public StepIntersection.Builder bearings(@Nullable List<Integer> bearings) {
      this.bearings = bearings;
      return this;
    }
    @Override
    public StepIntersection.Builder classes(@Nullable List<String> classes) {
      this.classes = classes;
      return this;
    }
    @Override
    public StepIntersection.Builder entry(@Nullable List<Boolean> entry) {
      this.entry = entry;
      return this;
    }
    @Override
    public StepIntersection.Builder in(@Nullable Integer in) {
      this.in = in;
      return this;
    }
    @Override
    public StepIntersection.Builder out(@Nullable Integer out) {
      this.out = out;
      return this;
    }
    @Override
    public StepIntersection.Builder lanes(@Nullable List<IntersectionLanes> lanes) {
      this.lanes = lanes;
      return this;
    }
    @Override
    public StepIntersection build() {
      return new AutoValue_StepIntersection(
          this.rawLocation,
          this.bearings,
          this.classes,
          this.entry,
          this.in,
          this.out,
          this.lanes);
    }
  }

}
