package com.example.myapplication;

import android.content.Intent;
import android.media.Image;
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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends AppCompatActivity {

    Button signUp_btn;
    Button signIn_btn;
    ImageButton cancelSignIn_btn;


    TextInputLayout loginLayout;
    TextInputLayout passwordLayout;
    EditText passwordTxt;
    EditText loginTxt;



    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        mAuth = FirebaseAuth.getInstance();
        signUp_btn=findViewById(R.id.signupFromSignIn_btn);
        signIn_btn=findViewById(R.id.signin_btn);
        cancelSignIn_btn=findViewById(R.id.cancelSignin_btn);
        loginTxt=findViewById(R.id.email_signin_EditText);
        loginLayout=findViewById(R.id.email_signin_textField);
        passwordTxt=findViewById(R.id.password_signin_EditText);
        passwordLayout=findViewById(R.id.password_signin_textField);


        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInFragment.this, SignUpFragment.class);
                startActivity(intent);
            }
        });

        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginTxt.getText().toString().isEmpty()){
                    showError(loginLayout,"Поле обязательно для заполнения");
                }
                else if(passwordTxt.getText().toString().isEmpty()){
                    showError(passwordLayout,"Поле обязательно для заполнения");
                }
                else {
                    signIn(loginTxt.getText().toString(),passwordTxt.getText().toString());
                    //Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    //startActivity(intent);
                }
            }
        });

        cancelSignIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SignInFragment.this, MainActivity.class);
                //startActivity(intent);
                toNewActivity("");
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

    private void reload() { }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            toNewActivity(loginTxt.getText().toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInFragment.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void toNewActivity(String login){
        Intent intent=new Intent(SignInFragment.this,MainActivity.class);
        intent.putExtra("login", login);
        Log.d("waiterSignInActivity",login);
        startActivity(intent);
    }

}
