package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements CitiesAdapter.ItemClickListener{

    public static HomeFragment newInstance()
    {
        return new HomeFragment();

    }

    List<Cities> citiesList;
    FirebaseFirestore db;
    RecyclerView cartRecyclerView;
    CitiesAdapter citiesAdapter;
    private static final String TAG = "cities";
    RecyclerView cities_rv;

    public void initialComponent(View view){


        db = FirebaseFirestore.getInstance();
        cities_rv=(RecyclerView) view.findViewById(R.id.rv_cities);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_layout,container,false);

        initialComponent(view);

        readCity();

        return view;
    }


    public void readCity(){

        db.collection("City")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ////Cities cities=document.toObject(Cities.class);
//
                                //Cities cities1=new Cities(document.toObject(Cities.class).getName(),document.toObject(Cities.class).getUrl_photo());
                                //citiesList.add(cities1);
//
                                //// Создание адаптера
                                //citiesAdapter = new CitiesAdapter(getContext(), citiesList,HomeFragment.this);
                                //// размещение элементов
                                //cities_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                //// Прикрепрепляем адаптер к recyclerView
                                //cities_rv.setAdapter(citiesAdapter);
                                //citiesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });

    }

    @Override
    public void onItemClick(Cities cities, int position) {

    }
}
