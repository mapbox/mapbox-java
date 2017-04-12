package com.mapbox.services.api.staticimage.v1.models;

import com.mapbox.services.api.ServicesException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mapbox Static Image API polyline overlay. Building this object allows you to place a line or object on top or within
 * your static image. The polyline must be encoded with a precision of 5 decimal places and can be created using the
 * {@link com.mapbox.services.commons.utils.PolylineUtils#encode(List, int)}.
 *
 * @since 2.1.0
 */
public class Polyline {

  private String path;

  /**
   * A Polyline constructor
   *
   * @param builder a Polyline builder.
   * @since 2.1.0
   */
  public Polyline(Builder builder) {

    StringBuilder sb = new StringBuilder();

    sb.append("path");
    if (builder.getStrokeWidth() != null) {
      sb.append("-").append(builder.getStrokeWidth());
    }
    if (builder.getStrokeColor() != null) {
      sb.append("+").append(builder.getStrokeColor());
    }
    if (builder.getStrokeOpacity() != null) {
      sb.append("-").append(builder.getStrokeOpacity());
    }
    if (builder.getFillColor() != null) {
      sb.append("+").append(builder.getFillColor());
    }
    if (builder.getFillOpacity() != null) {
      sb.append("-").append(builder.getFillOpacity());
    }
    sb.append("(").append(builder.getPolyline()).append(")");
    path = sb.toString();
  }

  /**
   * Gives a formatted string containing the built polyline for usage with the static image API.
   *
   * @return A String representing a single polyline object usable when requesting a static image.
   * @since 2.1.0
   */
  public String getPath() {
    return path;
  }

  /**
   * Builder used for passing in custom parameters.
   *
   * @since 2.1.0
   */
  public static class Builder {

    private Double strokeWidth;
    private String strokeColor;
    private Float strokeOpacity;
    private String fillColor;
    private Float fillOpacity;
    private String polyline;

    /**
     * Get the currently set stroke width for the line.
     *
     * @return Positive Double value representing the stroke width.
     * @since 2.1.0
     */
    public Double getStrokeWidth() {
      return strokeWidth;
    }

    /**
     * Positive Double value for line stroke width. If a negative value is passed, a {@link ServicesException} will
     * occur.
     *
     * @param strokeWidth A positive double value.
     * @return This Polyline builder.
     * @since 2.1.0
     */
    public Builder setStrokeWidth(double strokeWidth) {
      this.strokeWidth = strokeWidth;
      return this;
    }

    /**
     * Provides a String representing the hex code for the line stroke.
     *
     * @return A 3- or 6-digit hexadecimal color code as a String.
     * @since 2.1.0
     */
    public String getStrokeColor() {
      return strokeColor;
    }

    /**
     * A 3- or 6-digit hexadecimal color code for the line stroke. If a non-valid color's provided, a
     * {@link ServicesException} will occur.
     *
     * @param strokeColor A string containing a 3 or 6 digit hexadecimal color code.
     * @return This Polyline builder.
     * @since 2.1.0
     */
    public Builder setStrokeColor(String strokeColor) {
      this.strokeColor = strokeColor;
      return this;
    }

    /**
     * A Float between 0 (transparent) and 1 (opaque) representing the lines opacity.
     *
     * @return a Float value between 0 and 1.
     * @since 2.1.0
     */
    public Float getStrokeOpacity() {
      return strokeOpacity;
    }

    /**
     * A number between 0 (transparent) and 1 (opaque) for line stroke opacity. If a value out of range is provided, a
     * {@link ServicesException} will occur.
     *
     * @param strokeOpacity a float value between 0 and 1 representing the lines opacity.
     * @return This Polyline builder.
     * @since 2.1.0
     */
    public Builder setStrokeOpacity(float strokeOpacity) {
      this.strokeOpacity = strokeOpacity;
      return this;
    }

    /**
     * Provides a String representing the hex code for the fill color.
     *
     * @return a 3- or 6-digit hexadecimal color code for the fill.
     * @since 2.1.0
     */
    public String getFillColor() {
      return fillColor;
    }

    /**
     * Optionally pass a 3- or 6-digit hexadecimal color code for the fill. If a non-valid color's provided, a
     * {@link ServicesException} will occur.
     *
     * @param fillColor a string containing a 3 or 6 digit hexadecimal color code.
     * @return This Polyline builder.
     * @since 2.1.0
     */
    public Builder setFillColor(String fillColor) {
      this.fillColor = fillColor;
      return this;
    }

    /**
     * If set, a number between 0 (transparent) and 1 (opaque) for the fill opacity.
     *
     * @return a float value between 0 and 1 representing the fills opacity.
     * @since 2.1.0
     */
    public Float getFillOpacity() {
      return fillOpacity;
    }

    /**
     * Optionally pass a number between 0 (transparent) and 1 (opaque) for the fill opacity. If a value out of range is
     * provided, a {@link ServicesException} will occur.
     *
     * @param fillOpacity a Float value between 0 and 1.
     * @return This Polyline builder.
     * @since 2.1.0
     */
    public Builder setFillOpacity(float fillOpacity) {
      this.fillOpacity = fillOpacity;
      return this;
    }

    /**
     * The current polyline string being used for the paths geometry. You can use
     * {@link com.mapbox.services.commons.utils.PolylineUtils#decode(String, int)} to decode the string using
     * precision 5.
     *
     * @return A string which containing the paths geometry information.
     * @since 2.1.0
     */
    public String getPolyline() {
      return polyline;
    }

    /**
     * A valid encoded polyline encoded as a URI component. This can be created using the
     * {@link com.mapbox.services.commons.utils.PolylineUtils#encode(List, int)} utility.
     *
     * @param polyline A string containing the paths geometry information.
     * @return This Polyline buidler.
     * @since 2.1.0
     */
    public Builder setPolyline(String polyline) {
      this.polyline = polyline;
      return this;
    }

    /**
     * Call to build a new instance of a Polyline object.
     *
     * @return a polyline object ready for usage with the Static Image API.
     * @throws ServicesException If any issues are recognized that could cause API errors.
     * @since 2.1.0
     */
    public Polyline build() throws ServicesException {

      if (polyline == null) {
        throw new ServicesException("Creating a path overlay requires a valid polyline string.");
      }

      if (strokeWidth != null) {
        if (strokeWidth < 0) {
          throw new ServicesException("The stroke width must be a positive number.");
        }
      }

      if (strokeOpacity != null) {
        if (strokeOpacity < 0 || strokeOpacity > 1) {
          throw new ServicesException("Stroke opacity can only be a float value between 0 and 1.");
        }
      }

      if (fillOpacity != null) {
        if (fillOpacity < 0 || fillOpacity > 1) {
          throw new ServicesException("Fill opacity can only be a float value between 0 and 1.");
        }
      }

      if (strokeColor != null) {
        String hexPattern = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        Pattern pattern = Pattern.compile(hexPattern);
        Matcher matcher = pattern.matcher(strokeColor);
        if (!matcher.matches()) {
          throw new ServicesException("You need to pass 3- or 6-digit hexadecimal color code.");
        }
      }

      if (fillColor != null) {
        String hexPattern = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        Pattern pattern = Pattern.compile(hexPattern);
        Matcher matcher = pattern.matcher(fillColor);
        if (!matcher.matches()) {
          throw new ServicesException("You need to pass 3- or 6-digit hexadecimal color code.");
        }
      }

      return new Polyline(this);
    }
  }
}






