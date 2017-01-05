package com.mapbox.services.android.testapp.turf;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.api.utils.turf.TurfException;
import com.mapbox.services.api.utils.turf.TurfMeasurement;

public class TurfDestinationActivity extends AppCompatActivity {

  private MapView mapView;
  private MapboxMap map;
  private Marker destinationMarker;

  private Position cadillacHotelPosition = Position.fromCoordinates(-118.479852, 33.993898);

  private View container;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_turf_destination);

    container = findViewById(R.id.turf_destination_container);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.turf_destination_fab);
    if (fab != null) {
      fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (map != null) {
            destination();
          }
        }
      });
    }

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        destination();
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

  private void destination() {

    map.addMarker(new MarkerViewOptions()
      .position(new LatLng(cadillacHotelPosition.getLatitude(), cadillacHotelPosition.getLongitude()))
      .title("point 1"));

    // custom dialog
    final Dialog dialog = new Dialog(TurfDestinationActivity.this);
    dialog.setContentView(R.layout.layout_turf_destination_dialog);

    final EditText distanceInput = (EditText) dialog.findViewById(R.id.turf_destination_distance_input);
    final EditText bearingInput = (EditText) dialog.findViewById(R.id.turf_destination_bearing_input);
    final Spinner distanceUnitsSpinner = (Spinner) dialog.findViewById(R.id.turf_destination_unit_spinner);
    Button calculateButton = (Button) dialog.findViewById(R.id.turf_destination_calculate_button);
    if (calculateButton != null) {
      calculateButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          double distance;
          double bearing;


          if (distanceInput.getText().length() != 0) {
            distance = Double.parseDouble(distanceInput.getText().toString());
          } else {
            Toast.makeText(TurfDestinationActivity.this, "missing distance value", Toast.LENGTH_LONG).show();
            return;
          }

          if (bearingInput.getText().length() != 0) {
            bearing = Double.parseDouble(bearingInput.getText().toString());
          } else {
            Toast.makeText(TurfDestinationActivity.this, "missing bearing value", Toast.LENGTH_LONG).show();
            return;
          }
          try {
            Point result = TurfMeasurement.destination(
              Point.fromCoordinates(cadillacHotelPosition),
              distance,
              bearing,
              distanceUnitsSpinner.getSelectedItem().toString()
            );
            Position resultPosition = result.getCoordinates();
            if (destinationMarker != null) {
              map.removeMarker(destinationMarker);
            }
            destinationMarker = map.addMarker(new MarkerViewOptions()
              .position(new LatLng(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude()))
              .title("destination"));
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
              .include(new LatLng(cadillacHotelPosition.getLatitude(), cadillacHotelPosition.getLongitude()))
              .include(new LatLng(resultPosition.getLatitude(), resultPosition.getLongitude()))
              .build();

            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 80, 160, 80, 160), 1000);

            Snackbar.make(
              container,
              "Destination = " + resultPosition.getLatitude() + ", " + resultPosition.getLongitude(),
              Snackbar.LENGTH_INDEFINITE).show();
          } catch (TurfException turfException) {
            turfException.printStackTrace();
          }

          dialog.dismiss();

        }
      });
    }

    dialog.show();
  }
}