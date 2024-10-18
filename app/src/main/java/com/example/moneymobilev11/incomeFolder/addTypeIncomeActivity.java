package com.example.moneymobilev11.incomeFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moneymobilev11.DBHelper;
import com.example.moneymobilev11.R;

public class addTypeIncomeActivity extends AppCompatActivity {
    EditText typeincome;
    Button insert,updatebtn,deletebtn;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type_income);

        typeincome= findViewById(R.id.TypeIncomeTxt);
        insert=findViewById(R.id.btnAddTypeIncome);
        updatebtn=findViewById(R.id.btnUpdateTypeIncome);
        deletebtn=findViewById(R.id.btnDeleteTypeIncome);
        int idtypeIncome=0;
        DB=new DBHelper(this);
        try {
            Bundle bundle=getIntent().getExtras();
            if(bundle.getString("idincometype")!=null)
                idtypeIncome=Integer.parseInt(bundle.getString("idincometype"));
            typeincome.setText(bundle.getString("incometype"));
            insert.setVisibility(View.GONE);
            updatebtn.setVisibility(View.VISIBLE);
            deletebtn.setVisibility(View.VISIBLE);
        }catch (Exception ex)
        {
            insert.setVisibility(View.VISIBLE);
            updatebtn.setVisibility(View.GONE);
            deletebtn.setVisibility(View.GONE);
        }
        final int idincome=idtypeIncome;

        //boton update
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeincomeTXT=typeincome.getText().toString();
                Boolean checkupdatedata=DB.updatetypeincome(idincome,typeincomeTXT);//continuar en DBhelper
                if(checkupdatedata==true){
                    Toast.makeText(addTypeIncomeActivity.this,"Type of Income Updated",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(addTypeIncomeActivity.this, typeIncomeActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(addTypeIncomeActivity.this,"Type of Income not Updated",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //delete
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checkdeletedata=DB.deletetypeincome(idincome);
                if(checkdeletedata==true){
                    Toast.makeText(addTypeIncomeActivity.this,"Type of Income Deleted",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(addTypeIncomeActivity.this,typeIncomeActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(addTypeIncomeActivity.this,"Type of Income not Deleted",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void insertType(View V){
        String typeincomeTXT=typeincome.getText().toString();
        Boolean checkinsertdata=DB.inserttypeincome(typeincomeTXT);
        if(checkinsertdata==true){
            Toast.makeText(addTypeIncomeActivity.this,"New Type of Income Inserted",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(addTypeIncomeActivity.this,"New Type of Income not Inserted",Toast.LENGTH_SHORT).show();
        }
        Intent i = new Intent(addTypeIncomeActivity.this, typeIncomeActivity.class);//to go back after insert
        startActivity(i);
    }


    public void TypeIncome (View v)
    {
        Intent i = new Intent(addTypeIncomeActivity.this, typeIncomeActivity.class);
        startActivity(i);
    }
}
