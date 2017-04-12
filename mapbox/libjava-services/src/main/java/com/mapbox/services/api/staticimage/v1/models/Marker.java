package com.mapbox.services.api.staticimage.v1.models;

import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.models.Position;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mapbox Static Image API marker overlay. Building this object allows you to place a marker on top or within
 * your static image. The marker can either use the default marker (though you can change it's color and size) or you
 * have the option to also pass in a custom marker icon using it's url.
 *
 * @since 2.1.0
 */
public class Marker {

  private String marker;

  /**
   * A Marker constructor
   *
   * @param builder a Marker builder.
   * @since 2.1.0
   */
  public Marker(Builder builder) {

    if (builder.getPrecision() > 0) {
      String pattern = "0." + new String(new char[builder.getPrecision()]).replace("\0", "0");
      DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
      df.applyPattern(pattern);
      df.setRoundingMode(RoundingMode.FLOOR);

      // Check if using a custom marker url
      if (builder.getUrl() != null) {
        marker = String.format(Constants.DEFAULT_LOCALE, "url-%s(%s,%s)",
          builder.getUrl(), df.format(builder.getLon()), df.format(builder.getLat()));
      } else {
        marker = String.format(Constants.DEFAULT_LOCALE, "%s-%s+%s(%s,%s)",
          builder.getName(), builder.getLabel(), builder.getColor(), df.format(builder.getLon()),
          df.format(builder.getLat()));
      }
    } else {
      // Check if using a custom marker url
      if (builder.getUrl() != null) {
        marker = String.format(Constants.DEFAULT_LOCALE, "url-%s(%s,%s)",
          builder.getUrl(), builder.getLon(), builder.getLat());
      } else {
        marker = String.format(Constants.DEFAULT_LOCALE, "%s-%s+%s(%f,%f)", builder.getName(),
          builder.getLabel(), builder.getColor(), builder.getLon(), builder.getLat());
      }
    }
  }

  /**
   * Gives a formatted string containing the built marker for usage with the static image API.
   *
   * @return A String representing a single marker object usable when requesting a static image.
   * @since 2.1.0
   */
  public String getMarker() {
    return marker;
  }

  /**
   * Builder used for passing in custom parameters.
   *
   * @since 2.1.0
   */
  public static class Builder {

    private String name;
    private String label = "";
    private String color = "";
    private Double lat;
    private Double lon;
    private String url;

    // This field isn't part of the URL
    private int precision = -1;

    /**
     * The URL for the your custom marker icon.
     *
     * @return A string with the Image address URL being used for your marker.
     * @since 2.1.0
     */
    public String getUrl() {
      return url;
    }

    /**
     * The URL for the your custom marker icon. Can be of type {@code PNG} or {@code JPG}.
     *
     * @param url The direct url to your image that will be used for the marker icon.
     * @return This Marker builder.
     * @since 2.1.0
     */
    public Builder setUrl(String url) {
      this.url = url;
      return this;
    }

    /**
     * The markers shape and size which can only be one of the three constants.
     *
     * @return String containing the shape and size of the marker.
     * @since 2.1.0
     */
    public String getName() {
      return name;
    }

    /**
     * Marker shape and size. Options are {@link Constants#PIN_SMALL}, {@link Constants#PIN_MEDIUM}, or
     * {@link Constants#PIN_SMALL}.
     *
     * @param name String containing the shape and size
     * @return This Marker builder
     * @since 2.1.0
     */
    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * Marker symbol. Options are an alphanumeric label {@code a} through {@code z}, {@code 0} through {@code 99}, or a
     * valid Mapbox Maki icon. If a letter is requested, it will be rendered uppercase only.
     *
     * @return String containing the marker symbol.
     * @since 2.1.0
     */
    public String getLabel() {
      return label;
    }

    /**
     * Marker symbol. Options are an alphanumeric label {@code a} through {@code z}, {@code 0} through {@code 99}, or a
     * valid Maki icon. If a letter is requested, it will be rendered uppercase only.
     *
     * @param label String containing the marker symbol.
     * @return This Marker builder.
     * @since 2.1.0
     */
    public Builder setLabel(String label) {
      this.label = label;
      return this;
    }

    /**
     * A 3- or 6-digit hexadecimal color code represented as a String. This defines the marker background color.
     *
     * @return String containing the color.
     * @since 2.1.0
     */
    public String getColor() {
      return color;
    }

    /**
     * A 3- or 6-digit hexadecimal color code represented as a String. If a non-valid color value's provided, a
     * {@link ServicesException} will occur.
     *
     * @param color String containing the color.
     * @return This Marker builder.
     * @since 2.1.0
     */
    public Builder setColor(String color) {
      this.color = color;
      return this;
    }

    /**
     * Get the markers latitude and longitude coordinate as a {@link Position} object.
     *
     * @return a {@link Position} object representing where the marker will be placed on the static image.
     * @since 2.1.0
     */
    public Position getPosition() {
      return Position.fromCoordinates(lon, lat);
    }

    /**
     * Optionally pass in a {@link Position} object containing the latitude and longitude coordinates you'd like your
     * marker to be placed.
     *
     * @param position A {@link Position} object.
     * @return This Marker builder.
     * @since 2.1.0
     */
    public Builder setPosition(Position position) {
      this.lat = position.getLatitude();
      this.lon = position.getLongitude();
      return this;
    }

    /**
     * Latitude for the center point of the static map.
     *
     * @return double number between -90 and 90.
     * @since 2.1.0
     */
    public Double getLat() {
      return lat;
    }

    /**
     * Latitude for the center point of the static map.
     *
     * @param lat double number between -90 and 90.
     * @since 2.1.0
     */
    public Builder setLat(Double lat) {
      this.lat = lat;
      return this;
    }

    /**
     * Longitude for the center point of the static map.
     *
     * @return double number between -180 and 180.
     * @since 2.1.0
     */
    public Double getLon() {
      return lon;
    }

    /**
     * Longitude for the center point of the static map.
     *
     * @param lon double number between -180 and 180.
     * @since 2.1.0
     */
    public Builder setLon(Double lon) {
      this.lon = lon;
      return this;
    }

    /**
     * In order to make the returned images better cacheable on the client, you can set the
     * precision in decimals instead of manually rounding the parameters.
     *
     * @param precision int number representing the precision for the formatter
     * @since 2.1.0
     */
    public Builder setPrecision(int precision) {
      this.precision = precision;
      return this;
    }

    /**
     * The precision value being used for the coordinates.
     *
     * @return an integer value representing the precision being used.
     * @since 2.1.0
     */
    public int getPrecision() {
      return precision;
    }

    public Marker build() throws ServicesException {

      if (url == null && (name == null || name.isEmpty() || !name.equals(Constants.PIN_SMALL)
        && !name.equals(Constants.PIN_MEDIUM) && !name.equals(Constants.PIN_LARGE))) {
        throw new ServicesException(
          "You need to set a marker name using one of the three Mapbox Service Constant names."
        );
      }

      if (lat == null || lon == null) {
        throw new ServicesException("You need to give the marker either lon/lat coordinates or a Position object.");
      }

      if (!color.isEmpty()) {
        String hexPattern = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        Pattern pattern = Pattern.compile(hexPattern);
        Matcher matcher = pattern.matcher(color);
        if (!matcher.matches()) {
          throw new ServicesException("You need to pass 3- or 6-digit hexadecimal color code.");
        }
      }

      return new Marker(this);
    }
  }
}