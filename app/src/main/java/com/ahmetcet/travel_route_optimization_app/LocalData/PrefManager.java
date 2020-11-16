package com.ahmetcet.travel_route_optimization_app.LocalData;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    public static String userInfo = "user_authentication_info";
    //userInfo
    final public static String key_username="username";
    final public static String key_password="password";
    final public static String key_firstName="firstname";

    final public static String key_surname="surname";
    final public static String key_userId="userId";
    //////

    //Key - Value user ınfoya kaydeder.
    public static void putPref_UserInfo(String key, String value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(userInfo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    //Key - Value user ınfodan okur.
    public static String getPref_UserInfo(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(userInfo, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static Boolean pref_isAuthorized(Context context) {

        String pref_username = getPref_UserInfo(key_username, context);
        String pref_pass =  getPref_UserInfo(key_password, context);

        if (pref_username != "" && pref_pass != "") {
            return true;
        } else {
            return false;
        }
    }

    public static void pref_delete_Authority( Context context) {
        try{
            putPref_UserInfo(key_username,"",context);
            putPref_UserInfo(key_password,"",context);
            putPref_UserInfo(key_userId,"",context);
            putPref_UserInfo(key_firstName,"",context);
            putPref_UserInfo(key_surname,"",context);
        }catch(Exception ex){

        }
    }

}
