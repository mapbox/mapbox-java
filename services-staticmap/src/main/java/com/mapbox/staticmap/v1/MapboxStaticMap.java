package com.mapbox.staticmap.v1;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Point;
import com.mapbox.services.constants.Constants;
import com.mapbox.services.exceptions.ServicesException;
import com.mapbox.services.utils.MapboxUtils;
import com.mapbox.services.utils.TextUtils;
import com.mapbox.staticmap.v1.models.StaticMarkerAnnotation;
import com.mapbox.staticmap.v1.models.StaticPolylineAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;

/**
 * Static maps are standalone images that can be displayed in your mobile app without the aid of a
 * mapping library like the Mapbox Android SDK. They look like an embedded map without interactivity
 * or controls. the returned image can be a raster tile ans pulls from any map style in the Mapbox
 * Style Specification.
 * <p>
 * This class helps make a valid request and gets the information correctly formatted for Picaso or
 * Glide libraries which help download the image and place it into an Image View.
 *
 * @see <a href=https://www.mapbox.com/api-documentation/#static>API Documentation</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class MapboxStaticMap {

  @Nullable
  abstract String accessToken();

  @NonNull
  abstract String baseUrl();

  @NonNull
  abstract String user();

  @NonNull
  abstract String styleId();

  abstract boolean logo();

  abstract boolean attribution();

  abstract boolean retina();

  @NonNull
  abstract Point cameraPoint();

  abstract double cameraZoom();

  abstract double cameraBearing();

  abstract double cameraPitch();

  abstract boolean cameraAuto();

  @Nullable
  abstract String beforeLayer();

  abstract int width();

  abstract int height();

  @Nullable
  abstract GeoJson geoJson();

  @Nullable
  abstract List<StaticMarkerAnnotation> staticMarkerAnnotations();

  @Nullable
  abstract List<StaticPolylineAnnotation> staticPolylineAnnotations();

  abstract int precision();

  /**
   * Returns the formatted URL string meant to be passed to your Http client for retrieval of the
   * actual Mapbox Static Image.
   *
   * @return a {@link HttpUrl} which can be used for making the request for the image
   * @since 3.0.0
   */
  public HttpUrl url() {
    HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl()).newBuilder()
      .addPathSegment("styles")
      .addPathSegment("v1")
      .addPathSegment(user())
      .addPathSegment(styleId())
      .addPathSegment("static")
      .addQueryParameter("access_token", accessToken());

    List<String> annotations = new ArrayList<>();
    if (staticMarkerAnnotations() != null) {
      String[] markerStringArray = new String[staticMarkerAnnotations().size()];
      for (StaticMarkerAnnotation marker : staticMarkerAnnotations()) {
        markerStringArray[staticMarkerAnnotations().indexOf(marker)] = marker.url();
      }
      annotations.addAll(Arrays.asList(markerStringArray));
    }

    if (staticPolylineAnnotations() != null) {
      String[] polylineStringArray = new String[staticPolylineAnnotations().size()];
      for (StaticPolylineAnnotation polyline : staticPolylineAnnotations()) {
        polylineStringArray[staticPolylineAnnotations().indexOf(polyline)] = polyline.url();
      }
      annotations.addAll(Arrays.asList(polylineStringArray));
    }

    if (geoJson() != null) {
      annotations.add(String.format(Locale.US, "geojson(%s)", geoJson().toJson()));
    }

    if (annotations.size() > 0) {
      urlBuilder.addPathSegment(TextUtils.join(",", annotations.toArray()));
    }

    urlBuilder.addPathSegment(cameraAuto() ? StaticMapCriteria.CAMERA_AUTO
      : generateLocationPathSegment());

    if (beforeLayer() != null) {
      urlBuilder.addQueryParameter(StaticMapCriteria.BEFORE_LAYER, beforeLayer());
    }
    if (!attribution()) {
      urlBuilder.addQueryParameter("attribution", "false");
    }
    if (!logo()) {
      urlBuilder.addQueryParameter("logo", "false");
    }

    // Has to be last segment in URL
    urlBuilder.addPathSegment(generateSizePathSegment());
    return urlBuilder.build();

  }

  private String generateLocationPathSegment() {
    if (precision() > 0) {
      List<String> geoInfo = new ArrayList<>();
      geoInfo.add(TextUtils.formatCoordinate(cameraPoint().longitude(), precision()));
      geoInfo.add(TextUtils.formatCoordinate(cameraPoint().latitude(), precision()));
      geoInfo.add(TextUtils.formatCoordinate(cameraZoom(), precision()));
      geoInfo.add(TextUtils.formatCoordinate(cameraBearing(), precision()));
      geoInfo.add(TextUtils.formatCoordinate(cameraPitch(), precision()));
      return TextUtils.join(",", geoInfo.toArray());
    } else {
      return String.format(Locale.US, "%f,%f,%f,%f,%f", cameraPoint().longitude(),
        cameraPoint().latitude(), cameraZoom(), cameraBearing(), cameraPitch());
    }
  }

  private String generateSizePathSegment() {
    return String.format(Locale.US, "%dx%d%s", width(), height(), retina() ? "@2x" : "");
  }

  /**
   * Build a new {@link MapboxStaticMap} object with the initial values set for
   * {@link #baseUrl()}, {@link #user()}, {@link #attribution()}, {@link #styleId()},
   * and {@link #retina()}.
   *
   * @return a {@link Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxStaticMap.Builder()
      .styleId(StaticMapCriteria.STREET_STYLE)
      .baseUrl(Constants.BASE_API_URL)
      .user(Constants.MAPBOX_USER)
      .cameraPoint(Point.fromLngLat(0d,0d))
      .cameraAuto(false)
      .attribution(true)
      .width(250)
      .logo(true)
      .attribution(true)
      .retina(true)
      .height(250)
      .cameraZoom(0)
      .cameraPitch(0)
      .cameraBearing(0)
      .precision(0)
      .retina(false);
  }

  /**
   * Static image builder used to customize the image, including location, image width/height,
   * and camera position.
   *
   * @since 1.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    the Optimization API
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * The username for the account that the directions engine runs on. In most cases, this should
     * always remain the default value of {@link Constants#MAPBOX_USER}.
     *
     * @param user a non-null string which will replace the default user used in the directions
     *             request
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder user(@NonNull String user);

    /**
     * The returning map images style, which can be one of the provided Mapbox Styles or a custom
     * style made inside Mapbox Studio. Passing null will revert to using the default map Street
     * style.
     *
     * @param styleId either one of the styles defined inside {@link StaticMapCriteria} or a custom
     *                url pointing to a styled map made in Mapbox Studio
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder styleId(@NonNull String styleId);

    /**
     * Optionally, control whether there is a Mapbox logo on the image. Default is true. Check that
     * the current Mapbox plan you are under allows for hiding the Mapbox Logo from the mao.
     *
     * @param logo true places Mapbox logo on image
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder logo(boolean logo);

    /**
     * Optionally, control whether there is attribution on the image. Default is true. Check that
     * the current Mapbox plan you are under allows for hiding the Mapbox Logo from the mao.
     *
     * @param attribution true places attribution on image
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder attribution(boolean attribution);

    /**
     * Enhance your image by toggling retina to true. This will double the amount of pixels the
     * returning image will have.
     *
     * @param retina true if the desired image being returned should contain double pixels
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder retina(boolean retina);

    /**
     * Center point where the camera will be focused on.
     *
     * @param cameraPoint a GeoJSON Point object which defines the cameras center position
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder cameraPoint(@Nullable Point cameraPoint);

    /**
     * Static maps camera zoom level. This can be though of as how far away the camera is from the
     * subject (earth) thus a zoom of 0 will display the entire world vs zoom 16 which is street\
     * level zoom level. Fractional zoom levels will be rounded to two decimal places.
     *
     * @param cameraZoom double number between 0 and 22
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder cameraZoom(@FloatRange(from = 0, to = 22) double cameraZoom);

    /**
     * Optionally, bearing rotates the map around its center defined point given in
     * {@link #cameraPoint(Point)}. A value of 90 rotates the map 90 to the left. 180 flips the map.
     * Defaults is 0.
     *
     * @param cameraBearing double number between 0 and 360, interpreted as decimal degrees
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder cameraBearing(@FloatRange(from = 0, to = 360) double cameraBearing);

    /**
     * Optionally, pitch tilts the map, producing a perspective effect. Defaults is 0.
     *
     * @param cameraPitch double number between 0 and 60
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder cameraPitch(@FloatRange(from = 0, to = 60) double cameraPitch);

    /**
     * If auto is set to true, the viewport will fit the bounds of the overlay. Using this will
     * replace any latitude or longitude you provide.
     *
     * @param auto true if you'd like the viewport to be centered to display all map annotations,
     *             defaults false
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder cameraAuto(boolean auto);

    /**
     * String value for controlling where the overlay is inserted in the style. All overlays will be
     * inserted before this specified layer.
     *
     * @param beforeLayer s string representing the map layer you'd like to place your overlays
     *                    below.
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder beforeLayer(@Nullable String beforeLayer);

    /**
     * Width of the image.
     *
     * @param width int number between 1 and 1280.
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder width(@IntRange(from = 1, to = 1280) int width);

    /**
     * Height of the image.
     *
     * @param height int number between 1 and 1280.
     * @return Builder
     * @since 1.0.0
     */
    public abstract Builder height(@IntRange(from = 1, to = 1280) int height);

    /**
     * GeoJSON object which represents a specific annotation which will be placed on the static map.
     * The GeoJSON must be value.
     *
     * @param geoJson a formatted string ready to be added to the stiatic map image URL
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder geoJson(@Nullable GeoJson geoJson);

    /**
     * Optionally provide a list of marker annotations which can be placed on the static map image
     * during the rendering process.
     *
     * @param staticMarkerAnnotations a list made up of {@link StaticMarkerAnnotation} objects
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder staticMarkerAnnotations(
      @Nullable List<StaticMarkerAnnotation> staticMarkerAnnotations);

    /**
     * Optionally provide a list of polyline annotations which can be placed on the static map image
     * during the rendering process.
     *
     * @param staticPolylineAnnotations a list made up of {@link StaticPolylineAnnotation} objects
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder staticPolylineAnnotations(
      @Nullable List<StaticPolylineAnnotation> staticPolylineAnnotations);

    /**
     * In order to make the returned image better cache-able on the client, you can set the
     * precision in decimals instead of manually round the parameters.
     *
     * @param precision integer value greater than zero which represents the decimal precision of
     *                  coordinate values
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder precision(@IntRange(from = 0) int precision);

    abstract MapboxStaticMap autoBuild();

    public MapboxStaticMap build() {
      MapboxStaticMap staticMap = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(staticMap.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access"
          + " token.");
      }


      if (staticMap.styleId() == null || staticMap.styleId().isEmpty()) {
        throw new ServicesException("You need to set a map style.");
      }
      return staticMap;
    }
  }
}

