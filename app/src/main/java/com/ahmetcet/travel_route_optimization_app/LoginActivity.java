package com.ahmetcet.travel_route_optimization_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ThemedSpinnerAdapter;

import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CheckAuthentication();
    }

    private void CheckAuthentication() {
        if(PrefManager.pref_isAuthorized(LoginActivity.this)){
            startActivity(new Intent(LoginActivity.this,AppMainActivity.class));
        }
    }
    public void Login(View view) {
        EditText edt_loginMail = (EditText) findViewById(R.id.editText_loginEmail);
        String input_loginMail = edt_loginMail.getText().toString();
        EditText edt_loginPassword = (EditText) findViewById(R.id.editText_loginPassword);
        String input_loginPassword = edt_loginPassword.getText().toString();

        if(input_loginMail.length()==0){
            edt_loginMail.setError("Eposta alanı boş olamaz");
            return;
        }
        if(input_loginPassword.length()==0){
            edt_loginPassword.setError("Şifre alanı boş olamaz");
            return;
        }

        if(!input_loginMail.equals("ahmet") || !input_loginPassword.equals("1234")){
            Tools.ShowErrorDialog(LoginActivity.this,"Girilen kullanıcı adı veya şifre hatalı",true);
            return;
        }

        PrefManager.putPref_UserInfo(PrefManager.key_email,input_loginMail,this);
        PrefManager.putPref_UserInfo(PrefManager.key_password,input_loginPassword,this);
        PrefManager.putPref_UserInfo(PrefManager.key_userId,"1",this);

        startActivity(new Intent(LoginActivity.this,AppMainActivity.class));
    }

    public void GotoSignUpPage(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }
}