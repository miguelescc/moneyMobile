package com.example.moneymobilev11;


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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class expenseFragment extends Fragment {
    Activity context;

    DBHelper DB;
    List<String> item=null;
    EditText expense,note;
    Button insert,btnputexpense,btnexpenselist;
    Spinner spinner,spinner2;
    TextView txtNoSubCat;

    List<Integer> lista = new ArrayList<Integer>();
    List<Integer> lista2 = new ArrayList<Integer>();// esta es para almacenar el id, //una variable lista para el listener para comparar con el combobox
    static int Auxid,Auxsubid;

    public expenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getActivity();//el context para cargar las cosas del activity en el fragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense, container, false);
    }
    public void onStart() {
        super.onStart();
        expense= context.findViewById(R.id.ExpensesTxt);
        note=context.findViewById(R.id.noteTxt);
        insert=context.findViewById(R.id.btnputexpense);
        spinner = context.findViewById(R.id.spinnerCategory);
        spinner2 = context.findViewById(R.id.spinnerSubCategory);
        txtNoSubCat=context.findViewById(R.id.txtNoSubCat);
        btnputexpense=context.findViewById(R.id.btnputexpense);
        btnexpenselist=context.findViewById(R.id.btnexpenseslist);

        loadSpinnerData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//al hacer click en el spinner obtenemos un evento y podemos ejecutar lo que querramos dentro, lo estoy usando para guardar el id
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){//esta es la validacion para que no selecciona el 0 de la lista y se tire
                    //Toast.makeText(context,"prueba, positi.... "+lista.get(position-1),Toast.LENGTH_SHORT).show();
                    //lista.get(position-1) //Aqui tenemos el ID!!!!
                    //note.setText(lista.get(position-1)+"");
                    loadSpinner2Data(lista.get(position-1));
                    //te falto guardar en variable el id para guardar al final
                    //habilita recien el spinner 2
                    spinner2.setVisibility(View.VISIBLE);
                    Auxid=lista.get(position-1);

                }
                else {
                    //oculta el boton de vuelta para que no haya error
                    insert.setVisibility(View.GONE);
                    spinner2.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //aqui habilida recien el boton para ingresar, es el spinner dos para extraer los datos
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//al hacer click en el spinner obtenemos un evento y podemos ejecutar lo que querramos dentro, lo estoy usando para guardar el id
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){//esta es la validacion para que no selecciona el 0 de la lista y se tire
                    //Toast.makeText(ExpensesActivity.this,"prueba, positi.... "+lista2.get(position-1),Toast.LENGTH_SHORT).show();
                    //lista.get(position-1) //Aqui tenemos el ID!!!!
                    //note.setText(lista2.get(position-1)+"");
                    Auxsubid=lista2.get(position-1);
                    insert.setVisibility(View.VISIBLE);
                }
                else {
                    //oculta el boton de vuelta para que no haya error
                    insert.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btnputexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Auxnote=note.getText().toString();
                double auxExpense=Double.parseDouble(expense.getText().toString());
                if(auxExpense>=0){
                    DB=new DBHelper(context);
                    Boolean checkinsertdataexpense=DB.insertexpense(auxExpense,Auxnote,Auxid,Auxsubid);


                    Intent i = new Intent(context, MainActivity.class);
                    startActivity(i);
                    //Toast.makeText(context,"expen: "+auxExpense+"note: "+Auxnote+"id cat: "+Auxid+"idsub: "+Auxsubid,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"You have to insert a number in Expense......"+Auxsubid,Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnexpenselist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ifdelete=false;
                Intent i = new Intent(context, deleteExpensesActivity.class);
                i.putExtra("ifdelete", ifdelete);
                startActivity(i);
            }
        });

    }


    public void loadSpinnerData(){
        lista.clear();
        DB=new DBHelper(context);
        Cursor res=DB.getdatacategories();
        if(res.getCount()==0)
        {
            Toast.makeText(context,"No Categories registered",Toast.LENGTH_SHORT).show();
            return;
        }
        String category="",id="";
        item = new ArrayList<String>();
        item.add("  -  "+"Categories  ");
        if(res.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                id=res.getString(0);
                category=res.getString(1);
                item.add(id+".- "+category);
                lista.add(Integer.parseInt(res.getString(0)));
            }while(res.moveToNext());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,item);
        spinner.setAdapter(adapter);        //para almacenar los item en el adapter
    }
    //segundo spinner
    public void loadSpinner2Data(int idsub){
        lista2.clear();
        DB=new DBHelper(context);
        Cursor res=DB.getdatasubcategories(idsub);
        if(res.getCount()==0)
        {
            txtNoSubCat.setVisibility(View.VISIBLE);
            Toast.makeText(context,"No Sub Categories registered",Toast.LENGTH_SHORT).show();
            return;
        }
        txtNoSubCat.setVisibility(View.GONE);
        String subcategory="",id="";
        item = new ArrayList<String>();
        item.add("  -  "+"Sub Categories  ");
        if(res.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                id=res.getString(0);
                subcategory=res.getString(1);
                item.add(id+".- "+subcategory);
                lista2.add(Integer.parseInt(res.getString(0)));
            }while(res.moveToNext());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,item);
        spinner2.setAdapter(adapter);        //para almacenar los item en el adapter
    }





}
