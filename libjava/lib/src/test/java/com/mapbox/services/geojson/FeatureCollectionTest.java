package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.FeatureCollection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by antonio on 1/30/16.
 */
public class FeatureCollectionTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        FeatureCollection geo = FeatureCollection.fromJson(BaseGeoJSON.SAMPLE_FEATURECOLLECTION);
        assertEquals(geo.getType(), "FeatureCollection");
        assertEquals(geo.getFeatures().size(), 3);
        assertEquals(geo.getFeatures().get(0).getType(), "Feature");
        assertEquals(geo.getFeatures().get(0).getGeometry().getType(), "Point");
        assertEquals(geo.getFeatures().get(1).getType(), "Feature");
        assertEquals(geo.getFeatures().get(1).getGeometry().getType(), "LineString");
        assertEquals(geo.getFeatures().get(2).getType(), "Feature");
        assertEquals(geo.getFeatures().get(2).getGeometry().getType(), "Polygon");
    }

    @Test
    public void toJson() {
        FeatureCollection geo = FeatureCollection.fromJson(BaseGeoJSON.SAMPLE_FEATURECOLLECTION);
        compareJson(BaseGeoJSON.SAMPLE_FEATURECOLLECTION, geo.toJson());
    }

}
