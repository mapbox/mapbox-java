package com.mapbox.services.staticimage.v1;

import okhttp3.HttpUrl;

/**
 * Mapbox Static API
 * https://www.mapbox.com/developers/api/styles/#Request.static.images.from.a.style
 */
public class MapboxStaticImage {

    private HttpUrl url;

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
                .addQueryParameter("access_token", builder.accessToken);

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

    public HttpUrl getUrl() {
        return url;
    }

    /*
     * Builder
     */

    public static class Builder {

        // Set defaults for optional fields
        private String accessToken;
        private String username;
        private String styleId;
        private double lon;
        private double lat;
        private double zoom;
        private double bearing = 0;
        private double pitch = 0;
        private int width;
        private int height;
        private boolean retina = false;
        private boolean attribution = true;
        private boolean logo = true;

        /*
         * Setters
         */

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setStyleId(String styleId) {
            this.styleId = styleId;
            return this;
        }

        public Builder setLon(double lon) {
            this.lon = lon;
            return this;
        }

        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setZoom(double zoom) {
            this.zoom = zoom;
            return this;
        }

        public Builder setBearing(double bearing) {
            this.bearing = bearing;
            return this;
        }

        public Builder setPitch(double pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setRetina(boolean retina) {
            this.retina = retina;
            return this;
        }

        public Builder setAttribution(boolean attribution) {
            this.attribution = attribution;
            return this;
        }

        public Builder setLogo(boolean logo) {
            this.logo = logo;
            return this;
        }

        /*
         * Getters
         */

        public String getUsername() {
            return username;
        }

        public String getStyleId() {
            return styleId;
        }

        public String getLocationPathSegment() {
            return String.format("%f,%f,%f,%f,%f", lon, lat, zoom, bearing, pitch);
        }

        public String getSizePathSegment() {
            String retinaPath = retina ? "@2x" : "";
            return String.format("%dx%d%s", width, height, retinaPath);
        }

        public boolean isAttribution() {
            return attribution;
        }

        public boolean isLogo() {
            return logo;
        }

        /*
         * Build
         */

        public MapboxStaticImage build() {
            return new MapboxStaticImage(this);
        }

    }

}
