package com.mapbox.staticmap.v1.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.geojson.Point;
import com.mapbox.services.exceptions.ServicesException;
import com.mapbox.services.utils.TextUtils;
import com.mapbox.staticmap.v1.StaticMapCriteria;
import com.mapbox.staticmap.v1.StaticMapCriteria.MarkerCriteria;

import java.awt.Color;
import java.util.Locale;

/**
 * Mapbox Static Image API marker overlay. Building this object allows you to place a marker on top
 * or within your static image. The marker can either use the default marker (though you can change
 * it's color and size) or you have the option to also pass in a custom marker icon using it's url.
 *
 * @since 2.1.0
 */
@AutoValue
public abstract class StaticMarkerAnnotation {

  /**
   * Build a new {@link StaticMarkerAnnotation} object with the initial values set for the
   * {@link #name()} to {@link StaticMapCriteria#MEDIUM_PIN}.
   *
   * @return a {@link StaticMarkerAnnotation.Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_StaticMarkerAnnotation.Builder()
      .name(StaticMapCriteria.MEDIUM_PIN);
  }

  public String url() {
    String url;
    if (iconUrl() != null) {
      return String.format(
        Locale.US, "url-%s(%f,%f)", iconUrl(), lnglat().longitude(), lnglat().latitude());
    }

    if (color() != null && label() != null && !TextUtils.isEmpty(label())) {
      url = String.format(Locale.US, "%s-%s+%s", name(), label(), toHexString(color()));
    } else if (label() != null && !TextUtils.isEmpty(label())) {
      url = String.format(Locale.US, "%s-%s", name(), label());
    } else if (color() != null) {
      url = String.format(Locale.US, "%s-%s", name(), toHexString(color()));
    } else {
      url = name();
    }

    return String.format(Locale.US, "%s(%f,%f)", url, lnglat().longitude(), lnglat().latitude());
  }

  @Nullable
  abstract String name();

  @Nullable
  abstract String label();

  @Nullable
  abstract Color color();

  @Nullable
  abstract Point lnglat();

  @Nullable
  abstract String iconUrl();

  /**
   * This builder is used to create a new request to the Mapbox Static Map API. At a bare minimum,
   * your request must include a name and {@link StaticMarkerAnnotation.Builder#lnglat(Point)}.
   * All other fields can be left alone inorder to use the default behaviour of the API.
   *
   * @since 2.1.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Modify the markers scale factor using one of the pre defined
     * {@link StaticMapCriteria#SMALL_PIN}, {@link StaticMapCriteria#MEDIUM_PIN}, or
     * {@link StaticMapCriteria#LARGE_PIN}.
     *
     * @param name one of the three string sizes provided in this methods summary
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder name(@MarkerCriteria String name);

    /**
     * Marker symbol. Options are an alphanumeric label "a" through "z", "0" through  "99", or a
     * valid Maki icon. If a letter is requested, it will be rendered uppercase only.
     *
     * @param label a valid alphanumeric value
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    // TODO place a filter on only accepted labels
    public abstract Builder label(String label);

    /**
     * A reference to a {@link Color} which defines the markers color.
     *
     * @param color {@link Color} denoting the marker icon color
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder color(@Nullable Color color);

    /**
     * Represents where the marker should be shown on the map.
     *
     * @param lnglat a GeoJSON Point which denotes where the marker will be placed on the static
     *               map image. Altitude value, if given, will be ignored
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder lnglat(Point lnglat);

    public abstract Builder iconUrl(@Nullable String url);

    abstract StaticMarkerAnnotation autoBuild();

    public StaticMarkerAnnotation build() {
      StaticMarkerAnnotation marker = autoBuild();
      if (marker.lnglat() == null) {
        throw new ServicesException("A Static map marker requires a defined longitude and latitude"
          + " coordinate.");
      }
      return marker;
    }
  }

  // TODO move to utils class
  public final static String toHexString(Color color) throws NullPointerException {
    String hexColour = Integer.toHexString(color.getRGB() & 0xffffff);
    if (hexColour.length() < 6) {
      hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
    }
    return hexColour;
  }
}