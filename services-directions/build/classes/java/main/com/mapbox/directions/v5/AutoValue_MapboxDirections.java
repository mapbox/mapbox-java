
package com.mapbox.directions.v5;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_MapboxDirections extends MapboxDirections {

  private final String user;
  private final String profile;
  private final String coordinates;
  private final String baseUrl;
  private final String accessToken;
  private final Boolean alternatives;
  private final String geometries;
  private final String overview;
  private final String radiuses;
  private final String bearings;
  private final Boolean steps;
  private final Boolean continueStraight;
  private final String annotations;
  private final String language;
  private final String clientAppName;

  private AutoValue_MapboxDirections(
      String user,
      String profile,
      String coordinates,
      String baseUrl,
      @Nullable String accessToken,
      @Nullable Boolean alternatives,
      @Nullable String geometries,
      @Nullable String overview,
      @Nullable String radiuses,
      @Nullable String bearings,
      @Nullable Boolean steps,
      @Nullable Boolean continueStraight,
      @Nullable String annotations,
      @Nullable String language,
      @Nullable String clientAppName) {
    this.user = user;
    this.profile = profile;
    this.coordinates = coordinates;
    this.baseUrl = baseUrl;
    this.accessToken = accessToken;
    this.alternatives = alternatives;
    this.geometries = geometries;
    this.overview = overview;
    this.radiuses = radiuses;
    this.bearings = bearings;
    this.steps = steps;
    this.continueStraight = continueStraight;
    this.annotations = annotations;
    this.language = language;
    this.clientAppName = clientAppName;
  }

  @NonNull
  @Override
  String user() {
    return user;
  }

  @NonNull
  @Override
  String profile() {
    return profile;
  }

  @NonNull
  @Override
  String coordinates() {
    return coordinates;
  }

  @NonNull
  @Override
  String baseUrl() {
    return baseUrl;
  }

  @Nullable
  @Override
  String accessToken() {
    return accessToken;
  }

  @Nullable
  @Override
  Boolean alternatives() {
    return alternatives;
  }

  @Nullable
  @Override
  String geometries() {
    return geometries;
  }

  @Nullable
  @Override
  String overview() {
    return overview;
  }

  @Nullable
  @Override
  String radiuses() {
    return radiuses;
  }

  @Nullable
  @Override
  String bearings() {
    return bearings;
  }

  @Nullable
  @Override
  Boolean steps() {
    return steps;
  }

  @Nullable
  @Override
  Boolean continueStraight() {
    return continueStraight;
  }

  @Nullable
  @Override
  String annotations() {
    return annotations;
  }

  @Nullable
  @Override
  String language() {
    return language;
  }

  @Nullable
  @Override
  String clientAppName() {
    return clientAppName;
  }

  @Override
  public String toString() {
    return "MapboxDirections{"
        + "user=" + user + ", "
        + "profile=" + profile + ", "
        + "coordinates=" + coordinates + ", "
        + "baseUrl=" + baseUrl + ", "
        + "accessToken=" + accessToken + ", "
        + "alternatives=" + alternatives + ", "
        + "geometries=" + geometries + ", "
        + "overview=" + overview + ", "
        + "radiuses=" + radiuses + ", "
        + "bearings=" + bearings + ", "
        + "steps=" + steps + ", "
        + "continueStraight=" + continueStraight + ", "
        + "annotations=" + annotations + ", "
        + "language=" + language + ", "
        + "clientAppName=" + clientAppName
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof MapboxDirections) {
      MapboxDirections that = (MapboxDirections) o;
      return (this.user.equals(that.user()))
           && (this.profile.equals(that.profile()))
           && (this.coordinates.equals(that.coordinates()))
           && (this.baseUrl.equals(that.baseUrl()))
           && ((this.accessToken == null) ? (that.accessToken() == null) : this.accessToken.equals(that.accessToken()))
           && ((this.alternatives == null) ? (that.alternatives() == null) : this.alternatives.equals(that.alternatives()))
           && ((this.geometries == null) ? (that.geometries() == null) : this.geometries.equals(that.geometries()))
           && ((this.overview == null) ? (that.overview() == null) : this.overview.equals(that.overview()))
           && ((this.radiuses == null) ? (that.radiuses() == null) : this.radiuses.equals(that.radiuses()))
           && ((this.bearings == null) ? (that.bearings() == null) : this.bearings.equals(that.bearings()))
           && ((this.steps == null) ? (that.steps() == null) : this.steps.equals(that.steps()))
           && ((this.continueStraight == null) ? (that.continueStraight() == null) : this.continueStraight.equals(that.continueStraight()))
           && ((this.annotations == null) ? (that.annotations() == null) : this.annotations.equals(that.annotations()))
           && ((this.language == null) ? (that.language() == null) : this.language.equals(that.language()))
           && ((this.clientAppName == null) ? (that.clientAppName() == null) : this.clientAppName.equals(that.clientAppName()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.user.hashCode();
    h *= 1000003;
    h ^= this.profile.hashCode();
    h *= 1000003;
    h ^= this.coordinates.hashCode();
    h *= 1000003;
    h ^= this.baseUrl.hashCode();
    h *= 1000003;
    h ^= (accessToken == null) ? 0 : this.accessToken.hashCode();
    h *= 1000003;
    h ^= (alternatives == null) ? 0 : this.alternatives.hashCode();
    h *= 1000003;
    h ^= (geometries == null) ? 0 : this.geometries.hashCode();
    h *= 1000003;
    h ^= (overview == null) ? 0 : this.overview.hashCode();
    h *= 1000003;
    h ^= (radiuses == null) ? 0 : this.radiuses.hashCode();
    h *= 1000003;
    h ^= (bearings == null) ? 0 : this.bearings.hashCode();
    h *= 1000003;
    h ^= (steps == null) ? 0 : this.steps.hashCode();
    h *= 1000003;
    h ^= (continueStraight == null) ? 0 : this.continueStraight.hashCode();
    h *= 1000003;
    h ^= (annotations == null) ? 0 : this.annotations.hashCode();
    h *= 1000003;
    h ^= (language == null) ? 0 : this.language.hashCode();
    h *= 1000003;
    h ^= (clientAppName == null) ? 0 : this.clientAppName.hashCode();
    return h;
  }

  static final class Builder extends MapboxDirections.Builder {
    private String user;
    private String profile;
    private String coordinates;
    private String baseUrl;
    private String accessToken;
    private Boolean alternatives;
    private String geometries;
    private String overview;
    private String radiuses;
    private String bearings;
    private Boolean steps;
    private Boolean continueStraight;
    private String annotations;
    private String language;
    private String clientAppName;
    Builder() {
    }
    @Override
    public MapboxDirections.Builder user(String user) {
      if (user == null) {
        throw new NullPointerException("Null user");
      }
      this.user = user;
      return this;
    }
    @Override
    public MapboxDirections.Builder profile(String profile) {
      if (profile == null) {
        throw new NullPointerException("Null profile");
      }
      this.profile = profile;
      return this;
    }
    @Override
    MapboxDirections.Builder coordinates(String coordinates) {
      if (coordinates == null) {
        throw new NullPointerException("Null coordinates");
      }
      this.coordinates = coordinates;
      return this;
    }
    @Override
    public MapboxDirections.Builder baseUrl(String baseUrl) {
      if (baseUrl == null) {
        throw new NullPointerException("Null baseUrl");
      }
      this.baseUrl = baseUrl;
      return this;
    }
    @Override
    public MapboxDirections.Builder accessToken(@Nullable String accessToken) {
      this.accessToken = accessToken;
      return this;
    }
    @Override
    public MapboxDirections.Builder alternatives(@Nullable Boolean alternatives) {
      this.alternatives = alternatives;
      return this;
    }
    @Override
    public MapboxDirections.Builder geometries(@Nullable String geometries) {
      this.geometries = geometries;
      return this;
    }
    @Override
    public MapboxDirections.Builder overview(@Nullable String overview) {
      this.overview = overview;
      return this;
    }
    @Override
    MapboxDirections.Builder radiuses(@Nullable String radiuses) {
      this.radiuses = radiuses;
      return this;
    }
    @Override
    MapboxDirections.Builder bearings(@Nullable String bearings) {
      this.bearings = bearings;
      return this;
    }
    @Override
    public MapboxDirections.Builder steps(@Nullable Boolean steps) {
      this.steps = steps;
      return this;
    }
    @Override
    public MapboxDirections.Builder continueStraight(@Nullable Boolean continueStraight) {
      this.continueStraight = continueStraight;
      return this;
    }
    @Override
    public MapboxDirections.Builder annotations(@Nullable String annotations) {
      this.annotations = annotations;
      return this;
    }
    @Override
    MapboxDirections.Builder language(@Nullable String language) {
      this.language = language;
      return this;
    }
    @Override
    public MapboxDirections.Builder clientAppName(@Nullable String clientAppName) {
      this.clientAppName = clientAppName;
      return this;
    }
    @Override
    MapboxDirections autoBuild() {
      String missing = "";
      if (this.user == null) {
        missing += " user";
      }
      if (this.profile == null) {
        missing += " profile";
      }
      if (this.coordinates == null) {
        missing += " coordinates";
      }
      if (this.baseUrl == null) {
        missing += " baseUrl";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_MapboxDirections(
          this.user,
          this.profile,
          this.coordinates,
          this.baseUrl,
          this.accessToken,
          this.alternatives,
          this.geometries,
          this.overview,
          this.radiuses,
          this.bearings,
          this.steps,
          this.continueStraight,
          this.annotations,
          this.language,
          this.clientAppName);
    }
  }

}
