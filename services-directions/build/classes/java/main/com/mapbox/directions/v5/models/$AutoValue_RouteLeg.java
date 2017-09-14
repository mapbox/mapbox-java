
package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 abstract class $AutoValue_RouteLeg extends RouteLeg {

  private final Double distance;
  private final Double duration;
  private final String summary;
  private final List<LegStep> steps;
  private final LegAnnotation annotation;

  $AutoValue_RouteLeg(
      @Nullable Double distance,
      @Nullable Double duration,
      @Nullable String summary,
      @Nullable List<LegStep> steps,
      @Nullable LegAnnotation annotation) {
    this.distance = distance;
    this.duration = duration;
    this.summary = summary;
    this.steps = steps;
    this.annotation = annotation;
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
  public String summary() {
    return summary;
  }

  @Nullable
  @Override
  public List<LegStep> steps() {
    return steps;
  }

  @Nullable
  @Override
  public LegAnnotation annotation() {
    return annotation;
  }

  @Override
  public String toString() {
    return "RouteLeg{"
        + "distance=" + distance + ", "
        + "duration=" + duration + ", "
        + "summary=" + summary + ", "
        + "steps=" + steps + ", "
        + "annotation=" + annotation
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RouteLeg) {
      RouteLeg that = (RouteLeg) o;
      return ((this.distance == null) ? (that.distance() == null) : this.distance.equals(that.distance()))
           && ((this.duration == null) ? (that.duration() == null) : this.duration.equals(that.duration()))
           && ((this.summary == null) ? (that.summary() == null) : this.summary.equals(that.summary()))
           && ((this.steps == null) ? (that.steps() == null) : this.steps.equals(that.steps()))
           && ((this.annotation == null) ? (that.annotation() == null) : this.annotation.equals(that.annotation()));
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
    h ^= (summary == null) ? 0 : this.summary.hashCode();
    h *= 1000003;
    h ^= (steps == null) ? 0 : this.steps.hashCode();
    h *= 1000003;
    h ^= (annotation == null) ? 0 : this.annotation.hashCode();
    return h;
  }

  static final class Builder extends RouteLeg.Builder {
    private Double distance;
    private Double duration;
    private String summary;
    private List<LegStep> steps;
    private LegAnnotation annotation;
    Builder() {
    }
    @Override
    public RouteLeg.Builder distance(@Nullable Double distance) {
      this.distance = distance;
      return this;
    }
    @Override
    public RouteLeg.Builder duration(@Nullable Double duration) {
      this.duration = duration;
      return this;
    }
    @Override
    public RouteLeg.Builder summary(@Nullable String summary) {
      this.summary = summary;
      return this;
    }
    @Override
    public RouteLeg.Builder steps(@Nullable List<LegStep> steps) {
      this.steps = steps;
      return this;
    }
    @Override
    public RouteLeg.Builder annotation(@Nullable LegAnnotation annotation) {
      this.annotation = annotation;
      return this;
    }
    @Override
    public RouteLeg build() {
      return new AutoValue_RouteLeg(
          this.distance,
          this.duration,
          this.summary,
          this.steps,
          this.annotation);
    }
  }

}
