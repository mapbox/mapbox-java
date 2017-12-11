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
import retrofit2.HttpException;
import retrofit2.Response;

import java.util.concurrent.atomic.AtomicBoolean;

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

    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    private final MapboxDirections mapboxDirections;
    private final Observer<? super DirectionsResponse> observer;

    Listener(MapboxDirections mapboxDirections, Observer<? super DirectionsResponse> observer) {
      this.mapboxDirections = mapboxDirections;
      this.observer = observer;
    }

    @Override
    public void dispose() {
      unsubscribed.compareAndSet(false, true);
      mapboxDirections.cancelCall();
    }

    @Override
    public boolean isDisposed() {
      return unsubscribed.get();
    }

    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//      if (!isDisposed()) {
//        observer.onNext(response.body());
//      }



      if (response.isSuccessful()) {
        observer.onNext(response.body());
      } else {
        Throwable t = new HttpException(response);
        try {
          observer.onError(t);
        } catch (Throwable inner) {
          Exceptions.throwIfFatal(inner);
          RxJavaPlugins.onError(new CompositeException(t, inner));
        }
      }

    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
      if (!isDisposed()) {
        observer.onError(throwable);
      }
    }
  }
}