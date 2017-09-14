
package com.mapbox.directions.v5.models;

import android.support.annotation.Nullable;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 abstract class $AutoValue_IntersectionLanes extends IntersectionLanes {

  private final Boolean valid;
  private final List<String> indications;

  $AutoValue_IntersectionLanes(
      @Nullable Boolean valid,
      @Nullable List<String> indications) {
    this.valid = valid;
    this.indications = indications;
  }

  @Nullable
  @Override
  public Boolean valid() {
    return valid;
  }

  @Nullable
  @Override
  public List<String> indications() {
    return indications;
  }

  @Override
  public String toString() {
    return "IntersectionLanes{"
        + "valid=" + valid + ", "
        + "indications=" + indications
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof IntersectionLanes) {
      IntersectionLanes that = (IntersectionLanes) o;
      return ((this.valid == null) ? (that.valid() == null) : this.valid.equals(that.valid()))
           && ((this.indications == null) ? (that.indications() == null) : this.indications.equals(that.indications()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= (valid == null) ? 0 : this.valid.hashCode();
    h *= 1000003;
    h ^= (indications == null) ? 0 : this.indications.hashCode();
    return h;
  }

  static final class Builder extends IntersectionLanes.Builder {
    private Boolean valid;
    private List<String> indications;
    Builder() {
    }
    @Override
    public IntersectionLanes.Builder valid(@Nullable Boolean valid) {
      this.valid = valid;
      return this;
    }
    @Override
    public IntersectionLanes.Builder indications(@Nullable List<String> indications) {
      this.indications = indications;
      return this;
    }
    @Override
    public IntersectionLanes build() {
      return new AutoValue_IntersectionLanes(
          this.valid,
          this.indications);
    }
  }

}
