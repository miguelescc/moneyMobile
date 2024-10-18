package com.example.moneymobilev11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class balanceActivity extends AppCompatActivity {

    TableLayout tableBalance;
    //var tableBalance:TableLayout?=null

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        tableBalance=findViewById(R.id.tableBalance);
        tableBalance.removeAllViews();
        String [] tbBalance={"asd","qqq","wwwwww"};
        String [] tbBalancee={"asd","qqq","wwwwww"};
        //tableBalance.setColumnCollapsed();
        addTable();

    }

    public void addTable(){
        TableLayout TL=(TableLayout)findViewById(R.id.tableBalance);
        TableRow tbRow0=new TableRow(this);
        TextView txt0=new TextView(this);
        txt0.setText(" ");
        txt0.setTextSize(24);
        txt0.setTextColor(Color.BLACK);
        tbRow0.addView(txt0);
        TextView txt1=new TextView(this);
        txt1.setText(" Total");
        txt1.setTextSize(24);
        txt1.setTextColor(Color.BLACK);
        tbRow0.addView(txt1);
        TextView txt2=new TextView(this);
        txt2.setText(" Average");
        txt2.setTextSize(24);
        txt2.setTextColor(Color.BLACK);
        tbRow0.addView(txt2);
        TL.addView(tbRow0);

        for (int i=0;i<3;i++){
            TableRow tbRow=new TableRow(this);
            TextView txtData0=new TextView(this);
            txtData0.setText(" Income or Expense");
            txtData0.setTextColor(Color.BLACK);
            tbRow.addView(txtData0);
            TextView txtData1=new TextView(this);
            txtData1.setText(" data ");
            txtData1.setTextColor(Color.BLACK);
            tbRow.addView(txtData1);
            TextView txtData2=new TextView(this);
            txtData2.setText(" data");
            txtData2.setTextColor(Color.BLACK);
            tbRow.addView(txtData2);
            TL.addView(tbRow);
        }
    }


    public void Mainn (View v)
    {
        Intent i = new Intent(balanceActivity.this, MainActivity.class);
        startActivity(i);
    }
}
