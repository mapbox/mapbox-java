package com.mapbox.services.android.geocoder.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.utils.TextUtils;
import com.mapbox.services.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

/**
 * Adapter for the {@link GeocoderAutoCompleteView}. In this class we make the Mapbox Geocoding API
 * call.
 *
 * @since 1.0.0
 */
public class GeocoderAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private String accessToken;
    private String type;
    private Position position;

    private GeocoderFilter geocoderFilter;

    private List<CarmenFeature> features;

    public GeocoderAdapter(Context context) {
        this.context = context;
    }

    /*
     * Getters and setters
     */

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
     * Configure the geocoder type, pass in one of the constants found within
     * {@link com.mapbox.services.geocoding.v5.GeocodingCriteria}.
     *
     * @param type String containing "place", "poi", "neighborhood", etc.
     * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Geocoding API documentation</a>
     * @since 1.0.0
     */
    public void setType(String type) {
        this.type = type;
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
            geocoderFilter = new GeocoderFilter(this);
        }

        return geocoderFilter;
    }

    private class GeocoderFilter extends Filter {

        private final GeocoderAdapter geocoderAdapter;
        private final MapboxGeocoding.Builder builder;

        public GeocoderFilter(GeocoderAdapter geocoderAdapter) {
            super();
            this.geocoderAdapter = geocoderAdapter;
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
                response = builder.setAccessToken(geocoderAdapter.getAccessToken())
                        .setLocation(constraint.toString())
                        .setProximity(geocoderAdapter.getProximity())
                        .setGeocodingType(geocoderAdapter.getType())
                        .build().executeCall();
            } catch (IOException e) {
                e.printStackTrace();
                return results;
            } catch (ServicesException e) {
                e.printStackTrace();
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
