package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.BookTourAdapter;
import com.example.myapplication.Classes.BookTour;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment implements BookTourAdapter.ItemClickListener {

    private static final String TAG = "searchFragment";
    private static final String ARG_PARAM1="param1";
    String userId;
    RecyclerView wishListRV;
    FirebaseFirestore db;
    BookTourAdapter tours_adapter;
    List<String> wishList=new ArrayList<>();
    List<BookTour> tours=new ArrayList<>();

    public static SearchFragment newInstance(String userId)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,userId);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            userId = getArguments().getString(ARG_PARAM1);
        }
    }

    public void initializationComponents(View view){
        wishListRV=view.findViewById(R.id.search_rv);
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.search_layout,container,false);
        initializationComponents(view);

        if(!userId.isEmpty()){
            readData(getWishListFromFirebase());
        }
        else{
            Toast.makeText(getContext(), "Необходимо зарегистрироваться", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    public List<String> getWishListFromFirebase() {
        db.collection("WishList").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.get("favouriteList"));
                                ArrayList<String> tours = new ArrayList<>((Collection<String>) document.get("favouriteList"));
                                wishList = tours;
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        return wishList;
    }



    // чтение данных из базы данных
    public void readData(List<String> favouriteList){
        if(favouriteList.isEmpty()){
            Log.d(TAG,"wl is empty");
        }
        for(int i=0;i<favouriteList.size();i++){
            Log.d(TAG,favouriteList.get(i));
        }

        db.collection("BookTour")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                db.collection("WishList").document(userId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot doc = task.getResult();
                                                    if (doc.exists()) {
                                                        Log.d(TAG, "DocumentSnapshot data: " + doc.get("favouriteList"));
                                                        ArrayList<String> favList = new ArrayList<>((Collection<String>) doc.get("favouriteList"));
                                                        for (int i = 0; i < favList.size(); i++) {
                                                            Log.d(TAG, "fav[" + i + "]=" + favList.get(i) + " document=" + document.get("id"));
                                                            if (favList.get(i).equals(document.get("id"))) {
                                                                tours.add(new BookTour(
                                                                        document.get("id").toString(),
                                                                        document.get("photo").toString(),
                                                                        document.get("name").toString(),
                                                                        document.get("description").toString(),
                                                                        Integer.parseInt(document.get("price").toString()),
                                                                        Integer.parseInt(document.get("duration").toString()),
                                                                        document.get("place").toString(),
                                                                        Integer.parseInt(document.get("group").toString()),
                                                                        Integer.parseInt(document.get("toursSold").toString()),
                                                                        Boolean.parseBoolean(document.get("isAvailable").toString()),
                                                                        document.get("organizer").toString(),
                                                                        document.get("direction").toString(),
                                                                        document.get("dateStart").toString(),
                                                                        document.get("dateEnd").toString()));
                                                            }


                                                            // сортировка туров по дате
                                                            Collections.sort(tours);

                                                            // вывод туров в recycler view
                                                            tours_adapter = new BookTourAdapter(getContext(), tours, SearchFragment.this, userId);
                                                            wishListRV.setLayoutManager(new LinearLayoutManager(getContext()));
                                                            wishListRV.setAdapter(tours_adapter);
                                                            tours_adapter.notifyDataSetChanged();
                                                        }
                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });


                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onItemClick(BookTour tour, int position) {

    }

    @Override
    public void setCheckedStateCheckBox(String tourId, boolean isChecked) {

    }
}
