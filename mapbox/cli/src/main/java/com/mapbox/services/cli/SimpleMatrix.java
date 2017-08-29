package com.mapbox.services.cli;

import com.mapbox.services.api.matrix.v1.MapboxMatrix;
import com.mapbox.services.api.matrix.v1.models.MatrixResponse;
import com.mapbox.services.commons.geojson.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;

public class SimpleMatrix {

  public static void main(String[] args) {

    MapboxMatrix matrix = MapboxMatrix.builder()
      .coordinate(new Point(-77.03649, 38.89766)) // White house
      .coordinate(new Point(-77.00910, 38.88981)) // Capitol
      .coordinate(new Point(-77.04222, 38.84931)) // DCA
      .coordinate(new Point(-77.04330, 38.90962)) // Dupont Circle
      .accessToken("")
      .build();

    // Print out the url to help debug call
    System.out.println(matrix.cloneCall().request().url());

    matrix.enqueueCall(new Callback<MatrixResponse>() {
      @Override
      public void onResponse(Call<MatrixResponse> call, Response<MatrixResponse> response) {
        if (response.body() != null && response.body().durations() != null) {
          if (response.body().durations().size() > 0) {
            for (int i = 0; i < response.body().durations().size(); i++) {
              System.out.println(Arrays.toString(response.body().durations().get(i)));
            }
          }
        }
      }

      @Override
      public void onFailure(Call<MatrixResponse> call, Throwable throwable) {
        System.err.println("Error: " + throwable);
      }
    });
  }
}
