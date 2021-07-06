package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class profileViewForOthers extends AppCompatActivity {

    public TextView profileName, profileInstitution, inviteButton;
    public FirebaseFirestore db;
    public String mealStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_for_others);

        db = FirebaseFirestore.getInstance();

        profileName = (TextView) findViewById(R.id.textView26);
        profileInstitution = (TextView) findViewById(R.id.textView27);
        inviteButton = (Button) findViewById(R.id.button8);

    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("users").document(getIntent().getStringExtra("userId"))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        mealStatus = value.getString("Meal name");
                        if(mealStatus != null) inviteButton.setVisibility(View.INVISIBLE);
                        else inviteButton.setVisibility(View.VISIBLE);
                        Toast.makeText(profileViewForOthers.this,mealStatus,Toast.LENGTH_SHORT).show();
                    }
                });
        retrieveUserData();
    }

    private void retrieveUserData() {
        db.collection("users").document(getIntent().getStringExtra("userId")).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot d = task.getResult();
                        profileName.setText(d.getString("Name"));
                        profileInstitution.setText(d.getString("Institution"));
                        Toast.makeText(profileViewForOthers.this,"successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profileViewForOthers.this,"Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}