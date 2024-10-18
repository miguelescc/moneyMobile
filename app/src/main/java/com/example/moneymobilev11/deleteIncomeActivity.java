package com.example.moneymobilev11;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.moneymobilev11.categoryFolder.AddSubCategoryActivity;

import java.util.ArrayList;
import java.util.List;

public class deleteIncomeActivity extends AppCompatActivity {



    DBHelper DB;
    ListView list;
    List<String> item=null;
    List<String> listincomeid = new ArrayList<String>();//to save into the list
    List<String> listincome = new ArrayList<String>();
    List<String> listincomeType = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_income);
        list=(ListView)this.findViewById(R.id.List);
        loadincomedata();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//send list to the sub cat graphic
                if(position>0)//validacion para que no agarre el primero
                {
                    final int which_item=position;
                    new AlertDialog.Builder(deleteIncomeActivity.this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this Income?")
                            .setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Boolean checkdeletedata=DB.deleteincome(Integer.parseInt(listincomeid.get(which_item - 1)));
                                    if(checkdeletedata==true){
                                        DB.insertbalance(-(Double.parseDouble(listincome.get(which_item-1))));//aqui insertamos como expense para reducir el balance pero hay que poner una nota al hacer este registro cuando tengamos el campo notas en el income
                                        Toast.makeText(deleteIncomeActivity.this,"Income Deleted and Balance restored!",Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(deleteIncomeActivity.this,deleteIncomeActivity.class);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(deleteIncomeActivity.this,"Income not Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No ",null).show();
                }
            }
        });
    }






    public void menuMain (View v)
    {
        Intent i = new Intent(deleteIncomeActivity.this,MainActivity.class);
        startActivity(i);
    }


    public void loadincomedata(){
        DB=new DBHelper(this);
        Cursor responseIncomeList=DB.getdataincomes();
        if(responseIncomeList.getCount()==0)//bug cuando no hay datos
        {
            Toast.makeText(this,"No Income Exists",Toast.LENGTH_SHORT).show();
            return;
        }
        item = new ArrayList<String>();
        item.add("Income  "+" Income Type ");
        if(responseIncomeList.moveToFirst()){//aqui trabaja choco, crea varios lista para almacenar los datos que queremos y muestralo en la torta
            //para recorrer el cursor res hasta que no haya registros
            do{
                //item.add(id+".- "+category+" - "+budget+" - "+responseExpensedate.getString(4));//estos 4 anterioes son para la prueba. borralo luego
                listincomeid.add(responseIncomeList.getString(0));//income id
                listincome.add(responseIncomeList.getString(1));//income
                listincomeType.add(responseIncomeList.getString(2));//type
                item.add(".- "+responseIncomeList.getString(1)+" - "+responseIncomeList.getString(2));
            }while(responseIncomeList.moveToNext());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
        list.setAdapter(adapter);
    }
}
