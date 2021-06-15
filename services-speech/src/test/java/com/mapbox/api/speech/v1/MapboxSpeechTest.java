package com.mapbox.api.speech.v1;

import com.mapbox.geojson.TestUtils;
import com.mapbox.core.exceptions.ServicesException;

import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Okio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.startsWith;

public class MapboxSpeechTest extends TestUtils {
  private MockWebServer mockWebServer;
  private HttpUrl mockUrl;

  @Before
  public void setUp() throws IOException {
    mockWebServer = new MockWebServer();

    mockWebServer.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        okio.Buffer buffer = new okio.Buffer();
        try {
          buffer.writeAll(Okio.source(new File("src/test/resources/test_response.mp3")));
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
        return new MockResponse().setBody(buffer);
      }
    });

    mockWebServer.start();
    mockUrl = mockWebServer.url("");

  }

  @After
  public void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  public void sanity() throws IOException {
    MapboxSpeech mapboxSpeech = MapboxSpeech.builder()
            .accessToken(ACCESS_TOKEN)
            .instruction("hello")
            .textType("text")
            .baseUrl(mockUrl.toString())
            .build();

    assertNotNull(mapboxSpeech);
    retrofit2.Response<ResponseBody> response = mapboxSpeech.executeCall();
    assertEquals(200, response.code());
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void build_doesThrowRequiredAccessTokenException() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage(startsWith("Missing required properties: accessToken"));
    MapboxSpeech.builder()
            .instruction("hello")
            .textType("text")
            .baseUrl(mockUrl.toString())
            .build();
  }

  @Test
  public void build_doesThrowRequiredInstructionTextException() throws ServicesException {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage(startsWith("Missing required properties: text"));
    MapboxSpeech.builder()
            .accessToken(ACCESS_TOKEN)
            .textType("text")
            .baseUrl(mockUrl.toString())
            .build();
  }

  @Test
  public void build_doesThrowEmptyInstructionTextException() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(startsWith("Non-null, non-empty instruction text is required."));
    MapboxSpeech.builder()
            .accessToken(ACCESS_TOKEN)
            .instruction("")
            .textType("text")
            .baseUrl(mockUrl.toString())
            .build();
  }
}
