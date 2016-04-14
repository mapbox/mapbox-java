package com.mapbox.services.geocoding.v5;

import com.google.gson.internal.LinkedTreeMap;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.geocoding.v5.models.FeatureContext;
import com.mapbox.services.geocoding.v5.models.FeatureGeometry;
import com.mapbox.services.geocoding.v5.models.GeocodingFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by antonio on 4/14/16.
 */
public class MapboxGeocodingReverseTest {

    private final static double DELTA = 1E-10;

    private MockWebServer server;
    private HttpUrl mockUrl;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();

        byte[] content = Files.readAllBytes(Paths.get("src/test/fixtures/geocoding_reverse.json"));
        String body = new String(content, StandardCharsets.UTF_8);
        server.enqueue(new MockResponse().setBody(body));

        server.start();

        mockUrl = server.url("");
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSanity() throws ServicesException, IOException {
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken("pk.XXX")
                .setCoordinates(Position.fromCoordinates(-77.0366, 38.8971))
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<GeocodingResponse> response = client.executeCall();
        assertEquals(response.code(), 200);
    }

    @Test
    public void testBody() throws ServicesException, IOException {
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken("pk.XXX")
                .setLocation("1600 pennsylvania ave nw")
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<GeocodingResponse> response = client.executeCall();

        GeocodingResponse body = response.body();
        assertEquals(body.getType(), "FeatureCollection");

        assertEquals(body.getQuery().get(0), String.valueOf(-77.0366));
        assertEquals(body.getQuery().get(1), String.valueOf(38.8971));

        assertEquals(body.getFeatures().size(), 6);
        assertTrue(body.getAttribution().startsWith("NOTICE"));
    }

    @Test
    public void testFeature() throws ServicesException, IOException {
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken("pk.XXX")
                .setLocation("1600 pennsylvania ave nw")
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<GeocodingResponse> response = client.executeCall();

        GeocodingFeature feature = response.body().getFeatures().get(1);
        assertEquals(feature.getId(), "neighborhood.7130293780113160");
        assertEquals(feature.getType(), "Feature");
        assertEquals(feature.getText(), "Franklin Mcpherson Square");
        assertEquals(feature.getPlaceName(), "Franklin Mcpherson Square, Washington, 20006, District of Columbia, United States");
        assertEquals(feature.getRelevance(), 1, DELTA);
        assertEquals(feature.getProperties(), new LinkedTreeMap());
        assertEquals(feature.getBbox().get(0), -77.0375061034999, DELTA);
        assertEquals(feature.getBbox().get(1), 38.8942394196779, DELTA);
        assertEquals(feature.getBbox().get(2), -77.0302262036302, DELTA);
        assertEquals(feature.getBbox().get(3), 38.9038583397001, DELTA);
        assertEquals(feature.asPosition().getLongitude(), -77.033, DELTA);
        assertEquals(feature.asPosition().getLatitude(), 38.9015, DELTA);
        assertEquals(feature.getContext().size(), 4);
    }

    @Test
    public void testGeometry() throws ServicesException, IOException {
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken("pk.XXX")
                .setLocation("1600 pennsylvania ave nw")
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<GeocodingResponse> response = client.executeCall();

        FeatureGeometry geometry = response.body().getFeatures().get(1).getGeometry();
        assertEquals(geometry.getType(), "Point");
        assertEquals(geometry.getCoordinates().get(0), -77.033, DELTA);
        assertEquals(geometry.getCoordinates().get(1), 38.9015, DELTA);
    }

    @Test
    public void testContext() throws ServicesException, IOException {
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken("pk.XXX")
                .setLocation("1600 pennsylvania ave nw")
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<GeocodingResponse> response = client.executeCall();

        List<FeatureContext> contexts = response.body().getFeatures().get(1).getContext();
        assertEquals(contexts.get(3).getId(), "country.12862386939497690");
        assertEquals(contexts.get(3).getText(), "United States");
        assertEquals(contexts.get(3).getShortCode(), "us");
    }

}
