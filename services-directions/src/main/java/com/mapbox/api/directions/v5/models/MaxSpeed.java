package com.mapbox.api.directions.v5.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
public abstract class MaxSpeed implements Serializable {

  @Nullable
  public abstract Integer speed();
}
