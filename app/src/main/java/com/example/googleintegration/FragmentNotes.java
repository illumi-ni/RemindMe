package com.example.googleintegration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class FragmentNotes extends Fragment {
    View view;

    public FragmentNotes() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        FloatingActionButton fabWrite = view.findViewById(R.id.fabWrite);
        fabWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "You clicked!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return view;
    }
}
