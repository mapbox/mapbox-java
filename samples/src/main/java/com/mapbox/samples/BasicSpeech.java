package com.mapbox.samples;

import com.mapbox.api.speech.v1.MapboxSpeech;
import com.mapbox.sample.BuildConfig;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicSpeech {

    public static void main(String[] args) {
        MapboxSpeech mapboxSpeech = MapboxSpeech.builder()
                .accessToken(BuildConfig.MAPBOX_ACCESS_TOKEN)
                .instruction("turn right")
                .build();

        mapboxSpeech.enqueueCall(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.body().contentType());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }
}
