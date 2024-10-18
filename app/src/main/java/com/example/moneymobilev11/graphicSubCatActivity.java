package com.example.moneymobilev11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class graphicSubCatActivity extends AppCompatActivity {

    BarChart barChart;
    DBHelper DB;

    List<String> listacatid = new ArrayList<String>();//datos para el char
    List<String> listaexpense = new ArrayList<String>();//datos para el char
    List<String> listSumAux = new ArrayList<>();//datos para el char
    List<String> listSumAuxId = new ArrayList<>();//datos para el char
    List<String> listFinalCat = new ArrayList<>();
    List<String> listFinalIdCat = new ArrayList<>();
    List<String> listFinalSumPerCat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_sub_cat);
        Bundle bundle=getIntent().getExtras();
        String idcategory=bundle.getString("idcategory");
        Toast.makeText(this, "LISTA " + idcategory, Toast.LENGTH_SHORT).show();


        barChart=findViewById(R.id.mp_BarChart);
        //<BarEntry> barEntries1=new ArrayList<>();//to load data

        loadData(idcategory);



    }


    private void loadData(String idcategory){
        DB=new DBHelper(this);
        Cursor responseExpensedate=DB.getdataexpensemonthly();
        if(responseExpensedate.getCount()==0)
        {
            return;
        }
        if(responseExpensedate.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                if(responseExpensedate.getString(4).equals(idcategory)){
                listaexpense.add(responseExpensedate.getString(1));//expense
                listacatid.add(responseExpensedate.getString(5));//idsubcat
                }
            }while(responseExpensedate.moveToNext());
        }


        //listSumAux//we start adding into list the total sum
        int j=0;
        for (int i = 0; i < listacatid.size(); i++) {
            listSumAuxId.add(listacatid.get(i));
            listSumAux.add("0");
            double aux=0;
            //aux=Double.parseDouble(listaexpense.get(i));
            for (j = 0; j < listacatid.size(); j++) {
                if (Integer.parseInt(listacatid.get(i))==Integer.parseInt(listacatid.get(j))) {//Integer.parseInt(listacatid.get(i))!=Integer.parseInt(listacatid.get(j))
                    //listSumAuxId
                    //aux=Double.parseDouble(listSumAux.get(i));
                    aux=aux+Double.parseDouble(listaexpense.get(j));
                    listSumAux.set(i,(aux+""));
                    //listSumAux.set(aux+Double.parseDouble(listaexpense.get(i)));
                }
            }
        }

        double aux=0;//pending graphic here
        for (int i = 0; i < listacatid.size(); i++) {
            if(Double.parseDouble(listSumAuxId.get(i))!=0){
                aux = aux + Double.parseDouble(listSumAux.get(i));
                listFinalCat.add(listacatid.get(i));//in this list we save the final dates to use into the pie
                listFinalSumPerCat.add(listSumAux.get(i));
                listFinalIdCat.add(listacatid.get(i));
                listacatid.set(i,("0"));
                //add the new lista para escuchar
            }
            int aux1=Integer.parseInt(listSumAuxId.get(i));//we compare with each one to delete duplicated
            for (j = 0; j < listacatid.size(); j++) {
                if (aux1==Integer.parseInt(listSumAuxId.get(j))) {
                    listSumAux.set(j,("0"));
                    listSumAuxId.set(j,("0"));
                    listacatid.set(j,("0"));
                }
            }
        }

        //from here is the char
        ArrayList<BarEntry> barEntries1=new ArrayList<>();
        String [] subcatstring= new String[listFinalSumPerCat.size()];
        for (int i = 0; i < listFinalSumPerCat.size(); i++) {
            String cat = subcatVoid(i);
            int auxfinalporcent =(int) Double.parseDouble(listFinalSumPerCat.get(i));
            String  auxfinalcat =cat;//(int) Double.parseDouble(listFinalCat.get(i))+
            barEntries1.add(new BarEntry(i,(int)Double.parseDouble(listFinalSumPerCat.get(i))));
            //arEntries1.add(new BarEntry(4,11));//example
            subcatstring[i]=cat;

        }

        ArrayList<Integer> colors=new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for (int color:ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }
        String cataux = catVoid(idcategory);
        BarDataSet barDataSet1 = new BarDataSet(barEntries1, (cataux+" Sub-categories"));
        barDataSet1.setColors(colors);
        BarData data= new BarData();
        data.addDataSet(barDataSet1);
        barChart.setData(data);

        XAxis xAxis =barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(subcatstring));
        //xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(270);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        //float barSpace =0.25f;
        //float groupSpace =1f;
        //data.setBarWidth(0.25f);
                    //(barwith+barspace)*num of bar*groupspace=1
        //barChart.getXAxis().setAxisMinimum(0);
        //barChart.getXAxis().setAxisMaximum(+barChart.getBarData().getGroupWidth(groupSpace,barSpace)*listFinalSumPerCat.size());
        //barChart.getAxisLeft().setMinWidth(0);
        barChart.getDescription().setText(" ");
        barChart.invalidate();
        barChart.animateY(1400, Easing.EaseInOutQuad);




        //barChart.invalidate();
        //barChart.animateY(1400, Easing.EaseInOutQuad);
    }











    String subcatVoid(int i){
        Cursor res=DB.getdatasubcategory(Integer.parseInt(listFinalCat.get(i)));//llamamos a la bd para rescatar el nombre de la cat
        String subcat="";
        if(res.moveToFirst()){
            do{
                subcat=res.getString(1);
            }while(res.moveToNext());
        }
        return subcat;
    }
    String catVoid(String  cat){
        Cursor res=DB.getdatacategory(Integer.parseInt(cat));//llamamos a la bd para rescatar el nombre de la cat
        String catfin="";
        if(res.moveToFirst()){
            do{
                catfin=res.getString(1);
            }while(res.moveToNext());
        }
        return catfin;
    }

    public void Mainn (View v)
    {
        Intent i = new Intent(graphicSubCatActivity.this, MainActivity.class);
        startActivity(i);
    }
}
