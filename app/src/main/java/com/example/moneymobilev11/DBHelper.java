package com.example.moneymobilev11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


//COPIATETODO EL DOCUMENTO COMO CLASE PARA CREAR LA CONEXION, EN ON CREATE Y ONUPGRADE ANADIMOS LAS TABLAS
public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, "MoneyData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Categories(idcategory INTEGER,category TEXT, budget DOUBLE,estadoregistro INTEGER,fecharegistro DATETIME DEFAULT CURRENT_TIMESTAMP)");
        DB.execSQL("create Table SubCategories(idsubcategory INTEGER,subcategory TEXT,idcategory INTEGER,estadoregistro INTEGER,fecharegistro DATETIME DEFAULT CURRENT_TIMESTAMP)");
        DB.execSQL("create Table IncomeType(idincometype INTEGER,incometype TEXT, estadoregistro INTEGER,fecharegistro DATETIME DEFAULT CURRENT_TIMESTAMP)");
        DB.execSQL("create Table Income(idincome INTEGER,income DOUBLE,idincometype INTEGER, estadoregistro INTEGER,fecharegistro DATETIME DEFAULT CURRENT_TIMESTAMP)");
        DB.execSQL("create Table Expense(idexpense INTEGER,expense DOUBLE,expensedate DATETIME DEFAULT (datetime('now', 'localtime')),note TEXT,idcategory INTEGER, idsubcategory INTEGER, estadoregistro INTEGER,fecharegistro DATETIME DEFAULT CURRENT_TIMESTAMP)");
        DB.execSQL("create Table Balance(idbalance INTEGER,balance DOUBLE, estadoregistro INTEGER,fecharegistro DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Categories");
        DB.execSQL("drop Table if exists SubCategories");
        DB.execSQL("drop Table if exists IncomeType");
        DB.execSQL("drop Table if exists Income");
        DB.execSQL("drop Table if exists Expense");
        DB.execSQL("drop Table if exists Balance");
    }







    //insert category
    public int insertcategory(String category,double budget)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        //primero seleccionamos
        Cursor cursor=DB.rawQuery("SELECT idcategory FROM Categories ORDER BY idcategory DESC LIMIT '"+1+"' ",null);
        int idcatselected=0;
        if(cursor.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            idcatselected=Integer.parseInt(cursor.getString(0));
        }
        idcatselected=idcatselected+1;

        //luego insertamos
        ContentValues contentValues = new ContentValues();
        contentValues.put("idcategory",idcatselected);
        contentValues.put("category",category);
        contentValues.put("budget",budget);
        contentValues.put("estadoregistro",1);

        long result =DB.insert("Categories",null,contentValues);
        if(result==-1) {
            return 0;
        }else{
            return idcatselected;
        }
    }
    public boolean updatecategory(int idcategory,String category, double budget)
    {
        SQLiteDatabase DB= this.getWritableDatabase();

        ContentValues contentValuesIns = new ContentValues();
        contentValuesIns.put("estadoregistro",2);
        Cursor cursor=DB.rawQuery("select * from Categories where idcategory = '"+idcategory+"'AND estadoregistro=1", null);
        if (cursor.getCount()>0) {
            long results = DB.update("Categories", contentValuesIns, "idcategory='" + idcategory + "'  AND estadoregistro=1", null);
            //resultado de la actualizacion tiene que ser diferente de -1
            //y quedo actualizado
        }

        int idcatselected=idcategory;
        //luego insertamos
        ContentValues contentValues = new ContentValues();
        contentValues.put("idcategory",idcatselected);
        contentValues.put("category",category);
        contentValues.put("budget",budget);
        contentValues.put("estadoregistro",1);

        long result =DB.insert("Categories",null,contentValues);
        if(result==-1) {
            return false;
        }else{
            return true;
        }
    }
    public boolean deletecategory(int idcategory)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("estadoregistro",3);
        //Cursor cursor=DB.rawQuery("select * from Categories where idcategory = ?", new String[]{name});
        Cursor cursor=DB.rawQuery("select * from Categories where idcategory = '"+idcategory+"' AND estadoregistro=1", null);
        if (cursor.getCount()>0)
        {
            long results =DB.update("Categories",contentValues,"idcategory='"+idcategory+"' AND estadoregistro=1", null);
            if(results==-1)
            {
                return false;
            }
            else {
                return true;
            }
        }else{
            return false;
        }
    }
    //para la lista
    public Cursor getdatacategories()
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from Categories where estadoregistro=1", null);
        return cursor;
    }
    public Cursor getdatacategory(int id)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from Categories where idcategory='"+id+"' AND estadoregistro=1", null);
        return cursor;
    }







    //SubCategory
    public boolean insertsubcategory(String subcategory,int idcategory)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        //primero seleccionamos
        Cursor cursor=DB.rawQuery("SELECT idsubcategory FROM SubCategories ORDER BY idsubcategory DESC LIMIT '"+1+"' ",null);
        int idcatselected=0;
        if(cursor.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            idcatselected=Integer.parseInt(cursor.getString(0));
        }
        idcatselected=idcatselected+1;
        //luego insertamos
        ContentValues contentValues = new ContentValues();
        contentValues.put("idsubcategory",idcatselected);
        contentValues.put("subcategory",subcategory);
        contentValues.put("idcategory",idcategory);
        contentValues.put("estadoregistro",1);
        long result =DB.insert("SubCategories",null,contentValues);
        if(result==-1) {
            return false;
        }else{
            return true;
        }
    }
    public boolean updatesubcategory(int idsubcategory,String subcategory)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValuesIns = new ContentValues();
        int auxCategory=0;
        contentValuesIns.put("estadoregistro",2);
        Cursor cursor=DB.rawQuery("select * from SubCategories where idsubcategory = '"+idsubcategory+"'AND estadoregistro=1", null);
        if (cursor.getCount()>0) {
            long results = DB.update("SubCategories", contentValuesIns, "idsubcategory='" + idsubcategory + "'  AND estadoregistro=1", null);
            //resultado de la actualizacion tiene que ser diferente de -1
            //y quedo actualizado
            if(cursor.moveToFirst()){
                do{
                    auxCategory=Integer.parseInt(cursor.getString(2));
                }while(cursor.moveToNext());
            }
        }
        else {
            //para hacer validacion
        }
        //poner dentro del if
        int idcatselected=idsubcategory;
        //luego insertamos
        ContentValues contentValues = new ContentValues();
        contentValues.put("idsubcategory",idcatselected);
        contentValues.put("subcategory",subcategory);
        contentValues.put("idcategory",auxCategory);
        contentValues.put("estadoregistro",1);
        long result =DB.insert("SubCategories",null,contentValues);
        if(result==-1) {
            return false;
        }else{
            return true;
        }
    }
    public boolean deletesubcategory(int idsubcategory)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("estadoregistro",3);
        //Cursor cursor=DB.rawQuery("select * from Categories where idcategory = ?", new String[]{name});
        Cursor cursor=DB.rawQuery("select * from SubCategories where idsubcategory = '"+idsubcategory+"' AND estadoregistro=1", null);
        if (cursor.getCount()>0)
        {
            long results =DB.update("SubCategories",contentValues,"idsubcategory='"+idsubcategory+"' AND estadoregistro=1", null);
            if(results==-1)
            {
                return false;
            }
            else {
                return true;
            }
        }else{
            return false;
        }
    }
    //para la lista SUBCATEGORIES
    public Cursor getdatasubcategories(int id)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from SubCategories where idcategory='"+id+"' AND estadoregistro=1", null);
        return cursor;
    }
        public Cursor getdatasubcategory(int id)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from SubCategories where idsubcategory='"+id+"' AND estadoregistro=1", null);
        return cursor;
    }









    //Type incomes record
    public boolean inserttypeincome(String typeincome)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        //primero seleccionamos
        Cursor cursor=DB.rawQuery("SELECT idincometype FROM IncomeType ORDER BY idincometype DESC LIMIT '"+1+"' ",null);
        int idcatselected=0;
        if(cursor.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            idcatselected=Integer.parseInt(cursor.getString(0));
        }
        idcatselected=idcatselected+1;//incrementar id

        //luego insertamos
        ContentValues contentValues = new ContentValues();
        contentValues.put("idincometype",idcatselected);
        contentValues.put("incometype",typeincome);
        contentValues.put("estadoregistro",1);
        long result =DB.insert("IncomeType",null,contentValues);
        if(result==-1) {
            return false;
        }else{
            return true;
        }
    }
    public boolean updatetypeincome(int idincometype,String incometype)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValuesIns = new ContentValues();
        contentValuesIns.put("estadoregistro",2);
        Cursor cursor=DB.rawQuery("select * from IncomeType where idincometype = '"+idincometype+"'AND estadoregistro=1", null);
        if (cursor.getCount()>0) {
            long results = DB.update("IncomeType", contentValuesIns, "idincometype='" + idincometype + "'  AND estadoregistro=1", null);
            //resultado de la actualizacion tiene que ser diferente de -1
            //y quedo actualizado

        }
        else {
            //para hacer validacion
        }
        //poner dentro del if
        int idcatselected=idincometype;
        //luego insertamos
        ContentValues contentValues = new ContentValues();
        contentValues.put("idincometype",idcatselected);
        contentValues.put("incometype",incometype);
        contentValues.put("estadoregistro",1);
        long result =DB.insert("IncomeType",null,contentValues);
        if(result==-1) {
            return false;
        }else{
            return true;
        }
    }
    public boolean deletetypeincome(int idincometype)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("estadoregistro",3);
        //Cursor cursor=DB.rawQuery("select * from Categories where idcategory = ?", new String[]{name});
        Cursor cursor=DB.rawQuery("select * from IncomeType where idincometype = '"+idincometype+"' AND estadoregistro=1", null);
        if (cursor.getCount()>0)
        {
            long results =DB.update("IncomeType",contentValues,"idincometype='"+idincometype+"' AND estadoregistro=1", null);
            if(results==-1)
            {
                return false;
            }
            else {
                return true;
            }
        }else{
            return false;
        }
    }
    public Cursor getdatatypeincomes()
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from IncomeType where estadoregistro=1", null);
        return cursor;
    }







    //ingresar ingreso (income)
    public boolean insertincome(double income,int idincometype)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        //primero seleccionamos
        Cursor cursor=DB.rawQuery("SELECT idincome FROM Income ORDER BY idincome DESC LIMIT '"+1+"' ",null);
        int idcatselected=0;
        if(cursor.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            idcatselected=Integer.parseInt(cursor.getString(0));
        }
        idcatselected=idcatselected+1;
        //luego insertamos
        ContentValues contentValues = new ContentValues();
        contentValues.put("idincome",idcatselected);
        contentValues.put("income",income);
        contentValues.put("idincometype",idincometype);
        contentValues.put("estadoregistro",1);
        long result =DB.insert("Income",null,contentValues);
        if(result==-1) {
            return false;
        }else{
            //retornamos true pero la respuesta de inser es falso
            return insertbalance(income);
        }
    }
    public boolean deleteincome(int idincome)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("estadoregistro",3);
        //Cursor cursor=DB.rawQuery("select * from Categories where idcategory = ?", new String[]{name});
        Cursor cursor=DB.rawQuery("select * from Income where idincome = '"+idincome+"' AND estadoregistro=1", null);
        if (cursor.getCount()>0)
        {
            long results =DB.update("Income",contentValues,"idincome='"+idincome+"' AND estadoregistro=1", null);
            if(results==-1)
            {
                return false;
            }
            else {
                return true;
            }
        }else{
            return false;
        }
    }
    public Cursor getdataincomes()
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from Income where estadoregistro=1", null);
        return cursor;
    }



    //expense
    public boolean insertexpense(double expense, String note,int idcat,int idsubcat)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        //primero seleccionamos
        Cursor cursor=DB.rawQuery("SELECT idexpense FROM Expense ORDER BY idexpense DESC LIMIT '"+1+"' ",null);
        int idexpselected=0;
        if(cursor.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            idexpselected=Integer.parseInt(cursor.getString(0));
        }
        idexpselected=idexpselected+1;

        insertbalance(-expense);

        //luego insertamos EXPENSE
        ContentValues contentValues = new ContentValues();
        contentValues.put("idexpense",idexpselected);
        contentValues.put("expense",expense);
        contentValues.put("note",note);
        contentValues.put("idcategory",idcat);
        contentValues.put("idsubcategory",idsubcat);
        contentValues.put("estadoregistro",1);
        long result =DB.insert("Expense",null,contentValues);
        if(result==-1) {
            return false;
        }else{
            return true;
        }
    }
    public boolean deleteexpense(int idexpense)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("estadoregistro",3);
        //Cursor cursor=DB.rawQuery("select * from Categories where idcategory = ?", new String[]{name});
        Cursor cursor=DB.rawQuery("select * from Expense where idexpense = '"+idexpense+"' AND estadoregistro=1", null);
        if (cursor.getCount()>0)
        {
            long results =DB.update("Expense",contentValues,"idexpense='"+idexpense+"' AND estadoregistro=1", null);
            if(results==-1)
            {
                return false;
            }
            else {
                return true;
            }
        }else{
            return false;
        }
    }
    public Cursor getdataexpense()
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from Expense where estadoregistro=1 order by expensedate DESC", null);
        return cursor;
    }
    public Cursor getdataexpensemonthly()
    {
        SQLiteDatabase DB= this.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String monthStart = dateFormat.format(c.getTime());
        c = Calendar.getInstance(); // reset
        //String today = dateFormat.format(c.getTime());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;//plus one because months start in 0
        int day = c.get(Calendar.DAY_OF_MONTH);
        String firstofmonth;
        String today;
        if(month>=10){
            firstofmonth = year+"-"+month+"-01"+" 00:00:00";
            today = year+"-"+month+"-"+day+" 23:59:59";
        }else {
            firstofmonth =year+"-0"+month+"-01"+" 00:00:00";
            today = year+"-0"+month+"-"+day+" 23:59:59";//hay un error con el month aqui
        }
        String []selectionArgs = new String[]{firstofmonth, today};

        //Cursor cursor=DB.rawQuery("select * from Expense Where expensedate  BETWEEN date(?) AND date(?)", selectionArgs);
        Cursor cursor=DB.rawQuery("select * from Expense where expensedate >= (?) and expensedate <= (?) and estadoregistro=1", selectionArgs);
        //Cursor cursor=DB.rawQuery("select * from Expense Where expensedate(datetime(timestamp, 'unixepoch'))   BETWEEN date(?) AND date(?)", selectionArgs);
        return cursor;
    }
    public Cursor getdataexpenseBetween(String firstofmonth,String today)
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        String []selectionArgs = new String[]{firstofmonth, today};

        //Cursor cursor=DB.rawQuery("select * from Expense Where expensedate  BETWEEN date(?) AND date(?)", selectionArgs);
        Cursor cursor=DB.rawQuery("select * from Expense where expensedate >= (?) and expensedate <= (?) and estadoregistro=1", selectionArgs);
        //Cursor cursor=DB.rawQuery("select * from Expense Where expensedate(datetime(timestamp, 'unixepoch'))   BETWEEN date(?) AND date(?)", selectionArgs);
        return cursor;
    }



    //saldo, balance
    public boolean insertbalance(double balance)//la unica vez que insertamos es aqui, luego es update and update, si quieres mejorar el proceso mas adelante puedes volver a insertar e insertar el id del gasto para hacer un seguimiento o lo que quieras hacer
    {
        SQLiteDatabase DB= this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("SELECT * FROM Balance WHERE idbalance=1", null);
        if(cursor.getCount()==0)//si no hay insertamos el por defecto
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("idbalance",1);
            contentValues.put("balance",0);
            contentValues.put("estadoregistro",1);
            long result =DB.insert("Balance",null,contentValues);
            if(result==-1) {
                return false;
            }else{
                return true;
            }
        }else {
            double balanceselected=0;
            int idbalanceselected=0;
            if(cursor.moveToFirst()){
                idbalanceselected=Integer.parseInt(cursor.getString(0));
                balanceselected=Double.parseDouble(cursor.getString(1));
            }
            balanceselected=balanceselected+balance;
            ContentValues contentValues = new ContentValues();
            contentValues.put("idbalance",1);
            contentValues.put("balance",balanceselected);
            long results = DB.update("Balance", contentValues, "idbalance='" + idbalanceselected + "'  AND estadoregistro=1", null);
            if(results==-1) {
                return false;
            }else{
                return true;
            }
        }
    }


    public Cursor getdatabalance()
    {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("select * from Balance where idbalance=1", null);
        return cursor;
    }

}





