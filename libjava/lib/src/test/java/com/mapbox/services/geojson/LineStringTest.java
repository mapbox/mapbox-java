package com.mapbox.services.geojson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by antonio on 1/30/16.
 */
public class LineStringTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        com.mapbox.services.commons.geojson.LineString geo = com.mapbox.services.commons.geojson.LineString.fromJson(BaseGeoJSON.SAMPLE_LINESTRING);
        assertEquals(geo.getType(), "LineString");
        assertEquals(geo.getCoordinates().get(0).getLongitude(), 100.0, 0.0);
        assertEquals(geo.getCoordinates().get(0).getLatitude(), 0.0, 0.0);
        assertFalse(geo.getCoordinates().get(0).hasAltitude());
    }

    @Test
    public void toJson() {
        com.mapbox.services.commons.geojson.LineString geo = com.mapbox.services.commons.geojson.LineString.fromJson(BaseGeoJSON.SAMPLE_LINESTRING);
        compareJson(BaseGeoJSON.SAMPLE_LINESTRING, geo.toJson());
    }

}
