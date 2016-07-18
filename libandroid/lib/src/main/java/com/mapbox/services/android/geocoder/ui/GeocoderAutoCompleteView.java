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
import com.mapbox.services.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.commons.models.Position;

/**
 * An editable text view that shows geocoder result suggestions automatically
 * while the user is typing. The list of suggestions is displayed in a drop
 * down menu from which the user can choose an item and you can listen to and
 * act upon which item they chose
 *
 * @see <a href="https://developer.android.com/reference/android/widget/AutoCompleteTextView.html">Android AutoCompleteTextView</a>
 * @see <a href="https://www.mapbox.com/android-sdk/examples/geocoding/">Mapbox example</a>
 * @since 1.0.0
 */
public class GeocoderAutoCompleteView extends AutoCompleteTextView {

    private final static int DEFAULT_NUMBER_OF_LINES = 1;

    private GeocoderAdapter adapter;

    public interface OnFeatureListener {
        void OnFeatureClick(CarmenFeature feature);
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
                CarmenFeature result = adapter.getItem(position);
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

    /**
     * You'll need to have a Mapbox access token to use the geocoding API within MAS.
     *
     * @param accessToken Your Mapbox access token
     * @see <a href="https://www.mapbox.com/help/define-access-token/">Mapbox access token</a>
     * @since 1.0.0
     */
    public void setAccessToken(String accessToken) {
        adapter.setAccessToken(accessToken);
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
        adapter.setType(type);
    }

    /**
     * Bounding box within which to limit results.
     *
     * @param northeast The top right hand corner of your bounding box when the map is pointed north.
     * @param southwest The bottom left hand corner of your bounding box when the map is pointed north.
     * @since 2.0.0
     */
    public void setBbox(Position northeast, Position southwest) {
        adapter.setBbox(southwest.getLongitude(), southwest.getLatitude(),
                northeast.getLongitude(), northeast.getLatitude());
    }

    /**
     * Bounding box within which to limit results.
     *
     * @param minX Bottom of bounding box when map is pointed north.
     * @param minY Left of bounding box when map is pointed north.
     * @param maxX Top of bounding box when map is pointed north.
     * @param maxY Right of bounding box when map is pointed north.
     * @since 2.0.0
     */
    public void setBbox(double minX, double minY, double maxX, double maxY) {
        adapter.setBbox(minX, minY, maxX, maxY);
    }

    /**
     * Location around which to bias geocoder results.
     *
     * @param position {@link Position} coordinate.
     * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Geocoding API documentation</a>
     * @since 1.0.0
     */
    public void setProximity(Position position) {
        adapter.setProximity(position);
    }

    /**
     * Sets the listener that will be notified when the user clicks an item in the drop down list.
     *
     * @param onFeatureListener the item click listener.
     * @since 1.0.0
     */
    public void setOnFeatureListener(OnFeatureListener onFeatureListener) {
        this.onFeatureListener = onFeatureListener;
    }
}
