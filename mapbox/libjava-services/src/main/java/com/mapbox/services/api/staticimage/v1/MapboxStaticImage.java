package com.mapbox.services.api.staticimage.v1;

import com.mapbox.services.Constants;
import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.staticimage.v1.models.StaticMarkerAnnotation;
import com.mapbox.services.api.staticimage.v1.models.StaticPolylineAnnotation;
import com.mapbox.services.commons.geojson.GeoJson;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;

/**
 * Static maps are standalone images that can be displayed in your mobile app without the aid of a
 * mapping library like he Mapbox Android SDK. They look like an embedded map without interactivity
 * or controls.
 *
 * @see <a href=https://www.mapbox.com/developers/api/styles/#Request.static.images.from.a.style>API Documentation</a>
 * @since 1.0.0
 */
public class MapboxStaticImage {

  private HttpUrl url;

  /**
   * Build the static image API URL using the builder.
   *
   * @param builder The MapboxStaticImage builder.
   * @since 1.0.0
   */
  public MapboxStaticImage(Builder builder) {
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
   * Static image builder used to customize the image, including location, image width/height,
   * and camera position.
   *
   * @since 1.0.0
   */
  public static class Builder extends MapboxBuilder {

    // Set defaults for optional fields
    private String accessToken;
    private String username = Constants.MAPBOX_USER;
    private String styleId;
    private Double lon;
    private Double lat;
    private Double zoom;
    private double bearing = 0;
    private double pitch = 0;
    private boolean auto;
    private String beforeLayer;
    private Integer width;
    private Integer height;
    private boolean retina = false;
    private boolean attribution = true;
    private boolean logo = true;
    private StaticMarkerAnnotation[] staticMarkerAnnotations;
    private StaticPolylineAnnotation[] staticPolylineAnnotations;
    private String geoJson;

    // This field isn't part of the URL
    private int precision = -1;

    /**
     * If a {@link GeoJson} object was provided, you can get the formatted version of it by calling this method.
     *
     * @return a formatted string ready to be added to the static image URL.
     * @since 2.1.0
     */
    public String getGeoJson() {
      return geoJson;
    }

    /**
     * Pass in a {@link GeoJson} object which contains geometry where you'd like your annotations to be placed.
     *
     * @param geoJson a single {@link GeoJson} object which includes at least one annotation.
     * @return This Static image builder.
     * @since 2.1.0
     */
    public Builder setGeoJson(GeoJson geoJson) {
      this.geoJson = String.format(Constants.DEFAULT_LOCALE, "geojson(%s)", geoJson.toJson());
      return this;
    }

    /**
     * Required to call when building {@link com.mapbox.services.api.staticimage.v1.MapboxStaticImage.Builder}.
     *
     * @param accessToken Mapbox access token, you must have a Mapbox account in order to use
     *                    this library.
     * @return Builder
     * @since 1.0.0
     */
    @Override
    public Builder setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    /**
     * Set the map style username. Typically will either be your Mapbox username or if you are
     * using one of the <a href=https://mapbox.com/api-documentation/#styles>defaults</a> Mapbox
     * styles, the username will be "mapbox".
     *
     * @param username You will need to pass in your Mapbox username.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setUsername(String username) {
      this.username = username;
      return this;
    }

    /**
     * You'll need to set what map style you'd like the static image to display.
     *
     * @param styleId can be one of the <a href=https://mapbox.com/api-documentation/#styles>defaults</a> or your own.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setStyleId(String styleId) {
      this.styleId = styleId;
      return this;
    }

    /**
     * Longitude for the center point of the static map.
     *
     * @param lon double number between -180 and 180.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setLon(double lon) {
      this.lon = lon;
      return this;
    }

    /**
     * Latitude for the center point of the static map.
     *
     * @param lat double number between -90 and 90.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setLat(double lat) {
      this.lat = lat;
      return this;
    }

    /**
     * Location for the center point of the static map.
     *
     * @param position Position object with valid latitude and longitude values
     * @return Builder
     * @since 1.2.0
     * @deprecated Use {@link MapboxStaticImage.Builder#setPosition(Position)} instead.
     */
    @Deprecated
    public Builder setLocation(Position position) {
      this.lat = position.getLatitude();
      this.lon = position.getLongitude();
      return this;
    }

    /**
     * Location for the center point of the static map.
     *
     * @param position Position object with valid latitude and longitude values
     * @return Builder
     * @since 2.1.0
     */
    public Builder setPosition(Position position) {
      this.lat = position.getLatitude();
      this.lon = position.getLongitude();
      return this;
    }

    /**
     * static map zoom level. Fractional zoom levels will be rounded to two decimal places.
     *
     * @param zoom double number between 0 and 22.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setZoom(double zoom) {
      this.zoom = zoom;
      return this;
    }

    /**
     * Optionally, bearing rotates the map around its center. A value of 90 rotates the map 90
     * to the left. 180 flips the map. Defaults is 0.
     *
     * @param bearing double number between 0 and 360, interpreted as decimal degrees.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setBearing(double bearing) {
      this.bearing = bearing;
      return this;
    }

    /**
     * Optionally, pitch tilts the map, producing a perspective effect. Defaults is 0.
     *
     * @param pitch double number between 0 and 60.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setPitch(double pitch) {
      this.pitch = pitch;
      return this;
    }

    /**
     * If auto is set to true, the viewport will fit the bounds of the overlay. Using this will replace any latitude or
     * longitude you provide.
     *
     * @param auto true if you'd like the viewport to be centered to display all map annotations, defaults false.
     * @return Builder
     * @since 2.1.0
     */
    public Builder setAuto(boolean auto) {
      this.auto = auto;
      return this;
    }

    /**
     * Determine if auto has been set for the static image camera positioning.
     *
     * @return boolean true if auto has been set, otherwise false.
     * @since 2.1.0
     */
    public boolean isAuto() {
      return auto;
    }

    /**
     * Width of the image.
     *
     * @param width int number between 1 and 1280.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setWidth(int width) {
      this.width = width;
      return this;
    }

    /**
     * Height of the image.
     *
     * @param height int number between 1 and 1280.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setHeight(int height) {
      this.height = height;
      return this;
    }

    /**
     * Optionally, use to request a retina 2x image that will be returned.
     *
     * @param retina true if you'd like a retina image.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setRetina(boolean retina) {
      this.retina = retina;
      return this;
    }

    /**
     * Optionally, control whether there is attribution on the image. Default is true.
     *
     * @param attribution true places attribution on image.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setAttribution(boolean attribution) {
      this.attribution = attribution;
      return this;
    }

    /**
     * Optionally, control whether there is a Mapbox logo on the image. Default is true.
     *
     * @param logo true places Mapbox logo on image.
     * @return Builder
     * @since 1.0.0
     */
    public Builder setLogo(boolean logo) {
      this.logo = logo;
      return this;
    }

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
     * String value for controlling where the overlay is inserted in the style. All overlays will be inserted before
     * this specified layer.
     *
     * @param beforeLayer A string representing the map layer you'd like to place your overlays below.
     * @return Builder
     * @since 2.1.0
     */
    public Builder setBeforeLayer(String beforeLayer) {
      this.beforeLayer = beforeLayer;
      return this;
    }

    /**
     * Provides the string being used to represent the layer you wish to place all your overlays below.
     *
     * @return a String representing the map styles layer.
     * @since 2.1.0
     */
    public String getBeforeLayer() {
      return beforeLayer;
    }

    /**
     * Get the access token
     *
     * @return String with the access token
     * @since 1.0.0
     */
    @Override
    public String getAccessToken() {
      return accessToken;
    }

    /**
     * Get the username you set within the builder. Typically your own Mapbox username or if you
     * used a <a href=https://mapbox.com/api-documentation/#styles>defaults</a> Mapbox style, it will be "mapbox".
     *
     * @return String with the username.
     * @since 1.0.0
     */
    public String getUsername() {
      return username;
    }

    /**
     * Get what map style your static image will display.
     *
     * @return String containing static map id.
     * @since 1.0.0
     */
    public String getStyleId() {
      return styleId;
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
     * Determine if the static image will contain attribution.
     *
     * @return true if attribution will be on static image.
     * @since 1.0.0
     */
    public boolean isAttribution() {
      return attribution;
    }

    /**
     * Determine if the static image will contain Mapbox logo.
     *
     * @return true if Mapbox logo will be on static image.
     * @since 1.0.0
     */
    public boolean isLogo() {
      return logo;
    }

    /**
     * @return int number representing the precision for the formatter
     * @since 1.0.0
     */
    public int getPrecision() {
      return precision;
    }

    public Builder setClientAppName(String appName) {
      super.clientAppName = appName;
      return this;
    }

    /**
     * Set the base url of the API.
     *
     * @param baseUrl base url used as end point
     * @return the current MapboxBuilder instance
     * @since 2.0.0
     */
    @Override
    public MapboxBuilder setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return this;
    }

    /**
     * Build the client when all user parameters have been set.
     *
     * @return MapboxStaticImage
     * @since 1.0.0
     */
    @Override
    public MapboxStaticImage build() throws ServicesException {
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

      return new MapboxStaticImage(this);
    }
  }
}
