package com.mapbox.services.staticimage.v1;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.MapboxBuilder;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Position;

import java.util.Locale;

import okhttp3.HttpUrl;

/**
 * Static maps are standalone images that can be displayed in your mobile app without the aid of a
 * mapping library like he Mapbox Android SDK. They look like an embedded map without interactivity
 * or controls.
 *
 * @see <a href=https://www.mapbox.com/developers/api/styles/#Request.static.images.from.a.style>API Documentation</a>
 */
public class MapboxStaticImage {

    private HttpUrl url;

    /**
     * Build the static image API URL using the builder.
     *
     * @param builder The MapboxStaticImage builder.
     */
    public MapboxStaticImage(Builder builder) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme("https")
                .host("api.mapbox.com")
                .addPathSegment("styles")
                .addPathSegment("v1")
                .addPathSegment(builder.getUsername())
                .addPathSegment(builder.getStyleId())
                .addPathSegment("static")
                .addPathSegment(builder.getLocationPathSegment())
                .addPathSegment(builder.getSizePathSegment())
                .addQueryParameter("access_token", builder.getAccessToken());

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
     */
    public HttpUrl getUrl() {
        return url;
    }

    /**
     * Static image builder used to customize the image, including location, image width/height,
     * and camera position.
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
        private Integer width;
        private Integer height;
        private boolean retina = false;
        private boolean attribution = true;
        private boolean logo = true;

        /**
         * Required to call when building {@link com.mapbox.services.staticimage.v1.MapboxStaticImage.Builder}.
         *
         * @param accessToken Mapbox access token, you must have a Mapbox account in order to use
         *                    this library.
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
         */
        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        /**
         * You'll need to set what map style you'd like the static image to display.
         *
         * @param styleId can be one of the <a href=https://mapbox.com/api-documentation/#styles>defaults</a> or your own.
         */
        public Builder setStyleId(String styleId) {
            this.styleId = styleId;
            return this;
        }

        /**
         * Longitude for the center point of the static map.
         *
         * @param lon double number between -180 and 180.
         */
        public Builder setLon(double lon) {
            this.lon = lon;
            return this;
        }

        /**
         * Latitude for the center point of the static map.
         *
         * @param lat double number between -90 and 90.
         */
        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        /**
         * Location for the center point of the static map.
         *
         * @param position Position object with valid latitude and longitude values
         */
        public Builder setLocation(Position position) {
            this.lat = position.getLatitude();
            this.lon = position.getLongitude();
            return this;
        }

        /**
         * static map zoom level. Fractional zoom levels will be rounded to two decimal places.
         *
         * @param zoom double number between 0 and 22.
         */
        public Builder setZoom(double zoom) {
            this.zoom = zoom;
            return this;
        }

        /**
         * Optionally, bearing rotates the map around its center. A value of 90 rotates the map 90°
         * to the left. 180 flips the map. Defaults is 0.
         *
         * @param bearing double number between 0 and 360, interpreted as decimal degrees.
         */
        public Builder setBearing(double bearing) {
            this.bearing = bearing;
            return this;
        }

        /**
         * Optionally, pitch tilts the map, producing a perspective effect. Defaults is 0.
         *
         * @param pitch double number between 0 and 60.
         */
        public Builder setPitch(double pitch) {
            this.pitch = pitch;
            return this;
        }

        /**
         * width of the image.
         *
         * @param width double number between 1 and 1280.
         */
        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        /**
         * height of the image.
         *
         * @param height double number between 1 and 1280.
         */
        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        /**
         * Optionally, use to request a retina 2x image that will be returned.
         *
         * @param retina true if you'd like a retina image.
         */
        public Builder setRetina(boolean retina) {
            this.retina = retina;
            return this;
        }

        /**
         * Optionally, control whether there is attribution on the image. Default is true.
         *
         * @param attribution true places attribution on image.
         */
        public Builder setAttribution(boolean attribution) {
            this.attribution = attribution;
            return this;
        }

        /**
         * Optionally, control whether there is a Mapbox logo on the image. Default is true.
         *
         * @param logo true places Mapbox logo on image.
         */
        public Builder setLogo(boolean logo) {
            this.logo = logo;
            return this;
        }

        /**
         * Get the access token
         *
         * @return String with the access token
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
         */
        public String getUsername() {
            return username;
        }

        /**
         * Get what map style your static image will display.
         *
         * @return String containing static map id.
         */
        public String getStyleId() {
            return styleId;
        }

        /**
         * Gives location information about the static image including longitude, latitude, zoom,
         * bearing, and pitch.
         *
         * @return String value with static image location information.
         */
        public String getLocationPathSegment() {
            return String.format(Locale.US, "%f,%f,%f,%f,%f", lon, lat, zoom, bearing, pitch);
        }

        /**
         * Gives the height and width of the static image.
         *
         * @return String giving width, height and retina.
         */
        public String getSizePathSegment() {
            String retinaPath = retina ? "@2x" : "";
            return String.format(Locale.US, "%dx%d%s", width, height, retinaPath);
        }

        /**
         * Determine if the static image will contain attribution.
         *
         * @return true if attribution will be on static image.
         */
        public boolean isAttribution() {
            return attribution;
        }

        /**
         * Determine if the static image will contain Mapbox logo.
         *
         * @return true if Mapbox logo will be on static image.
         */
        public boolean isLogo() {
            return logo;
        }

        /**
         * Build the client when all user parameters have been set.
         */
        @Override
        public MapboxStaticImage build() throws ServicesException {
            validateAccessToken(accessToken);

            if (styleId == null || styleId.isEmpty()) {
                throw new ServicesException("You need to set a map style.");
            }

            if (lon == null || lat == null) {
                throw new ServicesException("You need to set the map lon/lat coordinates.");
            }

            if (zoom == null) {
                throw new ServicesException("You need to set the map zoom level.");
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
