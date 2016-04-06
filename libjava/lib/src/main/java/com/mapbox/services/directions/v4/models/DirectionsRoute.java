package com.mapbox.services.directions.v4.models;

import com.mapbox.services.commons.geojson.LineString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 11/6/15.
 */
@Deprecated
public class DirectionsRoute {

    private int distance;
    private int duration;
    private String summary;
    private String geometry;
    private List<RouteStep> steps;

    public DirectionsRoute() {
        steps = new ArrayList<>();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public List<RouteStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RouteStep> steps) {
        this.steps = steps;
    }

    /*
     * Easy decoding
     */

    public LineString asLineString(int precision) {
        return LineString.fromPolyline(getGeometry(), precision);
    }

}
