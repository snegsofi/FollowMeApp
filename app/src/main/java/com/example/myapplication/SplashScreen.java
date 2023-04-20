package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screeen);

        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);
                    startActivity(new Intent(SplashScreen.this,SignInFragment.class));
                    finish();
                }
                catch (Exception e){

                }
            }
        }; thread.start();

    }
}
