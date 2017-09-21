package com.mapbox.services.commons.geojson;

import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * A GeoJSON object with the type "GeometryCollection" is a geometry object which represents a
 * collection of geometry objects.
 *
 * @see <a href='geojson.org/geojson-spec.html#geometry-collection'>Official GeoJSON GeometryCollection Specifications</a>
 * @since 1.0.0
 */
public class GeometryCollection implements GeoJSON {

  private final String type = "GeometryCollection";
  private final List<Geometry> geometries;

  /**
   * Private constructor.
   *
   * @param geometries List of {@link Geometry}.
   * @since 1.0.0
   */
  public GeometryCollection(List<Geometry> geometries) {
    this.geometries = geometries;
  }

  /**
   * Should always be "GeometryCollection".
   *
   * @return String "GeometryCollection".
   * @since 1.0.0
   */
  @Override
  public String getType() {
    return type;
  }

  /**
   * Get the List containing all the geometries within collection.
   *
   * @return List of geometries within collection.
   * @since 1.0.0
   */
  public List<Geometry> getGeometries() {
    return geometries;
  }

  /**
   * Create a {@link GeometryCollection} from a List of geometries.
   *
   * @param geometries List of {@link Geometry}
   * @return new {@link GeometryCollection}
   * @since 1.0.0
   */
  public static GeometryCollection fromGeometries(List<Geometry> geometries) {
    return new GeometryCollection(geometries);
  }

  /**
   * Create a GeoJSON geometry collection object from JSON.
   *
   * @param json String of JSON making up a geometry collection.
   * @return {@link GeometryCollection} GeoJSON object.
   * @since 1.0.0
   */
  public static GeometryCollection fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Position.class, new PositionDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    return gson.create().fromJson(json, GeometryCollection.class);
  }

  /**
   * Convert geometry collection into JSON.
   *
   * @return String containing geometry collection JSON.
   * @since 1.0.0
   */
  @Override
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Position.class, new PositionSerializer());
    return gson.create().toJson(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GeometryCollection that = (GeometryCollection) o;

    if (!type.equals(that.type)) {
      return false;
    }
    return geometries != null ? geometries.equals(that.geometries) : that.geometries == null;
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + (geometries != null ? geometries.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return this.toJson();
  }
}
