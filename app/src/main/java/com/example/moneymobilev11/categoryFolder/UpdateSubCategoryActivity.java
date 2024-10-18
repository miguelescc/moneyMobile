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

public class UpdateSubCategoryActivity extends AppCompatActivity {


    EditText subcategorytxt;
    Button update,delete;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sub_category);
        update=findViewById(R.id.btnUpdateSave);
        delete=findViewById(R.id.btnDelete);
        DB=new DBHelper(this);

        subcategorytxt=findViewById(R.id.subcategory);
        Bundle bundle=getIntent().getExtras();

        //funcion pero el bindle hay que ver
        subcategorytxt.setText(bundle.getString("subcategory"));
        final int idsubcategory=Integer.parseInt(bundle.getString("idsubcategory"));


        //boton update
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(subcategorytxt.getText().toString().trim().length() > 0) {//validacion de textfield
                String subcategory=subcategorytxt.getText().toString();
                Boolean checkupdatedata=DB.updatesubcategory(idsubcategory,subcategory);
                if(checkupdatedata==true){
                    Toast.makeText(UpdateSubCategoryActivity.this,"Category Updated",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UpdateSubCategoryActivity.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(UpdateSubCategoryActivity.this,"Category not Updated",Toast.LENGTH_SHORT).show();
                }
                }else{Toast.makeText(UpdateSubCategoryActivity.this,"Insert a Non Empty Value!! ",Toast.LENGTH_SHORT).show();}
                }catch (Exception e){
                    Toast.makeText(UpdateSubCategoryActivity.this,"Insert a valid value! "+e,Toast.LENGTH_SHORT).show();
                }
            }
        });

        //delete
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checkdeletedata=DB.deletesubcategory(idsubcategory);
                if(checkdeletedata==true){
                    Toast.makeText(UpdateSubCategoryActivity.this,"Sub Category Deleted",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UpdateSubCategoryActivity.this,MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(UpdateSubCategoryActivity.this,"Category not Deleted",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void Categories (View v)
    {
        Intent i = new Intent(UpdateSubCategoryActivity.this, MainActivity.class);
        startActivity(i);
    }
}