package com.example.myapplication.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Classes.User;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1="param1";
    String login;
    LinearLayout exitLinearLayout;
    TextView nameTextView;
    TextView surnameTextView;
    TextView loginTextView;
    private FirebaseFirestore db;

    // метод получения id пользователя
    public static ProfileFragment newInstance(String userId)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,userId);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // получение id пользователя
        if(getArguments()!=null) {
            login = getArguments().getString(ARG_PARAM1);
        }
    }

    // иниализация компонентов
    public void initializationComponents(View view){
        db = FirebaseFirestore.getInstance();
        exitLinearLayout=view.findViewById(R.id.exitLinearLayout);
        nameTextView=view.findViewById(R.id.nameTextViewHome);
        surnameTextView=view.findViewById(R.id.surnameTextViewHome);
        loginTextView=view.findViewById(R.id.loginTextViewHome);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_layout,container,false);

        initializationComponents(view);

        if(!login.isEmpty()) {
            // чтение данных авторизованного пользователя
            readData(login);
        }
        else{
            Toast.makeText(getContext(), "Необходимо зарегистрироваться", Toast.LENGTH_SHORT).show();
        }
        // обработка нажатия на кнопку выхода из аккаунта
        exitLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.userLogOut(getContext());
                Intent intent=new Intent(getActivity(), SignInFragment.class);
                startActivity(intent);
            }
        });


        return view;
    }

    // чтение данных пользователя
    private void readData(String login){
        db.collection("Users")
                .document(login)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            try{
                                DocumentSnapshot document = task.getResult();
                                nameTextView.setText(document.get("firstname").toString());
                                surnameTextView.setText(document.get("lastname").toString());
                                loginTextView.setText(document.get("email").toString());
                            }
                            catch (Exception e){

                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
