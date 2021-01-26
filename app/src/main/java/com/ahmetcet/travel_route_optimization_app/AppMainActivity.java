package com.ahmetcet.travel_route_optimization_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AppMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String currRouteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currRouteId = PrefManager.getCurrentRouteId(AppMainActivity.this);

        final FloatingActionButton fab_createRoute = findViewById(R.id.fab_createRoute);
        fab_createRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppMainActivity.this, CreateRouteOnMapActivity.class));
            }
        });

        final FloatingActionButton fab_cancelRoute = findViewById(R.id.fab_cancelRoute);
        fab_cancelRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.ShowAlertDialog(AppMainActivity.this, "Rotayı iptal etmek istediğinize emin misiniz?",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PrefManager.setCurrentRouteId("",AppMainActivity.this);
                                startActivity(new Intent(AppMainActivity.this,AppMainActivity.class));
                            }
                        },true);
                return ;
            }
        });

        if(currRouteId.length()>0){
            fab_createRoute.setVisibility(View.INVISIBLE);
            fab_cancelRoute.setVisibility(View.VISIBLE);
        }else{
            fab_createRoute.setVisibility(View.VISIBLE);
            fab_cancelRoute.setVisibility(View.INVISIBLE);
        }




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_myRoutes, R.id.nav_offlineMap)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        MenuItem logOutItem = navigationView.getMenu().findItem(R.id.nav_logOut);
        logOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Tools.ShowAlertDialog(AppMainActivity.this, "Oturumu kapatmak istediğinize emin misiniz?",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PrefManager.pref_delete_Authority(AppMainActivity.this);
                                startActivity(new Intent(AppMainActivity.this,LoginActivity.class));
                            }
                        },true);
                return true;
            }
        });

        MenuItem createRouteItem = navigationView.getMenu().findItem(R.id.nav_createRoute);
        createRouteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(AppMainActivity.this, CreateRouteOnMapActivity.class));
                return true;
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}