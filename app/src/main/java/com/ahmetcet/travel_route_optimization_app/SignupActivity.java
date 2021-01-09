package com.ahmetcet.travel_route_optimization_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText editText_nameSurname;
    EditText editText_email;
    EditText editText_pass;
    EditText editText_passVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editText_nameSurname = (EditText) findViewById(R.id.editText_nameSurname);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_pass = (EditText) findViewById(R.id.editText_Password);
        editText_passVerify = (EditText) findViewById(R.id.editText_passwordVerify);
    }

    public void SignUp(View view) {
        if(ValidateInputs()){
            String nameSurname = editText_nameSurname.getText().toString();
            String email = editText_email.getText().toString();
            String password = editText_pass.getText().toString();

            ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setMessage("Lütfen bekleyiniz...");
            progressDialog.create();
            progressDialog.show();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

            //todo:Servis kodlamaları yapılacak
            new AlertDialog.Builder(SignupActivity.this)
                    .setTitle("İşlem başarılı")
                    .setMessage("Bilgileriniz başarıyla kaydedilmiştir.")
                    .setCancelable(false)
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

    }

    private boolean ValidateInputs(){
        Boolean result = true;
        if(editText_nameSurname.getText().toString().length()==0){
            editText_nameSurname.setError("Bu alan boş bırakılamaz");
            result = false;
        }
        if(!isEmailValid(editText_email.getText().toString())){
            editText_email.setError("Lütfen geçerli bir mail adresi giriniz");
            result = false;
        }
        if(editText_pass.getText().toString().length()==0){
            editText_pass.setError("Bu alan boş bırakılamaz");
            result = false;
        }else if (editText_pass.getText().toString().length() < 6){
            editText_pass.setError("Şifreniz en az 6 karakterden oluşmalıdır");
            result = false;
        }

        if(editText_passVerify.getText().toString().length()==0){
            editText_passVerify.setError("Bu alan boş bırakılamaz");
            result = false;
        }


        if(result && !editText_pass.getText().toString().equals(editText_passVerify.getText().toString())){
            Tools.ShowAlertDialog(SignupActivity.this,"Girdiğiniz şifreler eşleşmemektedir",true);
            result = false;

        }



        return result;
    }

    private boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

}