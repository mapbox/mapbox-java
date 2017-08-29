package com.mapbox.services.android.testapp.optimizedtrip;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.Constants;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.optimization.v1.MapboxOptimization;
import com.mapbox.services.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class BasicOptimizationActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static final String LINE_SOURCE = "line-source";
  private static final String LINE_LAYER = "line-layer";

  private MapboxMap mapboxMap;
  private MapView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic_optimization);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;

    MapboxOptimization.Builder builder = MapboxOptimization.builder()
      .accessToken(Mapbox.getAccessToken())
      .source(DirectionsCriteria.SOURCE_FIRST)
      .destination(DirectionsCriteria.DESTINATION_LAST)
      .coordinate(new Point(-77.03137, 38.91694))
      .coordinate(new Point(-77.0159, 38.9496))
      .roundTrip(true)
      .distribution(3, 1)
      .bearing(0d, 180d)
      .bearing(90d, 180d)
      .overview(DirectionsCriteria.OVERVIEW_FULL);

    builder.build().enqueueCall(new Callback<OptimizationResponse>() {
      @Override
      public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
        Timber.v("Call Url: %s", call.request().url().toString());

        drawLine(response.body().trips().get(0).geometry());
        Timber.v("Waypoint Index: %d", response.body().waypoints().get(0).waypointIndex());
        Timber.v("Trip Index: %d", response.body().waypoints().get(0).tripsIndex());
        Timber.v("Waypoint Name: %d", response.body().waypoints().get(0).name());
      }

      @Override
      public void onFailure(Call<OptimizationResponse> call, Throwable throwable) {
        Timber.e("Calling optimization failed: ", throwable);
      }
    });
  }

  private void drawLine(String geometry) {

    LineString linestring = LineString.fromPolyline(geometry, Constants.PRECISION_6);
    GeoJsonSource source = new GeoJsonSource(LINE_SOURCE, Feature.fromGeometry(linestring));
    mapboxMap.addSource(source);

    LineLayer layer = new LineLayer(LINE_LAYER, LINE_SOURCE).withProperties(
      PropertyFactory.lineWidth(5f),
      PropertyFactory.lineColor(Color.parseColor("#009DF9"))
    );

    mapboxMap.addLayerBelow(layer, "poi-parks-scalerank2");
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}