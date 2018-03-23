package com.allanlopez.youtubeapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private YouTubePlayerView youTubePlayerView;
    private String key="AIzaSyCd8f1McFmDKTTH5pIWYu7MbONzct0Wh2I";
    private String uri = "c0_WAbbgNGE";
    private ArrayList<String> history = new ArrayList<String>();
    private Button add;
    private EditText uriTxt;
    private YouTubePlayer player;
    private ListView listView;
    private String[] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.you);
        youTubePlayerView.initialize(key, this);
        add = (Button) findViewById(R.id.agregar);
        uriTxt = (EditText) findViewById(R.id.uriVideo);
        listView = (ListView) findViewById(R.id.historyList);
        history.add(uri);
        array = (String[]) history.toArray(new String[history.size()]);
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, array));

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.player = youTubePlayer;
        Toast.makeText(this, getString(R.string.saludo), Toast.LENGTH_LONG).show();
        if(!b){
            youTubePlayer.cueVideo(uri);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Youtube Error Not Available", Toast.LENGTH_LONG).show();

    }

    public void addVideo(View view){
        uri = uriTxt.getText().toString();
        history.add(uri);
        player.cueVideo(uri);
        array = (String[]) history.toArray(new String[history.size()]);
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, array));

    }
}
