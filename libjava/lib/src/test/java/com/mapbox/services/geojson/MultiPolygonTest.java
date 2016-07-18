package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.MultiPolygon;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by antonio on 1/30/16.
 */
public class MultiPolygonTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        MultiPolygon geo = MultiPolygon.fromJson(BaseGeoJSON.SAMPLE_MULTIPOLYGON);
        assertEquals(geo.getType(), "MultiPolygon");
        assertEquals(geo.getCoordinates().get(0).get(0).get(0).getLongitude(), 102.0, 0.0);
        assertEquals(geo.getCoordinates().get(0).get(0).get(0).getLatitude(), 2.0, 0.0);
        assertFalse(geo.getCoordinates().get(0).get(0).get(0).hasAltitude());
    }

    @Test
    public void toJson() {
        MultiPolygon geo = MultiPolygon.fromJson(BaseGeoJSON.SAMPLE_MULTIPOLYGON);
        compareJson(BaseGeoJSON.SAMPLE_MULTIPOLYGON, geo.toJson());
    }

}
