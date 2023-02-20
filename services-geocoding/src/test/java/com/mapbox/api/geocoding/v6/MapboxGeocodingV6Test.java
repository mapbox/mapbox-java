package com.mapbox.api.geocoding.v6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import com.mapbox.geojson.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

public class MapboxGeocodingV6Test extends TestUtils {

    private MockWebServer server;
    protected HttpUrl mockUrl;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                try {
                    final String response = loadJsonFixture("v6/forward_valid.json");
                    return new MockResponse().setBody(response);
                } catch (IOException ioException) {
                    throw new RuntimeException(ioException);
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

    @Test
    public void sanity() throws Exception {
        final MapboxGeocodingV6 geocoding = MapboxGeocodingV6.builder()
            .accessToken(ACCESS_TOKEN)
            .query("740 15th St NW, Washington, DC 20005")
            .baseUrl(mockUrl.toString())
            .build();

        assertNotNull(geocoding);

        final Response<V6Response> response = geocoding.executeCall();
        assertEquals(200, response.code());
    }

    @Test
    public void responseTest() throws Exception {
        final MapboxGeocodingV6 geocoding = MapboxGeocodingV6.builder()
            .accessToken(ACCESS_TOKEN)
            .query("740 15th St NW, Washington, DC 20005")
            .baseUrl(mockUrl.toString())
            .build();

        final V6Response body = geocoding.executeCall().body();
        assertNotNull(body);

        final List<V6Feature> features = body.features();
        assertEquals(5, features.size());

        final V6Feature feature = features.get(0);
        assertEquals(Point.fromLngLat(-77.03394, 38.899929), feature.geometry());

        final V6Properties properties = feature.properties();
        assertNotNull(properties);

        assertEquals("address.4268500394334256", properties.mapboxId());
        assertEquals("address", properties.featureType());
        assertEquals("740 15th Street Northwest", properties.name());
        assertEquals("Washington, District of Columbia 20005, United States", properties.placeFormatted());
        assertEquals("15th Street Northwest", properties.street());
        assertEquals("740", properties.addressNumber());

        final V6Coordinates coordinates = new AutoValue_V6Coordinates(-77.03394, 38.899929, "rooftop");
        assertEquals(coordinates, properties.coordinates());
        assertEquals(Point.fromLngLat(-77.03394, 38.899929), coordinates.point());

        final V6MatchCode matchCode = V6MatchCode.builder()
            .exactMatch(true)
            .houseNumber("matched")
            .street("matched")
            .postcode("matched")
            .place("matched")
            .region("matched")
            .locality("not_applicable")
            .country("inferred")
            .confidence("exact")
            .build();

        assertEquals(matchCode, properties.matchCode());
    }
}
