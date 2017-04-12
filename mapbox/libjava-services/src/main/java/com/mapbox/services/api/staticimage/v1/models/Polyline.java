package com.mapbox.services.api.staticimage.v1.models;


import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.utils.PolylineUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polyline {

  private double strokeWidth;
  private String strokeColor;
  private double strokeOpacity;
  private String fillColor;
  private double fillOpacity;
  private String polyline;

  public double getStrokeWidth() {
    return strokeWidth;
  }

  /**
   * Positive number for line stroke width.
   *
   * @param strokeWidth
   * @since 2.1.0
   */
  public void setStrokeWidth(double strokeWidth) {
    this.strokeWidth = strokeWidth;
  }

  public String getStrokeColor() {
    return strokeColor;
  }

  public void setStrokeColor(String strokeColor) {
    this.strokeColor = strokeColor;
  }

  public double getStrokeOpacity() {
    return strokeOpacity;
  }

  public void setStrokeOpacity(double strokeOpacity) {
    this.strokeOpacity = strokeOpacity;
  }

  public String getFillColor() {
    return fillColor;
  }

  public void setFillColor(String fillColor) {
    this.fillColor = fillColor;
  }

  public double getFillOpacity() {
    return fillOpacity;
  }

  public void setFillOpacity(double fillOpacity) {
    this.fillOpacity = fillOpacity;
  }

  public String getPolyline() {
    return polyline;
  }

  public void setPolyline(String polyline) {
    this.polyline = polyline;
  }

  public String getPolylinePathSegment() {

    return String.format(Constants.DEFAULT_LOCALE, "%f+%s-%f+%s-%f(%s)",
      strokeWidth, strokeColor, strokeOpacity, fillColor, fillOpacity, polyline);
  }


  public void isValid() throws ServicesException {

    if (polyline == null) {
      throw new ServicesException("Creating a path overlay requires a valid polyline string.");
    }

  }
}






