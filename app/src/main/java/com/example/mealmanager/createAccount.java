package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class createAccount extends AppCompatActivity {

    public EditText ETname, ETemail, ETpass, ETinstitution, ETmobileNumber;
    public Button create;
    private FirebaseAuth mAuth;
    public FirebaseUser user;
    public FirebaseFirestore db;
    public StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //initialization
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //storageRef = storage.getReference();

        ETname = (EditText)findViewById(R.id.editName);
        ETemail = (EditText)findViewById(R.id.editEmail);
        ETpass = (EditText)findViewById(R.id.editPass);
        ETinstitution = (EditText)findViewById(R.id.ETin);
        ETmobileNumber = (EditText)findViewById(R.id.editTextPhone);
        create = (Button)findViewById(R.id.button3);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserAccount();
            }
        });


    }

    public void createUserAccount(){

        String email = ETemail.getText().toString().trim();
        String password = ETpass.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            storingUsersData(userID);

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
    public void storingUsersData(String CurrentUserID){

        String institution = ETinstitution.getText().toString().trim();
        String mobileNumber = ETmobileNumber.getText().toString().trim();
        String name = ETname.getText().toString().trim();
        String doc = ETemail.getText().toString().trim();

        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("Mobile Number", mobileNumber);
        user.put("Institution", institution);
        user.put("Meal name", null);

        // Add a new document with a generated ID
        db.collection("users").document(CurrentUserID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(createAccount.this, "Data entry successful", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(createAccount.this, "data entry failed", Toast.LENGTH_LONG).show();
                    }
                });

    }
}