package com.mapbox.services.api.rx.optimization.v1;


import com.mapbox.services.api.optimization.v1.MapboxOptimization;
import com.mapbox.services.api.optimization.v1.models.OptimizationResponse;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Response;

final class OptimizationResponseObservable extends Observable<Response<OptimizationResponse>> {

  private final MapboxOptimization mapboxOptimization;

  OptimizationResponseObservable(MapboxOptimization mapboxOptimization) {
    this.mapboxOptimization = mapboxOptimization;
  }

  @Override
  protected void subscribeActual(Observer<? super Response<OptimizationResponse>> observer) {
    // Since Call is a one-shot type, clone it for each new observer.
    Call<OptimizationResponse> call = mapboxOptimization.cloneCall();
    Callback callback = new Callback(call, observer);
    observer.onSubscribe(callback);
    call.enqueue(callback);
  }

  static final class Callback implements Disposable, retrofit2.Callback<OptimizationResponse> {

    private final Call<?> call;
    private final Observer<? super Response<OptimizationResponse>> observer;
    boolean terminated = false;

    Callback(Call<?> call, Observer<? super Response<OptimizationResponse>> observer) {
      this.observer = observer;
      this.call = call;
    }

    @Override
    public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
      if (call.isCanceled()) {
        return;
      }

      try {
        observer.onNext(response);

        if (!call.isCanceled()) {
          terminated = true;
          observer.onComplete();
        }
      } catch (Throwable throwable) {
        if (terminated) {
          RxJavaPlugins.onError(throwable);
        } else if (!call.isCanceled()) {
          try {
            observer.onError(throwable);
          } catch (Throwable inner) {
            Exceptions.throwIfFatal(inner);
            RxJavaPlugins.onError(new CompositeException(throwable, inner));
          }
        }
      }
    }

    @Override
    public void onFailure(Call<OptimizationResponse> call, Throwable t) {
      if (call.isCanceled()) {
        return;
      }
      try {
        observer.onError(t);
      } catch (Throwable inner) {
        Exceptions.throwIfFatal(inner);
        RxJavaPlugins.onError(new CompositeException(t, inner));
      }
    }

    @Override
    public void dispose() {
      call.cancel();
    }

    @Override
    public boolean isDisposed() {
      return call.isCanceled();
    }
  }
}


