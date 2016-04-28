package com.mapbox.services.directions.v4.models;

import com.mapbox.services.commons.geojson.LineString;

import java.util.ArrayList;
import java.util.List;

/**
 * Gives detailed information about an individual route such as the duration, distance and geometry.
 */
public class DirectionsRoute {

    private int distance;
    private int duration;
    private String summary;
    private String geometry;
    private List<RouteStep> steps;

    public DirectionsRoute() {
        steps = new ArrayList<>();
    }

    /**
     * The distance traveled from origin to destination.
     *
     * @return an integer number with unit meters.
     */
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * The estimated travel time from origin to destination.
     *
     * @return an integer number with unit seconds.
     */
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * A short, human-readable summary of the route (e.g., major roads traversed).
     *
     * @return a string briefly describing route.
     */
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gives the geometry of the route. Commonly used to draw the route on the map view.
     *
     * @return encoded polyline with precision 6.
     */
    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    /**
     * Steps gives you the route instructions.
     *
     * @return List of {@link RouteStep} objects.
     */
    public List<RouteStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RouteStep> steps) {
        this.steps = steps;
    }

    /**
     * Gets a GeoJSON LineString which can be used to get route coordinates useful for
     * drawing on a map view.
     *
     * @param precision of encoded polyline.
     * @return GeoJSON LineString.
     */
    public LineString asLineString(int precision) {
        return LineString.fromPolyline(getGeometry(), precision);
    }

}
