package com.mapbox.services.android.geocoder;

import android.location.Address;

import com.mapbox.services.commons.models.Position;
import com.mapbox.services.geocoding.v5.models.CarmenFeature;

import java.util.Locale;

/**
 * Android Geocoder utils class used for the {@link AndroidGeocoder}.
 *
 * @since 1.0.0
 */
public class GeocoderUtils {

    /**
     * Used to convert from FeatureModel to an Address.
     *
     * @param geocodingFeature a {@link CarmenFeature}.
     * @param locale           {@link Locale}.
     * @return {@link Address}
     * @since 1.0.0
     */
    public static Address featureToAddress(CarmenFeature geocodingFeature, Locale locale) {
        Address address = new Address(locale);
        address.setAddressLine(0, geocodingFeature.getPlaceName());
        address.setFeatureName(geocodingFeature.getText());

        Position position = geocodingFeature.asPosition();
        address.setLongitude(position.getLongitude());
        address.setLatitude(position.getLatitude());

        return address;
    }
}
