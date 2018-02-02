package com.allanlopez.marvel.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allanlopez.marvel.R;
import com.allanlopez.marvel.VolleySingleton;
import com.allanlopez.marvel.pojo.Heroe;
import com.allanlopez.marvel.pojo.MarvelDude;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n16h7 on 1/29/2018.
 */

public class MarvelArrayAdapter extends ArrayAdapter<MarvelDude> {

    private ArrayList<MarvelDude> arrayList;
    private Context context;
    public MarvelArrayAdapter(Context context, int resource, List<MarvelDude> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MarvelDude marvelDude = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.marvel_layout, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.name);
        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.imageView);
        textView.setText(marvelDude.name);
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        ImageLoader imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {

                private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);

                    }
                });
        networkImageView.setImageUrl("http://i.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73/portrait_xlarge.jpg", imageLoader);

        return convertView;
    }
}
