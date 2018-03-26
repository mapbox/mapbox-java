package com.mapbox.api.speech.v1;

import com.mapbox.core.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Okio;

import static org.junit.Assert.assertNotNull;

public class MapboxSpeechTest extends TestUtils {
  private MockWebServer mockWebServer;
  private HttpUrl httpUrl;

  @Before
  public void setUp() throws IOException {
    mockWebServer = new MockWebServer();

    mockWebServer.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        okio.Buffer buffer = new okio.Buffer();
        try {
          buffer.writeAll(Okio.source(new File("test_response.mp3")));
        } catch (IOException ioException) {
          throw new RuntimeException(ioException);
        }
        return new MockResponse().setBody(buffer);
      }
    });

    mockWebServer.start();
    httpUrl = mockWebServer.url("");

  }

  @After
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  public void sanity() {
    MapboxSpeech mapboxSpeech = MapboxSpeech.builder()
      .accessToken(ACCESS_TOKEN)
      .instruction("hello")
      .textType("text")
      .build();

    assertNotNull(mapboxSpeech);
  }
}
