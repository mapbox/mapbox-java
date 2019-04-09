package com.mapbox.api.isochrone;

import com.mapbox.core.TestUtils;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;

public class IsochroneTestUtils extends TestUtils {
    protected static final String ISOCHRONE_WITH_POLYGONS_VALID = "isochroneWithPolygons.json";
    protected static final String ISOCHRONE_NO_POLYGONS_VALID = "isochroneNoPolygons.json";

    private MockWebServer server;
    protected HttpUrl mockUrl;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                try {
                    String response = loadJsonFixture(ISOCHRONE_WITH_POLYGONS_VALID);
                    if (request.getPath().contains("polygons=false")) {
                        response = loadJsonFixture(ISOCHRONE_NO_POLYGONS_VALID);
                    }
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
}
