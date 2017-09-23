package com.mapbox.turf.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * if the lines intersect, the result contains the x and y of the intersection (treating the lines
 * as infinite) and booleans for whether line segment 1 or line segment 2 contain the point
 *
 * @see <a href="http://jsfiddle.net/justin_c_rounds/Gd2S2/light/">Good example of how this class works written in JavaScript</a>
 * @since 1.2.0
 */
@AutoValue
public abstract class LineIntersectsResult {

  public static Builder builder() {
    return new AutoValue_LineIntersectsResult.Builder();
  }

  /**
   * If the lines intersect, use this method to get the intersecting point {@code X} value
   *
   * @return the {@code X} value where the lines intersect
   * @since 1.2.0
   */
  @Nullable
  public abstract Double horizontalIntersection();

  /**
   * If the lines intersect, use this method to get the intersecting point {@code Y} value
   *
   * @return the {@code Y} value where the lines intersect
   * @since 1.2.0
   */
  @Nullable
  public abstract Double verticalIntersection();

  /**
   * Determine if the intersecting point lands on line 1 or not.
   *
   * @return true if the intersecting point is located on line 1, otherwise false
   * @since 1.2.0
   */
  public abstract boolean onLine1();

  /**
   * Determine if the intersecting point lands on line 2 or not.
   *
   * @return true if the intersecting point is located on line 2, otherwise false
   * @since 1.2.0
   */
  public abstract boolean onLine2();

  /**
   * Convert current instance values into another Builder to quickly change one or more values.
   *
   * @return a new instance of {@link LineIntersectsResult} using the newly defined values
   * @since 3.0.0
   */
  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * If the lines intersect, use this method to get the intersecting point {@code X} value
     *
     * @return the {@code X} value where the lines intersect
     * @since 3.0.0
     */
    public abstract Builder horizontalIntersection(@Nullable Double horizontalIntersection);

    /**
     * If the lines intersect, use this method to get the intersecting point {@code Y} value
     *
     * @return the {@code Y} value where the lines intersect
     * @since 3.0.0
     */
    public abstract Builder verticalIntersection(@Nullable Double verticalIntersection);

    /**
     * Determine if the intersecting point lands on line 1 or not.
     *
     * @return true if the intersecting point is located on line 1, otherwise false
     * @since 3.0.0
     */
    public abstract Builder onLine1(boolean onLine1);

    /**
     * Determine if the intersecting point lands on line 2 or not.
     *
     * @return true if the intersecting point is located on line 2, otherwise false
     * @since 3.0.0
     */
    public abstract Builder onLine2(boolean onLine2);

    /**
     * Builds a new instance of a {@link LineIntersectsResult} class.
     *
     * @return a new instance of {@link LineIntersectsResult}
     * @since 3.0.0
     */
    public abstract LineIntersectsResult build();
  }
}
