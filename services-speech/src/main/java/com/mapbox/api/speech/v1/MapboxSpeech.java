package com.mapbox.api.speech.v1;


import com.google.auto.value.AutoValue;
import com.mapbox.core.exceptions.ServicesException;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mapbox.core.constants.Constants.BASE_API_URL;

/**
 * The Speech API is a text-to-speech APi with a server-side caching layer in front of AWS Polly.
 * The only requirements are text to dictate, and a Mapbox access token. For 3-step-ahead
 * client-side caching, cache directory is required.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class MapboxSpeech {
  private static final Logger LOGGER = Logger.getLogger(MapboxSpeech.class.getName());

  /**
   * Makes the API call to get the instruction using parameters specified when MapboxSpeech was
   * built.
   *
   * @param instruction text to pass to API voice for dictation
   * @param callback to alert status updates for API call
   * @since 3.0.0
   */
  public void getInstruction(String instruction, Callback<ResponseBody> callback) {
    voiceService().getInstruction(
      instruction, textType(),
      language(),
      outputType(),
      accessToken()).enqueue(callback);
  }

  /**
   * Makes the specified instruction call with an empty callback, subsequently caching it for later
   * use.
   *
   * @param instruction text to pass to API voice for dictation
   * @since 3.0.0
   */
  public void cacheInstruction(String instruction) {
    getInstruction(instruction, new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        // no op
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable throwable) {
        LOGGER.log(Level.WARNING, "Failed to cache instruction. ", throwable);
      }
    });
  }

  @Nullable
  abstract String language();

  @Nullable
  abstract String textType();

  @Nullable
  abstract String outputType();

  abstract String accessToken();

  abstract SpeechService voiceService();

  /**
   * Creates a builder for a MapboxSpeech object with a default cache size of 10 MB
   *
   * @return a builder to create a MapboxSpeech object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxSpeech.Builder()
      .cacheSize(10 * 1024 * 1024); // default cache size is 10 MB
  }

  /**
   * This builder is used to create a MapboxSpeech instance, with details about how the API calls
   * should be made (input/output format, language, etc.). To use caching, specify a cache
   * directory. Access token is required, along with cache directory if you choose to use caching.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {
    private long cacheSize;
    private File cacheDirectory;

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
     * @param outputType, either mp3, ogg_vorbis or pcm
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
    public abstract Builder accessToken(String accessToken);

    /**
     * Size for the OkHTTP cache. Default is 10 MB
     *
     * @param cacheSize in bytes
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder cacheSize(long cacheSize) {
      this.cacheSize = cacheSize;
      return this;
    }

    long cacheSize() {
      return cacheSize;
    }

    abstract Builder voiceService(SpeechService speechService);

    /**
     * Directory where to instantiate OkHTTP cache. Required for caching. If not specified, retrofit
     * will be utilized without a cache.
     *
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder cacheDirectory(File cacheDirectory) {
      this.cacheDirectory = cacheDirectory;
      return this;
    }

    File cacheDirectory() {
      return cacheDirectory;
    }

    abstract MapboxSpeech autoBuild();

    /**
     * Creates the Retrofit instance and builds a new MapboxSpeech object
     *
     * @return MapboxSpeech object with specified parameters
     * @since 3.0.0
     */
    public MapboxSpeech build() {
      voiceService(getVoiceService());
      return autoBuild();
    }

    private SpeechService getVoiceService() {
      OkHttpClient okHttpClient;
      if (cacheDirectory() != null) {
        Cache cache = new Cache(cacheDirectory(), cacheSize());

        okHttpClient = new OkHttpClient.Builder()
          .cache(cache)
          .build();
      } else {
        okHttpClient = new OkHttpClient.Builder()
          .build();
      }

      Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .client(okHttpClient)
        .build();

      return retrofit.create(SpeechService.class);
    }
  }
}
