package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1="param1";

    public static ProfileFragment newInstance(String login)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,login);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    String login;
    LinearLayout exitLinearLayout;

    TextView nameTextView;
    TextView surnameTextView;
    TextView loginTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {
            login = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_layout,container,false);

        db = FirebaseFirestore.getInstance();

        exitLinearLayout=view.findViewById(R.id.exitLinearLayout);

        nameTextView=view.findViewById(R.id.nameTextViewHome);
        surnameTextView=view.findViewById(R.id.surnameTextViewHome);
        loginTextView=view.findViewById(R.id.loginTextViewHome);

        readData(login);

        exitLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SignInFragment.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private FirebaseFirestore db;

    private void readData(String login){

        db.collection("Users")
                .whereEqualTo("email",login)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                nameTextView.setText(document.get("firstname").toString());
                                surnameTextView.setText(document.get("lastname").toString());
                                loginTextView.setText(document.get("email").toString());

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });
    }
}
