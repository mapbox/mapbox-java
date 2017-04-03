package com.mapbox.services.api.staticimage.v1.models;

import com.mapbox.services.Constants;
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
 * @since 2.0.1
 */

public class Marker {

  private String name;
  private String label = "";
  private String color = "";
  private Double lat;
  private Double lon;
  private Position position;

  // This field isn't part of the URL
  private int precision = -1;


  public Marker() {

  }

  /**
   * Marker shape and size. Options are pin-s, pin-m, pin-l
   *
   * @return String containing the shape and size.
   * @since 2.0.1
   */
  public String getName() {
    return name;
  }

  /**
   * Marker shape and size. Options are pin-s, pin-m, pin-l
   *
   * @param name String containing the shape and size
   * @since 2.0.1
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Marker symbol. Options are an alphanumeric label  a through  z ,
   * 0 through  99 , or a valid Maki icon. If a letter is requested,
   * it will be rendered uppercase only.
   *
   * @return String containing the marker symbol.
   * @since 2.0.1
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
   * @since 2.0.1
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * A 3- or 6-digit hexadecimal color code.
   *
   * @return String containing the color.
   * @since 2.0.1
   */
  public String getColor() {
    return color;
  }

  /**
   * A 3 or 6-digit hexadecimal color code.
   *
   * @param color String containing the color.
   * @since 2.0.1
   */
  public void setColor(String color) {
    this.color = color;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  /**
   * Latitude for the center point of the static map.
   *
   * @return double number between -90 and 90.
   * @since 2.0.1
   */
  public Double getLat() {
    return lat;
  }

  /**
   * Latitude for the center point of the static map.
   *
   * @param lat double number between -90 and 90.
   * @since 2.0.1
   */
  public void setLat(Double lat) {
    this.lat = lat;
  }

  /**
   * Longitude for the center point of the static map.
   *
   * @return double number between -180 and 180.
   * @since 2.0.1
   */
  public Double getLon() {
    return lon;
  }

  /**
   * Longitude for the center point of the static map.
   *
   * @param lon double number between -180 and 180.
   * @return void
   * @since 2.0.1
   */
  public void setLon(Double lon) {
    this.lon = lon;
  }


  /**
   * In order to make the returned images better cacheable on the client, you can set the
   * precision in decimals instead of manually rounding the parameters.
   *
   * @param precision int number representing the precision for the formatter
   * @since 2.0.1
   */
  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public int getPrecision() {
    return precision;
  }

  public String getMarkerPathSegment() {

    if (position == null) {
      if (precision > 0) {
        String pattern = "0." + new String(new char[precision]).replace("\0", "0");
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        df.applyPattern(pattern);
        df.setRoundingMode(RoundingMode.FLOOR);
        return String.format(Constants.DEFAULT_LOCALE, "%s-%s+%s(%s,%s)",
          name, label, color, df.format(lon), df.format(lat));
      } else {
        return String.format(Constants.DEFAULT_LOCALE, "%s-%s+%s(%f,%f)", name, label, color, lon, lat);
      }
    } else {
      return String.format(
        Constants.DEFAULT_LOCALE, "%s-%s+%s(%f,%f)", name, label, color, position.getLongitude(), position.getLatitude()
      );
    }
  }

  public void isValid() throws ServicesException {

    if (name == null || name.isEmpty() || !name.equals(Constants.PIN_SMALL)
      && !name.equals(Constants.PIN_MEDIUM) && !name.equals(Constants.PIN_LARGE)) {
      throw new ServicesException(
        "You need to set a marker name using one of the three Mapbox Service Constant names."
      );
    }

    if ((lat == null || lon == null) && position == null) {
      throw new ServicesException("You need to give the marker either lon/lat coordinates or a Position object.");
    }

    if (!color.isEmpty()) {
      String hex_pattern = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
      Pattern pattern = Pattern.compile(hex_pattern);
      Matcher matcher = pattern.matcher(color);
      if (!matcher.matches()) {
        throw new ServicesException(
          "You need to pass 3- or 6-digit hexadecimal color code.");

      }
    }

  }


}
