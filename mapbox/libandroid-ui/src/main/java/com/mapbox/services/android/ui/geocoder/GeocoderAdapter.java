package com.mapbox.services.android.ui.geocoder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mapbox.services.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Adapter for the {@link GeocoderAutoCompleteView}. In this class we make the Mapbox Geocoding API
 * call.
 *
 * @since 1.0.0
 */
public class GeocoderAdapter extends BaseAdapter implements Filterable {

  private final Context context;
  private String baseUrl;
  private String accessToken;
  private String country;
  private String[] countries;
  private String type;
  private String[] types;
  private double[] bbox;
  private Position position;
  private int limit;
  private String language;
  private Call call;

  private GeocoderFilter geocoderFilter;

  private List<CarmenFeature> features;

  public GeocoderAdapter(Context context) {
    this.context = context;
  }

  /*
   * Getters and setters
   */

  /**
   * Get the base url of the API.
   *
   * @return the base url used as endpoint.
   * @since 2.0.0
   */
  public String getBaseUrl() {
    return baseUrl;
  }

  /**
   * Set the base url of the API.
   *
   * @param baseUrl base url used as end point
   * @since 2.0.0
   */
  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Get the access token used with making the Mapbox geocoding API call.
   *
   * @return String containing your Mapbox access token.
   * @see <a href="https://www.mapbox.com/help/define-access-token/">Mapbox access token</a>
   * @since 1.0.0
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * You'll need to have a Mapbox access token to use the geocoding API within MAS.
   *
   * @param accessToken Your Mapbox access token
   * @see <a href="https://www.mapbox.com/help/define-access-token/">Mapbox access token</a>
   * @since 1.0.0
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }


  /**
   * Get the country you are limiting your geocoding results if applicable.
   *
   * @return <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166 alpha 2</a>
   * country code
   * @since 1.3.0
   */
  public String getCountry() {
    return country;
  }

  /**
   * Parameter limits results to a country. Use one of the
   * <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166 alpha 2</a> country codes.
   *
   * @param country String matching country code.
   * @since 1.3.0
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Get the countries you are limiting your geocoding results if applicable.
   *
   * @return <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166 alpha 2</a>
   * country code inside a String array.
   * @since 2.0.0
   */
  public String[] getCountries() {
    return countries;
  }

  /**
   * Parameter limits results to a set of one or more countries. Use one or more of the
   * <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166 alpha 2</a> country codes,
   * separated by commas inside a String array.
   *
   * @param countries String array containing the country codes you want to limit results to.
   * @since 2.0.0
   */
  public void setCountries(String[] countries) {
    this.countries = countries;
  }

  /**
   * Get the geocoder filter type.
   *
   * @return String containing "place", "poi", "neighborhood", etc.
   * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Geocoding API documentation</a>
   * @since 1.0.0
   */
  public String getType() {
    return type;
  }

  /**
   * Get the geocder filter types in a String array.
   *
   * @return String containing "place", "poi", "neighborhood", etc.
   * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Geocoding API documentation</a>
   * @since 2.0.0
   */
  public String[] getTypes() {
    return types;
  }

  /**
   * Configure the geocoder type, pass in one of the constants found within
   * {@link com.mapbox.services.api.geocoding.v5.GeocodingCriteria}.
   *
   * @param type String containing "place", "poi", "neighborhood", etc.
   * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Geocoding API documentation</a>
   * @since 1.0.0
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Configure the geocoder type, pass in one or more of the constants found within
   * {@link com.mapbox.services.api.geocoding.v5.GeocodingCriteria}.
   *
   * @param types String array containing "place", "poi", "neighborhood", etc.
   * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Geocoding API documentation</a>
   * @since 2.0.0
   */
  public void setTypes(String[] types) {
    this.types = types;
  }

  /**
   * Bounding box within which to limit results
   *
   * @return double array containing minX, minY, maxX, maxY
   * @since 1.3.0
   */
  public double[] getBbox() {
    return bbox;
  }

  /**
   * Bounding box within which to limit results.
   *
   * @param northeast The top right hand corner of your bounding box when the map is pointed north.
   * @param southwest The bottom left hand corner of your bounding box when the map is pointed north.
   * @since 1.3.0
   */
  public void setBbox(Position northeast, Position southwest) {
    setBbox(southwest.getLongitude(), southwest.getLatitude(),
      northeast.getLongitude(), northeast.getLatitude());
  }

  /**
   * Bounding box within which to limit results.
   *
   * @param minX Bottom of bounding box when map is pointed north.
   * @param minY Left of bounding box when map is pointed north.
   * @param maxX Top of bounding box when map is pointed north.
   * @param maxY Right of bounding box when map is pointed north.
   * @since 1.3.0
   */
  public void setBbox(double minX, double minY, double maxX, double maxY) {
    if (bbox == null) {
      bbox = new double[4];
    }
    bbox[0] = minX;
    bbox[1] = minY;
    bbox[2] = maxX;
    bbox[3] = maxY;
  }

  /**
   * Location around which to bias geocoder results.
   *
   * @return {@link Position} coordinate.
   * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Geocoding API documentation</a>
   * @since 1.0.0
   */
  public Position getProximity() {
    return position;
  }

  /**
   * Location around which to bias geocoder results.
   *
   * @param position {@link Position} coordinate.
   * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Geocoding API documentation</a>
   * @since 1.0.0
   */
  public void setProximity(Position position) {
    this.position = position;
  }

  /**
   * Returns integer number representing the amount of results
   *
   * @return integer value
   * @since 2.0.0
   */
  public int getLimit() {
    return limit;
  }

  /**
   * Limit the number of results returned. The default is 5.
   *
   * @param limit the integer value representing the amount of results desired.
   * @since 2.0.0
   */
  public void setLimit(int limit) {
    this.limit = limit;
  }

  /**
   * @return The locale in which results should be returned.
   * @since 2.0.0
   */
  public String getLanguage() {
    return language;
  }

  /**
   * The locale in which results should be returned.
   * <p>
   * This property affects the language of returned results; generally speaking,
   * it does not determine which results are found. If the Geocoding API does not
   * recognize the language code, it may fall back to another language or the default
   * language. Components other than the language code, such as the country and
   * script codes, are ignored.
   * <p>
   * By default, this property is set to `null`, causing results to be in the default
   * language.
   * <p>
   * This option is experimental.
   *
   * @param language The locale in which results should be returned.
   * @since 2.0.0
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Can be used to cancel any calls currently in progress. It's a good idea to include in onDestroy() to prevent
   * memory leaks
   *
   * @since 2.0.0
   */
  public void cancelApiCall() {
    if (call != null) {
      call.cancel();
    }
  }

  /*
   * Required by BaseAdapter
   */

  /**
   * Gives How many items are in the data set represented by this Adapter.
   *
   * @return int value.
   * @see <a href="https://developer.android.com/reference/android/widget/Adapter.html">Android Adapter</a>
   * @since 1.0.0
   */
  @Override
  public int getCount() {
    return features.size();
  }

  /**
   * Get the data item associated with the specified position in the data set.
   *
   * @param position int position within the data.
   * @return {@link CarmenFeature}.
   * @see <a href="https://developer.android.com/reference/android/widget/Adapter.html">Android Adapter</a>
   * @since 1.0.0
   */
  @Override
  public CarmenFeature getItem(int position) {
    return features.get(position);
  }

  /**
   * Get the row id associated with the specified position in the list.
   *
   * @param position int position within the data.
   * @return long value
   * @see <a href="https://developer.android.com/reference/android/widget/Adapter.html">Android Adapter</a>
   * @since 1.0.0
   */
  @Override
  public long getItemId(int position) {
    return position;
  }

  /**
   * Get a View that displays the data at the specified position in the data set.
   *
   * @param position    The position of the item within the adapter's data set of the item whose
   *                    view we want.
   * @param convertView The old view to reuse, if possible.
   * @param parent      The parent that this view will eventually be attached to.
   * @return A View corresponding to the data at the specified position.
   * @see <a href="https://developer.android.com/reference/android/widget/Adapter.html">Android Adapter</a>
   * @since 1.0.0
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get view
    View view;
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
    } else {
      view = convertView;
    }

    // It always is a textview
    TextView text = (TextView) view;

    // Set the place name
    CarmenFeature feature = getItem(position);
    text.setText(feature.toString());

    return view;
  }

  /*
   * Required by Filterable
   */

  /**
   * Returns a filter that can be used to constrain data with a filtering pattern.
   *
   * @return a filter used to constrain data
   * @see <a href="https://developer.android.com/reference/android/widget/Filterable.html">Filterable Class</a>
   * @since 1.0.0
   */
  @Override
  public Filter getFilter() {
    if (geocoderFilter == null) {
      geocoderFilter = new GeocoderFilter();
    }

    return geocoderFilter;
  }

  private class GeocoderFilter extends Filter {

    private final MapboxGeocoding.Builder builder;

    public GeocoderFilter() {
      super();
      builder = new MapboxGeocoding.Builder();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      FilterResults results = new FilterResults();

      // No constraint
      if (TextUtils.isEmpty(constraint)) {
        return results;
      }

      // Response object
      Response<GeocodingResponse> response;

      try {
        // Build client and execute
        builder.setAccessToken(getAccessToken())
          .setLocation(constraint.toString())
          .setAutocomplete(true);

        // Optional params
        if (getBaseUrl() != null) {
          builder.setBaseUrl(getBaseUrl());
        }
        if (getCountry() != null) {
          builder.setCountry(getCountry());
        }
        if (getCountries() != null) {
          builder.setCountries(getCountries());
        }
        if (getProximity() != null) {
          builder.setProximity(getProximity());
        }
        if (getType() != null) {
          builder.setGeocodingType(getType());
        }
        if (getTypes() != null) {
          builder.setGeocodingTypes(getTypes());
        }
        if (getBbox() != null) {
          builder.setBbox(bbox[0], bbox[1], bbox[2], bbox[3]);
        }
        if (getLimit() != 0) {
          builder.setLimit(limit);
        }
        if (getLanguage() != null) {
          builder.setLanguage(language);
        }

        call = builder.build().getCall();

        // Do request
        response = builder.build().executeCall();
      } catch (IOException ioException) {
        ioException.printStackTrace();
        return results;
      }

      // Check it went well
      if (response == null) {
        return results;
      }

      features = response.body().getFeatures();
      results.values = features;
      results.count = features.size();
      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      if (results != null && results.count > 0) {
        features = (List<CarmenFeature>) results.values;
        notifyDataSetChanged();
      } else {
        notifyDataSetInvalidated();
      }
    }
  }
}
