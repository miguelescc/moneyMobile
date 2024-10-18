package com.example.moneymobilev11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class graphicGenerateActivity extends AppCompatActivity {

    BarChart barChart;
    DBHelper DB;
    Spinner spinner;

    List<String> item=null;
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
        setContentView(R.layout.activity_graphic_generate);
        barChart=findViewById(R.id.mp_BarChart);
        spinner = this.findViewById(R.id.spinnerCategory);
        Bundle bundle=getIntent().getExtras();
        String Start="false";
        String End="false";
        //String SWCat=bundle.getString("SWCat");
        if(bundle.getString("End") != null){
             Start=bundle.getString("Start");
             End=bundle.getString("End");
        }
        loadData(Start,End,"true");

        //spinner
        final String StartFinal=Start;//to use it in the spinner....
        final String EndFinal=End;
        item = new ArrayList<String>();
        item.add(" Categories  ");
        item.add(" Sub Categories  ");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){//if it is category
                    cleaner();
                    loadData(StartFinal,EndFinal,"true");


                }else {
                    if(position==1){//if it is sub....
                        cleaner();
                        loadData(StartFinal,EndFinal,"false");

                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }




    private void loadData(String StartDate,String EndDate,String SWCat){
            DB=new DBHelper(this);
            Cursor responseExpensedate;
            if(StartDate.equals("false")){
                responseExpensedate=DB.getdataexpenseBetween("2022-01-01 00:00:00","2022-12-01 23:59:59");
            }else {
                StartDate=StartDate+" 00:00:00";
                EndDate=EndDate+" 23:59:59";
                responseExpensedate=DB.getdataexpenseBetween(StartDate,EndDate);
            }

        if(responseExpensedate.getCount()==0)
        {
            Toast.makeText(this,"No Data Found in the Generated Report",Toast.LENGTH_SHORT).show();
            return;
        }
        if(responseExpensedate.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                if(SWCat.equals("true")){
                    listaexpense.add(responseExpensedate.getString(1));//expense
                    listacatid.add(responseExpensedate.getString(4));//idsubcat
                }else {
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

        double auxTotal=0;
        ArrayList<BarEntry> barEntries1=new ArrayList<>();
        String [] finalstringnames= new String[listFinalSumPerCat.size()];
        for (int i = 0; i < listFinalSumPerCat.size(); i++) {

            String cat;

            if(SWCat.equals("true")){

                cat = catVoid((listFinalIdCat.get(i)+""));
            }else {
                cat = subcatVoid(i);
            }




            double auxfinalporcent =(Double) Double.parseDouble(listFinalSumPerCat.get(i));
            String  auxfinalcat =cat;//(int) Double.parseDouble(listFinalCat.get(i))+
            barEntries1.add(new BarEntry(i,(int)Double.parseDouble(listFinalSumPerCat.get(i))));
            //arEntries1.add(new BarEntry(4,11));//example
            finalstringnames[i]=cat;
            auxTotal=auxTotal+auxfinalporcent;//maybe is not necesary to declare this variable auxfinal porcent because we are using it just here
        }



        DecimalFormat df = new DecimalFormat("#.00");//this and the second are just to put two decimals in the double
        String result = df.format(auxTotal);
        /*barEntries1.add(new BarEntry(2014,400));*/

        BarDataSet barDataSet= new BarDataSet(barEntries1,"Graphic Generated");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        //barDataSet.setValueTextSize(16f);
        BarData barData=new BarData(barDataSet);

        XAxis xAxis =barChart.getXAxis();//xaxis is for the labels of the top, we are loading with subcatstring array
        xAxis.setValueFormatter(new IndexAxisValueFormatter(finalstringnames));
        //xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(270);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(finalstringnames.length);//with this we are putting all labels of the top
        //xAxis.setDrawAxisLine(false);
        //xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);


        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Total Expended "+result);//here we can modify depending the entry
        //barChart.animateY(2000);
        barChart.invalidate();
        barChart.animateY(1400, Easing.EaseInOutQuad);
    }



    public void cleaner(){
        listacatid.clear();
        listaexpense.clear();
        listSumAux.clear();
        listSumAuxId.clear();
        listFinalCat.clear();
        listFinalIdCat.clear();
        listFinalSumPerCat.clear();


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

    public void Mainn (View v)
    {
        Intent i = new Intent(graphicGenerateActivity.this, MainActivity.class);
        startActivity(i);
    }
}
