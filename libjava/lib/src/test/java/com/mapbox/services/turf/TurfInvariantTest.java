package com.mapbox.services.turf;

import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfInvariant;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.startsWith;

/**
 * Created by antonio on 7/14/16.
 */
public class TurfInvariantTest extends BaseTurf {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testInvariantGeojsonType1() throws TurfException {
        thrown.expect(TurfException.class);
        thrown.expectMessage(startsWith("Type and name required"));
        TurfInvariant.geojsonType(null, null, null);
    }

    @Test
    public void testInvariantGeojsonType2() throws TurfException {
        thrown.expect(TurfException.class);
        thrown.expectMessage(startsWith("Type and name required"));
        TurfInvariant.geojsonType(null, null, "myfn");
    }

    @Test
    public void testInvariantGeojsonType3() throws TurfException {
        String json = "{ type: 'Point', coordinates: [0, 0] }";
        thrown.expect(TurfException.class);
        thrown.expectMessage(startsWith("Invalid input to myfn: must be a Polygon, given Point"));
        TurfInvariant.geojsonType(Point.fromJson(json), "Polygon", "myfn");
    }

    @Test
    public void testInvariantGeojsonType4() throws TurfException {
        String json = "{ type: 'Point', coordinates: [0, 0] }";
        TurfInvariant.geojsonType(Point.fromJson(json), "Point", "myfn");
    }
}
