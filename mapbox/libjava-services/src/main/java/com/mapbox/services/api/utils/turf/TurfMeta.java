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
    int i;
    int g;
    int j;
    int k;
    int l;
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
    for (i = 0; i < stop; i++) {

      geometryMaybeCollection = (isFeatureCollection ? ((FeatureCollection) layer).getFeatures().get(i).getGeometry() :
        (isFeature ? ((Feature) layer).getGeometry() : layer));

      isGeometryCollection = geometryMaybeCollection.getType().equals("GeometryCollection");
      stopG = isGeometryCollection ? ((GeometryCollection) geometryMaybeCollection).getGeometries().size() : 1;

      for (g = 0; g < stopG; g++) {

        geometry = isGeometryCollection
          ? (((GeometryCollection) geometryMaybeCollection).getGeometries().get(g))
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
          for (j = 0; j < ((Polygon) geometry).getCoordinates().size(); j++) {
            for (k = 0; k < ((Polygon) geometry).getCoordinates().get(j).size() - wrapShrink; k++) {
              coords.add(((Polygon) geometry).getCoordinates().get(j).get(k));
            }
          }
        } else if (geometry.getType().equals("MultiLineString")) {
          for (j = 0; j < ((MultiLineString) geometry).getCoordinates().size(); j++) {
            for (k = 0; k < ((MultiLineString) geometry).getCoordinates().get(j).size(); k++) {
              coords.add(((MultiLineString) geometry).getCoordinates().get(j).get(k));
            }
          }
        } else if (geometry.getType().equals("MultiPolygon")) {
          for (j = 0; j < ((MultiPolygon) geometry).getCoordinates().size(); j++) {
            for (k = 0; k < ((MultiPolygon) geometry).getCoordinates().get(j).size(); k++) {
              for (l = 0; l < ((MultiPolygon) geometry).getCoordinates().get(j).get(k).size() - wrapShrink; l++) {
                coords.add(((MultiPolygon) geometry).getCoordinates().get(j).get(k).get(l));
              }
            }
          }
        } else if (geometry.getType().equals("GeometryCollection")) {
          for (j = 0; j < ((GeometryCollection) geometry).getGeometries().size(); j++) {
            coordEach(((GeometryCollection) geometry).getGeometries().get(j), excludeWrapCoord);
          }
        } else {
          throw new TurfException("Unknown Geometry Type");
        }
      }
    }
    return coords;
  }
}
