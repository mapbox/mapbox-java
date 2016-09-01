package com.mapbox.services.android.testapp.geocoding;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.services.android.geocoder.AndroidGeocoder;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.geocoding.service.Constants;
import com.mapbox.services.android.testapp.geocoding.service.FetchAddressIntentService;
import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LostApiClient;

/**
 * This activity is inspired by the stock Android Geocoder sample code in
 * https://github.com/googlesamples/android-play-location/tree/master/LocationAddress
 *
 * In this sample, we show how to use Mapbox' Geocoder simply replacing the
 * android.location.Geocoder object with com.mapbox.services.android.geocoder.AndroidGeocoder.
 * To simplify the code, we've replaced Google Play Services with LOST.
 */
public class GeocodingServiceActivity extends AppCompatActivity {

  protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
  protected static final String LOCATION_ADDRESS_KEY = "location-address";

  /**
   * Provides the entry point to LOST services.
   */
  protected LostApiClient lostApiClient;

  /**
   * Represents a geographical location.
   */
  protected Location lastLocation;

  /**
   * Tracks whether the user has requested an address. Becomes true when the user requests an
   * address and false when the address (or an error message) is delivered.
   * The user requests an address by pressing the Fetch Address button. This may happen
   * before LostApiClient connects. This activity uses this boolean to keep track of the
   * user's intent. If the value is true, the activity tries to fetch the address as soon as
   * LostApiClient connects.
   */
  protected boolean addressRequested;

  /**
   * The formatted location address.
   */
  protected String addressOutput;

  /**
   * Receiver registered with this activity to get the response from FetchAddressIntentService.
   */
  private AddressResultReceiver resultReceiver;

  /**
   * Displays the location address.
   */
  protected TextView locationAddressTextView;

  /**
   * Visible while the address is being fetched.
   */
  ProgressBar progressBar;

  /**
   * Kicks off the request to fetch an address when pressed.
   */
  Button fetchAddressButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geocoding_service);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    resultReceiver = new AddressResultReceiver(new Handler());

    locationAddressTextView = (TextView) findViewById(R.id.location_address_view);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    fetchAddressButton = (Button) findViewById(R.id.fetch_address_button);

    // Set defaults, then update using values stored in the Bundle.
    addressRequested = false;
    addressOutput = "";
    updateValuesFromBundle(savedInstanceState);

    updateUiWidgets();
    buildLostApiClient();
  }

  /**
   * Updates fields based on data stored in the bundle.
   */
  private void updateValuesFromBundle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      // Check savedInstanceState to see if the address was previously requested.
      if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
        addressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
      }

      // Check savedInstanceState to see if the location address string was previously found
      // and stored in the Bundle. If it was found, display the address string in the UI.
      if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
        addressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
        displayAddressOutput();
      }
    }
  }

  /**
   * Builds a LostApiClient.
   */
  protected synchronized void buildLostApiClient() {
    lostApiClient = new LostApiClient.Builder(this).build();
  }

  /**
   * Runs when user clicks the Fetch Address button. Starts the service to fetch the address if
   * LostApiClient is connected.
   */
  public void fetchAddressButtonHandler(View view) {
    // We only start the service to fetch the address if LostApiClient is connected.
    if (lostApiClient.isConnected() && lastLocation != null) {
      startIntentService();
    }

    // If LostApiClient isn't connected, we process the user's request by setting
    // addressRequested to true. Later, when LostApiClient connects, we launch the service to
    // fetch the address. As far as the user is concerned, pressing the Fetch Address button
    // immediately kicks off the process of getting the address.
    addressRequested = true;
    updateUiWidgets();
  }

  @Override
  protected void onStart() {
    super.onStart();
    lostApiClient.connect();
    getLastLocation();
  }

  private void getLastLocation() {
    // Gets the best and most recent location currently available, which may be null
    // in rare cases when a location is not available.
    lastLocation = LocationServices.FusedLocationApi.getLastLocation();
    if (lastLocation != null) {
      // Determine whether a Geocoder is available.
      if (!AndroidGeocoder.isPresent()) {
        Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
        return;
      }

      // It is possible that the user presses the button to get the address before the
      // LostApiClient object successfully connects. In such a case, addressRequested
      // is set to true, but no attempt is made to fetch the address (see
      // fetchAddressButtonHandler()) . Instead, we start the intent service here if the
      // user has requested an address, since we now have a connection to LostApiClient.
      if (addressRequested) {
        startIntentService();
      }
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (lostApiClient.isConnected()) {
      lostApiClient.disconnect();
    }
  }

  /**
   * Creates an intent, adds location data to it as an extra, and starts the intent service for
   * fetching an address.
   */
  protected void startIntentService() {
    // Create an intent for passing to the intent service responsible for fetching the address.
    Intent intent = new Intent(this, FetchAddressIntentService.class);

    // Pass the result receiver as an extra to the service.
    intent.putExtra(Constants.RECEIVER, resultReceiver);

    // Pass the location data as an extra to the service.
    intent.putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation);

    // Start the service. If the service isn't already running, it is instantiated and started
    // (creating a process for it if needed); if it is running then it remains running. The
    // service kills itself automatically once all intents are processed.
    startService(intent);
  }

  /**
   * Updates the address in the UI.
   */
  protected void displayAddressOutput() {
    locationAddressTextView.setText(addressOutput);
  }

  /**
   * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
   */
  private void updateUiWidgets() {
    if (addressRequested) {
      progressBar.setVisibility(ProgressBar.VISIBLE);
      fetchAddressButton.setEnabled(false);
    } else {
      progressBar.setVisibility(ProgressBar.GONE);
      fetchAddressButton.setEnabled(true);
    }
  }

  /**
   * Shows a toast with the given text.
   */
  protected void showToast(String text) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    // Save whether the address has been requested.
    savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, addressRequested);

    // Save the address string.
    savedInstanceState.putString(LOCATION_ADDRESS_KEY, addressOutput);
    super.onSaveInstanceState(savedInstanceState);
  }

  /**
   * Receiver for data sent from FetchAddressIntentService.
   */
  class AddressResultReceiver extends ResultReceiver {
    public AddressResultReceiver(Handler handler) {
      super(handler);
    }

    /**
     * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
      // Display the address string or an error message sent from the intent service.
      addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
      displayAddressOutput();

      // Show a toast message if an address was found.
      if (resultCode == Constants.SUCCESS_RESULT) {
        showToast(getString(R.string.address_found));
      }

      // Reset. Enable the Fetch Address button and stop showing the progress bar.
      addressRequested = false;
      updateUiWidgets();
    }
  }

}
