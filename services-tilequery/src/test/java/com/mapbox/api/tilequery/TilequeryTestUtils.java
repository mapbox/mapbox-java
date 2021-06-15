package com.mapbox.api.tilequery;

import com.mapbox.geojson.TestUtils;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.hamcrest.core.StringStartsWith;
import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class TilequeryTestUtils extends TestUtils {
    protected static final String TILEQUERY_VALID = "tilequery.json";
    protected static final String TILEQUERY_ALL_PARAM_VALID = "tilequery-all-params.json";

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
                    String response = loadJsonFixture(TILEQUERY_VALID);
                    if (request.getPath().contains("limit")) {
                        response = loadJsonFixture(TILEQUERY_ALL_PARAM_VALID);
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
