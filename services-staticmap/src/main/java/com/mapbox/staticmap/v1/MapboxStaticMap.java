package com.mapbox.staticmap.v1;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Point;
import com.mapbox.services.Constants;
import com.mapbox.services.exceptions.ServicesException;
import com.mapbox.staticmap.v1.models.StaticMarkerAnnotation;
import com.mapbox.staticmap.v1.models.StaticPolylineAnnotation;

import java.util.ArrayList;
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

  private HttpUrl url;

  /**
   * Build the static image API URL using the builder.
   *
   * @param builder The MapboxStaticMap builder.
   * @since 1.0.0
   */
  public MapboxStaticMap(Builder builder) {
    HttpUrl.Builder urlBuilder = HttpUrl.parse(builder.getBaseUrl()).newBuilder()
      .addPathSegment("styles")
      .addPathSegment("v1")
      .addPathSegment(builder.getUsername())
      .addPathSegment(builder.getStyleId())
      .addPathSegment("static")
      .addQueryParameter("access_token", builder.getAccessToken());

    if (builder.getOverlays() != null) {
      urlBuilder.addEncodedPathSegment(builder.getOverlays());
    }

    if (builder.isAuto()) {
      urlBuilder.addPathSegment("auto");
    } else {
      urlBuilder.addPathSegment(builder.getLocationPathSegment());
    }

    // Has to be last segment in URL
    urlBuilder.addPathSegment(builder.getSizePathSegment());

    if (builder.beforeLayer != null) {
      urlBuilder.addQueryParameter("before_layer", builder.beforeLayer);
    }

    if (!builder.isAttribution()) {
      // Default is true
      urlBuilder.addQueryParameter("attribution", "false");
    }

    if (!builder.isLogo()) {
      // Default is true
      urlBuilder.addQueryParameter("logo", "false");
    }

    url = urlBuilder.build();
  }

  /**
   * If you need the API URL you can request it with this method.
   *
   * @return the built API URL.
   * @since 1.0.0
   */
  public HttpUrl getUrl() {
    return url;
  }


  /**
   * Build a new {@link MapboxStaticMap} object with the initial values set for
   * {@link #baseUrl()}, {@link #profile()}, {@link #user()}, and {@link #geometries()}.
   *
   * @return a {@link Builder} object for creating this object
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxDirections.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
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
    public abstract Builder styleId(@Nullable String styleId);

    /**
     * Optionally, control whether there is a Mapbox logo on the image. Default is true. Check that
     * the current Mapbox plan you are under allows for hiding the Mapbox Logo from the mao.
     *
     * @param logo true places Mapbox logo on image
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder logo(@Nullable Boolean logo);

    /**
     * Optionally, control whether there is attribution on the image. Default is true. Check that
     * the current Mapbox plan you are under allows for hiding the Mapbox Logo from the mao.
     *
     * @param attribution true places attribution on image
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder attribution(@Nullable Boolean attribution);

    /**
     * Enhance your image by toggling retina to true. This will double the amount of pixels the
     * returning image will have.
     *
     * @param retina true if the desired image being returned should contain double pixels
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder retina(@Nullable Boolean retina);

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
    public abstract Builder cameraZoom(@FloatRange(from = 0, to = 22) @Nullable Double cameraZoom);

    /**
     * Optionally, bearing rotates the map around its center defined point given in
     * {@link #cameraPoint(Point)}. A value of 90 rotates the map 90 to the left. 180 flips the map.
     * Defaults is 0.
     *
     * @param cameraBearing double number between 0 and 360, interpreted as decimal degrees
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder cameraBearing(@Nullable Double cameraBearing);

    /**
     * Optionally, pitch tilts the map, producing a perspective effect. Defaults is 0.
     *
     * @param cameraPitch double number between 0 and 60
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder cameraPitch(@FloatRange(from = 0, to = 60)
                                        @Nullable Double cameraPitch);

    /**
     * If auto is set to true, the viewport will fit the bounds of the overlay. Using this will
     * replace any latitude or longitude you provide.
     *
     * @param auto true if you'd like the viewport to be centered to display all map annotations,
     *             defaults false
     * @return this builder for chaining options together
     * @since 2.1.0
     */
    public abstract Builder cameraAuto(Boolean auto);

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
    public abstract Builder width(@IntRange(from = 1, to = 1280) @Nullable Integer width);

    /**
     * Height of the image.
     *
     * @param height int number between 1 and 1280.
     * @return Builder
     * @since 1.0.0
     */
    public abstract Builder height(@IntRange(from = 1, to = 1280) @Nullable Integer height);

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
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder clientAppName(@NonNull String clientAppName);





    public abstract Builder staticMarkerAnnotations(
      @Nullable List<StaticMarkerAnnotation> staticMarkerAnnotations);

    public abstract Builder staticPolylineAnnotations(
      @Nullable List<StaticPolylineAnnotation> staticPolylineAnnotations);


    // This field isn't part of the URL
    private int precision = -1;


    /**
     * In order to make the returned images better cacheable on the client, you can set the
     * precision in decimals instead of manually rounding the parameters.
     *
     * @param precision int number representing the precision for the formatter
     * @return Builder
     * @since 1.0.0
     */
    public Builder setPrecision(int precision) {
      this.precision = precision;
      return this;
    }



    /**
     * Gives location information about the static image including longitude, latitude, zoom,
     * bearing, and pitch.
     *
     * @return String value with static image location information.
     * @since 1.0.0
     */
    public String getLocationPathSegment() {
      if (precision > 0) {
        return String.format(Locale.US, "%s,%s,%s,%s,%s",
          TextUtils.formatCoordinate(lon, precision),
          TextUtils.formatCoordinate(lat, precision),
          TextUtils.formatCoordinate(zoom, precision),
          TextUtils.formatCoordinate(bearing, precision),
          TextUtils.formatCoordinate(pitch, precision));
      } else {
        return String.format(Locale.US, "%f,%f,%f,%f,%f", lon, lat, zoom, bearing, pitch);
      }
    }

    /**
     * Gives the height and width of the static image.
     *
     * @return String giving width, height and retina.
     * @since 1.0.0
     */
    public String getSizePathSegment() {
      String retinaPath = retina ? "@2x" : "";
      return String.format(Locale.US, "%dx%d%s", width, height, retinaPath);
    }

    /**
     * Allows the passing of a {@link StaticMarkerAnnotation} or Markers which are placed on the static map image
     * returned.
     *
     * @param staticMarkerAnnotations One or more {@link StaticMarkerAnnotation}s to be placed on your static image.
     * @return This static image builder.
     * @since 2.1.0
     */
    public Builder setStaticMarkerAnnotations(StaticMarkerAnnotation... staticMarkerAnnotations) {
      this.staticMarkerAnnotations = staticMarkerAnnotations;
      return this;
    }

    /**
     * Allows the passing of a {@link StaticPolylineAnnotation} or Polylines which are placed on the static map image
     * returned.
     *
     * @param staticPolylineAnnotations One or more {@link StaticPolylineAnnotation}s to be placed on your static image.
     * @return This static image builder.
     * @since 2.1.0
     */
    public Builder setStaticPolylineAnnotations(StaticPolylineAnnotation... staticPolylineAnnotations) {
      this.staticPolylineAnnotations = staticPolylineAnnotations;
      return this;
    }

    /**
     * Provides a String containing all of the currently added overlays added to the static image builder.
     *
     * @return the formatted string which will be used to pass in any overlays to be placed on the static image.
     * @since 2.1.0
     */
    public String getOverlays() {
      List<String> formattedOverlays = new ArrayList<>();
      if (staticMarkerAnnotations != null) {
        for (StaticMarkerAnnotation staticMarkerAnnotation : staticMarkerAnnotations) {
          formattedOverlays.add(staticMarkerAnnotation.getMarker());
        }
      }
      if (staticPolylineAnnotations != null) {
        for (StaticPolylineAnnotation staticPolylineAnnotation : staticPolylineAnnotations) {
          formattedOverlays.add(staticPolylineAnnotation.getPath());
        }
      }

      if (geoJson != null) {
        formattedOverlays.add(geoJson);
      }

      return TextUtils.join(",", formattedOverlays.toArray());
    }



    /**
     * @return int number representing the precision for the formatter
     * @since 1.0.0
     */
    public int getPrecision() {
      return precision;
    }


    @Override
    public MapboxStaticMap build() throws ServicesException {
      validateAccessToken(accessToken);

      if (styleId == null || styleId.isEmpty()) {
        throw new ServicesException("You need to set a map style.");
      }

      if ((lon == null || lat == null) && !auto) {
        throw new ServicesException("You need to set the map lon/lat coordinates or set auto to true.");
      }

      if (zoom != null) {
        if (zoom < 0 || zoom > 20) {
          throw new ServicesException("The zoom level provided must be a value between 0 and 20.");
        }
      }

      if (width == null || width < 1 || width > 1280) {
        throw new ServicesException(
          "You need to set a valid image width (between 1 and 1280).");
      }

      if (height == null || height < 1 || height > 1280) {
        throw new ServicesException(
          "You need to set a valid image height (between 1 and 1280).");
      }

      return new MapboxStaticMap(this);
    }
  }
}
