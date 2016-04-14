package com.mapbox.services.android.geocoder.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.mapbox.services.android.R;
import com.mapbox.services.geocoding.v5.models.GeocodingFeature;
import com.mapbox.services.commons.models.Position;

/**
 * Created by antonio on 2/1/16.
 */
public class GeocoderAutoCompleteView extends AutoCompleteTextView {

    private final static int DEFAULT_NUMBER_OF_LINES = 1;

    private GeocoderAdapter adapter;

    public interface OnFeatureListener {
        void OnFeatureClick(GeocodingFeature feature);
    }

    private OnFeatureListener onFeatureListener = null;

    public GeocoderAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set custom adapter
        adapter = new GeocoderAdapter(context);
        setAdapter(adapter);

        // Set number of lines
        setLines(DEFAULT_NUMBER_OF_LINES);

        // Set click listener
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GeocodingFeature result = adapter.getItem(position);
                setText(result.toString());

                // Notify subscribers
                if (onFeatureListener != null) {
                    onFeatureListener.OnFeatureClick(result);
                }
            }
        });

        // Add clear button to autocomplete
        final Drawable imgClearButton = ContextCompat.getDrawable(context, R.drawable.ic_clear_black_24dp);
        setCompoundDrawablesWithIntrinsicBounds(null, null, imgClearButton, null);
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                GeocoderAutoCompleteView et = (GeocoderAutoCompleteView) v;
                if (et.getCompoundDrawables()[2] == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgClearButton.getIntrinsicWidth()) {
                    setText("");
                }
                return false;
            }
        });
    }

    /*
     * Setters
     */

    public void setAccessToken(String accessToken) {
        adapter.setAccessToken(accessToken);
    }

    public void setType(String type) {
        adapter.setType(type);
    }

    public void setProximity(Position position) {
        adapter.setProximity(position);
    }

    public void setOnFeatureListener(OnFeatureListener onFeatureListener) {
        this.onFeatureListener = onFeatureListener;
    }
}
