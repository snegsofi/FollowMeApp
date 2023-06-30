package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Classes.User;
import com.example.myapplication.R;

public class SplashScreen extends AppCompatActivity {

    // начальное окно загрузки
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screeen);

        // загрузка активности в течении 5 секунд
        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);
                    // пропуск авторизации для авторизованного пользователя
                    if(!User.getUserId(getApplicationContext()).isEmpty()){
                        Intent intent=new Intent(SplashScreen.this, MainActivity.class);
                        intent.putExtra("userID",User.getUserId(getApplicationContext()));
                        startActivity(intent);
                    }
                    else{
                        // переход на активность авторизации
                        startActivity(new Intent(SplashScreen.this, SignInFragment.class));
                    }
                    finish();
                }
                catch (Exception e){

                }
            }
        }; thread.start();

    }
}
