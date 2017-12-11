package com.mapbox.rx.api.directions.v5;

import static com.mapbox.rx.internal.Preconditions.checkNotNull;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.mapbox.api.directions.v5.MapboxDirections;

public final class RxDirections {

  private RxDirections() {
    throw new AssertionError("No instances.");
  }

  @CheckResult
  @NonNull
  public static EnqueueCallObservable enqueueCall(@NonNull MapboxDirections mapboxDirections) {
    checkNotNull(mapboxDirections, "MapboxDirections == null");
    return new EnqueueCallObservable(mapboxDirections);
  }
}
