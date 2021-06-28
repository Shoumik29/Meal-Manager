package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile extends AppCompatActivity {


    public TextView userName, userInstitution;
    private FirebaseAuth mAuth;
    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = (TextView) findViewById(R.id.textView5);
        userInstitution = (TextView)findViewById(R.id.textView10);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveUserData();
    }

    public void retrieveUserData(){

        String userID = mAuth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userName.setText(document.getString("Name"));
                        userInstitution.setText(document.getString("Institution"));
                        Toast.makeText(profile.this, "Data retrieve Successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(profile.this, "User not found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(profile.this, "Data retrieve failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}