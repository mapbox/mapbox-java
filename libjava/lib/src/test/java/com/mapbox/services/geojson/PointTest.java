package com.mapbox.services.geojson;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by antonio on 1/30/16.
 */
public class PointTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        com.mapbox.services.commons.geojson.Point geo = com.mapbox.services.commons.geojson.Point.fromJson(BaseGeoJSON.SAMPLE_POINT);
        assertEquals(geo.getType(), "Point");
        assertEquals(geo.getCoordinates().getLongitude(), 100.0, 0.0);
        assertEquals(geo.getCoordinates().getLatitude(), 0.0, 0.0);
        assertFalse(geo.getCoordinates().hasAltitude());
    }

    @Test
    public void toJson() {
        com.mapbox.services.commons.geojson.Point geo = com.mapbox.services.commons.geojson.Point.fromJson(BaseGeoJSON.SAMPLE_POINT);
        compareJson(BaseGeoJSON.SAMPLE_POINT, geo.toJson());
    }

}

