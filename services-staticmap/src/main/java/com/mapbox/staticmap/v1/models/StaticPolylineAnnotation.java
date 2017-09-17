package com.mapbox.staticmap.v1.models;


import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.staticmap.v1.MapboxStaticMap;

import java.util.List;

/**
 * Mapbox Static Image API polyline overlay. Building this object allows you to place a line or
 * object on top or within your static map image. The polyline must be encoded with a precision of 5
 * decimal places and can be created using the
 * {@link com.mapbox.geojson.utils.PolylineUtils#encode(List, int)}.
 *
 * @since 2.1.0
 */
@AutoValue
public abstract class StaticPolylineAnnotation {

  /**
   * Build a new {@link StaticPolylineAnnotation} object.
   *
   * @return a {@link StaticPolylineAnnotation.Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_StaticPolylineAnnotation.Builder();
  }

  public String url() {
    StringBuilder sb = new StringBuilder();

    sb.append("path");
    if (strokeWidth() != null) {
      sb.append("-").append(strokeColor());
    }
    if (strokeColor() != null) {
      sb.append("+").append(strokeColor());
    }
    if (strokeOpacity() != null) {
      sb.append("-").append(strokeOpacity());
    }
    if (fillColor() != null) {
      sb.append("+").append(fillColor());
    }
    if (fillOpacity() != null) {
      sb.append("-").append(fillOpacity());
    }
    sb.append("(").append(polyline()).append(")");
    return sb.toString();
  }

  @Nullable
  abstract Double strokeWidth();

  @Nullable
  abstract Integer strokeColor();

  @Nullable
  abstract Float strokeOpacity();

  @Nullable
  abstract Integer fillColor();

  @Nullable
  abstract Float fillOpacity();

  @NonNull
  abstract String polyline();

  /**
   * Builder used for passing in custom parameters.
   *
   * @since 2.1.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Defines the line stroke width for the path.
     *
     * @param strokeWidth a double value defining the stroke width
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder strokeWidth(@Nullable @FloatRange(from = 0) Double strokeWidth);

    /**
     * Set the line outer stroke color.
     *
     * @param strokeColor a ColorInt representing the stroke color
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder strokeColor(@ColorInt Integer strokeColor);

    /**
     * Value between 0, completely transparent, and 1, opaque for the line stroke.
     *
     * @param strokeOpacity value between 0 and 1 representing the stroke opacity
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder strokeOpacity(@Nullable @FloatRange(from = 0, to = 1) Float strokeOpacity);

    /**
     * Set the inner line fill color.
     *
     * @param fillColor a ColorInt representing the fill color
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder fillColor(@ColorInt Integer fillColor);

    /**
     * Value between 0, completely transparent, and 1, opaque for the line fill.
     *
     * @param fillOpacity value between 0 and 1 representing the fill opacity
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder fillOpacity(@Nullable @FloatRange(from = 0, to = 1) Float fillOpacity);

    /**
     * The current polyline string being used for the paths geometry. You can use
     * {@link com.mapbox.geojson.utils.PolylineUtils#decode(String, int)} to decode the string using
     * precision 5.
     *
     * @param polyline a String containing the paths geometry information
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder polyline(@NonNull String polyline);

    /**
     * This uses the provided parameters set using the {@link Builder} and creates a new
     * {@link StaticMarkerAnnotation} object which can be passed into the {@link MapboxStaticMap}
     * request.
     *
     * @return a new instance of StaticPolylineAnnotation
     * @since 2.1.0
     */
    public abstract StaticPolylineAnnotation build();
  }
}