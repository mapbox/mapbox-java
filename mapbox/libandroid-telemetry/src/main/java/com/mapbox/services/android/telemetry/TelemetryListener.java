package com.mapbox.services.android.telemetry;

/**
 * A callback for Telemetry events
 */
public interface TelemetryListener {

  /**
   * Invoked when sending a new batch of events.
   *
   * @param size the number of events being sent
   */
  void onSendEvents(int size);

  /**
   * Invoked when we obtain a HTTP response.
   *
   * @param successful whether the request was successful
   * @param code the HTTP code of the response
   */
  void onHttpResponse(boolean successful, int code);

  /**
   * Invoked when the HTTP request fails.
   *
   * @param message the error message
   */
  void onHttpFailure(String message);
}
