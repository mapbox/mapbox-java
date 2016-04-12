package com.mapbox.services.geocoding.v5;

public final class GeocodingCriteria {

    /**
     * Default geocoding mode
     */
    public static final String DATASET_PLACES = "mapbox.places";

    /**
     * Geocoding mode for for enterprise/batch geocoding
     */
    public static final String DATASET_PLACES_PERMANENT = "mapbox.places-permanent";

    /**
     * Filter results by country
     */
    public static final String TYPE_COUNTRY = "country";

    /**
     * Filter results by region
     */
    public static final String TYPE_REGION = "region";

    /**
     * Filter results by postcode
     */
    public static final String TYPE_POSTCODE = "postcode";

    /**
     * Filter results by place
     */
    public static final String TYPE_PLACE = "place";

    /**
     * Filter results by neighborhood
     */
    public static final String TYPE_NEIGHBORHOOD = "neighborhood";

    /**
     * Filter results by address
     */
    public static final String TYPE_ADDRESS = "address";

    /**
     * Filter results by POI
     */
    public static final String TYPE_POI = "poi";

}
