package com.allanlopez.soccerapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DosFragment extends Fragment {
   public int posicion;

    public DosFragment() {
        // Required empty public constructor
    }

    public void nextVersion(){
        imageView.setImageResource(Data.drawableArray[posicion]);
        textView.setText(Data.androidNames[posicion]);
        posicion++;
    }

    private ImageView imageView;
    private TextView textView;


    //Argumentos de la actividad
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        imageView = (ImageView) view.findViewById(R.id.imageView2);
        textView = (TextView) view.findViewById(R.id.textView);
    }
}
