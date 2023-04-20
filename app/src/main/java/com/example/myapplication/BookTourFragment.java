package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BookTourFragment extends Fragment implements BookTourAdapter.ItemClickListener,
    ChipDirectionAdapter.ItemClickListener{

    public static BookTourFragment newInstance()
    {
        return new BookTourFragment();
    }

    private static final String TAG = "tours";

    SearchView searchView;
    List<BookTour> tours;
    FirebaseFirestore db;
    RecyclerView tours_rv;
    BookTourAdapter tours_adapter;
    ChipGroup chipGroup;
    ChipDirectionAdapter chipAdapter;
    RecyclerView chip_rv;

    // инициализация компонентов
    public void initialization(View view){
        tours=new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        tours_rv=view.findViewById(R.id.rv_tour);
        searchView=view.findViewById(R.id.bookTour_SearchView);
        chip_rv=view.findViewById(R.id.chip_rv_tour);
        chipGroup=view.findViewById(R.id.chipGroup);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.book_tour_layout,container,false);

        initialization(view);

        // добавление данных в базу данных
        //addDataToFirebase();

        searchView.clearFocus();

        // чтение данных из таблицы BoorTour в Firebase
        readData();

        // обработка поиска текста на экране
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // вызывается при отправке запроса пользователем
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            // вызывается при изменение запроса пользователем
            @Override
            public boolean onQueryTextChange(String newText) {
                // метод для вывода данных в recycler view в соответствии с запросом пользователя
                filterList(newText,"search");
                return false;
            }
        });

        // программное добавление компонентов Chips
        List<ChipDirectionModel> chipDirectionList=new ArrayList<>();

        chipDirectionList.add(new ChipDirectionModel("Все", true));
        chipDirectionList.add(new ChipDirectionModel("Горы", false));
        chipDirectionList.add(new ChipDirectionModel("Озера", false));

        chipAdapter=new ChipDirectionAdapter(getContext(),chipDirectionList,BookTourFragment.this);
        chip_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        chip_rv.setAdapter(chipAdapter);
        chipAdapter.notifyDataSetChanged();

        return view;
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
                            Log.d(TAG, document.getId() + " => " + document.getData());

                            try {
                                // проверка актуальности даты туров
                                if(dateCheck(document.getId(),document.get("dates").toString())){
                                    // добавление тура в список
                                    tours.add(new BookTour(
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
                                    ));

                                    Log.d(TAG, tours.get(0).getName());
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        // сортировка туров по дате
                        Collections.sort(tours);

                        // вывод туров в recycler view
                        tours_adapter=new BookTourAdapter(getContext(),tours,BookTourFragment.this);
                        tours_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                        tours_rv.setAdapter(tours_adapter);
                        tours_adapter.notifyDataSetChanged();

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    // обработка нажатия на конкретный тур
    @Override
    public void onItemClick(BookTour tour, int position) {
        // переход на другой фрагмент (TourFragment)
        loadFragment(TourFragment.newInstance(tour.getId()));
    }

    // загрузка другого фрагмента
    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    // фильтрация данных
    public void filterList(String text, String searchType) {

        ArrayList<BookTour> newList = new ArrayList<>();

        // выбор данных, которые будут добавлены в recycler view
        for (int i = 0; i < tours.size(); i++) {
            if(searchType.contains("search")){
                if(tours.get(i).getName().toLowerCase().contains(text.toLowerCase())
                || tours.get(i).getPlace().toLowerCase().contains(text.toLowerCase())) {
                    newList.add(tours.get(i));
                }
            }
            if(searchType.contains("filter"))
            {
                if(tours.get(i).getDirection().toLowerCase().contains(text.toLowerCase())) {
                    newList.add(tours.get(i));
                }
            }
            if(text.toLowerCase().contains("все")){
                newList.add(tours.get(i));
            }
        }
        if (newList.isEmpty()) {
            ArrayList<BookTour> emptyList = new ArrayList<>();

            tours_adapter.setFilterList(emptyList);
        } else {
            tours_adapter.setFilterList(newList);
        }
    }


    // обработка нажатия на Chips
    @Override
    public void onChipDirectionItemClick(int position, String text) {

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

        // выбор подходящих запросу данных
        filterList(text, "filter");
    }

    // проверка актуальности даты туров
    public boolean dateCheck(String ID, String dateTour) throws ParseException {
        String[] parts1=dateTour.split(("-"));
        String date=parts1[0];

        Log.i("date", date);

        Calendar mydate = new GregorianCalendar();
        Date thedate = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(date);
        mydate.setTime(thedate);

        System.out.println("mydate -> "+mydate.getTime());

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

    // добавление данных в базу данных
    public void addDataToFirebase(){

        // добавление данных из метода data класса BookTour в Firestore в таблицу BookTour

        BookTour bookTour=new BookTour();
        List<BookTour> bookTours=bookTour.data();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for(int i=0;i<bookTours.size();i++){
            Map<String, Object> tour = new HashMap<>();
            tour.put("id", UUID.randomUUID().toString());
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
            tour.put("organizer", bookTours.get(i).getOrganizer());
            tour.put("direction", bookTours.get(i).getDirection());

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