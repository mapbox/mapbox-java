
package com.mapbox.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 abstract class $AutoValue_LegStep extends LegStep {

  private final Double distance;
  private final Double duration;
  private final String geometry;
  private final String name;
  private final String ref;
  private final String destinations;
  private final String mode;
  private final String pronunciation;
  private final String rotaryName;
  private final String rotaryPronunciation;
  private final StepManeuver maneuver;
  private final Double weight;
  private final List<StepIntersection> intersections;
  private final String exits;

  $AutoValue_LegStep(
      @Nullable Double distance,
      @Nullable Double duration,
      @Nullable String geometry,
      @Nullable String name,
      @Nullable String ref,
      @Nullable String destinations,
      String mode,
      @Nullable String pronunciation,
      @Nullable String rotaryName,
      @Nullable String rotaryPronunciation,
      @Nullable StepManeuver maneuver,
      @Nullable Double weight,
      @Nullable List<StepIntersection> intersections,
      @Nullable String exits) {
    this.distance = distance;
    this.duration = duration;
    this.geometry = geometry;
    this.name = name;
    this.ref = ref;
    this.destinations = destinations;
    if (mode == null) {
      throw new NullPointerException("Null mode");
    }
    this.mode = mode;
    this.pronunciation = pronunciation;
    this.rotaryName = rotaryName;
    this.rotaryPronunciation = rotaryPronunciation;
    this.maneuver = maneuver;
    this.weight = weight;
    this.intersections = intersections;
    this.exits = exits;
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
  public String name() {
    return name;
  }

  @Nullable
  @Override
  public String ref() {
    return ref;
  }

  @Nullable
  @Override
  public String destinations() {
    return destinations;
  }

  @NonNull
  @Override
  public String mode() {
    return mode;
  }

  @Nullable
  @Override
  public String pronunciation() {
    return pronunciation;
  }

  @Nullable
  @SerializedName(value = "rotary_name")
  @Override
  public String rotaryName() {
    return rotaryName;
  }

  @Nullable
  @SerializedName(value = "rotary_pronunciation")
  @Override
  public String rotaryPronunciation() {
    return rotaryPronunciation;
  }

  @Nullable
  @Override
  public StepManeuver maneuver() {
    return maneuver;
  }

  @Nullable
  @Override
  public Double weight() {
    return weight;
  }

  @Nullable
  @Override
  public List<StepIntersection> intersections() {
    return intersections;
  }

  @Nullable
  @Override
  public String exits() {
    return exits;
  }

  @Override
  public String toString() {
    return "LegStep{"
        + "distance=" + distance + ", "
        + "duration=" + duration + ", "
        + "geometry=" + geometry + ", "
        + "name=" + name + ", "
        + "ref=" + ref + ", "
        + "destinations=" + destinations + ", "
        + "mode=" + mode + ", "
        + "pronunciation=" + pronunciation + ", "
        + "rotaryName=" + rotaryName + ", "
        + "rotaryPronunciation=" + rotaryPronunciation + ", "
        + "maneuver=" + maneuver + ", "
        + "weight=" + weight + ", "
        + "intersections=" + intersections + ", "
        + "exits=" + exits
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof LegStep) {
      LegStep that = (LegStep) o;
      return ((this.distance == null) ? (that.distance() == null) : this.distance.equals(that.distance()))
           && ((this.duration == null) ? (that.duration() == null) : this.duration.equals(that.duration()))
           && ((this.geometry == null) ? (that.geometry() == null) : this.geometry.equals(that.geometry()))
           && ((this.name == null) ? (that.name() == null) : this.name.equals(that.name()))
           && ((this.ref == null) ? (that.ref() == null) : this.ref.equals(that.ref()))
           && ((this.destinations == null) ? (that.destinations() == null) : this.destinations.equals(that.destinations()))
           && (this.mode.equals(that.mode()))
           && ((this.pronunciation == null) ? (that.pronunciation() == null) : this.pronunciation.equals(that.pronunciation()))
           && ((this.rotaryName == null) ? (that.rotaryName() == null) : this.rotaryName.equals(that.rotaryName()))
           && ((this.rotaryPronunciation == null) ? (that.rotaryPronunciation() == null) : this.rotaryPronunciation.equals(that.rotaryPronunciation()))
           && ((this.maneuver == null) ? (that.maneuver() == null) : this.maneuver.equals(that.maneuver()))
           && ((this.weight == null) ? (that.weight() == null) : this.weight.equals(that.weight()))
           && ((this.intersections == null) ? (that.intersections() == null) : this.intersections.equals(that.intersections()))
           && ((this.exits == null) ? (that.exits() == null) : this.exits.equals(that.exits()));
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
    h ^= (name == null) ? 0 : this.name.hashCode();
    h *= 1000003;
    h ^= (ref == null) ? 0 : this.ref.hashCode();
    h *= 1000003;
    h ^= (destinations == null) ? 0 : this.destinations.hashCode();
    h *= 1000003;
    h ^= this.mode.hashCode();
    h *= 1000003;
    h ^= (pronunciation == null) ? 0 : this.pronunciation.hashCode();
    h *= 1000003;
    h ^= (rotaryName == null) ? 0 : this.rotaryName.hashCode();
    h *= 1000003;
    h ^= (rotaryPronunciation == null) ? 0 : this.rotaryPronunciation.hashCode();
    h *= 1000003;
    h ^= (maneuver == null) ? 0 : this.maneuver.hashCode();
    h *= 1000003;
    h ^= (weight == null) ? 0 : this.weight.hashCode();
    h *= 1000003;
    h ^= (intersections == null) ? 0 : this.intersections.hashCode();
    h *= 1000003;
    h ^= (exits == null) ? 0 : this.exits.hashCode();
    return h;
  }

  static final class Builder extends LegStep.Builder {
    private Double distance;
    private Double duration;
    private String geometry;
    private String name;
    private String ref;
    private String destinations;
    private String mode;
    private String pronunciation;
    private String rotaryName;
    private String rotaryPronunciation;
    private StepManeuver maneuver;
    private Double weight;
    private List<StepIntersection> intersections;
    private String exits;
    Builder() {
    }
    @Override
    public LegStep.Builder distance(@Nullable Double distance) {
      this.distance = distance;
      return this;
    }
    @Override
    public LegStep.Builder duration(@Nullable Double duration) {
      this.duration = duration;
      return this;
    }
    @Override
    public LegStep.Builder geometry(@Nullable String geometry) {
      this.geometry = geometry;
      return this;
    }
    @Override
    public LegStep.Builder name(@Nullable String name) {
      this.name = name;
      return this;
    }
    @Override
    public LegStep.Builder ref(@Nullable String ref) {
      this.ref = ref;
      return this;
    }
    @Override
    public LegStep.Builder destinations(@Nullable String destinations) {
      this.destinations = destinations;
      return this;
    }
    @Override
    public LegStep.Builder mode(String mode) {
      if (mode == null) {
        throw new NullPointerException("Null mode");
      }
      this.mode = mode;
      return this;
    }
    @Override
    public LegStep.Builder pronunciation(@Nullable String pronunciation) {
      this.pronunciation = pronunciation;
      return this;
    }
    @Override
    public LegStep.Builder rotaryName(@Nullable String rotaryName) {
      this.rotaryName = rotaryName;
      return this;
    }
    @Override
    public LegStep.Builder rotaryPronunciation(@Nullable String rotaryPronunciation) {
      this.rotaryPronunciation = rotaryPronunciation;
      return this;
    }
    @Override
    public LegStep.Builder maneuver(@Nullable StepManeuver maneuver) {
      this.maneuver = maneuver;
      return this;
    }
    @Override
    public LegStep.Builder weight(@Nullable Double weight) {
      this.weight = weight;
      return this;
    }
    @Override
    public LegStep.Builder intersections(@Nullable List<StepIntersection> intersections) {
      this.intersections = intersections;
      return this;
    }
    @Override
    public LegStep.Builder exits(@Nullable String exits) {
      this.exits = exits;
      return this;
    }
    @Override
    public LegStep build() {
      String missing = "";
      if (this.mode == null) {
        missing += " mode";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_LegStep(
          this.distance,
          this.duration,
          this.geometry,
          this.name,
          this.ref,
          this.destinations,
          this.mode,
          this.pronunciation,
          this.rotaryName,
          this.rotaryPronunciation,
          this.maneuver,
          this.weight,
          this.intersections,
          this.exits);
    }
  }

}
