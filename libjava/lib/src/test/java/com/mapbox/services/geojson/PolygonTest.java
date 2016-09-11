package com.mapbox.services.geojson;

import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void checksEqualityFromCoordinates() {
        Polygon aPolygon = Polygon.fromCoordinates(new double[][][]{
                {{100.0, 0.0}, {101.0, 0.0}, {101.0, 1.0}, {100.0, 1.0}, {100.0, 0.0}},
                {{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}
        });

        String polygonCoordinates = obtainLiteralCoordinatesFrom(aPolygon);

        assertEquals("Polygon: \n" +
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
                "Position [longitude=100.2, latitude=0.2, altitude=NaN]\n", polygonCoordinates);
    }

    @Test
    public void checksJsonEqualityFromCoordinates() {
        Polygon aPolygon = Polygon.fromCoordinates(new double[][][]{
                {{100.0, 0.0}, {101.0, 0.0}, {101.0, 1.0}, {100.0, 1.0}, {100.0, 0.0}},
                {{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}
        });

        String polygonJsonCoordinates = aPolygon.toJson();

        compareJson("{ \"type\": \"Polygon\",\n" +
                "\"coordinates\": [\n" +
                "[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ],\n" +
                "[ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]\n" +
                "]\n" +
                "}", polygonJsonCoordinates);
    }

    private String obtainLiteralCoordinatesFrom(Polygon polygon) {
        List<List<Position>> polygonCoordinates = polygon.getCoordinates();
        StringBuilder literalCoordinates = new StringBuilder();
        literalCoordinates.append("Polygon: \n");
        for (List<Position> lines : polygonCoordinates) {
            literalCoordinates.append("Lines: \n");
            for (Position point : lines) {
                literalCoordinates.append(point.toString());
                literalCoordinates.append("\n");
            }
        }
        return literalCoordinates.toString();
    }

}
