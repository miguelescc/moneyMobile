package com.example.moneymobilev11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;


import java.util.ArrayList;

public class graphicBudgetActivity extends AppCompatActivity {
    private BarChart mpBarChar;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_budget);
        mpBarChar=this.findViewById(R.id.mp_BarChart);
        //Bundle bundle=getIntent().getExtras();
        ArrayList<String> listFinalSumPerCat = getIntent().getStringArrayListExtra("listFinalSumPerCat");
        ArrayList<String> listFinalCat = getIntent().getStringArrayListExtra("listFinalCat");
        //Boolean ifanual=bundle.getBoolean("ifanual");
        loadBarChartData(listFinalSumPerCat,listFinalCat);
    }


    //barchar
    private void loadBarChartData(ArrayList<String> listFinalSumPerCat,ArrayList<String> listFinalCat){
        DB=new DBHelper(this);
        ArrayList<BarEntry> barEntries1=new ArrayList<>();//to load data
        ArrayList<BarEntry> barEntries2=new ArrayList<>();
        String [] catstring= new String[listFinalSumPerCat.size()];
        for (int i = 0; i < listFinalSumPerCat.size(); i++) {
            barEntries1.add(new BarEntry((i+1),((int)Double.parseDouble(listFinalSumPerCat.get(i)))));

                Cursor res=DB.getdatacategory(Integer.parseInt(listFinalCat.get(i)));//llamamos a la bd para rescatar el nombre y budget de la cat
                String cat="",budget="";
                if(res.moveToFirst()){
                    do{
                        cat=res.getString(1);
                        budget=res.getString(2);
                    }while(res.moveToNext());
                }
                barEntries2.add(new BarEntry((i+1),((int)Double.parseDouble(budget))));
                catstring[i]=cat;

        }


        BarDataSet barDataSet1=new BarDataSet(barEntries1,"Total Expended ");
        barDataSet1.setColor(Color.RED);
        BarDataSet barDataSet2=new BarDataSet(barEntries2,"Monthly Budget");
        //barDataSet2.setColor(Color.rgb(255, 165, 0));
        barDataSet2.setColor(Color.GREEN);

        BarData data= new BarData(barDataSet1,barDataSet2);
        mpBarChar.setData(data);

        //category


        XAxis xAxis =mpBarChar.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(catstring));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        float barSpace =0.05f;
        float groupSpace =0.70f;
        data.setBarWidth(0.10f);
        //(barwith+barspace)*num of bar*groupspace=1
        mpBarChar.getXAxis().setAxisMinimum(0);
        mpBarChar.getXAxis().setAxisMaximum(+mpBarChar.getBarData().getGroupWidth(groupSpace,barSpace)*listFinalSumPerCat.size());
        mpBarChar.getAxisLeft().setMinWidth(0);
        mpBarChar.groupBars(0,groupSpace,barSpace);
        mpBarChar.invalidate();
        mpBarChar.animateY(1400, Easing.EaseInOutQuad);
    }







    public void Mainn (View v)
    {
        Intent i = new Intent(graphicBudgetActivity.this, MainActivity.class);
        startActivity(i);
    }
}
