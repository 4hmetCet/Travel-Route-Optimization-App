package com.ahmetcet.travel_route_optimization_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);
    }

    public void GoToMap(View view) {
        startActivity(new Intent(CreateRouteActivity.this,MapActivity.class));
    }
}