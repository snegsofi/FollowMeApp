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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends AppCompatActivity {
    // окно авторизации

    Button signUp_btn;
    Button signIn_btn;
    ImageButton cancelSignIn_btn;
    TextInputLayout loginLayout;
    TextInputLayout passwordLayout;
    EditText passwordTxt;
    EditText loginTxt;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    // инициализация компонентов
    public void initializingComponents(){
        mAuth = FirebaseAuth.getInstance();
        signUp_btn=findViewById(R.id.signupFromSignIn_btn);
        signIn_btn=findViewById(R.id.signin_btn);
        cancelSignIn_btn=findViewById(R.id.cancelSignin_btn);
        loginTxt=findViewById(R.id.email_signin_EditText);
        loginLayout=findViewById(R.id.email_signin_textField);
        passwordTxt=findViewById(R.id.password_signin_EditText);
        passwordLayout=findViewById(R.id.password_signin_textField);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        initializingComponents();

        // переход на окно авторизации
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInFragment.this, SignUpFragment.class);
                startActivity(intent);
            }
        });

        // обработка нажатия на кнопку авторизации
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // проверка на заполненность полей
                if(loginTxt.getText().toString().isEmpty()){
                    showError(loginLayout,"Поле обязательно для заполнения");
                }
                else if(passwordTxt.getText().toString().isEmpty()){
                    showError(passwordLayout,"Поле обязательно для заполнения");
                }
                else {
                    // авторизация
                    signIn(loginTxt.getText().toString(),passwordTxt.getText().toString());
                }
            }
        });

        // переход в приложение без авторизации
        cancelSignIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewActivity("");
            }
        });

        // обработка данных в текстовом поле
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

        // обработка данных в текстовом поле
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
    }

    // отображение ошибки при неверном заполнении полей
    private void showError(TextInputLayout textInputLayout, String string) {
        textInputLayout.setError(string);
    }

    // скрытие ошибки
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

    private void reload() { }

    // метод авторизации пользователя по адресу электронной почту и паролю
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            // получение id выбранного пользователя и запись его в настройки
                            // для пропуска авторизации при следующем использовании приложения
                            FirebaseUser user = mAuth.getCurrentUser();
                            User.setUserId(getApplicationContext(),user.getUid());
                            toNewActivity(user.getUid());
                             } else {
                            // отображение сообщения при ошибки авторизации
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInFragment.this, "Ошибка аутентификации",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    // метод перехода на новую активность
    private void toNewActivity(String userId){
        Intent intent=new Intent(SignInFragment.this, MainActivity.class);
        intent.putExtra("userID", userId);
        Log.d("userIdSignInActivity",userId);
        startActivity(intent);
    }

}
