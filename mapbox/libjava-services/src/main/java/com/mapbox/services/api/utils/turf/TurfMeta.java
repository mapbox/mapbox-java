package com.mapbox.services.api.utils.turf;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.GeoJSON;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.GeometryCollection;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.MultiLineString;
import com.mapbox.services.commons.geojson.MultiPoint;
import com.mapbox.services.commons.geojson.MultiPolygon;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

public class TurfMeta {
  /**
   * Get all coordinates from any GeoJSON object, returning a {@code List} of Position objects.
   *
   * @param layer            Any GeoJSON object.
   * @param excludeWrapCoord whether or not to include the final coordinate of LinearRings that wraps the ring in its
   *                         iteration.
   * @return A {@code List} made up of {@link Position}s.
   * @throws TurfException Occurs when the GeoJSON provided isn't valid.
   * @since 2.0.0
   */
  public static List<Position> coordEach(GeoJSON layer, boolean excludeWrapCoord) throws TurfException {
    int integer1;
    int integer2;
    int integer3;
    int integer4;
    int integer5;
    int stopG;
    boolean isGeometryCollection;
    GeoJSON geometryMaybeCollection;
    Geometry geometry;
    List<Position> coords = new ArrayList<>();
    boolean isFeatureCollection = layer.getType().equals("FeatureCollection");
    boolean isFeature = layer.getType().equals("Feature");
    int stop = isFeatureCollection ? ((FeatureCollection) layer).getFeatures().size() : 1;

    // This logic may look a little weird. The reason why it is that way is because it's trying to be fast. GeoJSON
    // supports multiple kinds of objects at its root: FeatureCollection, Features, Geometries. This function has the
    // responsibility of handling all of them, and that means that some of the `for` loops you see below actually just
    // don't apply to certain inputs. For instance, if you give this just a Point geometry, then both loops are
    // short-circuited and all we do is gradually rename the input until it's called 'geometry'.
    //
    // This also aims to allocate as few resources as possible: just a few numbers and booleans, rather than any
    // temporary arrays as would be required with the normalization approach.
    for (integer1 = 0; integer1 < stop; integer1++) {

      geometryMaybeCollection = (isFeatureCollection ? ((FeatureCollection)
        layer).getFeatures().get(integer1).getGeometry() :
        (isFeature ? ((Feature) layer).getGeometry() : layer));

      isGeometryCollection = geometryMaybeCollection.getType().equals("GeometryCollection");
      stopG = isGeometryCollection ? ((GeometryCollection) geometryMaybeCollection).getGeometries().size() : 1;

      for (integer2 = 0; integer2 < stopG; integer2++) {

        geometry = isGeometryCollection
          ? (((GeometryCollection) geometryMaybeCollection).getGeometries().get(integer2))
          : (Geometry) geometryMaybeCollection;

        int wrapShrink = (excludeWrapCoord
          && (geometry.getType().equals("Polygon") || geometry.getType().equals("MultiPolygon")) ? 1 : 0);

        if (geometry.getType().equals("Point")) {
          coords.add(((Point) geometry).getCoordinates());
        } else if (geometry.getType().equals("LineString")) {
          coords.addAll(((LineString) geometry).getCoordinates());
        } else if (geometry.getType().equals("MultiPoint")) {
          coords.addAll(((MultiPoint) geometry).getCoordinates());
        } else if (geometry.getType().equals("Polygon")) {
          for (integer3 = 0; integer3 < ((Polygon) geometry).getCoordinates().size(); integer3++) {
            for (integer4 = 0; integer4 < ((Polygon) geometry).getCoordinates().get(integer3).size()
              - wrapShrink; integer4++) {
              coords.add(((Polygon) geometry).getCoordinates().get(integer3).get(integer4));
            }
          }
        } else if (geometry.getType().equals("MultiLineString")) {
          for (integer3 = 0; integer3 < ((MultiLineString) geometry).getCoordinates().size(); integer3++) {
            for (integer4 = 0; integer4 < ((MultiLineString) geometry).getCoordinates().get(integer3).size();
                 integer4++) {
              coords.add(((MultiLineString) geometry).getCoordinates().get(integer3).get(integer4));
            }
          }
        } else if (geometry.getType().equals("MultiPolygon")) {
          for (integer3 = 0; integer3 < ((MultiPolygon) geometry).getCoordinates().size(); integer3++) {
            for (integer4 = 0; integer4 < ((MultiPolygon) geometry).getCoordinates().get(integer3).size(); integer4++) {
              for (integer5 = 0; integer5 < ((MultiPolygon) geometry).getCoordinates().get(integer3)
                .get(integer4).size() - wrapShrink; integer5++) {
                coords.add(((MultiPolygon) geometry).getCoordinates().get(integer3).get(integer4).get(integer5));
              }
            }
          }
        } else if (geometry.getType().equals("GeometryCollection")) {
          for (integer3 = 0; integer3 < ((GeometryCollection) geometry).getGeometries().size(); integer3++) {
            coordEach(((GeometryCollection) geometry).getGeometries().get(integer3), excludeWrapCoord);
          }
        } else {
          throw new TurfException("Unknown Geometry Type");
        }
      }
    }
    return coords;
  }
}
