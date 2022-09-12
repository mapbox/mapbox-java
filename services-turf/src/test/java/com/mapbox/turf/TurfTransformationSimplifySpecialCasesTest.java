package com.mapbox.turf;

import com.mapbox.geojson.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TurfTransformationSimplifySpecialCasesTest {

    @Test
    public void emptyCoordinates() {
        List<Point> input = new ArrayList<>();
        List<Point> output = TurfTransformation.simplify(input, 0.01, true);
        assertEquals(0, output.size());
    }

    @Test
    public void oneCoordinate() {
        List<Point> input = Collections.singletonList(Point.fromLngLat(1.0, 2.0));
        List<Point> output = TurfTransformation.simplify(input, 0.01, false);
        assertEquals(input, output);
    }

    @Test
    public void twoCoordinates() {
        List<Point> input = Arrays.asList(
                Point.fromLngLat(1.0, 2.0),
                Point.fromLngLat(-56.12, 87.09)
        );
        List<Point> output = TurfTransformation.simplify(input, 0.01, true);
        assertEquals(input, output);
    }
}
