package com.ahmetcet.travel_route_optimization_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Login(View view) {
        EditText edt_loginMail = (EditText) findViewById(R.id.editText_loginEmail);
        EditText edt_loginPassword = (EditText) findViewById(R.id.editText_loginPassword);

        PrefManager.putPref_UserInfo(PrefManager.key_email,edt_loginMail.getText().toString(),this);
        PrefManager.putPref_UserInfo(PrefManager.key_password,edt_loginPassword.getText().toString(),this);

        startActivity(new Intent(LoginActivity.this,AppMainActivity.class));
    }
}