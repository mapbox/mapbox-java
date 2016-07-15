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
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;
import com.mapbox.services.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class GeocoderAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private String accessToken;
    private String type;
    private double[] bbox;
    private Position position;

    private GeocoderFilter geocoderFilter;

    private List<CarmenFeature> features;

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

    public double[] getBbox() {
        return bbox;
    }

    public void setBbox(Position northeast, Position southwest) {
        setBbox(southwest.getLongitude(), southwest.getLatitude(),
                northeast.getLongitude(), northeast.getLatitude());
    }

    public void setBbox(double minX, double minY, double  maxX, double  maxY){
        if (bbox == null) bbox = new double[4];
        bbox[0] = minX;
        bbox[1] = minY;
        bbox[2] = maxX;
        bbox[3] = maxY;
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
    public CarmenFeature getItem(int position) {
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
        CarmenFeature feature = getItem(position);
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
            if (TextUtils.isEmpty(constraint)) {
                return results;
            }

            // Response object
            Response<GeocodingResponse> response;

            try {
                // Build client and execute
                builder.setAccessToken(geocoderAdapter.getAccessToken())
                    .setLocation(constraint.toString())
                    .setGeocodingType(geocoderAdapter.getType())
                    .setAutocomplete(true);

                // Optional params
                if (getBbox() != null) builder.setBbox(bbox[0], bbox[1], bbox[2], bbox[3]);
                if (getProximity() != null) builder.setProximity(geocoderAdapter.getProximity());

                // Do request
                response = builder.build().executeCall();
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
