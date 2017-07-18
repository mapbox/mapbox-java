package com.mapbox.services.android.testapp.geocoding;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.services.android.geocoder.AndroidGeocoder;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEngineChainSupplier;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.geocoding.service.Constants;
import com.mapbox.services.android.testapp.geocoding.service.FetchAddressIntentService;

import java.lang.ref.WeakReference;

/**
 * This activity is inspired by the stock Android Geocoder sample code in
 * https://github.com/googlesamples/android-play-location/tree/master/LocationAddress
 * <p>
 * In this sample, we show how to use Mapbox's Geocoder simply replacing the
 * android.location.Geocoder object with com.mapbox.services.android.geocoder.AndroidGeocoder.
 * To simplify the code, we've replaced Google Play Services with Android.
 */
public class GeocodingServiceActivity extends AppCompatActivity implements LocationEngineListener {

  private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
  private static final String LOCATION_ADDRESS_KEY = "location-address";

  /**
   * Provides the entry point to location services.
   */
  private LocationEngine locationEngine;

  /**
   * Represents a geographical location.
   */
  private Location lastLocation;

  /**
   * Tracks whether the user has requested an address. Becomes true when the user requests an
   * address and false when the address (or an error message) is delivered.
   * The user requests an address by pressing the Fetch Address button. This may happen
   * before location engine connects. This activity uses this boolean to keep track of the
   * user's intent. If the value is true, the activity tries to fetch the address as soon as
   * location engine connects.
   */
  private static boolean addressRequested;

  /**
   * The formatted location address.
   */
  private static String addressOutput;

  /**
   * Receiver registered with this activity to get the response from FetchAddressIntentService.
   */
  private AddressResultReceiver resultReceiver;

  /**
   * Displays the location address.
   */
  private TextView locationAddressTextView;

  /**
   * Visible while the address is being fetched.
   */
  private ProgressBar progressBar;

  /**
   * Kicks off the request to fetch an address when pressed.
   */
  private Button fetchAddressButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geocoding_service);

    locationAddressTextView = (TextView) findViewById(R.id.location_address_view);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    fetchAddressButton = (Button) findViewById(R.id.fetch_address_button);

    resultReceiver = new AddressResultReceiver(new Handler(), this, locationAddressTextView, progressBar,
      fetchAddressButton);

    // Set defaults, then update using values stored in the Bundle.
    addressRequested = false;
    addressOutput = "";
    updateValuesFromBundle(savedInstanceState);

    updateUiWidgets(addressRequested, progressBar, fetchAddressButton);
    buildAndroidLocationEngine();
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
        displayAddressOutput(locationAddressTextView);
      }
    }
  }

  /**
   * Builds Android location engine
   */
  private synchronized void buildAndroidLocationEngine() {
    LocationEngineChainSupplier locationEngineChainSupplier = new LocationEngineChainSupplier();
    locationEngine = locationEngineChainSupplier.supply(this);
    locationEngine.addLocationEngineListener(this);
    locationEngine.activate();
  }

  /**
   * Runs when user clicks the Fetch Address button. Starts the service to fetch the address if
   * location engine is connected.
   */
  public void fetchAddressButtonHandler(View view) {
    // We only start the service to fetch the address if location engine is connected.
    if (locationEngine.isConnected() && lastLocation != null) {
      startIntentService();
    }

    // If location engine isn't connected, we process the user's request by setting
    // addressRequested to true. Later, when location engine connects, we launch the service to
    // fetch the address. As far as the user is concerned, pressing the Fetch Address button
    // immediately kicks off the process of getting the address.
    addressRequested = true;
    updateUiWidgets(addressRequested, progressBar, fetchAddressButton);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (locationEngine != null && locationEngine.isConnected()) {
      obtainLastAddress();
    }
  }

  private void obtainLastAddress() {
    // Gets the best and most recent location currently available, which may be null
    // in rare cases when a location is not available.

    lastLocation = locationEngine.getLastLocation();
    if (lastLocation != null) {
      // Determine whether a Geocoder is available.
      if (!AndroidGeocoder.isPresent()) {
        Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
        return;
      }

      // It is possible that the user presses the button to get the address before the
      // location engine object successfully connects. In such a case, addressRequested
      // is set to true, but no attempt is made to fetch the address (see
      // fetchAddressButtonHandler()) . Instead, we start the intent service here if the
      // user has requested an address, since we now have a connection to location engine.
      if (addressRequested) {
        startIntentService();
      }
    }
  }

  @Override
  public void onConnected() {
    obtainLastAddress();
  }

  @Override
  public void onLocationChanged(Location location) {
    // Unused on purpose
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (locationEngine != null) {
      locationEngine.removeLocationEngineListener(this);
      locationEngine.deactivate();
    }
  }

  /**
   * Creates an intent, adds location data to it as an extra, and starts the intent service for
   * fetching an address.
   */
  private void startIntentService() {
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
  private static void displayAddressOutput(TextView view) {
    view.setText(addressOutput);
  }

  /**
   * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
   */
  private static void updateUiWidgets(boolean addressRequested, ProgressBar progressBar, Button button) {
    if (addressRequested) {
      progressBar.setVisibility(ProgressBar.VISIBLE);
      button.setEnabled(false);
    } else {
      progressBar.setVisibility(ProgressBar.GONE);
      button.setEnabled(true);
    }
  }

  /**
   * Shows a toast with the given message.
   */
  private static void showToast(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
  private static class AddressResultReceiver extends ResultReceiver {
    private final WeakReference<Context> contextReference;
    private final WeakReference<TextView> textViewReference;
    private final WeakReference<ProgressBar> progressBarReference;
    private final WeakReference<Button> fetchAddressButtonReference;

    public AddressResultReceiver(Handler handler, Context context, TextView resultTextView, ProgressBar progressBar,
                                 Button fetchAddressButton) {
      super(handler);
      this.contextReference = new WeakReference<>(context);
      this.textViewReference = new WeakReference<>(resultTextView);
      this.progressBarReference = new WeakReference<>(progressBar);
      this.fetchAddressButtonReference = new WeakReference<>(fetchAddressButton);
    }

    /**
     * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
      // Display the address string or an error message sent from the intent service.
      addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
      TextView view = textViewReference.get();
      if (view != null) {
        displayAddressOutput(view);
      }

      // Show a toast message if an address was found.
      if (resultCode == Constants.SUCCESS_RESULT) {
        Context context = contextReference.get();
        if (context != null) {
          String message = context.getString(R.string.address_found);
          showToast(context, message);
        }
      }

      // Reset. Enable the Fetch Address button and stop showing the progress bar.
      addressRequested = false;

      ProgressBar progressBar = progressBarReference.get();
      Button fetchAddressButton = fetchAddressButtonReference.get();
      if (progressBar != null && fetchAddressButton != null) {
        updateUiWidgets(addressRequested, progressBar, fetchAddressButton);
      }
    }
  }

}
