package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TourFragment extends Fragment {

    private static final String TAG = "tour";

    private static final String ARG_PARAM1="tourId";

    String id;

    FirebaseFirestore db;
    BookTour tour;

    ImageView tourPhoto;
    TextView tourName;
    TextView tourPlace;
    TextView tourDescription;
    TextView tourDate;
    TextView tourDuration;
    TextView tourGroup;
    TextView tourPrice;
    ImageButton tourBack;

    // инициализация компонентов
    public void initialization(View view){
        db = FirebaseFirestore.getInstance();
        tourName=view.findViewById(R.id.tour_name);
        tourPhoto=view.findViewById(R.id.tour_image);
        tourPlace=view.findViewById(R.id.tour_place);
        tourDescription=view.findViewById(R.id.tour_description);
        tourDate=view.findViewById(R.id.tour_date);
        tourDuration=view.findViewById(R.id.tour_duration);
        tourGroup=view.findViewById(R.id.tour_group);
        tourPrice=view.findViewById(R.id.tour_price);
        tourBack=view.findViewById(R.id.back_tour_imageButton);
    }

    // метод для загрузки фрагмента из другой активности
    public static TourFragment newInstance(String id)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
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

        //Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

        // обработка нажатия на кнопку "Назад"
        tourBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new BookTourFragment());
            }
        });

        // чтение данных из базы данных
        readData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // получение значения переданных аргументов
        if(getArguments()!=null) {
            id = getArguments().getString(ARG_PARAM1);
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
                                            document.get("dates").toString(),
                                            Integer.parseInt(document.get("duration").toString()),
                                            document.get("place").toString(),
                                            Integer.parseInt(document.get("group").toString()),
                                            Integer.parseInt(document.get("toursSold").toString()),
                                            Boolean.parseBoolean(document.get("isAvailable").toString()),
                                            document.get("organizer").toString(),
                                            document.get("direction").toString()
                                    );

                                    Toast.makeText(getContext(),"yes", Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "YES");

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
        tourDate.setText(tour.getDates());
        tourDuration.setText(Integer.toString(tour.getDuration()));
        tourGroup.setText(Integer.toString(tour.getGroup()));
        tourPrice.setText(Integer.toString(tour.getPrice()));
        loadImage(tour.getPhoto(),tourPhoto);
    }

    // загрузка другого фрагмента
    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    // загрузка изображения из базы данных
    public void loadImage(String photoName, ImageView image){
        // создание ссылки с начальным путем к файлу в хранилище из приложения
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("images/"+photoName);
        // загрузка изображения из базы данных в компонент ImageView
        try {
            // создание временного файла
            File localFile=File.createTempFile("tempFile",".png");
            // получение файла из хранилища
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            image.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}








