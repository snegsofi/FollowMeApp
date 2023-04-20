package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    String login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation=(BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_tour);

        Intent intent=getIntent();
        login = intent.getStringExtra("login");
        Log.d("waiterMainActivity",login);

    }

    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    loadFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_search:
                    loadFragment(SearchFragment.newInstance());
                    return true;
                case R.id.navigation_tour:
                    loadFragment(BookTourFragment.newInstance());
                    return true;
                case R.id.navigation_account:
                    loadFragment(ProfileFragment.newInstance(login));
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}