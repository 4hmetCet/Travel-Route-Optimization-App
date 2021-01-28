package com.ahmetcet.travel_route_optimization_app.LocalData;

import android.content.Context;
import android.content.SharedPreferences;

import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefManager {
    public static String userInfo = "user_authentication_info";
    //userInfo
    final public static String key_email="email";
    final public static String key_password="password";
    final public static String key_firstName="firstname";
    final public static String key_surname="surname";
    final public static String key_userId="userId";

    final public static String currentRouteId="currentRoute";
    final public static String users="users";



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

        String pref_username = getPref_UserInfo(key_email, context);
        String pref_pass =  getPref_UserInfo(key_password, context);

        if (pref_username != "" && pref_pass != "") {
            return true;
        } else {
            return false;
        }
    }

    public static void pref_delete_Authority( Context context) {
        try{
            putPref_UserInfo(key_email,"",context);
            putPref_UserInfo(key_password,"",context);
            putPref_UserInfo(key_userId,"",context);
            putPref_UserInfo(key_firstName,"",context);
            putPref_UserInfo(key_surname,"",context);
        }catch(Exception ex){

        }
    }

    public static void setCurrentRouteId(String value,Context context) {
        SharedPreferences prefs = context.getSharedPreferences(userInfo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(currentRouteId, value);
        editor.commit();
    }
    //Key - Value user ınfodan okur.
    public static String getCurrentRouteId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(userInfo, Context.MODE_PRIVATE);
        return preferences.getString(currentRouteId, "");
    }


    //Key - Value user ınfoya kaydeder.
    public static boolean saveUser ( User user, Context context) {
        try {
            Gson gson = new Gson();
            ArrayList<User> allUsers = getAllUsers(context);
            allUsers.add(user);
            SharedPreferences prefs = context.getSharedPreferences(users, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(users, gson.toJson(allUsers));
            editor.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //Key - Value user ınfodan okur.
    public static boolean checkUser(User user, Context context) {
        boolean result = false;
        ArrayList<User> allUsers = getAllUsers(context);
        for (User user_item :
                allUsers) {
            if(user_item.getUserName().equals(user.getUserName()) && user_item.getPassword().equals(user.getPassword())){
                result = true;
                break;
            }
        }
        return result;
    }

    public static ArrayList<User> getAllUsers(Context context) {
        ArrayList<User> resultList = new ArrayList<>();
        Gson gson = new Gson();
        Type listOfMyClassObject = new TypeToken<ArrayList<User>>() {}.getType();
        SharedPreferences preferences = context.getSharedPreferences(users, Context.MODE_PRIVATE);
        String json_users = null;
        try {
            json_users = preferences.getString(users, "");
            resultList = gson.fromJson(json_users,listOfMyClassObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(resultList == null)
            return new ArrayList<User>();
        return resultList;
    }

}
