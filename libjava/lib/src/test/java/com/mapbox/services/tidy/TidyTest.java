package com.mapbox.services.tidy;

import com.google.gson.JsonArray;
import com.mapbox.services.BaseTest;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.tidy.Tidy;
import com.mapbox.services.commons.turf.TurfException;

import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by antonio on 7/26/16.
 */
public class TidyTest extends BaseTest {

    @Test
    public void testDates() throws IOException, ParseException {
        FeatureCollection walk1 = FeatureCollection.fromJson(loadJsonFixture("tidy", "walk-1.json"));
        JsonArray timeStamp = walk1.getFeatures().get(0).getProperties().getAsJsonArray(Tidy.KEY_COORD_TIMES);
        assertEquals(timeStamp.size(), 423);

        String sampleString = timeStamp.get(0).getAsString();
        assertEquals(sampleString, "2015-06-05T01:07:54Z");

        Tidy tidy = new Tidy();
        Calendar calendar = Calendar.getInstance();
        Date date = tidy.getDateFormat().parse(sampleString);
        calendar.setTime(date);
        assertEquals(calendar.get(Calendar.YEAR), 2015);
        assertEquals(calendar.get(Calendar.MONTH), Calendar.JUNE);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 5);
        assertEquals(calendar.get(Calendar.HOUR), 1);
        assertEquals(calendar.get(Calendar.MINUTE), 7);
        assertEquals(calendar.get(Calendar.SECOND), 54);
    }

    @Test
    public void testWithoutTimestamps() throws IOException, TurfException, ServicesException {
        // Process a feature collection without timestamps
        FeatureCollection walk2 = FeatureCollection.fromJson(loadJsonFixture("tidy", "walk-2.json"));

        Tidy tidy = new Tidy();
        FeatureCollection result = tidy.execute(walk2);
        compareJson(loadJsonFixture("tidy", "walk-2-tidy.json"), result.toJson());
    }

    @Test
    public void testWithTimestamps() throws IOException, TurfException, ServicesException {
        // Process a feature collection with timestamps
        FeatureCollection walk1 = FeatureCollection.fromJson(loadJsonFixture("tidy", "walk-1.json"));

        Tidy tidy = new Tidy();
        FeatureCollection result = tidy.execute(walk1);
        compareJson(loadJsonFixture("tidy", "walk-1-tidy.json"), result.toJson());
    }

    @Test
    public void testWithOptions() throws IOException, TurfException, ServicesException {
        // Process a feature collection with custom minimumDistance minimumTime and maximumPoints
        FeatureCollection walk1 = FeatureCollection.fromJson(loadJsonFixture("tidy", "walk-1.json"));

        Tidy tidy = new Tidy();
        tidy.setMinimumDistance(20);
        tidy.setMinimumTime(7000);
        tidy.setMaximumPoints(10);
        FeatureCollection result = tidy.execute(walk1);
        compareJson(loadJsonFixture("tidy", "walk-1-resampled.json"), result.toJson());
    }

    @Test
    public void testWithMultiple() throws IOException, TurfException, ServicesException {
        // Process a feature collection with multiple features
        FeatureCollection crossCountry = FeatureCollection.fromJson(loadJsonFixture("tidy", "cross-country.json"));

        Tidy tidy = new Tidy();
        FeatureCollection result = tidy.execute(crossCountry);
        compareJson(loadJsonFixture("tidy", "cross-country-tidy.json"), result.toJson());
    }
}
