package com.mapbox.services.geojson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by antonio on 1/30/16.
 */
public class PolygonTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        com.mapbox.services.commons.geojson.Polygon geo = com.mapbox.services.commons.geojson.Polygon.fromJson(BaseGeoJSON.SAMPLE_POLYGON);
        assertEquals(geo.getType(), "Polygon");
        assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, 0.0);
        assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, 0.0);
        assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
    }

    @Test
    public void fromJsonHoles() {
        com.mapbox.services.commons.geojson.Polygon geo = com.mapbox.services.commons.geojson.Polygon.fromJson(BaseGeoJSON.SAMPLE_POLYGON_HOLES);
        assertEquals(geo.getType(), "Polygon");
        assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, 0.0);
        assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, 0.0);
        assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
    }

    @Test
    public void toJson() {
        com.mapbox.services.commons.geojson.Polygon geo = com.mapbox.services.commons.geojson.Polygon.fromJson(BaseGeoJSON.SAMPLE_POLYGON);
        compareJson(BaseGeoJSON.SAMPLE_POLYGON, geo.toJson());
    }

    @Test
    public void toJsonHoles() {
        com.mapbox.services.commons.geojson.Polygon geo = com.mapbox.services.commons.geojson.Polygon.fromJson(BaseGeoJSON.SAMPLE_POLYGON_HOLES);
        compareJson(BaseGeoJSON.SAMPLE_POLYGON_HOLES, geo.toJson());
    }

}
