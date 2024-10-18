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

public class AddCategoryActivity extends AppCompatActivity {

    EditText budget,category;
    Button insert;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        category = findViewById(R.id.category);
        budget= findViewById(R.id.budget);
        insert=findViewById(R.id.btnAddCat);

        DB=new DBHelper(this);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(category.getText().toString().trim().length() > 0) {//validacion de textfield
                        String categoryTXT = category.getText().toString();
                        Double budgetTXT = Double.parseDouble(budget.getText().toString());
                        int checkinsertdata = DB.insertcategory(categoryTXT, budgetTXT);
                        if (checkinsertdata > 0) {
                            Toast.makeText(AddCategoryActivity.this, "New Category Inserted", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AddCategoryActivity.this, SubcategoryUpdateCategoryActivity.class);//enviar datos
                            i.putExtra("idcategory", (checkinsertdata+""));
                            startActivity(i);
                        } else {
                            Toast.makeText(AddCategoryActivity.this, "New Category not Inserted", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AddCategoryActivity.this, MainActivity.class);//to go back after insert
                            startActivity(i);
                        }

                    }else{Toast.makeText(AddCategoryActivity.this,"Insert a Non Empty Value!! ",Toast.LENGTH_SHORT).show();}
                }catch (Exception e){
                    Toast.makeText(AddCategoryActivity.this,"Insert a valid value! "+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void Categories (View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
