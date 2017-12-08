package com.mapbox.api.rx.directions.v5;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.mapbox.api.directions.v5.MapboxDirections;

public class RxDirections {

  private RxDirections() {
    // Hide constructor
  }

  @CheckResult
  @NonNull
  public static EnqueueCallObservable enqueueCall(
    @NonNull MapboxDirections directions) {
    return new EnqueueCallObservable(directions);
  }

}
