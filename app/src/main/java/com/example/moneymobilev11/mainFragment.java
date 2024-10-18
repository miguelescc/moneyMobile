package com.example.moneymobilev11;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class mainFragment extends Fragment {
    Activity context;
    DBHelper DB;
    TextView balanceLbl;
    Button btnExpense,buttonSeeBudget;



    ListView list;//para las pruebas solamente, //para cargar los datos en la estadistica
    List<String> item=null;

    List<String> listadate = new ArrayList<String>();//datos para el pie
    List<String> listacatid = new ArrayList<String>();//datos para el pie
    List<String> listaexpense = new ArrayList<String>();//datos para el pie
    List<String> listSumAux = new ArrayList<>();//datos para el pie
    List<String> listSumAuxId = new ArrayList<>();//datos para el pie
    List<String> listFinalCat = new ArrayList<>();
    List<String> listFinalIdCat = new ArrayList<>();
    List<String> listFinalSumPerCat = new ArrayList<>();
    double aux=0;

    private PieChart pieChart;

    public mainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            // add/replace your fragment as you would do the first time
        } else {
            // activity is recreated due to any configuration change . here the fragment will be automatically attached by the fragment manager. You can fetch that fragment by findFragmentByTag(String tag) and then use it.
        }

        context=getActivity();//el context para cargar las cosas del activity en el fragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    public void onStart() {
        super.onStart();
        balanceLbl= context.findViewById(R.id.LbBalance);
        //list=(ListView)context.findViewById(R.id.List);
        btnExpense=context.findViewById(R.id.btnExpense);
        buttonSeeBudget=context.findViewById(R.id.button2);
        try {
            balance();
            pieChart=context.findViewById(R.id.piechar);


            list=(ListView)context.findViewById(R.id.List);
            setupPieChart();
            loadPieChartData();
            buttonSeeBudget.setVisibility(View.VISIBLE);
        }
        catch (Exception E){
            Toast.makeText(context,"Error loading data, we need more information",Toast.LENGTH_SHORT).show();
        }//en caso de error
        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res=DB.getdataexpense();
                if(res.getCount()==0)
                {
                    Toast.makeText(context,"No Entry Exists",Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Expense: "+ res.getString(1)+"\n");
                    buffer.append("Date: "+ res.getString(2)+"\n");
                    buffer.append("Category: "+ catVoid((res.getString(4))+"")+"\n");
                    buffer.append("Sub Category: "+ subcatVoid((res.getString(5))+"")+"\n");
                    //buffer.append("Sub Category: "+ ""+"\n");
                    buffer.append("Note: "+ res.getString(3)+"\n\n");
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Expenses registered");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

        buttonSeeBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arraylistsum = new ArrayList<String>(listFinalSumPerCat);
                ArrayList<String> arraylistcat = new ArrayList<String>(listFinalCat);
                //boolean ifanual=false;
                Intent i = new Intent(context, graphicBudgetActivity.class);
                i.putStringArrayListExtra("listFinalSumPerCat", arraylistsum);
                i.putStringArrayListExtra("listFinalCat", arraylistcat);
                //i.putExtra("ifanual", ifanual);
                startActivity(i);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//send list to the sub cat graphic
                if(position>0)//validacion para que no agarre el primero
                {
                    Intent i = new Intent(context, graphicSubCatActivity.class);//enviar datos
                    i.putExtra("idcategory", listFinalIdCat.get(position - 1));
                    startActivity(i);
                    //menos 1 porque hemos anadido una linea en la lista para lofs titulos
                }
            }
        });
    }
    public void onResume(){
        super.onResume();
        // put your code here...

    }

    /*private void refreshNavigationView(){

        getActivity().setContentView(R.layout.activity_main);
        NavigationView navigationView = (NavigationView)  getActivity().findViewById(R.id.navigationView);

    }*/
    /*public void onDestroy() {
        super.onDestroy();
        //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        //getFragmentManager().beginTransaction().remove(() ).commitAllowingStateLoss();


    }*/

    public void balance(){
        DB=new DBHelper(context);
        Cursor res=DB.getdatabalance();
        String resultado;
        if(res.getCount()==0)
        {
            balanceLbl.setText(0+"");
            DB.insertbalance(0);
        }
        else{
            //item = new ArrayList<String>();//para borrar
            if(res.moveToFirst()){
                double aux;
                do{                //para recorrer el cursor res hasta que no haya registros
                    aux=res.getDouble(1);
                    DecimalFormat df = new DecimalFormat("#.00");//this and the second are just to put two decimals in the double
                     resultado = df.format(aux);
                    balanceLbl.setText(resultado+" €");
                    //item.add(".- "+aux+" ");//para borrar
                }while(res.moveToNext());
            }
            //ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,item);//cargamos la lista de incomes pero es solo para probar por eso esta comentado
            //para almacenar los item en el adapter, lo use para hacer pruebas con una lista y ver lo que tenia
            //list.setAdapter(adapter);

            //balanceTxt.setText(222+"");
        }
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        //pieChart.setCenterText("Spending by category");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l=pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }


    private void loadPieChartData(){
        ArrayList<PieEntry> entries=new ArrayList<>();
        DB=new DBHelper(context);
        cleaner();
        //listFinalCat.clear();
        //listFinalSumPerCat.clear();
        //listFinalIdCat.clear();
        //listacatid.clear();
        Cursor responseExpensedate=DB.getdataexpensemonthly();
        if(responseExpensedate.getCount()==0)//bug cuando no hay datos
        {
            //Toast.makeText(context,"No Entry Exists",Toast.LENGTH_SHORT).show();
            return;//estereturn se tiene que ir pero pruebalo bien
        }
        if(responseExpensedate.moveToFirst()){//aqui trabaja choco, crea varios lista para almacenar los datos que queremos y muestralo en la torta
            //para recorrer el cursor res hasta que no haya registros
            do{
                //item.add(id+".- "+category+" - "+budget+" - "+responseExpensedate.getString(4));//estos 4 anterioes son para la prueba. borralo luego
                listaexpense.add(responseExpensedate.getString(1));//expense
                listadate.add(responseExpensedate.getString(2));//date
                listacatid.add(responseExpensedate.getString(4));//idcat
            }while(responseExpensedate.moveToNext());
        }
        //listSumAux
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
        item = new ArrayList<String>();
        item.add("Category  "+" Total Expended ");
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
        for (int i = 0; i < listFinalCat.size(); i++) {//to load the items into the list
            DecimalFormat df = new DecimalFormat("#.00");//this and the second are just to put two decimals in the double
            String resultado = df.format(Double.parseDouble(listFinalSumPerCat.get(i)));
            item.add(catVoid((listFinalIdCat.get(i)+""))+".- "+resultado);
        }


        //Toast.makeText(context,"No "+listFinalIdCat.size(),Toast.LENGTH_SHORT).show();//here we add into the list
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,item);
        //para almacenar los item en el adapter
        list.setAdapter(adapter);


        //from here is the pie
        for (int i = 0; i < listFinalSumPerCat.size(); i++) {
            String cat = catVoid((listFinalIdCat.get(i)+""));
            int auxfinalporcent =(int) Double.parseDouble(listFinalSumPerCat.get(i));
            String  auxfinalcat =cat;//(int) Double.parseDouble(listFinalCat.get(i))+
            entries.add(new PieEntry(auxfinalporcent,auxfinalcat));//el o,2f es el porcentaje
        }
        /*
        entries.add(new PieEntry(30,"Car..."));*/
        ArrayList<Integer> colors=new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for (int color:ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }
        PieDataSet dataSet=new PieDataSet(entries,"Category");
        dataSet.setColors(colors);

        PieData data=new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        DecimalFormat df = new DecimalFormat("#.00");//this and the second are just to put two decimals in the double
        String resultado = df.format(aux);
        pieChart.setCenterText(resultado+" €");//we put this here to update the total expense
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }





    public void cleaner(){
        listadate.clear();
        listacatid.clear();
        listaexpense.clear();
        listSumAux.clear();
        listSumAuxId.clear();
        listFinalCat.clear();
        listFinalIdCat.clear();
        listFinalSumPerCat.clear();
        aux=0;
    }

    String subcatVoid(String subCat){
        Cursor res=DB.getdatasubcategory(Integer.parseInt(subCat));//llamamos a la bd para rescatar el nombre de la cat
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
}
