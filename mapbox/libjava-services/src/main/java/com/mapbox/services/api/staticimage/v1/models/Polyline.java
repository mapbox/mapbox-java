package com.mapbox.services.api.staticimage.v1.models;

import com.mapbox.services.api.ServicesException;

public class Polyline {

  private String path;

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

  public String getPath() {
    return path;
  }

  public static class Builder {

    private Double strokeWidth;
    private String strokeColor;
    private Float strokeOpacity;
    private String fillColor;
    private Float fillOpacity;
    private String polyline;

    public Double getStrokeWidth() {
      return strokeWidth;
    }

    /**
     * Positive number for line stroke width.
     *
     * @param strokeWidth
     * @since 2.1.0
     */
    public Builder setStrokeWidth(double strokeWidth) {
      this.strokeWidth = strokeWidth;
      return this;
    }

    public String getStrokeColor() {
      return strokeColor;
    }

    public Builder setStrokeColor(String strokeColor) {
      this.strokeColor = strokeColor;
      return this;
    }

    public Float getStrokeOpacity() {
      return strokeOpacity;
    }

    public Builder setStrokeOpacity(float strokeOpacity) {
      this.strokeOpacity = strokeOpacity;
      return this;
    }

    public String getFillColor() {
      return fillColor;
    }

    public Builder setFillColor(String fillColor) {
      this.fillColor = fillColor;
      return this;
    }

    public Float getFillOpacity() {
      return fillOpacity;
    }

    public Builder setFillOpacity(float fillOpacity) {
      this.fillOpacity = fillOpacity;
      return this;
    }

    public String getPolyline() {
      return polyline;
    }

    public Builder setPolyline(String polyline) {
      this.polyline = polyline;
      return this;
    }

    public Polyline build() throws ServicesException {

      if (polyline == null) {
        throw new ServicesException("Creating a path overlay requires a valid polyline string.");
      }

      return new Polyline(this);
    }
  }
}






