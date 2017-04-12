package com.mapbox.services.api.staticimage.v1.models;

import com.mapbox.services.Constants;
import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.mapbox.services.commons.models.Position;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Marker class containing name, label and color to pass it through the overlay for static map.
 *
 * @since 2.1.0
 */

public class Marker {

  private String marker;

  public Marker(Builder builder) {

    if (builder.getPrecision() > 0) {
      String pattern = "0." + new String(new char[builder.getPrecision()]).replace("\0", "0");
      DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
      df.applyPattern(pattern);
      df.setRoundingMode(RoundingMode.FLOOR);
      marker = String.format(Constants.DEFAULT_LOCALE, "%s-%s+%s(%s,%s)",
        builder.getName(), builder.getLabel(), builder.getColor(), df.format(builder.getLon()),
        df.format(builder.getLat()));
    } else {
      marker = String.format(Constants.DEFAULT_LOCALE, "%s-%s+%s(%f,%f)", builder.getName(),
        builder.getLabel(), builder.getColor(), builder.getLon(), builder.getLat());
    }
  }

  public String getMarker() {
    return marker;
  }


  public static class Builder {


    private String name;
    private String label = "";
    private String color = "";
    private Double lat;
    private Double lon;

    // This field isn't part of the URL
    private int precision = -1;

    /**
     * Marker shape and size. Options are pin-s, pin-m, pin-l
     *
     * @return String containing the shape and size.
     * @since 2.1.0
     */
    public String getName() {
      return name;
    }

    /**
     * Marker shape and size. Options are pin-s, pin-m, pin-l
     *
     * @param name String containing the shape and size
     * @since 2.1.0
     */
    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * Marker symbol. Options are an alphanumeric label  a through  z ,
     * 0 through  99 , or a valid Maki icon. If a letter is requested,
     * it will be rendered uppercase only.
     *
     * @return String containing the marker symbol.
     * @since 2.1.0
     */
    public String getLabel() {
      return label;
    }

    /**
     * Marker symbol. Options are an alphanumeric label  a through  z ,
     * 0 through  99 , or a valid Maki icon. If a letter is requested,
     * it will be rendered uppercase only.
     *
     * @param label String containing the marker symbol.
     * @since 2.1.0
     */
    public Builder setLabel(String label) {
      this.label = label;
      return this;
    }

    /**
     * A 3- or 6-digit hexadecimal color code.
     *
     * @return String containing the color.
     * @since 2.1.0
     */
    public String getColor() {
      return color;
    }

    /**
     * A 3 or 6-digit hexadecimal color code.
     *
     * @param color String containing the color.
     * @since 2.1.0
     */
    public Builder setColor(String color) {
      this.color = color;
      return this;
    }

    public Position getPosition() {
      return Position.fromCoordinates(lon, lat);
    }

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

    public int getPrecision() {
      return precision;
    }

    public Marker build() throws ServicesException {

      if (name == null || name.isEmpty() || !name.equals(Constants.PIN_SMALL)
        && !name.equals(Constants.PIN_MEDIUM) && !name.equals(Constants.PIN_LARGE)) {
        throw new ServicesException(
          "You need to set a marker name using one of the three Mapbox Service Constant names."
        );
      }

      if (lat == null || lon == null) {
        throw new ServicesException("You need to give the marker either lon/lat coordinates or a Position object.");
      }

      if (!color.isEmpty()) {
        String hex_pattern = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        Pattern pattern = Pattern.compile(hex_pattern);
        Matcher matcher = pattern.matcher(color);
        if (!matcher.matches()) {
          throw new ServicesException("You need to pass 3- or 6-digit hexadecimal color code.");
        }
      }

      return new Marker(this);
    }
  }
}