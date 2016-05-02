package com.mapbox.services.geocoding.v5;

import com.google.gson.internal.LinkedTreeMap;
import com.mapbox.services.commons.ServicesException;
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
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapboxGeocodingTest {

    private final static double DELTA = 1E-10;

    private MockWebServer server;
    private HttpUrl mockUrl;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();

        byte[] content = Files.readAllBytes(Paths.get("src/test/fixtures/geocoding.json"));
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
                .setLocation("1600 pennsylvania ave nw")
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

        assertEquals(body.getQuery().get(0), "1600");
        assertEquals(body.getQuery().get(1), "pennsylvania");
        assertEquals(body.getQuery().get(2), "ave");
        assertEquals(body.getQuery().get(3), "nw");

        assertEquals(body.getFeatures().size(), 5);
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

        GeocodingFeature feature = response.body().getFeatures().get(0);
        assertEquals(feature.getId(), "address.7626097581139850");
        assertEquals(feature.getType(), "Feature");
        assertEquals(feature.getText(), "Pennsylvania Ave NW");
        assertEquals(feature.getPlaceName(), "1600 Pennsylvania Ave NW, Washington, District of Columbia 20006, United States");
        assertEquals(feature.getRelevance(), 0.99, DELTA);
        assertEquals(feature.getProperties(), new LinkedTreeMap());
        assertEquals(feature.asPosition().getLongitude(), -77.036543, DELTA);
        assertEquals(feature.asPosition().getLatitude(), 38.897702, DELTA);
        assertEquals(feature.getAddress(), "1600");
        assertEquals(feature.getContext().size(), 5);
    }

    @Test
    public void testGeometry() throws ServicesException, IOException {
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken("pk.XXX")
                .setLocation("1600 pennsylvania ave nw")
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<GeocodingResponse> response = client.executeCall();

        FeatureGeometry geometry = response.body().getFeatures().get(3).getGeometry();
        assertEquals(geometry.getType(), "Point");
        assertEquals(geometry.getCoordinates().get(0), -75.563366, DELTA);
        assertEquals(geometry.getCoordinates().get(1), 39.75431, DELTA);
        assertEquals(geometry.isInterpolated(), true);
    }

    @Test
    public void testContext() throws ServicesException, IOException {
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken("pk.XXX")
                .setLocation("1600 pennsylvania ave nw")
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<GeocodingResponse> response = client.executeCall();

        List<FeatureContext> contexts = response.body().getFeatures().get(0).getContext();
        assertEquals(contexts.get(4).getId(), "country.12862386939497690");
        assertEquals(contexts.get(4).getText(), "United States");
        assertEquals(contexts.get(4).getShortCode(), "us");
    }

    @Test
    public void testObservable() throws ServicesException, IOException {
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken("pk.XXX")
                .setLocation("1600 pennsylvania ave nw")
                .build();
        client.setBaseUrl(mockUrl.toString());
        Observable<GeocodingResponse> geocodingResponseObservable = client.getObservable();
        assertNotNull(geocodingResponseObservable);

        TestSubscriber<GeocodingResponse> testSubscriber = new TestSubscriber<>();
        geocodingResponseObservable.subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        List<GeocodingResponse> responses = testSubscriber.getOnNextEvents();
        assertNotNull(responses);
        assertTrue("Geocoding Response Returned", responses.size() > 0);
        List<FeatureContext> contexts = responses.get(0).getFeatures().get(0).getContext();
        assertEquals(contexts.get(4).getId(), "country.12862386939497690");
        assertEquals(contexts.get(4).getText(), "United States");
        assertEquals(contexts.get(4).getShortCode(), "us");
    }
}
