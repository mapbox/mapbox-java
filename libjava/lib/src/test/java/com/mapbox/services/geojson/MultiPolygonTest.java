package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.MultiPolygon;
import com.mapbox.services.commons.models.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void checksEqualityFromCoordinates() {
        MultiPolygon aMultiPolygon = MultiPolygon.fromCoordinates(new double[][][][]{
                {{{102.0, 2.0}, {103.0, 2.0}, {103.0, 3.0}, {102.0, 3.0}, {102.0, 2.0}}},
                {{{100.0, 0.0}, {101.0, 0.0}, {101.0, 1.0}, {100.0, 1.0}, {100.0, 0.0}},
                        {{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}}
        });

        String multiPolygonCoordinates = obtainLiteralCoordinatesFrom(aMultiPolygon);

        assertEquals("Polygons: \n" +
                "Polygon: \n" +
                "Lines: \n" +
                "Position [longitude=102.0, latitude=2.0, altitude=NaN]\n" +
                "Position [longitude=103.0, latitude=2.0, altitude=NaN]\n" +
                "Position [longitude=103.0, latitude=3.0, altitude=NaN]\n" +
                "Position [longitude=102.0, latitude=3.0, altitude=NaN]\n" +
                "Position [longitude=102.0, latitude=2.0, altitude=NaN]\n" +
                "Polygon: \n" +
                "Lines: \n" +
                "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n" +
                "Position [longitude=101.0, latitude=0.0, altitude=NaN]\n" +
                "Position [longitude=101.0, latitude=1.0, altitude=NaN]\n" +
                "Position [longitude=100.0, latitude=1.0, altitude=NaN]\n" +
                "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n" +
                "Lines: \n" +
                "Position [longitude=100.2, latitude=0.2, altitude=NaN]\n" +
                "Position [longitude=100.8, latitude=0.2, altitude=NaN]\n" +
                "Position [longitude=100.8, latitude=0.8, altitude=NaN]\n" +
                "Position [longitude=100.2, latitude=0.8, altitude=NaN]\n" +
                "Position [longitude=100.2, latitude=0.2, altitude=NaN]\n", multiPolygonCoordinates);
    }

    @Test
    public void checksJsonEqualityFromCoordinates() {
        MultiPolygon aMultiPolygon = MultiPolygon.fromCoordinates(new double[][][][]{
                {{{102.0, 2.0}, {103.0, 2.0}, {103.0, 3.0}, {102.0, 3.0}, {102.0, 2.0}}},
                {{{100.0, 0.0}, {101.0, 0.0}, {101.0, 1.0}, {100.0, 1.0}, {100.0, 0.0}},
                        {{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}}
        });

        String multiPolygonJsonCoordinates = aMultiPolygon.toJson();

        compareJson("{ \"type\": \"MultiPolygon\",\n" +
                "\"coordinates\": [\n" +
                "[[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],\n" +
                "[[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],\n" +
                "[[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]\n" +
                "]\n" +
                "}", multiPolygonJsonCoordinates);
    }

    private String obtainLiteralCoordinatesFrom(MultiPolygon multiPolygon) {
        List<List<List<Position>>> multiPolygonCoordinates = multiPolygon.getCoordinates();
        StringBuilder literalCoordinates = new StringBuilder();
        literalCoordinates.append("Polygons: \n");
        for (List<List<Position>> polygon : multiPolygonCoordinates) {
            literalCoordinates.append("Polygon: \n");
            for (List<Position> lines : polygon) {
                literalCoordinates.append("Lines: \n");
                for (Position point : lines) {
                    literalCoordinates.append(point.toString());
                    literalCoordinates.append("\n");
                }
            }
        }
        return literalCoordinates.toString();
    }

}
