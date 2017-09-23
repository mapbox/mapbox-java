package com.mapbox.staticmap.v1.models;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.geojson.Point;
import com.mapbox.staticmap.v1.StaticMapCriteria;
import com.mapbox.staticmap.v1.StaticMapCriteria.MarkerCriteria;

/**
 * Mapbox Static Image API marker overlay. Building this object allows you to place a marker on top
 * or within your static image. The marker can either use the default marker (though you can change
 * it's color and size) or you have the option to also pass in a custom marker icon using it's url.
 *
 * @since 2.1.0
 */
@AutoValue
public abstract class StaticMarkerAnnotation {

  @NonNull
  @MarkerCriteria
  public abstract String name();

  @Nullable
  public abstract String label();

  @Nullable
  public abstract Integer color();

  @Nullable
  public abstract Point lnglat();

  public static Builder builder() {
    return new AutoValue_StaticMarkerAnnotation
      .name(StaticMapCriteria.MEDIUM_PIN).Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder name(@MarkerCriteria String name);

    public abstract Builder label(String label);

    public abstract Builder color(@ColorInt Integer color);

    public abstract Builder lnglat(Point lnglat);

    abstract autoBuild();

    public StaticMarkerAnnotation build() {

//
//      {
//        name
//      } -{label} + {color} ({lon}, {lat})
//
//
    }
  }


//  {name}-{label}+{color}({lon},{lat})


}
//
//  @NonNull
//  abstract String name();
//
//  @Nullable
//  abstract String label();
//
//  abstract Integer color();
//
//  @NonNull
//  abstract Point markerPoint();
//
//  abstract String url();
//
//  abstract int precision();
//
//  @NonNull
//  public abstract String formattedMarkerUrl();
//
//  abstract Builder toBuilder();
//
//  /**
//   * Build a new {@link StaticMarkerAnnotation} object with the initial values set for
//   * {@link StaticMarkerAnnotation.Builder#name(String)} set to medium.
//   *
//   * @return a {@link StaticMarkerAnnotation.Builder} object for creating this object
//   * @since 3.0.0
//   */
//  public static Builder builder() {
//    return new AutoValue_StaticMarkerAnnotation.Builder()
//      .precision(6)
//      .name(MEDIUM_PIN);
//  }
//
//  /**
//   * This builder is used to create a new request to the Mapbox Static Map API. At a bare minimum,
//   * your request must include a name and {@link StaticMarkerAnnotation.Builder#markerPoint(Point)}.
//   * All other fields can be left alone inorder to use the default behaviour of the API.
//   *
//   * @since 2.1.0
//   */
//  @AutoValue.Builder
//  public abstract static class Builder {
//
//    /**
//     * modify the markers scale factor using one of the pre defined
//     * {@link StaticMapCriteria#SMALL_PIN}, {@link StaticMapCriteria#MEDIUM_PIN}, or
//     * {@link StaticMapCriteria#LARGE_PIN}
//     *
//     * @param name one of the three string sizes provided in this methods summary
//     * @return this builder for chaining options together
//     * @since 2.1.0
//     */
//    public abstract Builder name(@MarkerSize @NonNull String name);
//
//    /**
//     * Marker symbol. Options are an alphanumeric label "a" through "z", "0" through  "99", or a
//     * valid Maki icon. If a letter is requested, it will be rendered uppercase only.
//     *
//     * @param label a valid alphanumeric value
//     * @return this builder for chaining options together
//     * @since 2.1.0
//     */
//    public abstract Builder label(@Nullable String label);
//
//    /**
//     * A packed color int, RRGGBB excluding the alpha value.
//     *
//     * @param color int denoting the marker icon color
//     * @return this builder for chaining options together
//     * @since 2.1.0
//     */
//    public abstract Builder color(@ColorInt Integer color);
//
//    /**
//     * Represents where the marker should be shown on the map.
//     *
//     * @param markerPoint a GeoJSON Point which denotes where the marker will be placed on the
//     *                    static map image
//     * @return this builder for chaining options together
//     * @since 2.1.0
//     */
//    public abstract Builder markerPoint(@NonNull Point markerPoint);
//
//    /**
//     * A url which may or may not be encoded, OkHttp takes care of encoding if it has not already
//     * been handled.
//     *
//     * @param url a string with the Image address URL being used for your marker
//     * @return this builder for chaining options together
//     * @since 3.0.0
//     */
//    public abstract Builder url(String url);
//
//    /**
//     * In order to make the returned images better cache-able on the client, you can set the
//     * precision in decimals instead of manually rounding the parameters.
//     *
//     * @param precision int number representing the precision for the formatter
//     * @return this builder for chaining options together
//     * @since 2.1.0
//     */
//    public abstract Builder precision(@IntRange(from = 0) int precision);
//
//    abstract Builder fullUrl(String fullUrl);
//
//    abstract StaticMarkerAnnotation autobuild();
//
//    /**
//     * This uses the provided parameters set using the {@link Builder} and first checks that all
//     * values are valid, formats the values as strings for easier consumption by the API, and lastly
//     * creates a new {@link StaticMarkerAnnotation} object which can be passed into the
//     * {@link MapboxStaticMap} request.
//     *
//     * @return a new instance of StaticMarkerAnnotation
//     * @since 2.1.0
//     */
//    public StaticMarkerAnnotation build() {
//      StaticMarkerAnnotation marker = autobuild();
//
//      String coordinates = String.format(Locale.US, "(%s,%s)",
//        TextUtils.formatCoordinate(marker.markerPoint().latitude(), marker.precision()),
//        TextUtils.formatCoordinate(marker.markerPoint().longitude(), marker.precision()));
//
//      String markerUrl;
//      if (marker.url() != null) {
//        markerUrl = String.format(Locale.US, "url-%s%s", marker.url(), coordinates);
//      } else {
//        if (marker.label() != null && marker.color() != null) {
//          markerUrl = String.format(Locale.US, "%s-%s+%d%s", marker.name(),
//            marker.label(), marker.color(), coordinates);
//        } else if (marker.label() != null && marker.color() == null) {
//          markerUrl = String.format(Locale.US, "%s-%s%s", marker.name(),
//            marker.label(), coordinates);
//        } else if (marker.label() == null && marker.color() != null) {
//          markerUrl = String.format(Locale.US, "%s-%d%s", marker.name(),
//            marker.color(), coordinates);
//        } else {
//          markerUrl = String.format(Locale.US, "%s-%s", marker.name(), coordinates);
//        }
//      }
//      return marker.toBuilder().fullUrl(markerUrl).autobuild();
//    }
//  }
//}
////
////    private static final String EMPTY = "";
////    private static final String HYPHEN_CHAR = "-";
////    private static final String PLUS_CHAR = "+";
////
////    private static final String NUMERIC_FROM_ZERO_TO_NINETYNINE_REGEX = "^(0|[1-9][0-9]{0,1})$";
////    private static final String ONE_ALPHABET_LETTER_REGEX = "^([a-zA-Z])$";
////
////
////    public StaticMarkerAnnotation build() {
////
////
////      if (!color.isEmpty()) {
////        String hexPattern = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
////        Pattern pattern = Pattern.compile(hexPattern);
////        Matcher matcher = pattern.matcher(color);
////        if (!matcher.matches()) {
////          throw new ServicesException("You need to pass 3- or 6-digit hexadecimal color code.");
////        }
////      }
////
////
////      if (!label.isEmpty()) {
////        boolean isANumber = true;
////        try {
////          Integer.parseInt(label);
////        } catch (NumberFormatException notANumber) {
////          isANumber = false;
////        }
////        if (isANumber) {
////          Pattern pattern = Pattern.compile(NUMERIC_FROM_ZERO_TO_NINETYNINE_REGEX);
////          Matcher matcher = pattern.matcher(label);
////          if (!matcher.matches()) {
////            throw new ServicesException("You need to pass an alphanumeric label [0-99] code.");
////          }
////        } else {
////          // TODO Find a better way to know when a label is not a valid Maki icon
////          // Right now, this is not verified
////          // At the moment there's no Maki icon name with 2 characters or less
////          if (label.length() < 3) {
////            Pattern pattern = Pattern.compile(ONE_ALPHABET_LETTER_REGEX);
////            Matcher matcher = pattern.matcher(label);
////            if (!matcher.matches()) {
////              throw new ServicesException("You need to pass an alphanumeric label [a-zA-Z] code.");
////            }
////          }
////        }
////      }
////
////      return new StaticMarkerAnnotation(this);
////    }
////  }
