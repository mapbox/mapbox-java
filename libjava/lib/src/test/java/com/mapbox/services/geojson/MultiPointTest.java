package com.mapbox.services.geojson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by antonio on 1/30/16.
 */
public class MultiPointTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        com.mapbox.services.commons.geojson.MultiPoint geo = com.mapbox.services.commons.geojson.MultiPoint.fromJson(BaseGeoJSON.SAMPLE_MULTIPOINT);
        assertEquals(geo.getType(), "MultiPoint");
        assertEquals(geo.getCoordinates().get(0).getLongitude(), 100.0, 0.0);
        assertEquals(geo.getCoordinates().get(0).getLatitude(), 0.0, 0.0);
        assertFalse(geo.getCoordinates().get(0).hasAltitude());
    }

    @Test
    public void toJson() {
        com.mapbox.services.commons.geojson.MultiPoint geo = com.mapbox.services.commons.geojson.MultiPoint.fromJson(BaseGeoJSON.SAMPLE_MULTIPOINT);
        compareJson(BaseGeoJSON.SAMPLE_MULTIPOINT, geo.toJson());
    }

}
