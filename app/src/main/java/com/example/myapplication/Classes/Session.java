package com.example.myapplication.Classes;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    // класс, хранящий данные авторизован ли пользователь

    public static void save(Context context, String key, String value){
        // сохранение в настройках id авторизованного пользователя
        SharedPreferences sharedPreferences=context.getSharedPreferences("tourismAppSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String get(Context context,String key){
        // получение id авторизованного пользователя
        SharedPreferences sharedPreferences=context.getSharedPreferences("tourismAppSettings",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}
