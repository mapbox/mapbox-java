package com.mapbox.services.android.telemetry;

/**
 * A {@code TelemetryException} is thrown by when MapboxTelemetry checks and finds that
 * telemetry has not been properly configured.
 */
public class TelemetryException extends RuntimeException {

  public TelemetryException(String message) {
    super(message);
  }

}
