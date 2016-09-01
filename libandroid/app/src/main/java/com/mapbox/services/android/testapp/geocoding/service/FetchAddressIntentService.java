package com.mapbox.services.android.testapp.geocoding.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.mapbox.services.android.geocoder.AndroidGeocoder;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.commons.ServicesException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {

  private static final String TAG = "FetchAddressService";

  /**
   * The receiver where results are forwarded from this service.
   */
  protected ResultReceiver receiver;

  /**
   * This constructor is required, and calls the super IntentService(String)
   * constructor with the name for a worker thread.
   */
  public FetchAddressIntentService() {
    // Use the TAG to name the worker thread.
    super(TAG);
  }

  /**
   * Tries to get the location address using a Geocoder. If successful, sends an address to a
   * result receiver. If unsuccessful, sends an error message instead.
   * Note: We define a {@link android.os.ResultReceiver} in * MainActivity to process content
   * sent from this service.
   *
   * This service calls this method from the default worker thread with the intent that started
   * the service. When this method returns, the service automatically stops.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
    String errorMessage = "";

    receiver = intent.getParcelableExtra(Constants.RECEIVER);

    // Check if receiver was properly registered.
    if (receiver == null) {
      Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
      return;
    }

    // Get the location passed to this service through an extra.
    Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

    // Make sure that the location data was really sent over through an extra. If it wasn't,
    // send an error error message and return.
    if (location == null) {
      errorMessage = getString(R.string.no_location_data_provided);
      Log.wtf(TAG, errorMessage);
      deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
      return;
    }

    // Errors could still arise from using the Geocoder (for example, if there is no
    // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
    // simply not have an address for a location. In all these cases, we communicate with the
    // receiver using a resultCode indicating failure. If an address is found, we use a
    // resultCode indicating success.

    // The Geocoder used in this sample. The Geocoder's responses are localized for the given
    // Locale, which represents a specific geographical or linguistic region. Locales are used
    // to alter the presentation of information such as numbers or dates to suit the conventions
    // in the region they describe.
    AndroidGeocoder geocoder = new AndroidGeocoder(this, Locale.getDefault());
    geocoder.setAccessToken(Utils.getMapboxAccessToken(this));

    // Address found using the Geocoder.
    List<Address> addresses = null;

    try {
      // Using getFromLocation() returns an array of Addresses for the area immediately
      // surrounding the given latitude and longitude. The results are a best guess and are
      // not guaranteed to be accurate.
      addresses = geocoder.getFromLocation(
        location.getLatitude(),
        location.getLongitude(),
        // In this sample, we get just a single address.
        1);
    } catch (IOException ioException) {
      // Catch network or other I/O problems.
      errorMessage = getString(R.string.service_not_available);
      Log.e(TAG, errorMessage, ioException);
    } catch (IllegalArgumentException illegalArgumentException) {
      // Catch invalid latitude or longitude values.
      errorMessage = getString(R.string.invalid_lat_long_used);
      Log.e(TAG, errorMessage + ". "
        + "Latitude = " + location.getLatitude()
        + ", Longitude = " + location.getLongitude(), illegalArgumentException);
    } catch (ServicesException servicesException) {
      // Catch Mapbox-specific exceptions
      errorMessage = servicesException.getMessage();
      Log.e(TAG, errorMessage, servicesException);
    }

    // Handle case where no address was found.
    if (addresses == null || addresses.size() == 0) {
      if (errorMessage.isEmpty()) {
        errorMessage = getString(R.string.no_address_found);
        Log.e(TAG, errorMessage);
      }
      deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
    } else {
      Address address = addresses.get(0);
      deliverResultToReceiver(Constants.SUCCESS_RESULT, address.getAddressLine(0));
    }
  }

  /**
   * Sends a resultCode and message to the receiver.
   */
  private void deliverResultToReceiver(int resultCode, String message) {
    Bundle bundle = new Bundle();
    bundle.putString(Constants.RESULT_DATA_KEY, message);
    receiver.send(resultCode, bundle);
  }

}
