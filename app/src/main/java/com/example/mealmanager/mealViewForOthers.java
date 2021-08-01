package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class mealViewForOthers extends AppCompatActivity {

    public TextView mealName, managerName;
    public Button joinButton;
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;
    public String userName, userInstitution, userMobile;
    public Boolean reqState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view_for_others);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mealName = (TextView)findViewById(R.id.textView30);
        managerName = (TextView)findViewById(R.id.textView31);
        joinButton = (Button)findViewById(R.id.button12);

    }

    private void cancelRequest() {

        joinButton.setEnabled(false);

        db.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Meal Request").document(mAuth.getCurrentUser().getUid())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        joinButton.setEnabled(true);
                        joinButton.setBackgroundColor(Color.BLUE);
                        joinButton.setText("Join Meal");
                        reqState = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mealViewForOthers.this, "Delete action failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mealRequest() {

        joinButton.setEnabled(false);

        Map<String, Object> req = new HashMap<>();
        req.put("user name", userName);
        req.put("user institution", userInstitution);
        req.put("user mobile", userMobile);

        db.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Meal Request").document(mAuth.getCurrentUser().getUid())
                .set(req)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        joinButton.setEnabled(true);
                        joinButton.setBackgroundColor(Color.GRAY);
                        joinButton.setText("Cancel Request");
                        reqState = false;
                        Toast.makeText(mealViewForOthers.this,"Request done", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mealViewForOthers.this,"Request failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Meal Request").document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value.exists()) {
                            joinButton.setBackgroundColor(Color.GRAY);
                            joinButton.setText("Cancel Request");

                            joinButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cancelRequest();
                                }
                            });
                        }
                        else {
                            joinButton.setBackgroundColor(Color.BLUE);
                            joinButton.setText("Join Meal");

                            joinButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mealRequest();
                                }
                            });
                        }
                    }
                });

        db.collection("users").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName = value.getString("Name");
                userInstitution = value.getString("Institution");
                userMobile = value.getString("Mobile Number");
            }
        });


        retrieveMealData();
    }

    private void retrieveMealData() {
        db.collection("meals").document(getIntent().getStringExtra("mealName")).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot d = task.getResult();
                        mealName.setText(d.getString("meal name"));
                        managerName.setText(d.getString("manager name"));
                        Toast.makeText(mealViewForOthers.this, "Action successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mealViewForOthers.this, "Action failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}