package com.mapbox.api.speech.v1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the speech service.
 *
 * @since 3.0.0
 */
public interface SpeechService {

  /**
   * Constructs the html call using the information passed in through the
   * {@link MapboxSpeech.Builder}.
   *
   * @param text            text to dictate
   * @param textType        text type, either "text" or "ssml" (default is "text")
   * @param language        language locale, default is "en-us"
   * @param outputFormat    output format, either "mp3" or "json", default is "mp3"
   * @param accessToken     Mapbox access token
   * @return the MapboxSpeech response in a Call wrapper
   * @since 3.0.0
   */
  @GET("/voice/v1/speak/{text}")
  Call<ResponseBody> getCall(
          @Path("text") String text,
          @Query("textType") String textType,
          @Query("language") String language,
          @Query("outputFormat") String outputFormat,
          @Query("access_token") String accessToken);
}
