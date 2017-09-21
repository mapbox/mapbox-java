package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

/**
 * A Point is a type of {@link Geometry}.
 *
 * @see <a href='http://geojson.org/geojson-spec.html#point'>Official GeoJSON Point Specifications</a>
 * @since 1.0.0
 */
public class Point implements Geometry<Position> {

  private final String type = "Point";
  private Position coordinates;

  /**
   * Private constructor.
   *
   * @param coordinates {@link Position} making up the Point.
   * @since 1.0.0
   */
  private Point(Position coordinates) {
    this.coordinates = coordinates;
  }

  /**
   * Should always be "Point".
   *
   * @return String "Point".
   * @since 1.0.0
   */
  @Override
  public String getType() {
    return type;
  }

  /**
   * Get the {@link Position} making up the Point.
   *
   * @return {@link Position} making up the Point.
   * @since 1.0.0
   */
  @Override
  public Position getCoordinates() {
    return coordinates;
  }

  @Override
  public void setCoordinates(Position coordinates) {
    this.coordinates = coordinates;
  }

  /**
   * Creates a {@link Point} from a given coordinate.
   *
   * @param coordinates {@link Position} where point should be located.
   * @return {@link Point}.
   * @since 1.0.0
   */
  public static Point fromCoordinates(Position coordinates) {
    return new Point(coordinates);
  }

  public static Point fromCoordinates(double[] coordinates) {
    return fromCoordinates(Position.fromCoordinates(coordinates));
  }

  /**
   * Create a GeoJSON Point object from JSON.
   *
   * @param json String of JSON making up a Point.
   * @return {@link Point} GeoJSON object.
   * @since 1.0.0
   */
  public static Point fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Position.class, new PositionDeserializer());
    return gson.create().fromJson(json, Point.class);
  }

  /**
   * Convert feature into JSON.
   *
   * @return String containing Point JSON.
   * @since 1.0.0
   */
  @Override
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Position.class, new PositionSerializer());
    return gson.create().toJson(this);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Point)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    Point point = (Point) object;
    return coordinates.equals(point.coordinates);
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return this.toJson();
  }
}