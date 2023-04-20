package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BookTourAdapter extends RecyclerView.Adapter<BookTourAdapter.ViewHolder> {

    List<BookTour> tours;
    Context context;

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

        BookTour tour = tours.get(position);

        // Установка значений элементам
        TextView textView=holder.placeTourTextView;
        textView.setText(tour.getPlace());

        TextView textView1=holder.priceTourTextView;
        textView1.setText(Integer.toString(tour.getPrice()));

        TextView textView2=holder.nameTourTextView;
        textView2.setText(tour.getName());

        ImageView imageView=holder.photoTourImageView;

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

    // инофрмация о ячейках
    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public TextView priceTourTextView;
        public TextView placeTourTextView;
        public TextView nameTourTextView;
        public ImageView photoTourImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            priceTourTextView = (TextView) itemView.findViewById(R.id.tour_rv_price);
            placeTourTextView = (TextView) itemView.findViewById(R.id.tour_rv_place);
            nameTourTextView = (TextView) itemView.findViewById(R.id.tour_rv_name);
            photoTourImageView=(ImageView) itemView.findViewById(R.id.imageView_tourPhoto_rv);

        }
    }

    // конструктор класса
    public BookTourAdapter(Context context, List<BookTour> dishList, ItemClickListener clickListener) {
        this.context=context;
        tours = dishList;
        this.clickListener=clickListener;

    }

    private ItemClickListener clickListener;

    public interface ItemClickListener{
        void onItemClick(BookTour tour, int position);
    }

    // изменение данных адаптера
    public void setFilterList(List<BookTour> filterList){
        this.tours=filterList;
        notifyDataSetChanged();
    }

}
