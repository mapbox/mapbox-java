package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClosureTest extends TestUtils {

    @Test
    public void sanity(){
        assertNotNull(getDefault());
    }

    private Closure getDefault() {
        return Closure.builder()
                .geometryIndexStart(3)
                .geometryIndexEnd(5)
                .build();
    }

    @Test
    public void testSerializableObject() throws Exception {
        Closure closure = Closure.builder()
                .geometryIndexStart(3)
                .geometryIndexEnd(5)
                .build();
        byte[] serialized = TestUtils.serialize(closure);
        assertEquals(closure, deserialize(serialized, Closure.class));
    }

    @Test
    public void testSerializableFromJson(){
        String json = "{\"geometry_index_start\":3,\"geometry_index_end\":5}";

        Closure fromJson = Closure.fromJson(json);

        assertNotNull(fromJson);
        assertEquals(3L, fromJson.geometryIndexStart().longValue());
        assertEquals(5L, fromJson.geometryIndexEnd().longValue());
    }

    @Test
    public void testFullRoute() throws Exception {
        String json = loadJsonFixture("directions_v5-with-closure_precision_6.json");
        DirectionsRoute response = DirectionsRoute.fromJson(json);

        assertEquals(13L, response.legs().get(0).closures().get(0).geometryIndexStart().longValue());
        assertEquals(21L, response.legs().get(0).closures().get(0).geometryIndexEnd().longValue());
    }
}