package com.example.moneymobilev11.incomeFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.moneymobilev11.DBHelper;
import com.example.moneymobilev11.MainActivity;
import com.example.moneymobilev11.R;

import java.util.ArrayList;
import java.util.List;

public class typeIncomeActivity extends AppCompatActivity {
    DBHelper DB;
    ListView list;
    List<String> item=null;
    List<String> lista = new ArrayList<String>();// esta es para almacenar el id, //una variable lista para el listener
    List<String> lista2 = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_income);
        list=(ListView)findViewById(R.id.ListTypeIn);
        showList();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)//validacion
                {
                    Intent i = new Intent(typeIncomeActivity.this, addTypeIncomeActivity.class);//enviar datos
                    i.putExtra("idincometype", lista.get(position - 1));
                    i.putExtra("incometype", lista2.get(position - 1));
                    startActivity(i);
                    Toast.makeText(typeIncomeActivity.this, "LISTA " + lista.get(position - 1), Toast.LENGTH_SHORT).show();
                    //menos 1 porque hemos anadido una linea en la lista para los titulos
                }
            }
        });
    }

    private void showList(){
        lista.clear();
        lista2.clear();
        DB=new DBHelper(this);
        Cursor res=DB.getdatatypeincomes();
        if(res.getCount()==0)
        {
            Toast.makeText(typeIncomeActivity.this,"No Entry Exists",Toast.LENGTH_SHORT).show();
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
                lista.add(res.getString(0));
                lista2.add(res.getString(1));
            }while(res.moveToNext());
        }


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
        //para almacenar los item en el adapter
        list.setAdapter(adapter);
    }


    public void Mainn (View v)
    {
        Intent i = new Intent(typeIncomeActivity.this, MainActivity.class);
        startActivity(i);
    }
    public void addtypeActivity (View v)
    {
        Intent i = new Intent(typeIncomeActivity.this, addTypeIncomeActivity.class);
        startActivity(i);
    }
}