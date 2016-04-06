package com.mapbox.services.geojson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by antonio on 1/30/16.
 */
public class FeatureTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        com.mapbox.services.commons.geojson.Feature geo = com.mapbox.services.commons.geojson.Feature.fromJson(BaseGeoJSON.SAMPLE_FEATURE);
        assertEquals(geo.getType(), "Feature");
        assertEquals(geo.getGeometry().getType(), "Point");
        assertEquals(geo.getProperties().get("name").getAsString(), "Dinagat Islands");
    }

    @Test
    public void toJson() {
        com.mapbox.services.commons.geojson.Feature geo = com.mapbox.services.commons.geojson.Feature.fromJson(BaseGeoJSON.SAMPLE_FEATURE);
        compareJson(BaseGeoJSON.SAMPLE_FEATURE, geo.toJson());
    }

}
