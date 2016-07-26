package com.mapbox.services.commons.tidy;

import com.google.gson.JsonArray;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by antonio on 7/26/16.
 */
public class Tidy {

    private final static int DEFAULT_MINIMUM_DISTANCE = 10; // 10 meters
    private final static int DEFAULT_MINIMUM_TIME = 5000; // 5 seconds
    private final static int DEFAULT_MAXIMUM_POINTS = 100;

    private int minimumDistance;
    private int minimumTime;
    private int maximumPoints;
    private SimpleDateFormat dateFormat;

    public final static String KEY_COORD_TIMES = "coordTimes";
    public final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public Tidy() {
        minimumDistance = DEFAULT_MINIMUM_DISTANCE;
        minimumTime = DEFAULT_MINIMUM_TIME;
        maximumPoints = DEFAULT_MAXIMUM_POINTS;
        dateFormat = new SimpleDateFormat(Tidy.DATE_FORMAT, Locale.US);
    }

    public Tidy(int minimumDistance, int minimumTime, int maximumPoints) {
        this.minimumDistance = minimumDistance;
        this.minimumTime = minimumTime;
        this.maximumPoints = maximumPoints;
        dateFormat = new SimpleDateFormat(Tidy.DATE_FORMAT, Locale.US);
    }

    public int getMinimumDistance() {
        return minimumDistance;
    }

    public void setMinimumDistance(int minimumDistance) {
        this.minimumDistance = minimumDistance;
    }

    public int getMinimumTime() {
        return minimumTime;
    }

    public void setMinimumTime(int minimumTime) {
        this.minimumTime = minimumTime;
    }

    public int getMaximumPoints() {
        return maximumPoints;
    }

    public void setMaximumPoints(int maximumPoints) {
        this.maximumPoints = maximumPoints;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

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
                double Dx = TurfMeasurement.distance(point1, point2, TurfConstants.UNIT_KILOMETERS) * 1000;

                // Skip point if its too close to each other
                if (Dx < minimumDistance) {
                    continue;
                }

                // Calculate sampling time difference between successive points in seconds
                if (timestamp != null && timestamp.size() > 0) {
                    Date time1;
                    Date time2;
                    try {
                        time1 = dateFormat.parse(timestamp.get(i).getAsString());
                        time2 = dateFormat.parse(timestamp.get(i + 1).getAsString());
                    } catch (ParseException e) {
                        throw new ServicesException("Tidy expects the date in this format "
                                + "(you can use setDateFormat() to set your own): " + DATE_FORMAT);
                    }

                    // Skip point if sampled to close to each other
                    long Tx = time2.getTime() - time1.getTime();
                    if (Tx < minimumTime) {
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
