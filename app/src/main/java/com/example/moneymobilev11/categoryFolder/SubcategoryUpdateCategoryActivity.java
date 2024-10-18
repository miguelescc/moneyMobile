package com.example.moneymobilev11.categoryFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneymobilev11.DBHelper;
import com.example.moneymobilev11.MainActivity;
import com.example.moneymobilev11.R;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryUpdateCategoryActivity extends AppCompatActivity {

    EditText budget,category;
    TextView idcategory;
    Button update,delete,updatedisable;
    DBHelper DB;

    ListView list;
    List<String> item=null;

    List<String> lista = new ArrayList<String>();//una variable lista para el listener
    List<String> lista2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_update_category);
        idcategory=findViewById(R.id.txtidcategory);
        category = findViewById(R.id.category);
        budget= findViewById(R.id.budget);

        updatedisable=findViewById(R.id.btnUpdateDisable);
        update=findViewById(R.id.btnUpdateSave);
        delete=findViewById(R.id.btnDelete);
        DB=new DBHelper(this);

        list=(ListView)findViewById(R.id.List);


        Bundle bundle=getIntent().getExtras();

        //funcion pero el bindle hay que ver



            idcategory.setText(bundle.getString("idcategory"));
            final int auxid = Integer.parseInt(idcategory.getText().toString());
            cargarDatos(auxid);//en las cajitas a partir del id que hemos recibido
        showList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)//validacion
                {
                    //String pos = list.getItemAtPosition(position).toString();// no utilizado
                    Intent i = new Intent(SubcategoryUpdateCategoryActivity.this, UpdateSubCategoryActivity.class);//enviar datos
                    i.putExtra("idsubcategory", lista.get(position - 1));
                    i.putExtra("subcategory", lista2.get(position - 1));
                    startActivity(i);
                    Toast.makeText(SubcategoryUpdateCategoryActivity.this, "LISTA " + lista.get(position - 1), Toast.LENGTH_SHORT).show();
                    //menos 1 porque hemos anadido una linea en la lista para los titulos
                }
            }
        });

        //boton update
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(category.getText().toString().trim().length() > 0) {//validacion de textfield
                String categoryTXT=category.getText().toString();
                double  budgetTXT= Double.parseDouble(budget.getText().toString());
                Boolean checkupdatedata=DB.updatecategory(auxid,categoryTXT,budgetTXT);
                    if(checkupdatedata==true){
                        Toast.makeText(SubcategoryUpdateCategoryActivity.this,"Category Updated",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SubcategoryUpdateCategoryActivity.this, MainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(SubcategoryUpdateCategoryActivity.this,"Category not Updated",Toast.LENGTH_SHORT).show();
                    }
                    }else{Toast.makeText(SubcategoryUpdateCategoryActivity.this,"Insert a Non Empty Value!! ",Toast.LENGTH_SHORT).show();}
                }catch (Exception e){
                    Toast.makeText(SubcategoryUpdateCategoryActivity.this,"Insert a valid value! "+e,Toast.LENGTH_SHORT).show();
                }
            }
        });

        //delete
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checkdeletedata=DB.deletecategory(auxid);
                if(checkdeletedata==true){
                    Toast.makeText(SubcategoryUpdateCategoryActivity.this,"Category Deleted",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SubcategoryUpdateCategoryActivity.this,MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(SubcategoryUpdateCategoryActivity.this,"Category not Deleted",Toast.LENGTH_SHORT).show();
                }
            }
        });

}
    public void cargarDatos(int auxid){
        Cursor res=DB.getdatacategory(auxid);
        int idcat;
        String cat;
        double budg;
        if(res.moveToFirst()){
            do{
                idcat=res.getInt(0);
                cat=res.getString(1);
                budg=res.getDouble(2);
            }while(res.moveToNext());
            category.setText(cat);
            budget.setText(budg+"");

        }
    }

    //botones
    public void updateenable (View v)
    {

        category.setEnabled(true);
        budget.setEnabled(true);

        updatedisable.setVisibility(View.GONE);
        delete.setVisibility(View.VISIBLE);
        update.setVisibility(View.VISIBLE);
    }


    //nuevas ventanas
    public void Categories (View v)
    {
        Intent i = new Intent(SubcategoryUpdateCategoryActivity.this,MainActivity.class);
        startActivity(i);
    }
    public void AddSubcategory (View v)
    {
        Intent i = new Intent(SubcategoryUpdateCategoryActivity.this, AddSubCategoryActivity.class);
        i.putExtra("idcategory", Integer.parseInt(idcategory.getText().toString()));
        startActivity(i);
    }






    private void showList(){

        lista.clear();//lista sirve para guardar los id en una lista para obtenerlo mas tarde
        DB=new DBHelper(this);
        Cursor res=DB.getdatasubcategories(Integer.parseInt(idcategory.getText().toString()));
        if(res.getCount()==0)
        {
            Toast.makeText(SubcategoryUpdateCategoryActivity.this,"No Entry Exists",Toast.LENGTH_SHORT).show();
            return;
        }
        String subcategory="";
        int idsub;
        item = new ArrayList<String>();
        item.add("    "+"Sub Category  ");//titulos
        if(res.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                idsub=res.getInt(0);
                subcategory=res.getString(1);
                item.add(idsub+".- "+subcategory+" ");
                lista.add(res.getString(0));
                lista2.add(res.getString(1));
            }while(res.moveToNext());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
        //para almacenar los item en el adapter
        list.setAdapter(adapter);
    }
}
