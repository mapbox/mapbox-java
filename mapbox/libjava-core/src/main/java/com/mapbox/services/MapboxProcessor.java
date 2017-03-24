package com.mapbox.services;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public class MapboxProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    MapboxVersionChecker updateChecker = MapboxVersionChecker.getInstance(processingEnv);
    updateChecker.executeMapboxVersionUpdate();

    return false;
  }
}
