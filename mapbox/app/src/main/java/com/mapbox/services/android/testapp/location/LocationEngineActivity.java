package com.mapbox.services.android.testapp.location;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapbox.services.android.location.MockLocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LocationEngineProvider;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.commons.models.Position;

import java.util.Map;

import timber.log.Timber;

public class LocationEngineActivity extends AppCompatActivity
  implements AdapterView.OnItemSelectedListener, LocationEngineListener {

  private static final String LOG_TAG = LocationEngineActivity.class.getSimpleName();

  private TextView textLocation;
  private LocationEngine locationEngine;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_engine);
    textLocation = (TextView) findViewById(R.id.text_location);
    setupSpinner();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (locationEngine != null && locationEngine.isConnected()) {
      locationEngine.requestLocationUpdates();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (locationEngine != null) {
      locationEngine.removeLocationUpdates();
      locationEngine.removeLocationEngineListener(this);
      locationEngine.deactivate();
    }
  }

  private void setupSpinner() {
    Spinner spinner = (Spinner) findViewById(R.id.spinner_engine);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
      R.array.location_engines, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    spinner.setOnItemSelectedListener(this);
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    String engineName = (String) parent.getItemAtPosition(pos);
    Log.d(LOG_TAG, "Engine selected: " + engineName);
    setNoEngine();

    String[] locationEngines = getResources().getStringArray(R.array.location_engines);
    LocationEngineProvider locationEngineProvider = new LocationEngineProvider(this);
    Map<String, LocationEngine> locationSourceDictionary = locationEngineProvider.obtainLocationEngineDictionary();
    if (engineName.equals(locationEngines[1])) {
      // Mock
      locationEngine = new MockLocationEngine();
      ((MockLocationEngine) locationEngine).setLastLocation(Position.fromLngLat(-87.62877, 41.87827));
      ((MockLocationEngine) locationEngine).moveToLocation(Position.fromLngLat(-87.6633, 41.8850));
    } else if (!engineName.equals(locationEngines[0])) {
      locationEngine = locationSourceDictionary.get(engineName);
    }

    if (!engineName.equals(locationEngines[0]) && locationEngine != null) {
      // Not None
      Timber.e("Last known location: %s", locationEngine.getLastLocation());
      locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
      locationEngine.addLocationEngineListener(this);
      locationEngine.activate();
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {
    Log.d(LOG_TAG, "No engine selected.");
    setNoEngine();
  }

  private void setNoEngine() {
    if (locationEngine != null) {
      locationEngine.removeLocationUpdates();
      locationEngine.removeLocationEngineListener(this);
      locationEngine.deactivate();
    }

    textLocation.setText("No location updates, yet.");
    locationEngine = null;
  }

  @Override
  public void onConnected() {
    Log.d(LOG_TAG, "Connected to engine, we can now request updates.");
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    if (location != null) {
      Log.d(LOG_TAG, "New location received: " + location.toString());
      textLocation.setText(location.toString());
    }
  }
}
