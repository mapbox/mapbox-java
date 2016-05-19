package com.mapbox.services.mapmatching.v4;

import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.directions.v4.DirectionsCriteria;
import com.mapbox.services.mapmatching.v4.models.MapMatchingResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;
import rx.observers.TestSubscriber;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Test the Mapbox Map Matching API
 */
public class MapboxMapMatchingTest {
    public static final String POLYLINE_FIXTURE = "src/test/fixtures/mapmatching_v5_polyline.json";
    public static final String NO_GEOMETRY_FIXTURE = "src/test/fixtures/mapmatching_v5_no_geometry.json";

    private static final String ACCESS_TOKEN = "pk.XXX";

    private MockWebServer server;
    private HttpUrl mockUrl;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();

        server.setDispatcher(new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                //Switch response on geometry parameter (only false supported, so nice and simple)
                String resource = POLYLINE_FIXTURE;
                if (request.getPath().contains("geometry=false")) {
                    resource = NO_GEOMETRY_FIXTURE;
                }

                try {
                    String body = new String(Files.readAllBytes(Paths.get(resource)), Charset.forName("utf-8"));
                    return new MockResponse().setBody(body);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.start();
        mockUrl = server.url("");
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    /**
     * Test the most basic request (default response format)
     */
    @Test
    public void testCallSanity() throws ServicesException, IOException {
        MapboxMapMatching client = new MapboxMapMatching.Builder()
                .setAccessToken(ACCESS_TOKEN)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<MapMatchingResponse> response = client.executeCall();
        assertEquals(response.code(), 200);

        //Check the response body
        assertNotNull(response.body());
        assertEquals(1, response.body().getFeatures().size());
        assertNotNull(response.body().getFeatures().get(0).getGeometry());
    }

    /**
     * Test the most basic request but now with RX
     */
    @Test
    public void testSanityRX() throws ServicesException, IOException {
        MapboxMapMatching client = new MapboxMapMatching.Builder()
                .setAccessToken(ACCESS_TOKEN)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .build();
        client.setBaseUrl(mockUrl.toString());

        TestSubscriber<MapMatchingResponse> testSubscriber = new TestSubscriber<>();
        client.getObservable().subscribe(testSubscriber);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);

        List<MapMatchingResponse> events = testSubscriber.getOnNextEvents();
        assertEquals(1, events.size());


        MapMatchingResponse response = events.get(0);
        assertEquals(response.getCode(), DirectionsCriteria.RESPONSE_OK);
    }

    /**
     * Test a basic request with polyline response
     */
    @Test
    public void testNoGeometryResponse() throws ServicesException, IOException {
        MapboxMapMatching client = new MapboxMapMatching.Builder()
                .setAccessToken(ACCESS_TOKEN)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .setNoGeometry()
                .build();
        client.setBaseUrl(mockUrl.toString());
        Response<MapMatchingResponse> response = client.executeCall();
        assertEquals(response.code(), 200);

        //Check the response body
        assertNotNull(response.body());
        assertEquals(1, response.body().getFeatures().size());
        assertNull(response.body().getFeatures().get(0).getGeometry());
    }

    @Test
    public void requiredAccessToken() throws ServicesException {
        thrown.expect(ServicesException.class);
        thrown.expectMessage(startsWith("Using Mapbox Services requires setting a valid access token"));
        new MapboxMapMatching.Builder().build();
    }

    @Test
    public void validGpsPrecisionLowerBounds() throws ServicesException {
        thrown.expect(ServicesException.class);
        thrown.expectMessage(startsWith("Using Mapbox Map Matching requires setting a valid gps precision"));
        new MapboxMapMatching.Builder()
                .setAccessToken(ACCESS_TOKEN)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .setGpsPrecison(0)
                .build();
    }

    @Test
    public void validGpsPrecisionUpperBounds() throws ServicesException {
        thrown.expect(ServicesException.class);
        thrown.expectMessage(startsWith("Using Mapbox Map Matching requires setting a valid gps precision"));
        new MapboxMapMatching.Builder()
                .setAccessToken(ACCESS_TOKEN)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .setGpsPrecison(11)
                .build();
    }

    @Test
    public void validProfileNonNull() throws ServicesException {
        thrown.expect(ServicesException.class);
        thrown.expectMessage(startsWith("Using Mapbox Map Matching requires setting a valid profile"));
        new MapboxMapMatching.Builder()
                .setAccessToken(ACCESS_TOKEN)
                .setProfile(null)
                .build();
    }

    @Test
    public void validProfileCorrectString() throws ServicesException {
        thrown.expect(ServicesException.class);
        thrown.expectMessage(startsWith("Using Mapbox Map Matching requires setting a valid profile"));
        new MapboxMapMatching.Builder()
                .setAccessToken(ACCESS_TOKEN)
                .setProfile("my_own_profile")
                .build();
    }
}
