package com.ahmetcet.travel_route_optimization_app;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class Tools {
    public static void ShowProcessDialog(Context context){
    }

    public static void ShowAlertDialog(Context context, String message, boolean isCancelable){

    }

    public static void ShowAlertDialog(Context context, String message, View.OnClickListener positiveButtonEvent, boolean isCancelable){

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
