package com.mapbox.turf.models;

import android.support.annotation.Nullable;

/**
 * if the lines intersect, the result contains the x and y of the intersection (treating the lines
 * as infinite) and booleans for whether line segment 1 or line segment 2 contain the point.
 *
 * @see <a href="http://jsfiddle.net/justin_c_rounds/Gd2S2/light/">Good example of how this class works written in JavaScript</a>
 * @since 1.2.0
 */
public class LineIntersectsResult {

  private final Double horizontalIntersection;

  private final Double verticalIntersection;

  private final boolean onLine1;

  private final boolean onLine2;

  private LineIntersectsResult(
          @Nullable Double horizontalIntersection,
          @Nullable Double verticalIntersection,
          boolean onLine1,
          boolean onLine2) {
    this.horizontalIntersection = horizontalIntersection;
    this.verticalIntersection = verticalIntersection;
    this.onLine1 = onLine1;
    this.onLine2 = onLine2;
  }

  /**
   * Builds a new instance of a lineIntersection. This class is mainly used internally for other
   * turf objects to recall memory when performing calculations.
   *
   * @return {@link LineIntersectsResult.Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * If the lines intersect, use this method to get the intersecting point {@code X} value.
   *
   * @return the {@code X} value where the lines intersect
   * @since 1.2.0
   */
  @Nullable
  public Double horizontalIntersection() {
    return horizontalIntersection;
  }


  /**
   * If the lines intersect, use this method to get the intersecting point {@code Y} value.
   *
   * @return the {@code Y} value where the lines intersect
   * @since 1.2.0
   */
  @Nullable
  public Double verticalIntersection() {
    return verticalIntersection;
  }


  /**
   * Determine if the intersecting point lands on line 1 or not.
   *
   * @return true if the intersecting point is located on line 1, otherwise false
   * @since 1.2.0
   */
  public boolean onLine1() {
    return onLine1;
  }

  /**
   * Determine if the intersecting point lands on line 2 or not.
   *
   * @return true if the intersecting point is located on line 2, otherwise false
   * @since 1.2.0
   */
  public boolean onLine2() {
    return onLine2;
  }

  @Override
  public String toString() {
    return "LineIntersectsResult{"
            + "horizontalIntersection=" + horizontalIntersection + ", "
            + "verticalIntersection=" + verticalIntersection + ", "
            + "onLine1=" + onLine1 + ", "
            + "onLine2=" + onLine2
            + "}";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof LineIntersectsResult) {
      LineIntersectsResult that = (LineIntersectsResult) obj;
      return ((this.horizontalIntersection == null)
              ? (that.horizontalIntersection() == null) :
              this.horizontalIntersection.equals(that.horizontalIntersection()))
              && ((this.verticalIntersection == null)
              ? (that.verticalIntersection() == null) :
              this.verticalIntersection.equals(that.verticalIntersection()))
              && (this.onLine1 == that.onLine1())
              && (this.onLine2 == that.onLine2());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode *= 1000003;
    hashCode ^= (horizontalIntersection == null) ? 0 : horizontalIntersection.hashCode();
    hashCode *= 1000003;
    hashCode ^= (verticalIntersection == null) ? 0 : verticalIntersection.hashCode();
    hashCode *= 1000003;
    hashCode ^= onLine1 ? 1231 : 1237;
    hashCode *= 1000003;
    hashCode ^= onLine2 ? 1231 : 1237;
    return hashCode;
  }

  /**
   * Convert current instance values into another Builder to quickly change one or more values.
   *
   * @return a new instance of {@link LineIntersectsResult} using the newly defined values
   * @since 3.0.0
   */
  public Builder toBuilder() {
    return new Builder(this);
  }


  /**
   * Build a new {@link LineIntersectsResult} instance and define its features by passing in
   * information through the offered methods.
   *
   * @since 3.0.0
   */
  public static class Builder {

    private Double horizontalIntersection;
    private Double verticalIntersection;
    private Boolean onLine1 = false;
    private Boolean onLine2 = false;

    Builder() {
    }

    private Builder(LineIntersectsResult source) {
      this.horizontalIntersection = source.horizontalIntersection();
      this.verticalIntersection = source.verticalIntersection();
      this.onLine1 = source.onLine1();
      this.onLine2 = source.onLine2();
    }

    /**
     * If the lines intersect, use this method to get the intersecting point {@code X} value.
     *
     * @param horizontalIntersection the x coordinates intersection point
     * @return the {@code X} value where the lines intersect
     * @since 3.0.0
     */
    public Builder horizontalIntersection(@Nullable Double horizontalIntersection) {
      this.horizontalIntersection = horizontalIntersection;
      return this;
    }

    /**
     * If the lines intersect, use this method to get the intersecting point {@code Y} value.
     *
     * @param verticalIntersection the y coordinates intersection point
     * @return the {@code Y} value where the lines intersect
     * @since 3.0.0
     */
    public Builder verticalIntersection(@Nullable Double verticalIntersection) {
      this.verticalIntersection = verticalIntersection;
      return this;
    }


    /**
     * Determine if the intersecting point lands on line 1 or not.
     *
     * @param onLine1 true if the points land on line one, else false
     * @return true if the intersecting point is located on line 1, otherwise false
     * @since 3.0.0
     */
    public Builder onLine1(boolean onLine1) {
      this.onLine1 = onLine1;
      return this;
    }


    /**
     * Determine if the intersecting point lands on line 2 or not.
     *
     * @param onLine2 true if the points land on line two, else false
     * @return true if the intersecting point is located on line 2, otherwise false
     * @since 3.0.0
     */
    public Builder onLine2(boolean onLine2) {
      this.onLine2 = onLine2;
      return this;
    }


    /**
     * Builds a new instance of a {@link LineIntersectsResult} class.
     *
     * @return a new instance of {@link LineIntersectsResult}
     * @since 3.0.0
     */
    public LineIntersectsResult build() {
      String missing = "";
      if (this.onLine1 == null) {
        missing += " onLine1";
      }
      if (this.onLine2 == null) {
        missing += " onLine2";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new LineIntersectsResult(
              this.horizontalIntersection,
              this.verticalIntersection,
              this.onLine1,
              this.onLine2);
    }
  }

}
