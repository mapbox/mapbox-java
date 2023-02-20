package com.mapbox.api.geocoding.v6;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Geographic Feature Types.
 *
 * Various types of geographic features are available in the Mapbox geocoder.
 * Any type might appear as a top-level response, as context in a top-level response,
 * or as a filtering option using the types parameter.
 * Not all features are available or relevant in all parts of the world.
 * New types are occasionally added as necessary to correctly capture global administrative hierarchies.
 */
public class V6FeatureType {

    /**
     * Generally recognized countries or, in some cases like Hong Kong,
     * an area of quasi-national administrative status that has been given a designated country code under ISO 3166-1.
     */
    public static final String COUNTRY = "country";

    /**
     * Top-level sub-national administrative features, such as states in the United States or provinces in Canada or China.
     */
    public static final String REGION = "region";

    /**
     * Postal codes used in country-specific national addressing systems.
     */
    public static final String POSTCODE = "postcode";

    /**
     * Features that are smaller than top-level administrative features but typically larger than cities,
     * in countries that use such an additional layer in postal addressing (for example, prefectures in China).
     */
    public static final String DISTRICT = "district";

    /**
     * Typically these are cities, villages, municipalities, etc. They’re usually features used in postal addressing,
     * and are suitable for display in ambient end-user applications where current-location context is needed
     * (for example, in weather displays).
     */
    public static final String PLACE = "place";

    /**
     * Official sub-city features present in countries where such an additional administrative layer
     * is used in postal addressing, or where such features are commonly referred to in local parlance.
     * Examples include city districts in Brazil and Chile and arrondissements in France.
     */
    public static final String LOCALITY = "locality";

    /**
     * Colloquial sub-city features often referred to in local parlance. Unlike locality features,
     * these typically lack official status and may lack universally agreed-upon boundaries.
     */
    public static final String NEIGHBORHOOD = "neighborhood";

    /**
     * Street features which host one or more addresses.
     */
    public static final String STREET = "street";

    /**
     * Special feature type reserved for Japanese addresses.
     */
    public static final String BLOCK = "block";

    /**
     * Individual residential or business addresses.
     */
    public static final String ADDRESS = "address";

    private V6FeatureType() {
        // private constructor to prevent the class being instantiated
    }

    /**
     * Retention policy for the various filter result types.
     */
    @Retention(RetentionPolicy.CLASS)
    @StringDef( {
        COUNTRY,
        REGION,
        POSTCODE,
        DISTRICT,
        PLACE,
        LOCALITY,
        NEIGHBORHOOD,
        STREET,
        BLOCK,
        ADDRESS
    })
    public @interface V6FeatureTypeCriteria {
    }
}
