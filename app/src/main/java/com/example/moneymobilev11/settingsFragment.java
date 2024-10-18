package com.example.moneymobilev11;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



/**
 * A simple {@link Fragment} subclass.
 */
public class settingsFragment extends Fragment {
    Activity context;
    CardView card,card1,card5,card6;


    public settingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=getActivity();
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void onStart() {
        super.onStart();
        card=(CardView)context.findViewById(R.id.card);
        card1=(CardView)context.findViewById(R.id.card2);//cuidado
        card5=(CardView)context.findViewById(R.id.card5);
        card6 = (CardView) context.findViewById(R.id.card6);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, deleteIncomeActivity.class);
                startActivity(i);
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ifdelete=true;
                Intent i = new Intent(context, deleteExpensesActivity.class);
                i.putExtra("ifdelete", ifdelete);
                startActivity(i);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, backupActivity.class);
                startActivity(i);
            }
        });


        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuffer buffer=new StringBuffer();

                    buffer.append("Developed by Miguel Angel Escobar Pujol "+"\n");
                    buffer.append("miguelescc@gmail.com"+"\n");
                    buffer.append("+353 08148605 "+"\n\n");
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("About ");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }
}
