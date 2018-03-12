package com.mapbox.api.speech.v1;


import com.google.auto.value.AutoValue;
import com.mapbox.core.exceptions.ServicesException;
import com.sun.istack.internal.Nullable;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mapbox.core.constants.Constants.BASE_API_URL;

@AutoValue
public abstract class MapboxSpeech {
  /**
   * Makes the API call to get the instruction using parameters specified when MapboxSpeech was
   * built.
   *
   * @param instruction text to pass to API voice for dictation
   * @param callback to alert status updates for API call
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
   */
  public void cacheInstruction(String instruction) {
    getInstruction(instruction, new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable throwable) {

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

  abstract VoiceService voiceService();


  @AutoValue.Builder
  public abstract static class Builder {
    long cacheSize;
    File cacheDirectory;

    /**
     * Language of which to request the instructions be spoken
     *
     * @param language as a string, i.e., "en-us"
     * @return this builder for chaining options together
     */
    public abstract Builder language(String language);

    /**
     * Format which the input is specified
     *
     * @param textType either text or ssml
     * @return this builder for chaining options together
     */
    public abstract Builder textType(String textType);

    /**
     * Output format for spoken instructions
     *
     * @param outputType, either mp3, ogg_vorbis or pcm
     * @return this builder for chaining options together
     */
    public abstract Builder outputType(String outputType);

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account in order to use
     *                    the Optimization API
     * @return this builder for chaining options together
     */
    public abstract Builder accessToken(String accessToken);

    /**
     * Size for the OkHTTP cache
     *
     * @param cacheSize in bytes
     * @return this builder for chaining options together
     */
    public Builder cacheSize(long cacheSize) {
      this.cacheSize = cacheSize;
      return this;
    }

    long cacheSize() {
      return cacheSize;
    }

    abstract Builder voiceService(VoiceService voiceService);

    /**
     * Directory where to instantiate OkHTTP cache
     *
     * @return this builder for chaining options together
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
     */
    public MapboxSpeech build() {
      voiceService(getVoiceService());
      return autoBuild();
    }

    private VoiceService getVoiceService() {
      Cache cache = new Cache(cacheDirectory(), cacheSize());

      OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .cache(cache)
        .build();

      Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .client(okHttpClient)
        .build();

      return retrofit.create(VoiceService.class);
    }
  }

  /**
   * Creates a builder for a MapboxSpeech object with a default cache size of 10 MB
   *
   * @return a builder to create a MapboxSpeech object
   */
  public static Builder builder() {
    return new AutoValue_MapboxSpeech.Builder()
      .cacheSize(10 * 1024 * 1024); // default cache size is 10 MB
  }
}
