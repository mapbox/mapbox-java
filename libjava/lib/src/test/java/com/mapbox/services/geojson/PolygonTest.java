package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by antonio on 1/30/16.
 */
public class PolygonTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        Polygon geo = Polygon.fromJson(BaseGeoJSON.SAMPLE_POLYGON);
        assertEquals(geo.getType(), "Polygon");
        assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, DELTA);
        assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, DELTA);
        assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
    }

    @Test
    public void fromJsonHoles() {
        Polygon geo = Polygon.fromJson(BaseGeoJSON.SAMPLE_POLYGON_HOLES);
        assertEquals(geo.getType(), "Polygon");
        assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, DELTA);
        assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, DELTA);
        assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
    }

    @Test
    public void toJson() {
        Polygon geo = Polygon.fromJson(BaseGeoJSON.SAMPLE_POLYGON);
        compareJson(BaseGeoJSON.SAMPLE_POLYGON, geo.toJson());
    }

    @Test
    public void toJsonHoles() {
        Polygon geo = Polygon.fromJson(BaseGeoJSON.SAMPLE_POLYGON_HOLES);
        compareJson(BaseGeoJSON.SAMPLE_POLYGON_HOLES, geo.toJson());
    }

}
