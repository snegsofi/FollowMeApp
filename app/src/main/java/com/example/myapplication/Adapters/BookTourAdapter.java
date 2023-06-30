package com.example.myapplication.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Classes.BookTour;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookTourAdapter extends RecyclerView.Adapter<BookTourAdapter.ViewHolder> {

    List<BookTour> tours;
    Context context;
    private static final String TAG = "bookTourAdapter";
    FirebaseFirestore db;
    String userId;

    // создание новой ячейки
    @NonNull
    @Override
    public BookTourAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.tours_rv, parent, false);

        return new ViewHolder(contactView);
    }

    // привязка данных к ячейкам
    @Override
    public void onBindViewHolder(@NonNull BookTourAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        db = FirebaseFirestore.getInstance();

        // получение тура по текущей позиции
        BookTour tour = tours.get(position);

        // Установка значений элементам
        TextView textView=holder.placeTourTextView;
        textView.setText(tour.getPlace());

        TextView textView1=holder.priceTourTextView;
        textView1.setText(Integer.toString(tour.getPrice()));

        TextView textView2=holder.nameTourTextView;
        textView2.setText(tour.getName());

        ImageView imageView=holder.photoTourImageView;
        imageView.setClipToOutline(true);

        // создание ссылки с начальным путем к файлу в хранилище из приложения
        StorageReference storageReference=FirebaseStorage.getInstance().getReference("images/"+tour.getPhoto());

        // загрузка изображения из базы данных в компонент ImageView
        try {
            // создание временного файла
            File localFile=File.createTempFile("tempFile",".png");
            // получение файла из хранилища
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Bitmap - объект, хранящий в себе изображение
                            // decodeFile - метод, для создания изображения из различных источников
                            Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        CheckBox checkBox=holder.favouriteTourCheckBox;
        checkBox.setChecked(false);

        // проверка, авторизован пользователь или нет
        if(!userId.isEmpty()){
            // получение списка избранных туров определенного пользователя в таблице WishList
            db.collection("WishList").document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.get("favouriteList"));

                                    // получение списка избранных туров
                                    ArrayList<String> tours=new ArrayList<>((Collection<String>)document.get("favouriteList"));

                                    // выделение на экране избранных туров
                                    if(tours.contains(tour.getId())){
                                        checkBox.setChecked(true);
                                    }
                                    else{
                                        checkBox.setChecked(false);
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


        // обработка нажатия на элемент itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(tour, position);
            }
        });
    }

    // возвращает количество элементов в списке
    @Override
    public int getItemCount() {
        return tours.size();
    }

    // информация о ячейках
    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public TextView priceTourTextView;
        public TextView placeTourTextView;
        public TextView nameTourTextView;
        public ImageView photoTourImageView;
        public CheckBox favouriteTourCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            priceTourTextView = (TextView) itemView.findViewById(R.id.tour_rv_price);
            placeTourTextView = (TextView) itemView.findViewById(R.id.tour_rv_place);
            nameTourTextView = (TextView) itemView.findViewById(R.id.tour_rv_name);
            photoTourImageView=(ImageView) itemView.findViewById(R.id.imageView_tourPhoto_rv);
            favouriteTourCheckBox=itemView.findViewById(R.id.tour_checkbox_addToFavourite);

            // обработка нажатия на checkBox для добавления в избранное или его удаление из него
            favouriteTourCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"checkbox clicked");
                    // проверка, авторизован пользователь или нет
                    if(userId.isEmpty()){
                        // запрет добавления в избранное для неавторизованного пользователя
                        // кнопка checkBox будет не доступна
                        favouriteTourCheckBox.setChecked(false);
                        Toast.makeText(context, "Необходимо авторизоваться", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // метод обработки нажатия на checkBox
                        clickListener.setCheckedStateCheckBox(tours.get(getAdapterPosition()).getId(), favouriteTourCheckBox.isChecked());
                    }
                }
            });
        }
    }

    // конструктор класса
    public BookTourAdapter(Context context, List<BookTour> tours, ItemClickListener clickListener, String userId) {
        this.context=context;
        this.tours = tours;
        this.clickListener=clickListener;
        this.userId=userId;
    }

    private ItemClickListener clickListener;

    public interface ItemClickListener{
        void onItemClick(BookTour tour, int position);
        void setCheckedStateCheckBox(String tourId, boolean isChecked);
    }

    // изменение данных адаптера
    public void setFilterList(List<BookTour> filterList){
        this.tours=filterList;
        notifyDataSetChanged();
    }
}
