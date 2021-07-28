package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class log_in_activity extends AppCompatActivity {


    public Button signUpB, logIn;
    private FirebaseAuth mAuth;
    public EditText ETemail, ETpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_activity);

        signUpB = (Button)findViewById(R.id.button2);
        logIn = (Button)findViewById(R.id.button);
        ETemail = (EditText)findViewById(R.id.eTEmail);
        ETpassword = (EditText)findViewById(R.id.eTPass);

        mAuth = FirebaseAuth.getInstance();

        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpB();
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ETemail.getText().toString().trim()) || TextUtils.isEmpty(ETpassword.getText().toString().trim())){
                    if(TextUtils.isEmpty(ETemail.getText().toString().trim())){
                        ETemail.setError("Enter Your Email");
                    }
                    else if(TextUtils.isEmpty(ETpassword.getText().toString().trim())){
                        ETpassword.setError("Enter Your Password");
                    }
                }
                else logIn();
            }
        });

    }

    public void signUpB(){
        Intent i = new Intent(this, createAccount.class);
        startActivity(i);
    }
    public void logIn(){
        String email = ETemail.getText().toString().trim();
        String password = ETpassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(log_in_activity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(log_in_activity.this, "Log in Successfull", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(log_in_activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}