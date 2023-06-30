package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Classes.User;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends AppCompatActivity {

    ImageButton toLogin_btn;
    Button signUp_btn;
    TextInputLayout firstnameLayout;
    TextInputLayout lastnameLayout;
    TextInputLayout loginLayout;
    TextInputLayout passwordLayout;
    EditText passwordTxt;
    EditText loginTxt;
    EditText firstnameTxt;
    EditText lastnameTxt;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // инициализация компонентов
    public void initializationComponents(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        toLogin_btn=findViewById(R.id.backFromSignup_btn);
        signUp_btn=findViewById(R.id.signup_btn);
        loginTxt=findViewById(R.id.email_signup_EditText);
        loginLayout=findViewById(R.id.email_signup_textField);
        passwordTxt=findViewById(R.id.password_signup_EditText);
        passwordLayout=findViewById(R.id.password_signup_textField);
        firstnameTxt=findViewById(R.id.firstname_signup_EditText);
        firstnameLayout=findViewById(R.id.firstname_signup_textField);
        lastnameTxt=findViewById(R.id.lastname_signup_EditText);
        lastnameLayout=findViewById(R.id.lastname_signup_textField);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        initializationComponents();

        // кнопка возврата на форму авторизации
        toLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpFragment.this, SignInFragment.class);
                startActivity(intent);
            }
        });

        // кнопка регистрация пользователя
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // проверка заполненности полей
                if(loginTxt.getText().toString().isEmpty()){
                    showError(loginLayout,"Поле обязательно для заполнения");
                }
                else if(passwordTxt.getText().toString().isEmpty()){
                    showError(passwordLayout,"Поле обязательно для заполнения");
                }
                else{
                    // добавление нового пользователя
                    createAccount(firstnameTxt.getText().toString(),
                            lastnameTxt.getText().toString(),
                            loginTxt.getText().toString(),
                            passwordTxt.getText().toString());
                }
            }
        });


        loginTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!loginTxt.getText().toString().isEmpty()){
                    hideError(loginLayout);
                }
            }
        });

        passwordTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!passwordTxt.getText().toString().isEmpty()){
                    hideError(passwordLayout);
                }
            }
        });

        firstnameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!firstnameTxt.getText().toString().isEmpty()){
                    hideError(firstnameLayout);
                }
            }
        });

        lastnameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!lastnameTxt.getText().toString().isEmpty()){
                    hideError(lastnameLayout);
                }
            }
        });
    }

    private void showError(TextInputLayout textInputLayout, String string) {
        textInputLayout.setError(string);
    }

    private void hideError(TextInputLayout textInputLayout) {
        textInputLayout.setError(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    // добавление новго пользователя с помощью службы аутентификации firebase
    private void createAccount(String firstname, String lastname,String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            // получение id нового пользователя
                            FirebaseUser user = mAuth.getCurrentUser();
                            // добавление id нового пользователя в настройки
                            // для пропуска авторизации при дальнейшем использовании приложения
                            User.setUserId(getApplicationContext(),user.getUid());
                            // добавление нового пользователя
                            addUser(user.getUid(),firstname,lastname,email);
                            // переход на главную страницу
                            toNewActivity(email);
                        } else {
                            // сообщение при ошибки регистрации
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpFragment.this, "Ошибка аутентификации",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // переход на другую активность
    private void toNewActivity(String login){
        Intent intent=new Intent(SignUpFragment.this, MainActivity.class);
        intent.putExtra("login", login);
        Log.d("loginSignInActivity",login);
        startActivity(intent);
    }

    private void reload() { }

    // добавление нового пользователя в базу данных Cloud Firestore
    private void addUser(String uid, String firstname, String lastname,String email){
        // Создание пользователя со следующими данными: id, имя, фамилия, email
        Map<String, Object> user = new HashMap<>();
        user.put("id", uid);
        user.put("firstname", firstname);
        user.put("lastname",lastname);
        user.put("email", email);

        Log.d(TAG,"id="+uid);
//        db.collection("Users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });

        // добавление нового документа в таблицу пользователей приложения
        db.collection("Users").document(uid)
                .set(user)
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
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "DocumentSnapshot complete listener");
                    }
                });
    }
}
