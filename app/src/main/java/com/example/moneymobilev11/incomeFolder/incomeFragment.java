package com.example.moneymobilev11.incomeFolder;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.moneymobilev11.DBHelper;
import com.example.moneymobilev11.MainActivity;
import com.example.moneymobilev11.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class incomeFragment extends Fragment {
    DBHelper DB;
    List<String> item=null;
    EditText income;
    Button insert,btnadd;
    Spinner spinner;
    static int idincomeaux=0;
    List<Integer> lista = new ArrayList<Integer>();// esta es para almacenar el id, //una variable lista para el listener para comparar con el combobox



    Activity context;
    public incomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getActivity();//el context para cargar las cosas del activity en el fragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    public void onStart() {
        super.onStart();
        income= context.findViewById(R.id.IncomeTxt);
        insert=context.findViewById(R.id.btnputincome);
        spinner = context.findViewById(R.id.spinnerTypeIncome);
        btnadd=context.findViewById(R.id.btnAdd);
        loadSpinnerData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//al hacer click en el spinner obtenemos un evento y podemos ejecutar lo que querramos dentro, lo estoy usando para guardar el id
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){//esta es la validacion para que no selecciona el 0 de la lista y se tire
                    //Toast.makeText(context,"prueba, positi.... "+lista.get(position-1),Toast.LENGTH_SHORT).show();
                    idincomeaux= lista.get(position-1); //Aqui tenemos el ID!!!!
                    //income.setText(lista.get(position-1)+"");
                    insert.setVisibility(View.VISIBLE);
                    //aqui habilida recien el boton para ingresar
                }
                else {
                    //oculta el boton de vuelta para que no haya error
                    insert.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double auxincome=Double.parseDouble(income.getText().toString());
                DB=new DBHelper(context);
                boolean boo= DB.insertincome(auxincome,idincomeaux);
                if(boo==true&&auxincome>=0){
                    Toast.makeText(context,"id "+idincomeaux+" "+auxincome,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, MainActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(context,"false, it has not been insarted "+idincomeaux+" "+auxincome,Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, typeIncomeActivity.class);
                startActivity(i);
            }
        });

    }


    public void loadSpinnerData(){
        lista.clear();
        DB=new DBHelper(context);
        Cursor res=DB.getdatatypeincomes();
        if(res.getCount()==0)
        {
            Toast.makeText(context,"No Type of Incomes registered",Toast.LENGTH_SHORT).show();
            return;
        }
        String typeincome="",id="";
        item = new ArrayList<String>();
        item.add("  -  "+"Type of Income  ");
        if(res.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                id=res.getString(0);
                typeincome=res.getString(1);
                item.add(id+".- "+typeincome);
                lista.add(Integer.parseInt(res.getString(0)));
            }while(res.moveToNext());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,item);
        spinner.setAdapter(adapter);        //para almacenar los item en el adapter
    }



}
