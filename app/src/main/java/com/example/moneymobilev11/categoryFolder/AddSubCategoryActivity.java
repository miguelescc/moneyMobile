package com.example.moneymobilev11.categoryFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moneymobilev11.DBHelper;
import com.example.moneymobilev11.MainActivity;
import com.example.moneymobilev11.R;

public class AddSubCategoryActivity extends AppCompatActivity {

    EditText subcategory;
    Button insert;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_category);
        subcategory= findViewById(R.id.subcategory);
        insert=findViewById(R.id.btnAddSubCat);

        DB=new DBHelper(this);

        Bundle bundle=getIntent().getExtras();
        final int idCategory=bundle.getInt("idcategory");

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(subcategory.getText().toString().trim().length() > 0) {//validacion de textfield
                    String categoryTXT=subcategory.getText().toString();
                    Boolean checkinsertdata = DB.insertsubcategory(categoryTXT, idCategory);
                    if (checkinsertdata == true) {
                        Toast.makeText(AddSubCategoryActivity.this, "New Sub Category Inserted", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddSubCategoryActivity.this, SubcategoryUpdateCategoryActivity.class);//enviar datos
                        i.putExtra("idcategory", (idCategory+""));
                        startActivity(i);
                    } else {
                        Toast.makeText(AddSubCategoryActivity.this, "New Sub Category not Inserted", Toast.LENGTH_SHORT).show();
                    }
                }else{Toast.makeText(AddSubCategoryActivity.this,"Insert a Non Empty Value!! ",Toast.LENGTH_SHORT).show();}
                }catch (Exception e){
                    Toast.makeText(AddSubCategoryActivity.this,"Insert a valid value! "+e,Toast.LENGTH_SHORT).show();
                }
                //Intent i = new Intent(AddSubCategoryActivity.this,CategoriesActivity.class);//to go back after insert
                //startActivity(i);
            }
        });
    }




    public void Categories (View v)
    {
        Intent i = new Intent(AddSubCategoryActivity.this,MainActivity.class);
        startActivity(i);
    }
}
