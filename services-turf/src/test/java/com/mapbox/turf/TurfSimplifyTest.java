package com.mapbox.turf;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.geojson.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TurfSimplifyTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return new TestUtils().getResourceFolderFileNames("turf-simplify/input")
                .stream()
                .map(s -> new Object[] { s })
                .collect(Collectors.toList());
    }

    private final String fileName;
    private final TestUtils testUtils = new TestUtils();

    public TurfSimplifyTest(String fileName) {
        this.fileName = fileName;
    }

    @Test
    public void simplify() {
        String inputContent = testUtils.readFile("src/test/resources/turf-simplify/input/" + fileName);
        JsonObject json = JsonParser.parseString(inputContent).getAsJsonObject();
        JsonObject properties = json.getAsJsonObject("properties");
        double tolerance = properties.get("tolerance").getAsDouble();
        boolean highQuality = properties.get("highQuality").getAsBoolean();
        List<Point> input = parseCoordinates(json);
        String expectedContent = testUtils.readFile("src/test/resources/turf-simplify/output/" + fileName);
        List<Point> expected = parseCoordinates(JsonParser.parseString(expectedContent).getAsJsonObject());

        List<Point> output = TurfSimplify.simplify(input, tolerance, highQuality);

        checkPointsEquality(output, expected, 0.00001);
    }

    private List<Point> parseCoordinates(JsonObject json) {
        List<Point> points = new ArrayList<>();
        JsonArray jsonArray = json.getAsJsonArray("coordinates");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonArray coord = jsonArray.get(i).getAsJsonArray();
            double longitude = coord.get(0).getAsDouble();
            double latitude = coord.get(1).getAsDouble();
            points.add(Point.fromLngLat(longitude, latitude));
        }
        return points;
    }

    private void checkPointsEquality(List<Point> actual, List<Point> expected, double tolerance) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).latitude(), actual.get(i).latitude(), tolerance);
            assertEquals(expected.get(i).longitude(), actual.get(i).longitude(), tolerance);
        }
    }
}
