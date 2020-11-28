package com.ahmetcet.travel_route_optimization_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class Tools {
    public static ProgressDialog ShowProgressDialog(Context context, String message, boolean isCancelable){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancelable);
        progressDialog.create();
        progressDialog.show();
        return progressDialog;
    }

    public static void ShowAlertDialog(Context context, String message, boolean isCancelable){

    }

    public static void ShowAlertDialog(Context context, String message, DialogInterface.OnClickListener positiveButtonEvent, boolean isCancelable){
        new AlertDialog.Builder(context)
                .setTitle("Uyarı")
                .setMessage(message)
                .setCancelable(isCancelable)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Evet", positiveButtonEvent)
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Vazgeç", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    public static void ShowErrorDialog(Context context, String message, boolean isCancelable){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setTitle("Hata!!");
        builder1.setCancelable(isCancelable);
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }





}
