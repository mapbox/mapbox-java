package com.mapbox.services.geocoding.v5.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonio on 6/30/16.
 */
public class CarmenContext {

    private String id;
    private String text;

    @SerializedName("short_code") private String shortCode;
    private String wikidata;
    private String category;
    private String maki;

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    /**
     * ISO 3166-1 country and ISO 3166-2 region codes
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * The Wikidata identifier for a country, region or place
     */
    public String getWikidata() {
        return wikidata;
    }

    /**
     * Comma-separated list of categories applicable to a poi
     */
    public String getCategory() {
        return category;
    }

    /**
     * Suggested icon mapping from the most current version of the Maki project for a poi feature,
     * based on its  category
     */
    public String getMaki() {
        return maki;
    }
}
