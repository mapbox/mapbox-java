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
import com.mapbox.services.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.geocoding.v5.models.GeocodingFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

/**
 * Created by antonio on 2/1/16.
 */
public class GeocoderAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private String accessToken;
    private String type;
    private Position position;

    private GeocoderFilter geocoderFilter;

    private List<GeocodingFeature> features;

    public GeocoderAdapter(Context context) {
        this.context = context;
    }

    /*
     * Getters and setters
     */

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Position getProximity() {
        return position;
    }

    public void setProximity(Position position) {
        this.position = position;
    }

    /*
     * Required by BaseAdapter
     */

    @Override
    public int getCount() {
        return features.size();
    }

    @Override
    public GeocodingFeature getItem(int position) {
        return features.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Get a View that displays the data at the specified position in the data set.
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
        GeocodingFeature feature = getItem(position);
        text.setText(feature.toString());

        return view;
    }

    /*
     * Required by Filterable
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
            if (StringUtils.isEmpty(constraint)) {
                return results;
            }

            // Response object
            Response<GeocodingResponse> response;

            try {
                // Build client and execute
                response = builder.setAccessToken(geocoderAdapter.getAccessToken())
                        .setLocation(constraint.toString())
                        .setProximity(geocoderAdapter.getProximity())
                        .setType(geocoderAdapter.getType())
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
                features = (List<GeocodingFeature>) results.values;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
