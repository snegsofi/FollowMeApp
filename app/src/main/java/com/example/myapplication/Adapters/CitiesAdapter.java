package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Classes.Cities;
import com.example.myapplication.R;

import java.util.List;

public class CitiesAdapter /*extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> */{
//
//
//    Context context;
//    List<Cities> cities;
//
//    @NonNull
//    @Override
//    public CitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//
//        // Inflate the custom layout
//        View contactView = inflater.inflate(R.layout.cities_rv, parent, false);
//
//        return new ViewHolder(contactView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CitiesAdapter.ViewHolder holder, int position) {
//        Cities city = cities.get(position);
//
//        // Установка значений элементам
//        TextView textView = holder.nameCityTextView;
//        textView.setText(city.getName());
//        //ImageView imageView=holder.photoCityImageView;
//        //imageView.setImageResource(city.getPhoto());
//
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return cities.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView nameCityTextView;
//        public ImageView photoCityImageView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameCityTextView = (TextView) itemView.findViewById(R.id.textView_cityName);
//            photoCityImageView=(ImageView) itemView.findViewById(R.id.imageView_cityPhoto);
//        }
//    }
//
//    public CitiesAdapter(Context context, List<Cities> citiesList, ItemClickListener clickListener) {
//        this.context=context;
//        cities = citiesList;
//        this.clickListener=clickListener;
//
//    }
//
//    private ItemClickListener clickListener;
//
//    public interface ItemClickListener{
//        void onItemClick(Cities cities, int position);
//    }
}
