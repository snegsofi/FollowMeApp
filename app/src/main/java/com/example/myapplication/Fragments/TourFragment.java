package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapters.ViewPagerAdapter;
import com.example.myapplication.Classes.BookTour;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TourFragment extends Fragment {

    private static final String TAG = "tour";

    private static final String ARG_PARAM1="tourId";
    private static final String ARG_PARAM2="userId";
    private static final String ARG_PARAM3="organizerId";
    private static final String ARG_PARAM4="citySearchItem";
    private static final String ARG_PARAM5="dateSearchItem";
    private static final String ARG_PARAM6="chipSelectedIndex";


    String id;
    String citySearchItemArgs;
    String dateSearchItemArgs;
    Integer chipSelectedIndexArgs;
    FirebaseFirestore db;
    BookTour tour;
    //ImageView tourPhoto;
    TextView tourName;
    TextView tourPlace;
    TextView tourDescription;
    TextView tourDate;
    TextView tourDuration;
    TextView tourGroup;
    TextView tourPrice;
    ImageButton tourBack;
    Button bookingBtn;
    ViewPager viewPager;
    List<String> photoList=new ArrayList<>();

    // инициализация компонентов
    public void initialization(View view){
        db = FirebaseFirestore.getInstance();
        tourName=view.findViewById(R.id.tour_name);
        //tourPhoto=view.findViewById(R.id.tour_image);
        tourPlace=view.findViewById(R.id.tour_place);
        tourDescription=view.findViewById(R.id.tour_description);
        tourDate=view.findViewById(R.id.tour_date);
        tourDuration=view.findViewById(R.id.tour_duration);
        tourGroup=view.findViewById(R.id.tour_group);
        tourPrice=view.findViewById(R.id.tour_price);
        tourBack=view.findViewById(R.id.back_tour_imageButton);
        bookingBtn=view.findViewById(R.id.btn_booking);
        viewPager=view.findViewById(R.id.tour_view_pager);
    }

    // метод для загрузки фрагмента из другой активности
    public static TourFragment newInstance(String id, String userId, String organizerId)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, userId);
        args.putString(ARG_PARAM3, organizerId);
        TourFragment fragment = new TourFragment();
        fragment.setArguments(args);
        Log.d("idTourFragment",id);
        return fragment;
    }

    // метод для загрузки фрагмента из другой активности
    public static TourFragment newInstance(String id, String userId, String organizerId, String citySearchItem, String dateSearchItem, Integer chipSelectedIndex)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, userId);
        args.putString(ARG_PARAM3, organizerId);
        args.putString(ARG_PARAM4,citySearchItem);
        args.putString(ARG_PARAM5,dateSearchItem);
        args.putInt(ARG_PARAM6,chipSelectedIndex);
        TourFragment fragment = new TourFragment();
        fragment.setArguments(args);
        Log.d("idTourFragment",id);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tour_layout,container,false);

        // инициализация компонентов
        initialization(view);

        // обработка нажатия на кнопку "Назад"
        tourBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(BookTourFragment.newInstance(userId,citySearchItemArgs,dateSearchItemArgs,chipSelectedIndexArgs));
            }
        });

        // обработка нажатия на кнопку для перехода в другое приложение для связи с турагентом
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // отображение диалогового окна
                String[] AppChoose = { "WhatsApp", "Telegram", "Телефон", "Сообщение", "Электронная почта" };
                AlertDialog.Builder dialog= new AlertDialog.Builder(getContext());
                dialog
                        .setTitle("Свяжитесь с нами")
                        //.setMessage("Перейти в WhatsApp?")
                        //.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        //    @Override
                        //    public void onClick(DialogInterface dialog, int which) {
                        //        // переход в WhatsApp
                        //        String url = "https://api.whatsapp.com/send?phone="+"+79967689413";
                        //        Intent i = new Intent(Intent.ACTION_VIEW);
                        //        i.setData(Uri.parse(url));
                        //        startActivity(i);
                        //    }
                        //})
                        .setNeutralButton("Назад", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        //.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        //    @Override
                        //    public void onClick(DialogInterface dialog, int which) {
                        //    }
                        //})
                        .setSingleChoiceItems(AppChoose, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Agents").document(organizerId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Log.d(TAG, "DocumentSnapshot data: " + document.get("favouriteList"));
                                                        String phone=document.getString("phone");
                                                        String email=document.getString("email");

                                                        String url="";
                                                        switch(which){
                                                            case 0:
                                                                url = "https://api.whatsapp.com/send?phone="+phone;
                                                                break;
                                                            case 1:
                                                                url="https://telegram.me/"+phone;
                                                                break;
                                                            case 2:
                                                                url="tel:"+phone;
                                                                break;
                                                            case 3:
                                                                url="smsto:"+phone;
                                                                break;
                                                            case 4:
                                                                url="mailto:"+email;
                                                                break;
                                                        }
                                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                                        i.setData(Uri.parse(url));
                                                        startActivity(i);
                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                }
                                            }
                                        });
                            }
                        })
                        .show();
            }
        });

        // чтение данных из базы данных
        readData();
        return view;
    }

    String userId;
    String organizerId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // получение значения переданных аргументов
        if(getArguments()!=null) {
            id = getArguments().getString(ARG_PARAM1);
            userId=getArguments().getString(ARG_PARAM2);
            organizerId=getArguments().getString(ARG_PARAM3);
            citySearchItemArgs=getArguments().getString(ARG_PARAM4);
            dateSearchItemArgs=getArguments().getString(ARG_PARAM5);
            chipSelectedIndexArgs=getArguments().getInt(ARG_PARAM6);
        }
    }


    // чтение данных из базы данных
    public void readData(){
        db.collection("BookTour")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // выбор необходимой строки таблицы
                                if(id.equals(document.get("id").toString())){
                                    tour=new BookTour(
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
                                            document.get("dateEnd").toString()
                                    );

                                    // выгрузка изображения из базы данных
                                    loadImagesFromFolder(tour.getId());
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        // заполнение элементов экрана данными
                        initializationData(tour);
                    }
                });
    }

    // заполнение элементов экрана данными
    public void initializationData(BookTour tour){
        tourName.setText(tour.getName());
        tourPlace.setText(tour.getPlace());
        tourDescription.setText(tour.getDescription());
        tourDate.setText(tour.getDateStart()+"-"+tour.getDateEnd());
        tourDuration.setText(Integer.toString(tour.getDuration()));
        tourGroup.setText(Integer.toString(tour.getGroup()));
        tourPrice.setText(Integer.toString(tour.getPrice()));
    }

    // загрузка другого фрагмента
    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    // загрузка изображения из базы данных
    public void loadImagesFromFolder(String photoFolder){
        // создание ссылки с начальным путем к файлу в хранилище из приложения
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("images/"+photoFolder);
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        ArrayList<String> photo=new ArrayList<>();
                        for (StorageReference item : listResult.getItems()) {
                            photo.add("images/"+photoFolder+"/"+item.getName());
                            Log.d("tourFragment", item.getName());
                        }
                        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getContext(),photo, "TourFragment");
                        viewPager.setAdapter(viewPagerAdapter);
                        viewPager.setCurrentItem(0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failure on load image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}








