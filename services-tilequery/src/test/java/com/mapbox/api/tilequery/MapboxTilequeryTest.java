package com.mapbox.api.tilequery;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import org.junit.Test;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapboxTilequeryTest extends TilequeryTestUtils {

    @Test
    public void sanity() throws Exception {
        MapboxTilequery mapboxGeocoding = MapboxTilequery.builder()
                .accessToken(ACCESS_TOKEN)
                .mapIds("mapbox.mapbox-streets-v7")
                .query(Point.fromLngLat(1.2345, 6.7890))
                .baseUrl(mockUrl.toString())
                .build();
        assertNotNull(mapboxGeocoding);
        Response<FeatureCollection> response = mapboxGeocoding.executeCall();
        assertEquals(200, response.code());
    }
}
