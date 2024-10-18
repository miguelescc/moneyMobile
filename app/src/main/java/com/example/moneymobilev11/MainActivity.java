package com.example.moneymobilev11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.example.moneymobilev11.categoryFolder.AddCategoryActivity;
import com.example.moneymobilev11.categoryFolder.categoryFragment;
import com.example.moneymobilev11.incomeFolder.incomeFragment;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textTitle=findViewById(R.id.textTitle);
        textTitle.setText("Main Menu");
        final DrawerLayout drawerLayout=findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


/*

        NavigationView navigationView=findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        NavController navController= Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);
        final TextView textTitle=findViewById(R.id.textTitle);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {//to set The title
                if(textTitle.getText()==destination.getLabel()){
                        //getSupportFragmentManager().beginTransaction().remove(mainFragment).commit();
                    //Toast.makeText(MainActivity.this,"compara"+textTitle.getText()+"="+destination.getLabel(),Toast.LENGTH_SHORT).show();
                        //validacion para que retorne en caso de presionar el mismo boton, falta arreglar
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);



                }
                textTitle.setText(destination.getLabel());
            }
        });
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
*/




        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        NavController navController= Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);
        //final TextView textTitle=findViewById(R.id.textTitle);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.close_menu);//"open", "close"// we have set this on values/strings
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);//this add the arrow
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.menuMain:

                        /*if(textTitle.getText().equals("Main Menu")){
                            Toast.makeText(MainActivity.this,"Already opened ",Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }else{
                            fragmentManager.beginTransaction()
                                    .replace(R.id.navHostFragment, new mainFragment(), null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("replacement") // name can be null
                                    .commit();
                            Log.i("Menu Drawer TAG", "Cat Item is clicked");
                            textTitle.setText("Main Menu");
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }*/
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(i);



                        break;
                    case R.id.menuCategory:
                        if(textTitle.getText().equals("Category")){
                            Toast.makeText(MainActivity.this,"Already opened ",Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }else {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.navHostFragment, new categoryFragment(), null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("replacement") // name can be null
                                    .commit();
                            //Log.i("Menu Drawer TAG", "Cat Item is clicked");
                            textTitle.setText("Category");
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;
                    case R.id.menuIncome:
                        if(textTitle.getText().equals("Income")){
                            Toast.makeText(MainActivity.this,"Already opened ",Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }else {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.navHostFragment, new incomeFragment(), null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("replacement") // name can be null
                                    .commit();
                            //Log.i("Menu Drawer TAG", "Income Item is clicked");
                            textTitle.setText("Income");
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;
                    case R.id.menuExpense:
                        if(textTitle.getText().equals("Expenses")){
                            Toast.makeText(MainActivity.this,"Already opened ",Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }else {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.navHostFragment, new expenseFragment(), null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("replacement") // name can be null
                                    .commit();
                            //Log.i("Menu Drawer TAG", "ex Item is clicked");
                            textTitle.setText("Expenses");
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;
                    case R.id.menuCalendar:
                        //Fragment frg=null;
                        //frg =getSupportFragmentManager().findFragmentByTag(calendarFragment);
                        //final FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                        //ft.detach(frg);
                        //ft.attach(frg);
                        //ft.commit();

                        //Fragment fragment= new calendarFragment();
                        //FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        //transaction.replace(R.id.drawerLayout, fragment); // fragmen container id in first parameter is the  container(Main layout id) of Activity
                        //transaction.addToBackStack(null);  // this will manage backstack
                        //transaction.commit();

                        //FragmentManager fm = getSupportFragmentManager();
                        //calendarFragment fragment = new calendarFragment();
                        //fm.beginTransaction().replace(R.id.navHostFragment,fragment).commit();

                        //FragmentManager fragmentManager = getSupportFragmentManager();
                        //FragmentTransaction transaction = fragmentManager.beginTransaction();
                        //transaction.replace(R.id.navHostFragment,new calendarFragment()).commit();

                        if(textTitle.getText().equals("Calendar")){
                            Toast.makeText(MainActivity.this,"Already opened ",Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }else {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.navHostFragment, new calendarFragment(), null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("replacement") // name can be null
                                    .commit();
                            //fm.beginTransaction().add(R.id.drawerLayout,fragment).commit();
                            //Log.i("Menu Drawer TAG", "C Item is clicked");
                            textTitle.setText("Calendar");
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;
                    case R.id.menuSettings:
                        if(textTitle.getText().equals("Settings")){
                            Toast.makeText(MainActivity.this,"Already opened ",Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }else {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.navHostFragment, new settingsFragment(), null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("replacement") // name can be null
                                    .commit();
                            //Log.i("Menu Drawer TAG", "sett Item is clicked");
                            textTitle.setText("Settings");
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;
                    case R.id.menuStatistics:
                        if(textTitle.getText().equals("Statistics")){
                            Toast.makeText(MainActivity.this,"Already opened ",Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }else {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.navHostFragment, new AnnualCategoryFragment(), null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("replacement") // name can be null
                                    .commit();
                            Log.i("Menu Drawer TAG", "Statistics Item is clicked");
                            textTitle.setText("Statistics");
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;
                }
                return true;
            }
        });

    }





    public void Category (View v)
    {
        Intent i = new Intent(MainActivity.this, AddCategoryActivity.class);
        startActivity(i);
    }
    public void Reload (View v)
    {
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
    }


    public void openExpense (View v)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.navHostFragment, new expenseFragment(), null)
                .setReorderingAllowed(true)
                .addToBackStack("replacement") // name can be null
                .commit();
        //Log.i("Menu Drawer TAG", "ex Item is clicked");
        final TextView textTitle=findViewById(R.id.textTitle);
        textTitle.setText("Expenses");
    }

}
