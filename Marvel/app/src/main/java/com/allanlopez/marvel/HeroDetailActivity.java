package com.allanlopez.marvel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;

import com.allanlopez.marvel.adapters.MarvelArrayAdapter;
import com.allanlopez.marvel.pojo.Heroe;
import com.allanlopez.marvel.pojo.MarvelDude;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class HeroDetailActivity extends Activity {
    private TextView nombre, descripcion;
    private NetworkImageView heroImg;
    private RequestQueue mQueue;
    private String heroId;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);

        nombre = (TextView) findViewById(R.id.heroName);
        heroImg = (NetworkImageView) findViewById(R.id.heroImage);
        descripcion = (TextView) findViewById(R.id.heroDescription);

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();

        heroId = (String) getIntent().getSerializableExtra( "id");
        jsonMarvel(getMarvelString());


    }


    private final String LOG_TAG = "MARVEL";

    private static char[] HEXCodes = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    private void jsonMarvel(String url){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("results");

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    JSONObject thumbnail = jsonObject.getJSONObject("thumbnail");
                    nombre.setText(jsonObject.getString("name"));
                    descripcion.setText(jsonObject.getString("description"));
                    imgUrl = thumbnail.getString("path") + "/portrait_medium." + thumbnail.getString("extension");
                    LoadImage(imgUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);
    }
    private String getMarvelString(){
        String ts = Long.toString(System.currentTimeMillis() / 1000);
        String apikey = "d9c9993a63b316379f898412b03435a8";
        String hash = md5(ts + "9441edf550393ac90d79cc4a5f40308c85ae9580" + "d9c9993a63b316379f898412b03435a8");


            /*
                Conexión con el getway de marvel
            */
        final String CHARACTER_BASE_URL =
                "http://gateway.marvel.com/v1/public/characters/";


            /*
                Configuración de la petición
            */
        String characterJsonStr = null;
        final String TIMESTAMP = "ts";
        final String API_KEY = "apikey";
        final String HASH = "hash";
        final String ORDER = "orderBy";

        Uri builtUri;
        builtUri = Uri.parse(CHARACTER_BASE_URL+ heroId +"?").buildUpon()
                .appendQueryParameter(TIMESTAMP, ts)
                .appendQueryParameter(API_KEY, apikey)
                .appendQueryParameter(HASH, hash)
                .build();

        return (builtUri.toString());
    }



    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            String hash = new String(hexEncode(digest.digest()));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String hexEncode(byte[] bytes) {
        char[] result = new char[bytes.length*2];
        int b;
        for (int i = 0, j = 0; i < bytes.length; i++) {
            b = bytes[i] & 0xff;
            result[j++] = HEXCodes[b >> 4];
            result[j++] = HEXCodes[b & 0xf];
        }
        return new String(result);
    }

    private  void LoadImage(String url){

        ImageLoader imageLoader = new ImageLoader(mQueue,
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
        heroImg.setImageUrl(url, imageLoader);
    }
}
