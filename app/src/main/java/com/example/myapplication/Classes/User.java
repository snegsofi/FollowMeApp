package com.example.myapplication.Classes;

import android.content.Context;

public class User {
    // класс, обрабатываюший авторизацию пользователя

    private static final String KEY = "userId";

    public static String getUserId(Context context){
        return Session.get(context,KEY);
    }

    public static void setUserId(Context context,String userId){
        Session.save(context,KEY,userId);
    }

    public static void userLogOut(Context context){
        Session.save(context,KEY,"");
    }
}
