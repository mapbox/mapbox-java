package com.mapbox.services.android.testapp.staticimage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mapbox.services.android.testapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by antonio on 4/1/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final static String LOG_TAG = "GalleryAdapter";

    private Context mContext;
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final static String LOG_TAG = "ViewHolder";

        public ImageView mImageView;

        public ViewHolder(ImageView itemView) {
            super(itemView);
            mImageView = itemView;
        }
    }

    public GalleryAdapter(Context context, String[] dataset) {
        mContext = context;
        mDataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView v = (ImageView) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.image_container, parent, false);
        v.setAdjustViewBounds(true);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(mDataset[position])
                .placeholder(R.drawable.mapbox)
                .error(R.drawable.mapbox_error)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
