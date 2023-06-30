package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.ChipDirectionAdapter;
import com.example.myapplication.Adapters.CitiesAdapter;
import com.example.myapplication.Classes.ChipDirectionModel;
import com.example.myapplication.Classes.Cities;
import com.example.myapplication.Json.Data;
import com.example.myapplication.Json.Gallery;
import com.example.myapplication.Json.General;
import com.example.myapplication.Json.JsonApi;
import com.example.myapplication.Json.MuseumAdapter;
import com.example.myapplication.Json.Museums;
import com.example.myapplication.R;
import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Cache;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements MuseumAdapter.ItemClickListener,
    ChipDirectionAdapter.ItemClickListener{

    public static HomeFragment newInstance()
    {

        Log.d(TAG,"constructor end");
        return new HomeFragment();

    }

    private static final String ARG_PARAM1="citySearchItem";
    private static final String ARG_PARAM2="chipSelectedItem";
    private static final String ARG_PARAM3="rvPosition";

    public static HomeFragment newInstance(String citySearchItem, Integer chipSelectedPosition, Integer position)
    {
        Log.d(TAG,"newInstance start");
        // передача данных о выбранном месте (парке, событии, музее)
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,citySearchItem);
        args.putInt(ARG_PARAM2,chipSelectedPosition);
        args.putInt(ARG_PARAM3,position);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        Log.d(TAG,"newInstance end");
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"on create start");
        super.onCreate(savedInstanceState);
        // получение информации о выбранном музее
        Bundle bundle = getArguments();
        if(bundle!=null){
            citySearchItem=bundle.getString(ARG_PARAM1);
            chipSelectedPosition=bundle.getInt(ARG_PARAM2);
            rvPosition=bundle.getInt(ARG_PARAM3);
        }

        Log.d(TAG,"on create end");


    }

    private static final String TAG = "HomeFragment";
    RecyclerView attraction_rv;
    RecyclerView chip_rv;
    ChipDirectionAdapter chipAdapter;
    MuseumAdapter museumAdapter;
    TextView searchTextView;
    String chipSelectedItem="Музеи";
    String citySearchItem="";
    Integer chipSelectedPosition=0;
    Integer rvPosition=0;

    // инициализация компонентов
    public void initialComponent(View view){
        Log.d(TAG,"initial start");

        attraction_rv=(RecyclerView) view.findViewById(R.id.rv_attraction);
        chip_rv=view.findViewById(R.id.chip_rv);
        searchTextView=view.findViewById(R.id.home_et_search);
        searchTextView.setText(citySearchItem);

        // программное добавление компонентов Chips
        List<ChipDirectionModel> chipDirectionList=new ArrayList<>();
        chipDirectionList.add(new ChipDirectionModel("Музеи", false));
        chipDirectionList.add(new ChipDirectionModel("Выставки", false));
        chipDirectionList.add(new ChipDirectionModel("Парки", false));

        for(int i=0;i<chipDirectionList.size();i++){
            if(i==chipSelectedPosition){
                chipSelectedItem=chipDirectionList.get(i).getTitle();
                chipDirectionList.get(i).setChecked(true);
                break;
            }
        }

        chipAdapter=new ChipDirectionAdapter(getContext(),chipDirectionList,HomeFragment.this);
        chip_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        chip_rv.setAdapter(chipAdapter);
        chipAdapter.notifyDataSetChanged();

        Log.d(TAG,"initial end");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        Log.d(TAG,"oncreateview start");

        View view=inflater.inflate(R.layout.home_layout,container,false);

        initialComponent(view);

        Log.d(TAG,"before filter"+chipSelectedItem);
        filterList(chipSelectedItem, "filter");

        // обработка изменение текста в textView
        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // фильрация мест (парков, выставок, мызеев) по введенному тексту
                filterList(s.toString(),"search");
                citySearchItem=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.d(TAG,"oncreateview end");

        return view;
    }

    // Переход в окно подробной информации о выбранном месте по нажатию на view-элемент
    @Override
    public void onItemClickMuseum(General generalMuseumInfo, Integer position)  {
        //loadFragment(AttractionFragment.newInstance(generalMuseumInfo, citySearchItem,chipSelectedPosition,position));

        Log.d(TAG,"on item click museum start");

        String name=generalMuseumInfo.getName();
        String description=generalMuseumInfo.getDescription();
        String address="";
        String website="";
        String price="";
        String dates="";
        ArrayList<String> gallery=new ArrayList<>();

        // адрес
        if(generalMuseumInfo.getAddress()!=null){
            address=generalMuseumInfo.getAddress().getFullAddress();
        }
        else
        {
            address=generalMuseumInfo.getPlaces().get(0).getAddress().getFullAddress();
        }
        // вебсайт
        if(generalMuseumInfo.getContacts()!=null){
            if(generalMuseumInfo.getContacts().getWebsite()!=null){
                website=generalMuseumInfo.getContacts().getWebsite();
                }
        }
        // цена
        if(generalMuseumInfo.isFree()){
            price="Бесплатно";
        }
        if(generalMuseumInfo.getPrice()>0){
            price="от "+generalMuseumInfo.getPrice()+" руб.";
        }
        // даты
        if(generalMuseumInfo.getStart()!=null && generalMuseumInfo.getEnd()!=null){
            dates=new SimpleDateFormat("dd.MM.yyyy").format(generalMuseumInfo.getStart())+"-"+new SimpleDateFormat("dd.MM.yyyy").format(generalMuseumInfo.getEnd());
        }
        // список изображений
        List<Gallery> galleryList;
        gallery.add(generalMuseumInfo.getImage().getUrl());
        if(generalMuseumInfo.getGallery()!=null){
            galleryList=generalMuseumInfo.getGallery();

            for(int i=0;i<galleryList.size();i++){
                gallery.add(galleryList.get(i).getUrl());
            }
        }

        loadFragment(AttractionFragment.newInstance(name,description,address,website, price,dates,gallery, citySearchItem,chipSelectedPosition,position));

        Log.d(TAG,"on item museum click end");
    }


    // загрузка другого фрагмента
    private void loadFragment(Fragment fragment) {
        Log.d(TAG,"loadfragment start");
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
        Log.d(TAG,"loadfragment end");
    }


    // Обработка нажатия на элемент Chip.
    // Вызов метода filterList для фильтрации списка в соответствии с выбранным Chip-элементов
    @Override
    public void onChipDirectionItemClick(int position, String text) {
        Log.d(TAG,"chipitemclick start");

        // изменение состояния Chips
        for(int i=0;i<3;i++){
            Chip chip1=(Chip) chip_rv.getChildAt(i);
            if(i!=position){
                chip1.setChecked(false);
            }
            else{
                chip1.setChecked(true);
            }
        }

        chipSelectedPosition=position;
        chipSelectedItem=text;
        // выбор подходящих запросу данных
        filterList(text, "filter");

        Log.d(TAG,"chipitemclick end");
    }

    // фильтрация данных
    private void filterList(String text, String searchType) {

        Log.d(TAG,"filter start");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opendata.mkrf.ru/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonApi json=retrofit.create(JsonApi.class);

        // фильтрация по названию места
        if(!searchTextView.getText().toString().isEmpty() || searchType.equals("search"))
        {   if(chipSelectedItem.equals("Музеи")){
                readJson(json.sortMuseums("{\"data.general.locale.name\":{\"$contain\":\""+searchTextView.getText()+"\"}}",10));
            }
            if(chipSelectedItem.equals("Выставки")){
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                Log.d(TAG,"events/$?f={\"data.general.start\":{\"$gt\":\""+new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()) +"\"},\"data.general.organizerPlace.name\":{\"$contain\":\""+searchTextView.getText()+"\"}}&l=10");
                readJson(json.sortEvent("{\"data.general.start\":{\"$gt\":\"" +new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime())+"\"},\"data.general.organizerPlace.name\":{\"$contain\":\""+searchTextView.getText()+"\"}}",10));
            }
            if(chipSelectedItem.equals("Парки")){
                readJson(json.sortParks("{\"data.general.locale.name\":{\"$contain\":\""+searchTextView.getText()+"\"}}",10));
            }
        }
        // фильтрация по типу места
        else if (searchType.equals("filter")) {
                if (text.equals("Музеи")) {
                    Log.d(TAG,"filter museum");
                    readJson(json.getMuseums());
                }
                if (text.equals("Выставки")) {
                    Log.d(TAG,"filter events");
                    readJson(json.getEvents("{\"data.general.start\":{\"$eq\":\""+new SimpleDateFormat("dd.MM.yyyy").format(new Date())+"\"}}",10));
                }
                if (text.equals("Парки")) {
                    Log.d(TAG,"filter parks");
                    readJson(json.getParks());
                }
        }
        Log.d(TAG,"filter end");
    }


    // загрузка данных в recycler view
    public void loadRecyclerView(List<Data> data){

        Log.d(TAG,"loadRecyclerView start");
        Museums attraction=new Museums();
        attraction.setData(data);


        // вывод мест в recycler view
        museumAdapter=new MuseumAdapter(getContext(), attraction, HomeFragment.this);
        attraction_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        attraction_rv.setAdapter(museumAdapter);
        museumAdapter.notifyDataSetChanged();

        if(data.isEmpty()){
            Toast.makeText(getContext(), "Информация не найдена", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG,"loadRecyclerView end");
    }

    public void readJson(Call<Museums> call){

        Log.d(TAG,"readJson start");
        Log.d(TAG,call.toString());
        call.enqueue(new Callback<Museums>() {
            @Override
            public void onResponse(Call<Museums> call, Response<Museums> response) {
                // код 200
                Log.d(TAG, "Response"+response.message());
                System.out.println("Response"+response.message());


                if (response.isSuccessful()) {

                    loadRecyclerView(response.body().getData());

                } else {

                    // Также можете использовать ResponseBody для получения текста ошибки
                    ResponseBody errorBody = response.errorBody();
                    try {
                        System.out.println(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Museums> call, Throwable t) {
                Log.d(TAG, "Failure: "+t.getMessage());
                System.out.println("Failure: "+t.getMessage());
            }
        });
        Log.d(TAG,"readjson end");
    }


}
