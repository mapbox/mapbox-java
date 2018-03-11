package com.mapbox.rx.api.directions.v5;

import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class EnqueueCallObservable extends Observable<DirectionsResponse> {

  private final MapboxDirections mapboxDirections;

  EnqueueCallObservable(MapboxDirections mapboxDirections) {
    this.mapboxDirections = mapboxDirections;
  }

  @Override
  protected void subscribeActual(Observer<? super DirectionsResponse> observer) {
    Listener listener = new Listener(mapboxDirections, observer);
    mapboxDirections.enqueueCall(listener);
    observer.onSubscribe(listener);
  }

  static final class Listener implements Disposable, Callback<DirectionsResponse> {

    private final MapboxDirections mapboxDirections;
    private final Observer<? super DirectionsResponse> observer;
    private boolean terminated = false;

    Listener(MapboxDirections mapboxDirections, Observer<? super DirectionsResponse> observer) {
      this.mapboxDirections = mapboxDirections;
      this.observer = observer;
    }

    @Override
    public void dispose() {
      mapboxDirections.cancelCall();
    }

    @Override
    public boolean isDisposed() {
      return mapboxDirections.isCanceled();
    }

    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
      if (isDisposed()) {
        return;
      }

      try {
        observer.onNext(response.body());
        if (!call.isCanceled()) {
          terminated = true;
          observer.onComplete();
        }
      } catch (Exception exception) {
        if (terminated) {
          RxJavaPlugins.onError(exception);
        } else if (!call.isCanceled()) {
          try {
            observer.onError(exception);
          } catch (Exception inner) {
            Exceptions.throwIfFatal(inner);
            RxJavaPlugins.onError(new CompositeException(exception, inner));
          }
        }
      }
    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
      if (call.isCanceled()) {
        return;
      }

      try {
        observer.onError(throwable);
      } catch (Exception inner) {
        Exceptions.throwIfFatal(inner);
        RxJavaPlugins.onError(new CompositeException(throwable, inner));
      }
    }
  }
}