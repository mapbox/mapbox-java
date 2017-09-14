
package com.mapbox.directions.v5.models;

import android.support.annotation.NonNull;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 abstract class $AutoValue_DirectionsResponse extends DirectionsResponse {

  private final String code;

  $AutoValue_DirectionsResponse(
      String code) {
    if (code == null) {
      throw new NullPointerException("Null code");
    }
    this.code = code;
  }

  @NonNull
  @Override
  public String code() {
    return code;
  }

  @Override
  public String toString() {
    return "DirectionsResponse{"
        + "code=" + code
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof DirectionsResponse) {
      DirectionsResponse that = (DirectionsResponse) o;
      return (this.code.equals(that.code()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.code.hashCode();
    return h;
  }

  static final class Builder extends DirectionsResponse.Builder {
    private String code;
    Builder() {
    }
    @Override
    public DirectionsResponse.Builder code(String code) {
      if (code == null) {
        throw new NullPointerException("Null code");
      }
      this.code = code;
      return this;
    }
    @Override
    public DirectionsResponse build() {
      String missing = "";
      if (this.code == null) {
        missing += " code";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_DirectionsResponse(
          this.code);
    }
  }

}
