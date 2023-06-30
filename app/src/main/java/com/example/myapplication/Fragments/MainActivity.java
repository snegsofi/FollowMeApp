package com.example.myapplication.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    String userId;
    BottomNavigationView navigation;
    Integer selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation=(BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

    }

    // обработка нажатия на нижнюю панель управления
    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
//                @Override
//                public void onAvailable(@NonNull Network network) {
//                    super.onAvailable(network);
//                    Toast.makeText(getApplicationContext(),"available",Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onLost(@NonNull Network network) {
//                    super.onLost(network);
//                    Toast.makeText(getApplicationContext(),"lost",Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
//                    super.onCapabilitiesChanged(network, networkCapabilities);
//                    final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
//                    Toast.makeText(getApplicationContext(),"changed",Toast.LENGTH_SHORT).show();
//
//                }
//            };

            selectedItem=item.getItemId();
            Intent intent=getIntent();
            userId = intent.getStringExtra("userID");
            // вывод фрагмента соответсвтвующего выбранному пункту на панеле управления
            switch (item.getItemId()){
                case R.id.navigation_home:
                    loadFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_tour:
                    loadFragment(BookTourFragment.newInstance(userId));
                    return true;
                case R.id.navigation_search:
                    loadFragment(SearchFragment.newInstance(userId));
                    return true;
                case R.id.navigation_account:
                    loadFragment(ProfileFragment.newInstance(userId));
                    return true;
            }
            return false;
        }
    };

    // загрузка другого фрагмента
    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//        Log.d("Main", "coutn="+count);
//        if (count == 0) {
//            super.onBackPressed();
//            finish();
//        } else {
//            getSupportFragmentManager().popBackStack();
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_content);
//            if (fragment instanceof HomeFragment) {
//                navigation.setSelectedItemId(R.id.navigation_home);
//            } else if (fragment instanceof BookTourFragment) {
//                navigation.setSelectedItemId(R.id.navigation_tour);
//            } else if (fragment instanceof SearchFragment) {
//                navigation.setSelectedItemId(R.id.navigation_search);
//            } else if (fragment instanceof ProfileFragment) {
//                navigation.setSelectedItemId(R.id.navigation_account);
//            }
//        }
    }
}