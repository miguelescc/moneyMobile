package com.example.moneymobilev11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class backupActivity extends AppCompatActivity {

    Button btnBackup,btnRestore,btnExcel;
    DBHelper DB;
    //List<String> item=null;
    ListView list;
    List<String> listexpenseid = new ArrayList<String>();//to save into the list
    List<String> listexpense = new ArrayList<String>();
    List<String> listexpenseCat = new ArrayList<String>();
    List<String> listexpenseSubCat = new ArrayList<String>();
    List<String> listexpenseDate = new ArrayList<String>();
    List<String> listexpenseNote = new ArrayList<String>();
    List<String> listincome = new ArrayList<String>();
    List<String> listincomeId = new ArrayList<String>();
    List<String> listincomeType = new ArrayList<String>();

    //private static final int STORAGE_REQUEST_CODE_EXPORT=100;
    //private static final int STORAGE_REQUEST_CODE_IMPORT=2;
    private String[] storagePermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        btnBackup=(Button) findViewById(R.id.btnBackup);
        btnRestore=(Button) findViewById(R.id.btnRestore);
        btnExcel=(Button) findViewById(R.id.btnExcel);

        //storagePermissions =new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(backupActivity.this, "Backup Button", Toast.LENGTH_SHORT).show();
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    //File sd= new File(Environment.getExternalStorageDirectory()+"/"+"SQLiteBackup");//SQLiteBackup is folder name
                    boolean isFolderCreated=false;
                    if (!sd.exists()){
                        isFolderCreated=sd.mkdir();
                    }
                    File data = Environment.getDataDirectory();
                    if (sd.canWrite()) {
                        String currentDBPath = "//data//com.example.moneymobilev11//databases//MoneyData.db";
                        String backupDBPath = "MoneyData.db";

                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        Log.d("backupDB path", "" + backupDB.getAbsolutePath());

                        if (currentDB.exists()) {
                            FileChannel src = new     FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                            Toast.makeText(getApplicationContext(), "Backup is successful to SD card", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*if(checkStoragePermission()){
                    exportCSV();
                }else {
                    requestStoragePermissionExport();
                }*/

            }
        });
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(backupActivity.this, "Restore Button", Toast.LENGTH_SHORT).show();

                try {
                    File sd = Environment.getExternalStorageDirectory();
                    //File sd= new File(Environment.getExternalStorageDirectory()+"/"+"SQLiteBackup");//SQLiteBackup is folder name
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        String currentDBPath = "//data//com.example.moneymobilev11//databases//MoneyData.db";;
                        String backupDBPath = "MoneyData.db";
                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(backupDB).getChannel();
                            FileChannel dst = new FileOutputStream(currentDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                            Toast.makeText(getApplicationContext(), "Database Restored successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*if(checkStoragePermission()){
                    exportCSV();
                }else {
                    requestStoragePermissionImport();
                }*/
            }
        });
        btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(backupActivity.this, "Excel Button", Toast.LENGTH_SHORT).show();

                loadExcel();


            }
        });
    }


    public void loadExcel(){
        cleaner();

        HSSFWorkbook workbook = new HSSFWorkbook();
        Cell cell = null;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        HSSFSheet sheet = null;
        sheet = workbook.createSheet("Sheet1 test");
        HSSFRow row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Expense");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellValue("Date");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(3);
        cell.setCellValue("Note");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(4);
        cell.setCellValue("Category");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(5);
        cell.setCellValue("Sub Category");
        cell.setCellStyle(cellStyle);
        sheet.setColumnWidth(0,(10*200));
        sheet.setColumnWidth(1,(10*200));
        DB=new DBHelper(backupActivity.this);
        int aux=0;
        Cursor responseList=DB.getdataexpense();
        if(responseList.getCount()==0)//bug cuando no hay datos
        {
            Toast.makeText(backupActivity.this,"No Data Exists",Toast.LENGTH_SHORT).show();
            return;
        }
        if(responseList.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                listexpenseid.add(responseList.getString(0));//expense id
                listexpense.add(responseList.getString(1));//expense
                String auxcat=catVoid(responseList.getString(4));//cat
                listexpenseCat.add(auxcat);
                listexpenseNote.add(responseList.getString(3));//notes
                String auxsubcat=subcatVoid(responseList.getString(5));//sub cat
                listexpenseSubCat.add(auxsubcat);
                listexpenseDate.add((responseList.getString(2)));//date
            }while(responseList.moveToNext());



            for (int i = 0; i < listexpenseCat.size(); i++) {
                        row = sheet.createRow((i+1));
                        cell = row.createCell(0);
                        cell.setCellValue(listexpenseid.get(i));
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(1);
                        cell.setCellValue(listexpense.get(i));
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(2);
                        cell.setCellValue(listexpenseDate.get(i));
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(3);
                        cell.setCellValue(listexpenseNote.get(i));
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(4);
                        cell.setCellValue(listexpenseCat.get(i));
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(5);
                        cell.setCellValue(listexpenseSubCat.get(i));
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(6);
                        cell.setCellValue("Expense");
                        cell.setCellStyle(cellStyle);

                sheet.setColumnWidth(0,(10*200));
                sheet.setColumnWidth(1,(10*200));
                sheet.setColumnWidth(2,(10*200));
                sheet.setColumnWidth(3,(10*200));
                sheet.setColumnWidth(4,(10*200));
                sheet.setColumnWidth(5,(10*200));

                aux=i;
            }
        }
        Cursor responseListIncome=DB.getdataincomes();//crear las nuevas listas
        if(responseListIncome.getCount()==0)//bug cuando no hay datos
        {
            Toast.makeText(backupActivity.this,"No Data Exists",Toast.LENGTH_SHORT).show();
            return;
        }
        if(responseListIncome.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                listincomeId.add(responseListIncome.getString(0));
                listincome.add(responseListIncome.getString(1));//income

                listincomeType.add((responseListIncome.getString(2)));//tipe
            }while(responseListIncome.moveToNext());
        }
        for (int i = 0; i < listincome.size(); i++) {
            //currentRow.setHeight((short)-1)
            //row = currSht.getRow(rowNum);
            row = sheet.createRow((i+aux+1));
            cell = row.createCell(0);
            cell.setCellValue(listincomeId.get(i));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(listincome.get(i));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(listincomeType.get(i));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(6);
            cell.setCellValue("Income");
            cell.setCellStyle(cellStyle);
        }

        //File file =new File(backupActivity.this.getExternalFilesDir(null),"balance2.xls");//old one, you can remove this line // it is storing in data hidden folder
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/balance.xls");
        FileOutputStream outputStream=null;

        try {
            outputStream =new FileOutputStream(file);
            workbook.write(outputStream);
            Toast.makeText(backupActivity.this, "Document created, check on your Download files", Toast.LENGTH_SHORT).show();
        }catch (java.io.IOException e){
            e.printStackTrace();
            Toast.makeText(backupActivity.this, "NO OK "+e, Toast.LENGTH_SHORT).show();

            try {
                outputStream.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }



    }


    /*private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermissionExport(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE_IMPORT);
    }
    private void requestStoragePermissionImport(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE_EXPORT);
    }
    private void importCSV() {

    }
    private void exportCSV() {
        File folder= new File(Environment.getExternalStorageDirectory()+"/"+"SQLiteBackup");//SQLiteBackup is folder name
        boolean isFolderCreated=false;
        if (!folder.exists()){
            isFolderCreated=folder.mkdir();
        }

        Log.d("CSC_TAG","exportCSV: "+isFolderCreated);

        String csvFileName="SQLite_backup.csv";
        String filePathAndName=folder.toString()+"/"+csvFileName;


        //ArrayList <ModelRecord> recordsList=new ArrayList<>();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case STORAGE_REQUEST_CODE_EXPORT:{
                if(grantResults.length==0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission granted
                    exportCSV();
                }
                else {
                    Toast.makeText(this,"Storage Permission Required... ",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE_IMPORT:{
                if(grantResults.length==0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission granted
                    importCSV();
                }
                else {
                    Toast.makeText(this,"Storage Permission Required... ",Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }*/


    String catVoid(String i){
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

    public void menuMain (View v)
    {
        Intent i = new Intent(backupActivity.this,MainActivity.class);
        startActivity(i);
    }
    public void cleaner(){
        listexpenseid.clear();
        listexpense.clear();
        listexpenseCat.clear();
        listexpenseSubCat.clear();
        listexpenseDate.clear();
        listexpenseNote.clear();

    }


}
