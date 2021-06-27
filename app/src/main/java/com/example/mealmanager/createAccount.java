package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class createAccount extends AppCompatActivity {

    public EditText ETname, ETemail, ETpass;
    public Button create;
    private FirebaseAuth mAuth;
    public FirebaseUser user;
    public StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //initialization
        mAuth = FirebaseAuth.getInstance();
        //storageRef = storage.getReference();

        ETname = (EditText)findViewById(R.id.editName);
        ETemail = (EditText)findViewById(R.id.editEmail);
        ETpass = (EditText)findViewById(R.id.editPass);
        create = (Button)findViewById(R.id.button3);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserAccount();
            }
        });


    }

    public void createUserAccount(){
        String name = ETname.getText().toString().trim();
        String email = ETemail.getText().toString().trim();
        String password = ETpass.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user = mAuth.getCurrentUser();
                            Toast.makeText(createAccount.this, "Authintication Successfull", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(createAccount.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(createAccount.this, "Authintication failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }
}