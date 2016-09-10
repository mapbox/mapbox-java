package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.MultiLineString;
import com.mapbox.services.commons.models.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MultiLineStringTest extends BaseGeoJSON {

    @Test
    public void fromJson() {
        com.mapbox.services.commons.geojson.MultiLineString geo = com.mapbox.services.commons.geojson.MultiLineString.fromJson(BaseGeoJSON.SAMPLE_MULTILINESTRING);
        assertEquals(geo.getType(), "MultiLineString");
        assertEquals(geo.getCoordinates().get(0).get(0).getLongitude(), 100.0, 0.0);
        assertEquals(geo.getCoordinates().get(0).get(0).getLatitude(), 0.0, 0.0);
        assertFalse(geo.getCoordinates().get(0).get(0).hasAltitude());
    }

    @Test
    public void toJson() {
        com.mapbox.services.commons.geojson.MultiLineString geo = com.mapbox.services.commons.geojson.MultiLineString.fromJson(BaseGeoJSON.SAMPLE_MULTILINESTRING);
        compareJson(BaseGeoJSON.SAMPLE_MULTILINESTRING, geo.toJson());
    }

    @Test
    public void checksEqualityFromCoordinates() {
        MultiLineString aMultiLine = MultiLineString.fromCoordinates(new double[][][]{
                {{100.0, 0.0}, {101.0, 1.0}},
                {{102.0, 2.0}, {103.0, 3.0}}
        });

        String multiLineCoordinates = obtainLiteralCoordinatesFrom(aMultiLine);

        assertEquals("Lines: \n" +
                        "Line: \n" +
                        "Position [longitude=100.0, latitude=0.0, altitude=NaN]\n" +
                        "Position [longitude=101.0, latitude=1.0, altitude=NaN]\n" +
                        "Line: \n" +
                        "Position [longitude=102.0, latitude=2.0, altitude=NaN]\n" +
                        "Position [longitude=103.0, latitude=3.0, altitude=NaN]\n",
                multiLineCoordinates);
    }

    @Test
    public void checksJsonEqualityFromCoordinates() {
        MultiLineString aMultiLine = MultiLineString.fromCoordinates(new double[][][]{
                {{100.0, 0.0}, {101.0, 1.0}},
                {{102.0, 2.0}, {103.0, 3.0}}
        });

        String multiLineJsonCoordinates = aMultiLine.toJson();

        compareJson("{ \"type\": \"MultiLineString\",\n" +
                "\"coordinates\": [\n" +
                "[ [100.0, 0.0], [101.0, 1.0] ],\n" +
                "[ [102.0, 2.0], [103.0, 3.0] ]\n" +
                "]\n" +
                "}", multiLineJsonCoordinates);
    }

    private String obtainLiteralCoordinatesFrom(MultiLineString multiLine) {
        List<List<Position>> multiLineCoordinates = multiLine.getCoordinates();
        StringBuilder literalCoordinates = new StringBuilder();
        literalCoordinates.append("Lines: \n");
        for (List<Position> line : multiLineCoordinates) {
            literalCoordinates.append("Line: \n");
            for (Position point : line) {
                literalCoordinates.append(point.toString());
                literalCoordinates.append("\n");
            }
        }
        return literalCoordinates.toString();
    }

}
