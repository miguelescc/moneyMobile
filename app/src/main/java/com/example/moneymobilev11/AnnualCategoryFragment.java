package com.example.moneymobilev11;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;


import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnualCategoryFragment extends Fragment {
    Activity context;
    //DBHelper DB;
    Button btnGenerate,startDate,endDate,btnBalance;
    private DatePickerDialog datePickerDialog,datePickerDialog2;

    RadioButton rbTotal,rbBetween;
    TextView txt;


    public AnnualCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=getActivity();
        return inflater.inflate(R.layout.fragment_annual_category, container, false);
    }


    public void onStart() {
        super.onStart();

        btnBalance=context.findViewById(R.id.btnBalance);
        btnGenerate=context.findViewById(R.id.btnGenerate);

        rbTotal = (RadioButton) context.findViewById(R.id.rbTotal);
        rbBetween = (RadioButton) context.findViewById(R.id.rbBetween);
        txt=(TextView) context.findViewById(R.id.textView6);
        //rbCat =(RadioButton) context.findViewById(R.id.rbCat);
        //rbSubCat=(RadioButton) context.findViewById(R.id.rbSubCat);
        initDataPicker();
        initDataPicker2();
        startDate=context.findViewById(R.id.startDate);
        endDate=context.findViewById(R.id.endDate);
        startDate.setText(getTodayDate());
        endDate.setText(getTodayDate());


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker2();
            }
        });


        rbBetween.setOnClickListener(new View.OnClickListener() {//set visibility
            @Override
            public void onClick(View v) {
                startDate.setVisibility(View.VISIBLE);
                endDate.setVisibility(View.VISIBLE);
                txt.setVisibility(View.VISIBLE);


            }
        });
        rbTotal.setOnClickListener(new View.OnClickListener() {//set visibility
            @Override
            public void onClick(View v) {
                startDate.setVisibility(View.INVISIBLE);
                endDate.setVisibility(View.INVISIBLE);
                txt.setVisibility(View.INVISIBLE);
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rbTotal.isChecked()){
                    //if(rbCat.isChecked()){
                        Intent i = new Intent(context, graphicGenerateActivity.class);
                        i.putExtra("SWCat", "true");
                        startActivity(i);
                    /*}
                    else
                    {
                        if(rbSubCat.isChecked()){
                            Intent i = new Intent(context, graphicGenerateActivity.class);
                            i.putExtra("SWCat", "false");
                            startActivity(i);
                        }
                    }*/

                }
                else
                {
                    //if(rbBetween.isChecked()){

                        //if(rbCat.isChecked()){
                            Intent i = new Intent(context, graphicGenerateActivity.class);
                            i.putExtra("Start", startDate.getText().toString());
                            i.putExtra("End", endDate.getText().toString());
                            i.putExtra("SWCat", "true");
                            startActivity(i);
                        /*}
                        else
                        {
                            if(rbSubCat.isChecked()){
                                Intent i = new Intent(context, graphicGenerateActivity.class);
                                i.putExtra("Start", startDate.getText().toString());
                                i.putExtra("End", endDate.getText().toString());
                                i.putExtra("SWCat", "false");
                                startActivity(i);
                            }
                        }


                    }*/
                }
            }
        });

        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, balanceActivity.class);
                startActivity(i);
            }
        });
    }







    private void initDataPicker(){
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month=month+1;

            String date=makeDateString(dayOfMonth,month,year);
            //String date=makeDateString(dayOfMonth,month,year);

            startDate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(context,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void initDataPicker2(){
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=makeDateString(dayOfMonth,month,year);
                //String date=makeDateString(dayOfMonth,month,year);
                endDate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog2 = new DatePickerDialog(context,style,dateSetListener,year,month,day);
        datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
    }



    private String makeDateString(int day,int month, int year){

        String date="";
        if(day<10){
            if(month<10){
                date=year+"-0"+month+"-0"+day;
            }else {
                date=year+"-"+month+"-0"+day;
            }
        }else {
            if(month<10){
                date=year+"-0"+month+"-"+day;
            }else {
                date=year+"-"+month+"-"+day;
            }
        }
        return date;
    }


    private String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        month=month+1;
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        return makeDateString(day,month,year);

    }

    public void openDatePicker(){
        datePickerDialog.show();
    }
    public void openDatePicker2(){
        datePickerDialog2.show();
    }


}
