package com.example.moneymobilev11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class deleteExpensesActivity extends AppCompatActivity {

    DBHelper DB;
    ListView list,list1,list2,list3,list4;
    List<String> item=null;
    List<String> item1=null;List<String> item2=null;List<String> item3=null;List<String> item4=null;
    List<String> listexpenseid = new ArrayList<String>();//to save into the list
    List<String> listexpense = new ArrayList<String>();
    List<String> listexpenseCat = new ArrayList<String>();
    List<String> listexpenseSubCat = new ArrayList<String>();
    List<String> listexpenseDate = new ArrayList<String>();
    List<String> listexpenseNote = new ArrayList<String>();

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expenses);
        Bundle bundle=getIntent().getExtras();
        //Boolean ifdelete=bundle.getBoolean("ifdelete");//line to delete, it was givin problems, check what where you trying to do
        list=(ListView)this.findViewById(R.id.List0);
        textView=(TextView)findViewById(R.id.texttitle);

        loadincomedata();

        //if(ifdelete==true){// commented due to an error when deleting expenses //line to delete, it was givin problems, check what where you trying to do
            textView.setText("Select an Expense to Delete....");
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//send list to the sub cat graphic
                if(position>0)//validacion para que no agarre el primero
                {
                    final int which_item=position;
                    new AlertDialog.Builder(deleteExpensesActivity.this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this Expense?")
                            .setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Boolean checkdeletedata=DB.deleteexpense(Integer.parseInt(listexpenseid.get(which_item - 1)));
                                    if(checkdeletedata==true){
                                            DB.insertbalance((Double.parseDouble(listexpense.get(which_item-1))));//aqui insertamos como expense para reducir el balance pero hay que poner una nota al hacer este registro cuando tengamos el campo notas en el income
                                            Toast.makeText(deleteExpensesActivity.this,"Expense Deleted and Balance restored!",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(deleteExpensesActivity.this,deleteExpensesActivity.class);
                                            startActivity(i);
                                    }else{
                                        Toast.makeText(deleteExpensesActivity.this,"Expense not Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No ",null).show();
                    }
                }
        });
        /*}else { // commented due to an error when deleting expenses  //line to delete, it was givin problems, check what where you trying to do
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                StringBuffer buffer=new StringBuffer();

                buffer.append("Date: "+listexpenseDate.get(position-1)+"\n");
                buffer.append("Amount: "+listexpense.get(position-1)+"\n");
                buffer.append("Category: "+listexpenseCat.get(position-1)+"\n");
                buffer.append("Sub-Category: "+listexpenseSubCat.get(position-1)+"\n");
                buffer.append("Aditional note: "+listexpenseNote.get(position-1)+"\n\n");

                AlertDialog.Builder builder=new AlertDialog.Builder(deleteExpensesActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Expenditure Made ");
                builder.setMessage(buffer.toString());
                builder.show();
                }
            }
        });
        }*/ // commented due to an error when deleting expenses, last line

    }


    public void menuMain (View v)
    {
        Intent i = new Intent(deleteExpensesActivity.this,MainActivity.class);
        startActivity(i);
    }
    public void loadincomedata(){
        DB=new DBHelper(this);
        Cursor responseEspenseList=DB.getdataexpense();
        if(responseEspenseList.getCount()==0)//bug cuando no hay datos
        {
            Toast.makeText(this,"No Expense Exists",Toast.LENGTH_SHORT).show();
            return;
        }
        item = new ArrayList<String>();
        //item.add("Amount ");
        item.add("Date and Time           Amount     Category ");
        //item1 = new ArrayList<String>();
        //item1.add("Category ");
        //item2 = new ArrayList<String>();
        //item2.add("Date ");
            //item3 = new ArrayList<String>();//already commented, useless
            //item3.add("Sub Category ");
        //item4 = new ArrayList<String>();
        //item4.add("More");
        if(responseEspenseList.moveToFirst()){//aqui trabaja choco, crea varios lista para almacenar los datos que queremos y muestralo en la torta
            //para recorrer el cursor res hasta que no haya registros
            do{
                listexpenseid.add(responseEspenseList.getString(0));//expense id
                listexpense.add(responseEspenseList.getString(1));//expense
                String auxcat=catVoid(responseEspenseList.getString(4));//cat
                listexpenseCat.add(auxcat);
                listexpenseNote.add(responseEspenseList.getString(3));//notes
                String auxsubcat=subcatVoid(responseEspenseList.getString(5));//sub cat
                listexpenseSubCat.add(auxsubcat);
                listexpenseDate.add((responseEspenseList.getString(2)));//date


                item.add(responseEspenseList.getString(2)+" - "+responseEspenseList.getString(1)+" - "+auxcat);
                //item.add(responseEspenseList.getString(1));
                //item1.add(auxcat);
                //item2.add(responseEspenseList.getString(2));
                        //item3.add(auxsubcat);
                //item4.add("See more");
            }while(responseEspenseList.moveToNext());

            //items.add("1 , Hello11 , Hello12");
        //griditems[1][1]="asd";
        }

        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
        //list.setAdapter(adapter);
        //ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item1);
        //list1.setAdapter(adapter1);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);//item2
        list.setAdapter(adapter2);//if you go back this is the list 2
                //ArrayAdapter<String> adapter3=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item3);//these alrady disabled
                //list3.setAdapter(adapter3);
        //ArrayAdapter<String> adapter4=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item4);
        //list4.setAdapter(adapter4);
    }







    String catVoid(String i){
        //Toast.makeText(this,"No Expense Exists"+i,Toast.LENGTH_SHORT).show();
        Cursor res=DB.getdatacategory(Integer.parseInt(i));//llamamos a la bd para rescatar el nombre de la cat
        String cat="";
        if(res.moveToFirst()){
            do{
                cat=res.getString(1);
            }while(res.moveToNext());
        }
        return cat;
    }
    String subcatVoid(String i){
        Cursor res=DB.getdatasubcategory(Integer.parseInt(i));//llamamos a la bd para rescatar el nombre de la cat
        String subcat="";
        if(res.moveToFirst()){
            do{
                subcat=res.getString(1);
            }while(res.moveToNext());
        }
        return subcat;
    }
}
