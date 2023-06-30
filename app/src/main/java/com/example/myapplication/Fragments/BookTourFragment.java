package com.example.myapplication.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.BookTourAdapter;
import com.example.myapplication.Adapters.ChipDirectionAdapter;
import com.example.myapplication.Classes.ChipDirectionModel;
import com.example.myapplication.Classes.BookTour;
import com.example.myapplication.Classes.WishList;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.DayViewDecorator;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BookTourFragment extends Fragment implements BookTourAdapter.ItemClickListener,
        ChipDirectionAdapter.ItemClickListener {

    // метод для получения id пользователя
    public static BookTourFragment newInstance(String userId)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,userId);
        BookTourFragment fragment = new BookTourFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // метод для получения id пользователя
    public static BookTourFragment newInstance(String userId, String citySearchItem,String dateSearchItem,Integer chipSelected)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,userId);
        args.putString(ARG_PARAM2,citySearchItem);
        args.putString(ARG_PARAM3,dateSearchItem);
        args.putInt(ARG_PARAM4,chipSelected);
        BookTourFragment fragment = new BookTourFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final String TAG = "tours";
    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="citySearchItem";
    private static final String ARG_PARAM3="dateSearchItem";
    private static final String ARG_PARAM4="chipSelectedIndex";

    String userId;
    List<BookTour> tours=new ArrayList<>();
    FirebaseFirestore db;
    RecyclerView tours_rv;
    BookTourAdapter tours_adapter;
    ChipGroup chipGroup;
    ChipDirectionAdapter chipAdapter;
    RecyclerView chip_rv;
    EditText etSearch;
    TextView txtDate;
    String citySearchItemArgs="";
    String dateSearchItemArgs="";
    MaterialDatePicker materialDatePicker;

    // инициализация компонентов
    public void initialization(View view){
        tours=new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        tours_rv=view.findViewById(R.id.rv_tour);
        chip_rv=view.findViewById(R.id.chip_rv_tour);
        chipGroup=view.findViewById(R.id.chipGroup);
        etSearch=view.findViewById(R.id.book_tour_et_search);
        txtDate=view.findViewById(R.id.book_tour_txt_date);
        etSearch.setText(citySearchItemArgs);
        txtDate.setText(dateSearchItemArgs);
        //addToFavouriteTourIdList=new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // получение id пользователя
        if(getArguments()!=null) {
            userId = getArguments().getString(ARG_PARAM1);
            citySearchItemArgs=getArguments().getString(ARG_PARAM2);
            dateSearchItemArgs=getArguments().getString(ARG_PARAM3);
            chipSelectedIndex=getArguments().getInt(ARG_PARAM4);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.book_tour_layout,container,false);

        initialization(view);

        // чтение данных из таблицы BoorTour в Firebase
        readData();

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        // Работа с DatePickerDialog
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.YEAR,1);
        calendar.add(Calendar.MONTH,6);

        CalendarConstraints.DateValidator dateValidator= DateValidatorPointForward.from(new Date().getTime());
        CalendarConstraints calendarConstraints=new CalendarConstraints.Builder().setValidator(dateValidator).setEnd(calendar.getTime().getTime()).build();

         materialDatePicker=MaterialDatePicker.Builder.dateRangePicker()
                .setCalendarConstraints(calendarConstraints)
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).build();
        // возможность выбора дат на следующие полтора года
        calendar.add(Calendar.YEAR,1);
        calendar.add(Calendar.MONTH,6);

        // работа с календарем
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(materialDatePicker.isAdded()){
                    return;
                }

                materialDatePicker.show(getParentFragmentManager(),"Tag_picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {


                        //txtDate.setText(materialDatePicker.getHeaderText());

                        // получение диапазона выбранных дат
                        Pair selectedDates = (Pair) materialDatePicker.getSelection();
                        //then obtain the startDate & endDate from the range
                        final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                        //assigned variables
                        Date startDate = rangeDate.first;
                        Date endDate = rangeDate.second;

                        startDate=removeTime(startDate);
                        endDate=removeTime(endDate);

                        String dateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(startDate);
                        String dateEnd= new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(endDate);


                        txtDate.setText(dateStart+"-"+dateEnd);

                        filterList(tours);

                    }
                });
            }
        });

        // поиск по городам
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // метод для вывода данных в recycler view в соответствии с запросом пользователя
                filterList(tours);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        //etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        //    @Override
        //    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //        // метод для вывода данных в recycler view в соответствии с запросом пользователя
        //        filterList(v.getText().toString(),"search");
        //        return false;
        //    }
        //});

        //// обработка поиска текста на экране
        //searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        //    // вызывается при отправке запроса пользователем
        //    @Override
        //    public boolean onQueryTextSubmit(String query) {
        //        return false;
        //    }
        //    // вызывается при изменение запроса пользователем
        //    @Override
        //    public boolean onQueryTextChange(String newText) {
        //        // метод для вывода данных в recycler view в соответствии с запросом пользователя
        //        filterList(newText,"search");
        //        return false;
        //    }
        //});

        // программное добавление компонентов Chips
        List<ChipDirectionModel> chipDirectionList=new ArrayList<>();

        chipDirectionList.add(new ChipDirectionModel("Все", false));
        chipDirectionList.add(new ChipDirectionModel("Горы", false));
        chipDirectionList.add(new ChipDirectionModel("Озера", false));
        chipDirectionList.add(new ChipDirectionModel("Поход", false));
        chipDirectionList.add(new ChipDirectionModel("Велотур", false));


        chipDirectionList.get(chipSelectedIndex).setChecked(true);
        chipSelectedItem=chipDirectionList.get(chipSelectedIndex).getTitle();

        if(!tours.isEmpty()){
            filterList(tours);
        }
        chipAdapter=new ChipDirectionAdapter(getContext(),chipDirectionList,BookTourFragment.this);
        chip_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        chip_rv.setAdapter(chipAdapter);
        chipAdapter.notifyDataSetChanged();

        return view;
    }


    // метод, обрабатывающий нажатие на checkBox
    @Override
    public void setCheckedStateCheckBox(String tourId, boolean isChecked) {
        Log.d(TAG,"userId"+ userId);
        // проверка авторизации пользователя
        if (!userId.isEmpty()) {
            // получение списка избранных туров определенного пользователя из таблицы userId
            db.collection("WishList").document(userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> tours;
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.get("favouriteList"));

                                // получение списка избранных туров
                                tours = new ArrayList<>((Collection<String>) documentSnapshot.get("favouriteList"));

                                // проход по списку туров
                                for (String s : tours) {
                                    // добавление тура при выделении checkbox и отсутсвие выбранного тура в списке
                                    if (isChecked && !s.contains(tourId)) {
                                        tours.add(tourId);
                                        break;
                                    }
                                    // удаление тура из списка избранного
                                    if (!isChecked && s.contains(tourId)) {
                                        tours.remove(tourId);
                                        break;
                                    }
                                }

                                if (!tours.isEmpty()) {
                                    // обновление списка избранных туров
                                    addWishListToFirebase(tours);
                                } else {
                                    // удаление документа со списком избранных туров определенного пользователя
                                    // при отсутствии данных в нем
                                    deleteWishListFromFirebase();
                                }

                            } else {
                                Log.d(TAG, "No such document");
                                // добавление нового документа со списком избранных
                                // для пользователя, который ранее его не имел
                                if (isChecked) {
                                    tours = new ArrayList<>();
                                    tours.add(tourId);
                                    addWishListToFirebase(tours);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "on failure: "+e.getMessage());
                        }
                    });
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            //if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                ArrayList<String> tours;
//                                if (document.exists()) {
//                                    Log.d(TAG, "DocumentSnapshot data: " + document.get("favouriteList"));
//
//                                    // получение списка избранных туров
//                                    tours = new ArrayList<>((Collection<String>) document.get("favouriteList"));
//
//                                    // проход по списку туров
//                                    for (String s : tours) {
//                                        // добавление тура при выделении checkbox и отсутсвие выбранного тура в списке
//                                        if (isChecked && !s.contains(tourId)) {
//                                            tours.add(tourId);
//                                            break;
//                                        }
//                                        // удаление тура из списка избранного
//                                        if (!isChecked && s.contains(tourId)) {
//                                            tours.remove(tourId);
//                                            break;
//                                        }
//                                    }
//
//
//                                    if (!tours.isEmpty()) {
//                                        // обновление списка избранных туров
//                                        addWishListToFirebase(tours);
//                                    } else {
//                                        // удаление документа со списком избранных туров определенного пользователя
//                                        // при отсутствии данных в нем
//                                        deleteWishListFromFirebase();
//                                    }
//
//                                } else {
//                                    Log.d(TAG, "No such document");
//                                    // добавление нового документа со списком избранных
//                                    // для пользователя, который ранее его не имел
//                                    if (isChecked) {
//                                        tours = new ArrayList<>();
//                                        tours.add(tourId);
//                                        addWishListToFirebase(tours);
//                                    }
//                                }
//                            } else {
//                                Log.d(TAG, "get failed with ", task.getException());
//                            }
//                        }
//                    });
        }
    }

    // удаление определенного документа из таблицы избранных туров
    private void deleteWishListFromFirebase() {
        db.collection("WishList").document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }


    // добавление или обновление документа со списком избранных
    public void addWishListToFirebase(List<String> newList) {
        Map<String, Object> wishList = new HashMap<>();
        wishList.put("favouriteList", newList);

        db.collection("WishList").document(userId)
                .set(wishList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

//    public void getWishListFromFirebase(){
//        db.collection("WishList").document(userId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Log.d(TAG, "DocumentSnapshot data: " + document.get("favouriteList"));
//                                ArrayList<String> tours=new ArrayList<>((Collection<String>)document.get("favouriteList"));
//                                selectedBookTourIdList=tours;
//                            } else {
//                                Log.d(TAG, "No such document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//                    }
//                });
//    }


    ////////////////////////
    ////////////////////////
    ////////////////////////


    // метод для обнуления времени
    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // обработка нажатия на конкретный тур
    @Override
    public void onItemClick(BookTour tour, int position) {
        // переход на другой фрагмент (TourFragment)
        loadFragment(TourFragment.newInstance(tour.getId(),userId, tour.getOrganizer(), etSearch.getText().toString(),dateSearchItemArgs,chipSelectedIndex));

        //loadFragment(TourFragment.newInstance(tour.getId(),userId, tour.getOrganizer()));
    }

    // загрузка другого фрагмента
    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


//    public void filterDateList(List<BookTour> list,Date dateStart, Date dateEnd) throws ParseException {
//            ArrayList<BookTour> newList = new ArrayList<>();
//
//            // выбор данных, которые будут добавлены в recycler view
//            for (int i = 0; i < list.size(); i++) {
//                    Date tourDateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(list.get(i).getDateStart());
//                    Date tourDateEnd = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(list.get(i).getDateEnd());
//
//                    System.out.println("ts "+tourDateStart+" te "+tourDateEnd+" s "+dateStart+" e "+dateEnd);
//
//                    if(!tourDateStart.before(dateStart) && !tourDateEnd.after(dateEnd)){
//                            newList.add(list.get(i));
//                        }
//                }
//            if (newList.isEmpty()) {
//                    ArrayList<BookTour> emptyList = new ArrayList<>();
//                    tours_adapter.setFilterList(emptyList);
//                } else {
//                    tours_adapter.setFilterList(newList);
//                }
//        }


    String chipSelectedItem="Все";

    public ArrayList<BookTour> sortByNameAndPlace(List<BookTour> list) {
        ArrayList<BookTour> sortList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            // поиск туров по названию или месту расположения
            if (!etSearch.getText().toString().isEmpty()) {
                if (list.get(i).getName().toLowerCase().contains(etSearch.getText().toString())
                        || list.get(i).getPlace().toLowerCase().contains(etSearch.getText().toString())) {
                    //newList.add(newList.get(i));
                    sortList.add(list.get(i));
                }
            }
        }
        return sortList;
    }

    public ArrayList<BookTour> sortByDate(List<BookTour> list) {
        ArrayList<BookTour> sortList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            // поиск туров, соответсвующих дате начала и конца проведения тура
            if(!txtDate.getText().toString().isEmpty()) {
                // получение диапазона выбранных дат
//                Pair selectedDates = (Pair) materialDatePicker.getSelection();
//                //then obtain the startDate & endDate from the range
//                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
//                //assigned variables
//                Date startDate = rangeDate.first;
//                Date endDate = rangeDate.second;
//
//                startDate = removeTime(startDate);
//                endDate = removeTime(endDate);
//
//                String dateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(startDate);
//                String dateEnd = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(endDate);

                String[] splitDates=txtDate.getText().toString().split("-");
                String dateStartString = splitDates[0];
                String dateEndString = splitDates[1];

                dateSearchItemArgs=dateStartString+"-"+dateEndString;

//                String[] str=txtDate.getText().toString().split("-");
//                String dateStartString=str[0];
//                String dateEndString=str[1];
//                Date dateStart=new Date();
//                Date dateEnd=new Date();
                Date dateStart=new Date();
                Date dateEnd= new Date();
                Date tourDateStart = new Date();
                Date tourDateEnd = new Date();
                try {
                    dateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(dateStartString);
                    dateEnd= new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(dateEndString);

                    tourDateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(list.get(i).getDateStart());
                    tourDateEnd = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(list.get(i).getDateEnd());
                } catch (Exception e) {
                }

//                if(!tourDateStart.before(dateStart) && !tourDateEnd.after(dateEnd)){
//                    //newList.add(list.get(i));
//                    sortList.add(newList.get(i));
//                }
                if (!tourDateStart.before(dateStart) && !tourDateEnd.after(dateEnd)) {
                    //newList.add(list.get(i));
                    sortList.add(list.get(i));
                }
            }
        }
        return sortList;
    }

    // фильтрация списка туров
    public void filterList(List<BookTour> list) {
        ArrayList<BookTour> newList = new ArrayList<>();

        // фильтрация по категориям
        for(int i=0;i<list.size();i++) {
            if (chipSelectedItem.equals("Все")) {
                Log.d(TAG, "chipSelectedItem=Все");
                newList.add(list.get(i));
            } else {
                if (list.get(i).getDirection().toLowerCase().contains(chipSelectedItem.toLowerCase())) {
                    newList.add(list.get(i));
                }
            }
        }

        List<BookTour> sortList=new ArrayList<>();
        if(!etSearch.getText().toString().isEmpty() && !txtDate.getText().toString().isEmpty()){
            ArrayList<BookTour> sortByNameAndPlaceList=sortByNameAndPlace(tours);
            ArrayList<BookTour> sortByDate=sortByDate(tours);

            for(BookTour tour:sortByNameAndPlaceList){
                if(sortByDate.contains(tour)){
                    sortList.add(tour);
                }
            }
        }
        else if(!etSearch.getText().toString().isEmpty() && txtDate.getText().toString().isEmpty()){
            sortList=sortByNameAndPlace(tours);
        }
        else if(etSearch.getText().toString().isEmpty() && !txtDate.getText().toString().isEmpty()){
            sortList=sortByDate(tours);
        }

        // удаление дубликатов
        Set<BookTour> set = new HashSet<>(sortList);
        sortList.clear();
        sortList.addAll(set);
        // обновление отображение туров на экране
        if(etSearch.getText().toString().isEmpty() && txtDate.getText().toString().isEmpty()){
            Collections.sort(newList);
            tours_adapter.setFilterList(newList);
        }
        else if (sortList.isEmpty()) {
            ArrayList<BookTour> emptyList = new ArrayList<>();
            tours_adapter.setFilterList(emptyList);
            Toast.makeText(getContext(), "Информация не найдена", Toast.LENGTH_SHORT).show();
        } else {
            Collections.sort(sortList);
            tours_adapter.setFilterList(sortList);
        }

//        for(int i=0;i<newList.size();i++){
//            // поиск туров по названию или месту расположения
//            if(!etSearch.getText().toString().isEmpty()){
//                if(newList.get(i).getName().toLowerCase().contains(s.toLowerCase())
//                        || newList.get(i).getPlace().toLowerCase().contains(s.toLowerCase())) {
//                    newList.add(newList.get(i));
//                }
//            }
//
//            // поиск туров, соответсвующих дате начала и конца проведения тура
//            if(!txtDate.getText().toString().isEmpty()){
//                String[] str=s.split("-");
//                String dateStartString=str[0];
//                String dateEndString=str[1];
//                Date dateStart=new Date();
//                Date dateEnd=new Date();
//                Date tourDateStart=new Date();
//                Date tourDateEnd=new Date();
//                try{
//                    dateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(dateStartString);
//                    dateEnd= new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(dateEndString);
//
//                    tourDateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(newList.get(i).getDateStart());
//                    tourDateEnd = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(newList.get(i).getDateEnd());
//                }
//                catch (Exception e){
//                }
//
//                if(!tourDateStart.before(dateStart) && !tourDateEnd.after(dateEnd)){
//                    newList.add(list.get(i));
//                }
//            }
//        }
//
//        // удаление дубликатов
//        Set<BookTour> set = new HashSet<>(newList);
//        newList.clear();
//        newList.addAll(set);


//        // выбор данных, которые будут добавлены в recycler view
//        for (int i = 0; i < list.size(); i++) {
//            if(searchType.contains("search")){
//                // если мы применили какую-либо фильтрацию к списку (например, по дате), поиск будет осуществляться только по
//                // элементам, подходящим под выбранные фильры (например, даты)
//                if(list.get(i).getName().toLowerCase().contains(text.toLowerCase())
//                        || list.get(i).getPlace().toLowerCase().contains(text.toLowerCase())) {
//                    newList.add(list.get(i));
//                }
//            }
//            // фильрация по направлению (море, озера и т.д.)
//            if(searchType.contains("filter"))
//            {
//                if(list.get(i).getDirection().toLowerCase().contains(text.toLowerCase())) {
//                    newList.add(list.get(i));
//                }
//            }
//            if(text.toLowerCase().contains("все")){
//                newList.add(list.get(i));
//            }
//        }

//        // обновление отображение туров на экране
//        if (newList.isEmpty()) {
//            ArrayList<BookTour> emptyList = new ArrayList<>();
//            tours_adapter.setFilterList(emptyList);
//        } else {
//            Collections.sort(newList);
//            tours_adapter.setFilterList(newList);
//        }
    }


    Integer chipSelectedIndex=0;

    // обработка нажатия на Chips
    @Override
    public void onChipDirectionItemClick(int position, String text) {

        // изменение состояния Chips
        for(int i=0;i<5;i++){
            Chip chip1=(Chip) chip_rv.getChildAt(i);
            if(i!=position){
                chip1.setChecked(false);
            }
            else{
                chip1.setChecked(true);
            }
        }

        chipSelectedIndex=position;
        chipSelectedItem=text;

        // сортировка тура по категориям
        filterList(tours);
    }

    // проверка актуальности даты туров
    public boolean dateCheck(String ID, String dateTour) throws ParseException {
        Calendar mydate = new GregorianCalendar();
        Date thedate = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(dateTour);
        mydate.setTime(thedate);

        System.out.println("mydate -> "+mydate.getTime());

        // если тур уже начался он становится недоступным для просмотра пользователем
        // в базе данных изменяется статус его доступности
        if(thedate.before(new Date())){
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("isAvailable", false);
            // изменение доступности тура в базе данных
            dateSetFirebase("BookTour", ID, dataMap);
            return false;
        }
        else{
            return true;
        }
    }

    // изменение данных в базе данных
    public void dateSetFirebase(String tableName, String id, Map<String, Object> update){
        db.collection(tableName).document(id).update(update);
    }


    // чтение данных из базы данных
    public void readData(){
        // получение данных таблицы авторских туров
        db.collection("BookTour")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                try {
                                    // проверка актуальности даты туров
                                    if (dateCheck(document.getId(), document.get("dateStart").toString())/*dateCheck(document.getId(),document.get("dates").toString())*/) {
                                        // добавление тура в список
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
                                                document.get("dateEnd").toString()
                                        ));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            // сортировка туров по дате
                            Collections.sort(tours);

                            // вывод туров в recycler view
                            tours_adapter=new BookTourAdapter(getContext(),tours,BookTourFragment.this,userId);
                            tours_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                            tours_rv.setAdapter(tours_adapter);
                            tours_adapter.notifyDataSetChanged();

                            filterList(tours);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void addData(){
        BookTour bookTour=new BookTour();
        List<BookTour> bookTours=bookTour.load();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for(int i=0;i<bookTours.size();i++){
            Map<String, Object> tour = new HashMap<>();
            tour.put("id", UUID.randomUUID().toString());
            tour.put("name", bookTours.get(i).getName());
            tour.put("description", bookTours.get(i).getDescription());
            tour.put("price", bookTours.get(i).getPrice());
            tour.put("duration", bookTours.get(i).getDuration());
            tour.put("place", bookTours.get(i).getPlace());
            tour.put("group", bookTours.get(i).getGroup());
            tour.put("toursSold", bookTours.get(i).getToursSold());
            tour.put("isAvailable", bookTours.get(i).isAvailable());
            tour.put("organizer", bookTours.get(i).getOrganizer());
            tour.put("dateStart", bookTours.get(i).getDateStart());
            tour.put("dateEnd", bookTours.get(i).getDateEnd());


            // Add a new document with a generated ID
                db.collection("BookTour")
                    .add(tour)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }
}


//    public List<BookTour> setCheckedStateOnTours(){
//        for(int i=0;i<tours.size();i++){
//            if(selectedBookTourIdList.contains(tours.get(i))){
//                tours.get(i).setFavourite(true);
//            }
//            else{
//                tours.get(i).setFavourite(false);
//            }
//        }
//        return tours;
//    }


// добавление данных в базу данных
//    public void addDataToFirebase(){
//
//        // добавление данных из метода data класса BookTour в Firestore в таблицу BookTour
//
//        BookTour bookTour=new BookTour();
//        List<BookTour> bookTours=bookTour.data();
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        for(int i=0;i<bookTours.size();i++) {
//            Map<String, Object> tour = new HashMap<>();
//            tour.put("id", UUID.randomUUID().toString());
//            tour.put("photo", bookTours.get(i).getPhoto());
//            tour.put("name", bookTours.get(i).getName());
//            tour.put("description", bookTours.get(i).getDescription());
//            tour.put("price", bookTours.get(i).getPrice());
//            tour.put("dates", bookTours.get(i).getDates());
//            tour.put("duration", bookTours.get(i).getDuration());
//            tour.put("place", bookTours.get(i).getPlace());
//            tour.put("group", bookTours.get(i).getGroup());
//            tour.put("toursSold", bookTours.get(i).getToursSold());
//            tour.put("isAvailable", bookTours.get(i).isAvailable());
//            tour.put("organizer", bookTours.get(i).getOrganizer());
//            tour.put("direction", bookTours.get(i).getDirection());
//
//            // Add a new document with a generated ID
//            db.collection("BookTour")
//                    .add(tour)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error adding document", e);
//                        }
//                    });
//        }
//    }

/*

// добавление данных из метода data класса BookTour в Firestore в таблицу BookTour

        BookTour bookTour=new BookTour();
        List<BookTour> bookTours=bookTour.data();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for(int i=0;i<bookTours.size();i++){
            Map<String, Object> tour = new HashMap<>();
            tour.put("photo", bookTours.get(i).getPhoto());
            tour.put("name", bookTours.get(i).getName());
            tour.put("description", bookTours.get(i).getDescription());
            tour.put("price", bookTours.get(i).getPrice());
            tour.put("dates", bookTours.get(i).getDates());
            tour.put("duration", bookTours.get(i).getDuration());
            tour.put("place", bookTours.get(i).getPlace());
            tour.put("group", bookTours.get(i).getGroup());
            tour.put("toursSold", bookTours.get(i).getToursSold());
            tour.put("isAvailable", bookTours.get(i).isAvailable());
            tour.put("Organizer", bookTours.get(i).getOrganizer());

// Add a new document with a generated ID
            db.collection("BookTour")
                    .add(tour)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
 */





//   // чтение данных из базы данных
//   public void readData(){
//       db.collection("BookTour")
//           .get()
//           .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//               @Override
//               public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                   if (task.isSuccessful()) {
//                       for (QueryDocumentSnapshot document : task.getResult()) {
//                           Log.d(TAG, document.getId() + " => " + document.getData());
//
//                           try {
//                               // проверка актуальности даты туров
//                               if(dateCheck(document.getId(),document.get("dateStart").toString())/*dateCheck(document.getId(),document.get("dates").toString())*/){
//       // добавление тура в список
//        tours.add(new BookTour(
//        document.get("id").toString(),
//        document.get("photo").toString(),
//        document.get("name").toString(),
//        document.get("description").toString(),
//        Integer.parseInt(document.get("price").toString()),
//        Integer.parseInt(document.get("duration").toString()),
//        document.get("place").toString(),
//        Integer.parseInt(document.get("group").toString()),
//        Integer.parseInt(document.get("toursSold").toString()),
//        Boolean.parseBoolean(document.get("isAvailable").toString()),
//        document.get("organizer").toString(),
//        document.get("direction").toString(),
//        document.get("dateStart").toString(),
//        document.get("dateEnd").toString()
//        ));
//        Log.d(TAG, tours.get(0).getName());
//        }
//        } catch (ParseException e) {
//        e.printStackTrace();
//        }
//        }
//
//
//        ///////////////////////
//        //readFavouriteListFromFirebase();
//        //////////////////////////////
//
//
//        // сортировка туров по дате
//        Collections.sort(tours);
//
//        // вывод туров в recycler view
//        tours_adapter=new BookTourAdapter(getContext(),tours,BookTourFragment.this);
//        tours_rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        tours_rv.setAdapter(tours_adapter);
//        tours_adapter.notifyDataSetChanged();
//
//        } else {
//        Log.w(TAG, "Error getting documents.", task.getException());
//        }
//        }
//        });
//        }
//
//
//
//// чтение данных из базы данных
//public void readFavouriteListFromFirebase(){
//
//final WishList wishList=new WishList();
//
//        db.collection("WishList")
//        .get()
//        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//@Override
//public void onComplete(@NonNull Task<QuerySnapshot> task) {
//        if (task.isSuccessful()) {
//        for (QueryDocumentSnapshot document : task.getResult()) {
//        Log.d(TAG, document.getId() + " => " + document.getData());
//
//        Log.d("BookTourID",document.get("userId")+" "+userId);
//        if(document.get("userId").equals(userId)) {
//        WishList list=document.toObject(WishList.class);
//        Log.d("BookTourId",list.getUserId());
//
//        wishList.setUserId(list.getUserId());
//        wishList.setFavouriteList(list.getFavouriteList());
//
//
//        for(int i=0;i<tours.size();i++){
//        if(wishList.getFavouriteList().contains(tours.get(i).getId())){
//        tours.get(i).setFavourite(true);
//        }
//        }
//        }
//        }
//        } else {
//        Log.w(TAG, "Error getting documents.", task.getException());
//        }
//        }
//        });
//
//        }
//
//        List<String> addToFavouriteTourIdList;
//@Override
//public void setCheckedStateCheckBox(String tourId, boolean isChecked) {
//        if(isChecked){
//        addToFavouriteTourIdList.add(tourId);
//        }
//        if(!isChecked){
//        if(addToFavouriteTourIdList.contains(tourId)){
//        addToFavouriteTourIdList.remove(tourId);
//        }
//        }
//        }
//
//
//// добавление данных в базу данных
//public void addFavouriteListToFirebase(){
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> favouriteList = new HashMap<>();
//        favouriteList.put("userId", userId);
//        favouriteList.put("favouriteList", addToFavouriteTourIdList);
//
//        // Add a new document with a generated ID
//        db.collection("WishList")
//        .add(favouriteList)
//        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//@Override
//public void onSuccess(DocumentReference documentReference) {
//        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        Log.w(TAG, "Error adding document", e);
//        }
//        });
//        }
//
//@Override
//public void onStop() {
//        super.onStop();
//        //addFavouriteListToFirebase();
//
//        }
//
//
