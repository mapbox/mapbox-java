package com.mapbox.services.android.testapp.directions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.geojson.Point;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DirectionsV5Activity extends AppCompatActivity implements OnMapReadyCallback,
  Callback<DirectionsResponse> {

  private static final Point origin = Point.fromLngLat(-122.416667, 37.783333);
  private static final Point destination = Point.fromLngLat(-121.9, 37.333333);
  private static final String LINE_SOURCE = "line-source";
  private static final String LINE_LAYER = "line-layer";

  @BindView(R.id.mapView)
  MapView mapView;

  private MapboxMap mapboxMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_directions_v5);
    ButterKnife.bind(this);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    fitRouteInViewPort();
    addMarkers();
    requestRoute();
  }

  private void addMarkers() {
    // Add origin and destination to the map
    mapboxMap.addMarker(new MarkerOptions()
      .position(new LatLng(origin.latitude(), origin.longitude()))
      .title("Origin")
      .snippet("San Francisco"));
    mapboxMap.addMarker(new MarkerOptions()
      .position(new LatLng(destination.latitude(), destination.longitude()))
      .title("Destination")
      .snippet("San Jose"));
  }

  private void fitRouteInViewPort() {
    LatLngBounds bounds = new LatLngBounds.Builder()
      .include(new LatLng(origin.latitude(), origin.longitude()))
      .include(new LatLng(destination.latitude(), destination.longitude()))
      .build();
    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
  }

  private void requestRoute() {
    MapboxDirections.builder()
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .accessToken(Mapbox.getAccessToken())
      .destination(destination)
      .origin(origin)
      .steps(true)
      .build()
      .enqueueCall(this);
  }

  @Override
  public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
    Timber.v("API call URL: %s", call.request().url().toString());
    if (response.body() != null
      && response.body().routes() != null
      && response.body().routes().size() > 0) {
      DirectionsRoute currentRoute = response.body().routes().get(0);
      Timber.v("Distance: %s", currentRoute.distance());
      Snackbar.make(
        mapView,
        String.format(Locale.US, "requestRoute is %.1f meters long.", currentRoute.distance()),
        Snackbar.LENGTH_LONG
      );
      drawRoute(currentRoute);
    }
  }

  @Override
  public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
    Timber.e("Error: %s", throwable);
  }

  private void drawRoute(DirectionsRoute route) {
    mapboxMap.addSource(new GeoJsonSource(LINE_SOURCE, route.geometry()));
    mapboxMap.addLayer(new LineLayer(LINE_LAYER, LINE_SOURCE)
      .withProperties(
        PropertyFactory.lineColor(Color.parseColor("#4264fb")),
        PropertyFactory.lineWidth(7f),
        PropertyFactory.lineOpacity(0.8f)
      )
    );
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}