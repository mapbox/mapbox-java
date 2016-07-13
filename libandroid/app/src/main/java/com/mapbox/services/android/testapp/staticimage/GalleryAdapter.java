package com.mapbox.services.android.testapp.staticimage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mapbox.services.android.testapp.R;
import com.squareup.picasso.Picasso;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private static final String LOG_TAG = "GalleryAdapter";

    private Context context;
    private String[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final String LOG_TAG = "ViewHolder";

        public ImageView imageView;

        public ViewHolder(ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }
    }

    public GalleryAdapter(Context context, String[] dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.image_container, parent, false);
        imageView.setAdjustViewBounds(true);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context)
                .load(dataset[position])
                .placeholder(R.drawable.mapbox)
                .error(R.drawable.mapbox_error)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

}
