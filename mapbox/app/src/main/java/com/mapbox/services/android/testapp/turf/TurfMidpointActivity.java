package com.mapbox.services.android.testapp.turf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.api.utils.turf.TurfMeasurement;
import com.mapbox.services.commons.models.Position;

public class TurfMidpointActivity extends AppCompatActivity {

  private MapView mapView;
  private MapboxMap map;
  private Marker secondMarker;
  private Marker midpointMarker;

  private Position cadillacHotelPosition = Position.fromCoordinates(-118.479852, 33.993898);

  private View container;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_turf_midpoint);

    container = findViewById(R.id.turf_midpoint_map_container);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;

        midpoint();
      }
    });
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
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
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
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  private void midpoint() {

    map.addMarker(new MarkerViewOptions()
      .position(new LatLng(cadillacHotelPosition.getLatitude(), cadillacHotelPosition.getLongitude()))
      .title("point 1"));

    Snackbar.make(container, "Click map anywhere to add midpoint", Snackbar.LENGTH_INDEFINITE).show();

    map.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
      @Override
      public void onMapClick(@NonNull LatLng point) {
        if (secondMarker != null) {
          map.removeMarker(secondMarker);
        }
        if (midpointMarker != null) {
          map.removeMarker(midpointMarker);
        }

        secondMarker = map.addMarker(new MarkerViewOptions()
          .position(point)
          .title("point 2"));

        Position midpoint = TurfMeasurement.midpoint(cadillacHotelPosition,
          Position.fromCoordinates(point.getLongitude(), point.getLatitude()));

        midpointMarker = map.addMarker(new MarkerViewOptions()
          .position(new LatLng(midpoint.getLatitude(), midpoint.getLongitude()))
          .title("midpoint"));

        Snackbar.make(container, "Midpoint = " + " Latitiude: " + midpoint.getLatitude()
          + "Longitude: " + midpoint.getLongitude(), Snackbar.LENGTH_INDEFINITE).show();
      }
    });
  }
}