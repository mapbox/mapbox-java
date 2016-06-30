package com.mapbox.services.geojson;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import static org.junit.Assert.assertEquals;

/**
 * Created by antonio on 1/30/16.
 */
public class BaseGeoJSON {

    /*
     * These all samples come from the official spec:
     * http://geojson.org
     */

    public static final String SAMPLE_POINT = "{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }";

    public static final String SAMPLE_MULTIPOINT = "{ \"type\": \"MultiPoint\",\n" +
            "    \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]\n" +
            "    }";

    public static final String SAMPLE_LINESTRING = "{ \"type\": \"LineString\",\n" +
            "    \"coordinates\": [ [100.0, 0.0], [101.0, 1.0] ]\n" +
            "    }";

    public static final String SAMPLE_MULTILINESTRING = "{ \"type\": \"MultiLineString\",\n" +
            "    \"coordinates\": [\n" +
            "        [ [100.0, 0.0], [101.0, 1.0] ],\n" +
            "        [ [102.0, 2.0], [103.0, 3.0] ]\n" +
            "      ]\n" +
            "    }";

    public static final String SAMPLE_POLYGON = "{ \"type\": \"Polygon\",\n" +
            "    \"coordinates\": [\n" +
            "      [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]\n" +
            "      ]\n" +
            "   }";

    public static final String SAMPLE_POLYGON_HOLES = "{ \"type\": \"Polygon\",\n" +
            "    \"coordinates\": [\n" +
            "      [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ],\n" +
            "      [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]\n" +
            "      ]\n" +
            "   }";

    public static final String SAMPLE_MULTIPOLYGON = "{ \"type\": \"MultiPolygon\",\n" +
            "    \"coordinates\": [\n" +
            "      [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],\n" +
            "      [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],\n" +
            "       [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]\n" +
            "      ]\n" +
            "    }";

    public static final String SAMPLE_GEOMETRYCOLLECTION = "{ \"type\": \"GeometryCollection\",\n" +
            "    \"geometries\": [\n" +
            "      { \"type\": \"Point\",\n" +
            "        \"coordinates\": [100.0, 0.0]\n" +
            "        },\n" +
            "      { \"type\": \"LineString\",\n" +
            "        \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]\n" +
            "        }\n" +
            "    ]\n" +
            "  }";

    public static final String SAMPLE_FEATURE = "{\n" +
            "  \"type\": \"Feature\",\n" +
            "  \"geometry\": {\n" +
            "    \"type\": \"Point\",\n" +
            "    \"coordinates\": [125.6, 10.1]\n" +
            "  },\n" +
            "  \"properties\": {\n" +
            "    \"name\": \"Dinagat Islands\"\n" +
            "  }\n" +
            "}";

    public static final String SAMPLE_FEATURECOLLECTION = "{ \"type\": \"FeatureCollection\",\n" +
            "    \"features\": [\n" +
            "      { \"type\": \"Feature\",\n" +
            "        \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]},\n" +
            "        \"properties\": {\"prop0\": \"value0\"}\n" +
            "        },\n" +
            "      { \"type\": \"Feature\",\n" +
            "        \"geometry\": {\n" +
            "          \"type\": \"LineString\",\n" +
            "          \"coordinates\": [\n" +
            "            [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]\n" +
            "            ]\n" +
            "          },\n" +
            "        \"properties\": {\n" +
            "          \"prop0\": \"value0\",\n" +
            "          \"prop1\": 0.0\n" +
            "          }\n" +
            "        },\n" +
            "      { \"type\": \"Feature\",\n" +
            "         \"geometry\": {\n" +
            "           \"type\": \"Polygon\",\n" +
            "           \"coordinates\": [\n" +
            "             [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0],\n" +
            "               [100.0, 1.0], [100.0, 0.0] ]\n" +
            "             ]\n" +
            "         },\n" +
            "         \"properties\": {\n" +
            "           \"prop0\": \"value0\",\n" +
            "           \"prop1\": {\"this\": \"that\"}\n" +
            "           }\n" +
            "         }\n" +
            "       ]\n" +
            "     }";

    /*
     * Utils
     */

    void compareJson(String json1, String json2) {
        JsonParser parser = new JsonParser();
        assertEquals(parser.parse(json1), parser.parse(json2));
    }
}
