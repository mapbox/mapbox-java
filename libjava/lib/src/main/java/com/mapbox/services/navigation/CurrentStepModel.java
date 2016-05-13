package com.mapbox.services.navigation;

import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.turf.TurfMeasurement;

/**
 * Created by antonio on 5/13/16.
 */
public class CurrentStepModel {

    public double step;
    public double distance;
    public Point snapToLocation;

    public double stepDistance;
    public boolean shouldReRoute;
    public double absoluteDistance;

    public boolean alertUserLevelLow;
    public boolean alertUserLevelHigh;

}
