package com.example.moneymobilev11;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class calendarFragment extends Fragment {

    Activity context;
    EditText editText;

    Button button3;
    public calendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Intent i = new Intent(getContext(), MainActivity.class);
        //startActivity(i);
        context=getActivity();

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    public void onStart() {
        super.onStart();
        //context=getActivity();







        //getActivity().onBackPressed();
    }

}
