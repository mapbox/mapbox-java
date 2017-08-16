package com.mapbox.services.android.telemetry;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An experimental user-facing API. Experimental APIs are quickly evolving
 * and might change or be removed in minor versions.
 * <p>
 * Inspired by org.apache.spark.annotation.Experimental
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
  ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
@interface Experimental {
}
