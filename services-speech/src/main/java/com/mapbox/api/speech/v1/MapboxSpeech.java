package com.mapbox.api.speech.v1;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.TextUtils;

import java.util.logging.Logger;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;

/**
 * The Speech API is a text-to-speech APi with a server-side caching layer in front of AWS Polly.
 * The only requirements are text to dictate, and a Mapbox access token. For 3-step-ahead
 * client-side caching, cache directory is required.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class MapboxSpeech extends MapboxService<ResponseBody, SpeechService> {
  private static final Logger LOGGER = Logger.getLogger(MapboxSpeech.class.getName());

  protected MapboxSpeech() {
    super(SpeechService.class);
  }

  @Override
  protected Call<ResponseBody> initializeCall() {
    return getService().getCall(
            instruction(),
            textType(),
            language(),
            outputType(),
            accessToken());
  }

  @Nullable
  abstract String language();

  @Nullable
  abstract String textType();

  @Nullable
  abstract String outputType();

  @Nullable
  abstract Cache cache();

  @NonNull
  abstract String accessToken();

  @NonNull
  abstract String instruction();

  @Override
  protected abstract String baseUrl();

  @Override
  public synchronized OkHttpClient getOkHttpClient() {
    if (okHttpClient == null) {
      OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
      if (isEnableDebug()) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);
      }
      if (cache() != null) {
        httpClient.cache(cache());
      }

      okHttpClient = httpClient.build();
    }
    return okHttpClient;
  }

  /**
   * Creates a builder for a MapboxSpeech object with a default cache size of 10 MB.
   *
   * @return a builder to create a MapboxSpeech object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxSpeech.Builder()
            .baseUrl(Constants.BASE_API_URL);
  }

  /**
   * This builder is used to create a MapboxSpeech instance, with details about how the API calls
   * should be made (input/output format, language, etc.). To use caching, specify a cache
   * directory. Access token and instruction are required, along with cache directory if you choose
   * to use caching.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {
    /**
     * Language of which to request the instructions be spoken. Default is "en-us"
     *
     * @param language as a string, i.e., "en-us"
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder language(String language);

    /**
     * Format which the input is specified. If not specified, default is text
     *
     * @param textType either text or ssml
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder textType(String textType);

    /**
     * Output format for spoken instructions. If not specified, default is mp3
     *
     * @param outputType either mp3 or json
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder outputType(String outputType);

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account in order to use
     *                    the Optimization API
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * Add the instruction text to dictate, either as plain text or ssml.
     *
     * @param instruction to dictate
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder instruction(@NonNull String instruction);

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * Adds an optional cache to set in the OkHttp client.
     *
     * @param cache to set for OkHttp
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder cache(Cache cache);

    abstract MapboxSpeech autoBuild();

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, formats the values as strings for easier consumption by the API, and lastly
     * creates a new {@link MapboxSpeech} object with the values provided.
     *
     * @return a new instance of Mapbox Speech
     * @throws ServicesException when a provided parameter is detected to be incorrect
     * @since 3.0.0
     */
    public MapboxSpeech build() {
      MapboxSpeech mapboxSpeech = autoBuild();

      if (TextUtils.isEmpty(mapboxSpeech.instruction())) {
        throw new ServicesException("Non-null, non-empty instruction text is required.");
      }

      return mapboxSpeech;
    }
  }
}
