package com.allanlopez.marvel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allanlopez.marvel.adapters.MarvelArrayAdapter;
import com.allanlopez.marvel.pojo.Heroe;
import com.allanlopez.marvel.pojo.MarvelDude;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private MarvelArrayAdapter marvelArrayAdapter;
    private ListView listView;
    private RequestQueue mQueue;
    private SeekBar seekBar;
    private Button search;
    private TextView seekBarValue;
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        search = (Button)findViewById(R.id.search);
        seekBarValue = (TextView) findViewById(R.id.seekBarValue);
        seekBar.setProgress(0);
        seekBar.setMax(900);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 100;
                progress = progress * 100;
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        listView = (ListView) findViewById(R.id.lista);
        /*
        ituneItuneArrayAdapter = new ItuneArrayAdapter(this, R.layout.marvel_layout, new ArrayList<Itune>());
        listView.setAdapter(ituneItuneArrayAdapter);

        new ProcesaJson(ituneItuneArrayAdapter).execute("https://itunes.apple.com/search?term=foals");*/


        /*heroeArrayAdapter = new MarvelArrayAdapter(this, R.layout.marvel_layout, new ArrayList<Heroe>());
        listView.setAdapter(heroeArrayAdapter);*/
        //new MarvelJson(heroeArrayAdapter).execute();
        marvelArrayAdapter = new MarvelArrayAdapter(this, R.layout.marvel_layout, new ArrayList<MarvelDude>());
        listView.setAdapter(marvelArrayAdapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonMarvel(getMarvelString(), marvelArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MarvelDude md = marvelArrayAdapter.getItem((int)id);
                Intent intent = new Intent(MainActivity.this, HeroDetailActivity.class);
                intent.putExtra("id", md.id);
                startActivity(intent);
            }
        });


    }

    /*public class ProcesaJson extends AsyncTask<String, Integer, ArrayList<Itune>>{
        private ItuneArrayAdapter adapter;
        public ProcesaJson(ItuneArrayAdapter adapter){
            this.adapter = adapter;
        }
        @Override
        protected ArrayList<Itune> doInBackground(String... urls) {
            Json json = new Json();
            String jsonString = json.serviceCall(urls[0]);
            ArrayList<Itune> arrayList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject dato = jsonArray.getJSONObject(i);
                    Itune itune = new Itune();
                    itune.collectionName = dato.getString("collectionName");
                    itune.trackName = dato.getString("trackName");
                    itune.trackPrice = dato.getDouble("trackPrice");

                    arrayList.add(itune);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Itune> strings) {
            adapter.clear();
            adapter.addAll(strings);
            adapter.notifyDataSetChanged();
        }
    }*/


    private final String LOG_TAG = "MARVEL";

    private static char[] HEXCodes = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    private void jsonMarvel(String url, final MarvelArrayAdapter adapter){
        adapter.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject thumbnail = jsonObject.getJSONObject("thumbnail");
                        MarvelDude marvelDude = new MarvelDude();
                        marvelDude.name = jsonObject.getString("name");
                        marvelDude.url = thumbnail.getString("path") + "/portrait_small." + thumbnail.getString("extension");
                        marvelDude.id = jsonObject.getString("id");
                        adapter.add(marvelDude);
                    }
                    adapter.notifyDataSetChanged();
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
        ArrayList<Heroe> arrayList = new ArrayList<>();


            /*
                Conexión con el getway de marvel
            */
        final String CHARACTER_BASE_URL =
                "http://gateway.marvel.com/v1/public/characters";


            /*
                Configuración de la petición
            */
        String characterJsonStr = null;
        final String TIMESTAMP = "ts";
        final String API_KEY = "apikey";
        final String HASH = "hash";
        final String ORDER = "orderBy";

        Uri builtUri;
        builtUri = Uri.parse(CHARACTER_BASE_URL+"?").buildUpon()
                .appendQueryParameter(TIMESTAMP, ts)
                .appendQueryParameter(API_KEY, apikey)
                .appendQueryParameter(HASH, hash)
                .appendQueryParameter(ORDER, "name")
                .appendQueryParameter("offset", offset + "")
                .appendQueryParameter("limit", "100")
                .build();

       return (builtUri.toString());
    }

/* md5

MD5 son las siglas de “Message Digest Algorithm 5”. Se utiliza como una
función de codificación o huella digital de un archivo. Es empleado para codificar contraseñas
en bases de datos, el MD5 es igualmente capaz de generar una huella de archivo para asegurar que no
haya cambios en el mismo tras una transferencia, un hash MD5 está compuesto por 32 caracteres hexadecimales.

 */

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


    /*
        Investiga y reporta qué hace esta aplicación

        Devuelve datos como texto en formato hexadecimal.
        Si bien la codificación del nombre de usuario y la contraseña con el algoritmo Base64
        generalmente los hace ilegibles a simple vista, se decodifican facilmente. La seguridad
        no es la intención del paso de codificación. La intención de la codificación es codificar
        los caracteres no compatibles con HTTP que pueden estar en el nombre de usuario o contraseña
        en aquellos que son compatibles con HTTP.
    */
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

    public void changeOffset(View view){
        offset = Integer.parseInt( seekBarValue.getText().toString());
        jsonMarvel(getMarvelString(), marvelArrayAdapter);
    }
}
