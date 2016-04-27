package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.GeometryCollection;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by antonio on 1/30/16.
 */
public class GeometryCollectionTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        GeometryCollection geo = GeometryCollection.fromJson(BaseGeoJSON.SAMPLE_GEOMETRYCOLLECTION);
        assertEquals(geo.getType(), "GeometryCollection");
        assertEquals(geo.getGeometries().get(0).getType(), "Point");
        assertEquals(geo.getGeometries().get(1).getType(), "LineString");
    }

    @Test
    public void toJson() {
        GeometryCollection geo = GeometryCollection.fromJson(BaseGeoJSON.SAMPLE_GEOMETRYCOLLECTION);
        compareJson(BaseGeoJSON.SAMPLE_GEOMETRYCOLLECTION, geo.toJson());
    }

}
