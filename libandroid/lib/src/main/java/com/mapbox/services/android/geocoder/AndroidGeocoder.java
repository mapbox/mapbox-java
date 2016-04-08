package com.mapbox.services.android.geocoder;

import android.content.Context;
import android.location.Address;

import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.geocoding.v5.models.GeocodingFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

/**
 * Created by antonio on 1/30/16.
 */
public class AndroidGeocoder {

    private final static String LOG_TAG = "MapboxGeocoding";

    private Context _context;
    private Locale _locale;

    private String _accessToken;

    /**
     * Constructs a Geocoder whose responses will be localized for the
     * given Locale.
     *
     * @param context the Context of the calling Activity
     * @param locale the desired Locale for the query results
     *
     * @throws NullPointerException if Locale is null
     */
    public AndroidGeocoder(Context context, Locale locale) {
        _context = context;
        _locale = locale;
    }

    /**
     * Constructs a Geocoder whose responses will be localized for the
     * default system Locale.
     *
     * @param context the Context of the calling Activity
     */
    public AndroidGeocoder(Context context) {
        _context = context;
    }

    /**
     * Returns true if the Geocoder methods getFromLocation and
     * getFromLocationName are implemented.  Lack of network
     * connectivity may still cause these methods to return null or
     * empty lists.
     */
    public static boolean isPresent() {
        return true;
    }

    /**
     * Returns an array of Addresses that are known to describe the
     * area immediately surrounding the given latitude and longitude.
     * The returned addresses will be localized for the locale
     * provided to this class's constructor.
     *
     * <p> The returned values may be obtained by means of a network lookup.
     * The results are a best guess and are not guaranteed to be meaningful or
     * correct. It may be useful to call this method from a thread separate from your
     * primary UI thread.
     *
     * @param latitude the latitude a point for the search
     * @param longitude the longitude a point for the search
     * @param maxResults max number of addresses to return. Smaller numbers (1 to 5) are recommended
     *
     * @return a list of Address objects. Returns null or empty list if no matches were
     * found or there is no backend service available.
     *
     * @throws IllegalArgumentException if latitude is
     * less than -90 or greater than 90
     * @throws IllegalArgumentException if longitude is
     * less than -180 or greater than 180
     * @throws IOException if the network is unavailable or any other
     * I/O problem occurs
     */
    public List<Address> getFromLocation(double latitude, double longitude, int maxResults)
            throws IOException, ServicesException {
        List<Address> addresses = new ArrayList<>();

        Position position = Position.fromCoordinates(longitude, latitude);
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken(_accessToken)
                .setCoordinates(position)
                .build();

        Response<GeocodingResponse> response = client.executeCall();
        if (!response.isSuccessful()) {
            return addresses;
        }

        List<GeocodingFeature> features = response.body().getFeatures();

        // Trim the list if needed
        if (features.size() > maxResults) {
            features = features.subList(0, maxResults);
        }

        // Convert from FeatureModel to Address
        for (GeocodingFeature feature: features) {
            addresses.add(GeocoderUtils.featureToAddress(feature, _locale));
        }

        return addresses;
    }

    /**
     * Returns an array of Addresses that are known to describe the
     * named location, which may be a place name such as "Dalvik,
     * Iceland", an address such as "1600 Amphitheatre Parkway,
     * Mountain View, CA", an airport code such as "SFO", etc..  The
     * returned addresses will be localized for the locale provided to
     * this class's constructor.
     *
     * <p> The query will block and returned values will be obtained by means of a network lookup.
     * The results are a best guess and are not guaranteed to be meaningful or
     * correct. It may be useful to call this method from a thread separate from your
     * primary UI thread.
     *
     * @param locationName a user-supplied description of a location
     * @param maxResults max number of results to return. Smaller numbers (1 to 5) are recommended
     *
     * @return a list of Address objects. Returns null or empty list if no matches were
     * found or there is no backend service available.
     *
     * @throws IllegalArgumentException if locationName is null
     * @throws IOException if the network is unavailable or any other
     * I/O problem occurs
     */
    public List<Address> getFromLocationName(String locationName, int maxResults) throws IOException, ServicesException {
        List<Address> addresses = new ArrayList<>();

        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken(_accessToken)
                .setLocation(locationName)
                .build();

        Response<GeocodingResponse> response = client.executeCall();
        if (!response.isSuccessful()) {
            return addresses;
        }

        List<GeocodingFeature> features = response.body().getFeatures();

        // Trim the list if needed
        if (features.size() > maxResults) {
            features = features.subList(0, maxResults);
        }

        // Convert from FeatureModel to Address
        for (GeocodingFeature feature: features) {
            addresses.add(GeocoderUtils.featureToAddress(feature, _locale));
        }

        return addresses;
    }

