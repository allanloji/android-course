package com.allanlopez.soccerapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnoFragment extends Fragment {


    public UnoFragment() {
        // Required empty public constructor
    }
    private Button button;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DosFragment dosFragment = (DosFragment) fragmentManager.findFragmentByTag("dos");
                dosFragment.nextVersion();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uno, container, false);
    }

}
