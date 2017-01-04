package com.mapbox.services.api.utils.tidy;

import com.google.gson.JsonArray;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfMeasurement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Create a tidy geojson by resampling points in the feature based on sampling time and distance.
 * Handy when geometries that have been converted from a noisy GPS/GPX output.
 *
 * @since 1.3.0
 */
public class Tidy {

  private static final int DEFAULT_MINIMUM_DISTANCE = 10; // 10 meters
  private static final int DEFAULT_MINIMUM_TIME = 5000; // 5 seconds
  private static final int DEFAULT_MAXIMUM_POINTS = 100;

  private int minimumDistance;
  private int minimumTime;
  private int maximumPoints;
  private SimpleDateFormat dateFormat;

  public static final String KEY_COORD_TIMES = "coordTimes";
  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  /**
   * Constructor that uses a default 10 meter distance, 5 second minimum time and 100
   * default points.
   *
   * @since 1.3.0
   */
  public Tidy() {
    minimumDistance = DEFAULT_MINIMUM_DISTANCE;
    minimumTime = DEFAULT_MINIMUM_TIME;
    maximumPoints = DEFAULT_MAXIMUM_POINTS;
    dateFormat = new SimpleDateFormat(Tidy.DATE_FORMAT, Locale.US);
  }

  /**
   * Constructor allowing you to pass in specific values for distance, time, and max points.
   *
   * @param minimumDistance in meters between successive coordinates.
   * @param minimumTime     in milliseconds between successive coordinates.
   * @param maximumPoints   for output.
   * @since 1.3.0
   */
  public Tidy(int minimumDistance, int minimumTime, int maximumPoints) {
    this.minimumDistance = minimumDistance;
    this.minimumTime = minimumTime;
    this.maximumPoints = maximumPoints;
    dateFormat = new SimpleDateFormat(Tidy.DATE_FORMAT, Locale.US);
  }

  /**
   * @return the minimum distance set; defaults 10 meters
   * @since 1.3.0
   */
  public int getMinimumDistance() {
    return minimumDistance;
  }

  /**
   * @param minimumDistance set/update the minimum distance in meters between successive
   *                        coordinates.
   * @since 1.3.0
   */
  public void setMinimumDistance(int minimumDistance) {
    this.minimumDistance = minimumDistance;
  }

  /**
   * @return the minimum time set; defaults 5 seconds
   * @since 1.3.0
   */
  public int getMinimumTime() {
    return minimumTime;
  }

  /**
   * @param minimumTime set/update the minimum time in milliseconds between successive coordinates.
   * @since 1.3.0
   */
  public void setMinimumTime(int minimumTime) {
    this.minimumTime = minimumTime;
  }

  /**
   * @return the maximum points set; defaults 100.
   * @since 1.3.0
   */
  public int getMaximumPoints() {
    return maximumPoints;
  }

  /**
   * @param maximumPoints set/update the maximum points output; defaults 100.
   * @since 1.3.0
   */
  public void setMaximumPoints(int maximumPoints) {
    this.maximumPoints = maximumPoints;
  }

  /**
   * @return either the default date format or one that you have set using
   * {@link #setDateFormat(SimpleDateFormat)}.
   * @since 1.3.0
   */
  public SimpleDateFormat getDateFormat() {
    return dateFormat;
  }

  /**
   * @param dateFormat set/update the date format used when {@link #execute(FeatureCollection)}
   *                   is called.
   * @since 1.3.0
   */
  public void setDateFormat(SimpleDateFormat dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * Method that performs the tidying of geojson route passed in. It uses the parameters set when
   * constructing the {@link Tidy} object or the values set using their respected setters. The
   * result returned will be a GeoJSON FeatureCollection with similar coordinates removed.
   *
   * @param geojson {@link FeatureCollection} representing your route that you'd like to tidy up
   *                and remove similar unnecessary coordinates from.
   * @return {@link FeatureCollection} with similar unnecessary coordinates that met within the
   * parameters you specified, removed.
   * @throws TurfException     signals that a Turf exception of some sort has occurred.
   * @throws ServicesException if error occurs Mapbox API related.
   * @since 1.3.0
   */
  public FeatureCollection execute(FeatureCollection geojson) throws TurfException, ServicesException {
    ArrayList<Feature> features = new ArrayList<>();

    List<Position> lineString;
    JsonArray timestamp;

    for (int featureIndex = 0; featureIndex < geojson.getFeatures().size(); featureIndex++) {
      // Skip non LineString features in the collections
      Feature feature = geojson.getFeatures().get(featureIndex);
      if (!feature.getGeometry().getType().equals("LineString")) {
        continue;
      }

      lineString = ((LineString) feature.getGeometry()).getCoordinates();
      timestamp = feature.getProperties().getAsJsonArray(KEY_COORD_TIMES);

      // Loop through the coordinate array of the noisy linestring and build a tidy linestring
      ArrayList<Position> coordinates = new ArrayList<>();
      JsonArray timestamps = new JsonArray();
      for (int i = 0; i < lineString.size(); i++) {
        // Add first and last points
        if (i == 0 || i == lineString.size() - 1) {
          coordinates.add(lineString.get(i));
          if (timestamp != null && timestamp.size() > 0) {
            timestamps.add(timestamp.get(i));
          }
          continue;
        }

        // Calculate distance between successive points in metres
        Point point1 = Point.fromCoordinates(lineString.get(i));
        Point point2 = Point.fromCoordinates(lineString.get(i + 1));
        double distance = TurfMeasurement.distance(point1, point2, TurfConstants.UNIT_KILOMETERS) * 1000;

        // Skip point if its too close to each other
        if (distance < minimumDistance) {
          continue;
        }

        // Calculate sampling time difference between successive points in seconds
        if (timestamp != null && timestamp.size() > 0) {
          Date time1;
          Date time2;
          try {
            time1 = dateFormat.parse(timestamp.get(i).getAsString());
            time2 = dateFormat.parse(timestamp.get(i + 1).getAsString());
          } catch (ParseException parseException) {
            throw new ServicesException("Tidy expects the date in this format "
              + "(you can use setDateFormat() to set your own): " + DATE_FORMAT);
          }

          // Skip point if sampled to close to each other
          long time = time2.getTime() - time1.getTime();
          if (time < minimumTime) {
            continue;
          }
        }

        // Copy the point and timestamp to the tidyOutput
        coordinates.add(lineString.get(i));
        if (timestamp != null && timestamp.size() > 0) {
          timestamps.add(timestamp.get(i));
        }

        // If feature exceeds maximum points, start a new feature beginning at the previous end point
        if (coordinates.size() % maximumPoints == 0) {
          Feature emptyFeature = Feature.fromGeometry(LineString.fromCoordinates(coordinates));
          emptyFeature.addProperty(KEY_COORD_TIMES, timestamps);
          features.add(emptyFeature);

          coordinates = new ArrayList<>();
          timestamps = new JsonArray();

          coordinates.add(lineString.get(i));
          if (timestamp != null && timestamp.size() > 0) {
            timestamps.add(timestamp.get(i));
          }
        }
      }

      Feature emptyFeature = Feature.fromGeometry(LineString.fromCoordinates(coordinates));
      emptyFeature.addProperty(KEY_COORD_TIMES, timestamps);
      features.add(emptyFeature);
    }

    return FeatureCollection.fromFeatures(features);
  }
}
