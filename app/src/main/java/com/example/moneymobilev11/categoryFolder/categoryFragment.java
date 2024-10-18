package com.example.moneymobilev11.categoryFolder;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.moneymobilev11.DBHelper;
import com.example.moneymobilev11.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class categoryFragment extends Fragment {
    DBHelper DB;

    ListView list;
    List<String> item=null;
    List<String> lista = new ArrayList<String>();// esta es para almacenar el id, //una variable lista para el listener

    Activity context;




    public categoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getActivity();//el context para cargar las cosas del activity en el fragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //onCreateView(auxinflater,auxcontainer,new Bundle());
                //auxinflater.inflate(R.layout.fragment_category, auxcontainer, false);
        //auxinflater.inflate(R.layout.fragment_category, auxcontainer, false);
        list=(ListView)context.findViewById(R.id.ListCat);
        showList();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)//validacion
                {
                    Intent i = new Intent(context, SubcategoryUpdateCategoryActivity.class);//enviar datos
                    i.putExtra("idcategory", lista.get(position - 1));
                    startActivity(i);
                    //Toast.makeText(context, "LIST " + lista.get(position - 1), Toast.LENGTH_SHORT).show();
                    //menos 1 porque hemos anadido una linea en la lista para lofs titulos
                }
            }
        });
    }




    private void showList(){
        lista.clear();


        DB=new DBHelper(context);

        Cursor res=DB.getdatacategories();
        if(res.getCount()==0)
        {
            Toast.makeText(context,"No Entry Exists",Toast.LENGTH_SHORT).show();
            return;
        }
        String category="",budget="",id="";
        item = new ArrayList<String>();
        item.add("    "+"Category  "+"  Budget Set");
        if(res.moveToFirst()){
            //para recorrer el cursor res hasta que no haya registros
            do{
                id=res.getString(0);
                category=res.getString(1);
                budget=res.getString(2);
                item.add(id+".- "+category+" "+budget);
                lista.add(res.getString(0));
                //Toast.makeText(context,"No "+res.getString(0)+"aux ",Toast.LENGTH_SHORT).show();
            }while(res.moveToNext());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,item);
        //para almacenar los item en el adapter
        list.setAdapter(adapter);
    }
}
