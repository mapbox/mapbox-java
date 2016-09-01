package com.mapbox.services.android.testapp.turf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfMeasurement;

public class TurfBearingActivity extends AppCompatActivity {

  private MapView mapView;
  private MapboxMap map;
  private Marker bearingMarker;

  private Position cadillacHotelPosition = Position.fromCoordinates(-118.479852, 33.993898);

  private View container;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_turf_bearing);

    container = findViewById(R.id.turf_bearing_map_container);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;

        bearing();
      }
    });
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
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  private void bearing() {

    map.addMarker(new MarkerViewOptions()
      .position(new LatLng(cadillacHotelPosition.getLatitude(), cadillacHotelPosition.getLongitude()))
      .title("point 1"));

    Snackbar.make(container, "Click map anywhere to calculate bearing", Snackbar.LENGTH_INDEFINITE).show();

    map.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
      @Override
      public void onMapClick(@NonNull LatLng point) {
        if (bearingMarker != null) {
          map.removeMarker(bearingMarker);
        }

        bearingMarker = map.addMarker(new MarkerViewOptions()
          .position(point)
          .title("point 2"));

        double bearing = TurfMeasurement.bearing(
          Point.fromCoordinates(cadillacHotelPosition),
          Point.fromCoordinates(Position.fromCoordinates(point.getLongitude(), point.getLatitude())));
        Snackbar.make(container, "Bearing = " + bearing, Snackbar.LENGTH_INDEFINITE).show();
      }
    });
  }
}