    /**
     * Returns an array of Addresses that are known to describe the
     * named location, which may be a place name such as "Dalvik,
     * Iceland", an address such as "1600 Amphitheatre Parkway,
     * Mountain View, CA", an airport code such as "SFO", etc..  The
     * returned addresses will be localized for the locale provided to
     * this class's constructor.
     *
     * <p> You may specify a bounding box for the search results by including
     * the Latitude and Longitude of the Lower Left point and Upper Right
     * point of the box.
     *
     * <p> The query will block and returned values will be obtained by means of a network lookup.
     * The results are a best guess and are not guaranteed to be meaningful or
     * correct. It may be useful to call this method from a thread separate from your
     * primary UI thread.
     *
     * @param locationName a user-supplied description of a location
     * @param maxResults max number of addresses to return. Smaller numbers (1 to 5) are recommended
     * @param lowerLeftLatitude the latitude of the lower left corner of the bounding box
     * @param lowerLeftLongitude the longitude of the lower left corner of the bounding box
     * @param upperRightLatitude the latitude of the upper right corner of the bounding box
     * @param upperRightLongitude the longitude of the upper right corner of the bounding box
     *
     * @return a list of Address objects. Returns null or empty list if no matches were
     * found or there is no backend service available.
     *
     * @throws IllegalArgumentException if locationName is null
     * @throws IllegalArgumentException if any latitude is
     * less than -90 or greater than 90
     * @throws IllegalArgumentException if any longitude is
     * less than -180 or greater than 180
     * @throws IOException if the network is unavailable or any other
     * I/O problem occurs
     */
    public List<Address> getFromLocationName(String locationName, int maxResults,
                                             double lowerLeftLatitude, double lowerLeftLongitude,
                                             double upperRightLatitude, double upperRightLongitude) throws IOException, ServicesException {
        List<Address> addresses = new ArrayList<>();

        // We use the bbox to infer a proximity location
        double proximityLatitude = (lowerLeftLatitude + upperRightLatitude) / 2.0;
        double proximityLongitude = (lowerLeftLongitude + upperRightLongitude) / 2.0;

        Position position = Position.fromCoordinates(proximityLongitude, proximityLatitude);
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken(_accessToken)
                .setLocation(locationName)
                .setProximity(position)
                .build();

        Response<GeocodingResponse> response = client.executeCall();
        if (!response.isSuccessful()) {
            return addresses;
        }

        List<GeocodingFeature> features = response.body().getFeatures();

        // Find results not contained within the bbox
        List<GeocodingFeature> featuresToRemove = new ArrayList<>();
        for (GeocodingFeature feature: features) {
            if (feature.getLatitude() < lowerLeftLatitude) {
                featuresToRemove.add(feature);
            } else if (feature.getLatitude() > upperRightLatitude) {
                featuresToRemove.add(feature);
            } else if (feature.getLongitude() < lowerLeftLongitude) {
                featuresToRemove.add(feature);
            } else if (feature.getLongitude() > upperRightLongitude) {
                featuresToRemove.add(feature);
            }
        }

        // Remove features outside the bbox
        if (featuresToRemove.size() > 0) {
            features.removeAll(featuresToRemove);
        }

        // Trim the list if needed
        if (features.size() > maxResults) {
            features = features.subList(0, maxResults);
        }

        // Convert from FeatureModel to Address
        for (GeocodingFeature feature: features) {
            addresses.add(GeocoderUtils.featureToAddress(feature, _locale));
        }

        return addresses;
    }

    /*
     * Extended API
     */

    public void setAccessToken(String accessToken) {
        _accessToken = accessToken;
    }

}
