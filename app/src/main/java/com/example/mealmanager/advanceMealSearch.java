package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class advanceMealSearch extends AppCompatActivity {

    public TextView TmealName;
    public TextView managerName;
    public Button joinButton;
    public FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public String userName, userInstitution, userMobile;
    public Boolean reqState;
    public String managerID, mealName;
    public EditText searchBar;
    public ImageView search;
    public ConstraintLayout mealView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_meal_search);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        TmealName = (TextView)findViewById(R.id.textView70);
        managerName = (TextView)findViewById(R.id.textView71);
        joinButton = (Button)findViewById(R.id.button15A);
        searchBar = (EditText)findViewById(R.id.searchMTA);
        search = (ImageView)findViewById(R.id.imageView15A);
        mealView = (ConstraintLayout)findViewById(R.id.constraintLayout9);

        search.setClickable(true);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });



    }

    private void cancelRequest(String mealName) {

        joinButton.setEnabled(false);

        db.collection("meals").document(mealName).collection("Meal Request").document(mAuth.getCurrentUser().getUid())
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
                        Toast.makeText(advanceMealSearch.this, "Delete action failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mealRequest(String mealName) {

        joinButton.setEnabled(false);

        Map<String, Object> req = new HashMap<>();
        req.put("user name", userName);
        req.put("user institution", userInstitution);
        req.put("user mobile", userMobile);

        db.collection("meals").document(mealName).collection("Meal Request").document(mAuth.getCurrentUser().getUid())
                .set(req)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        joinButton.setEnabled(true);
                        joinButton.setBackgroundColor(Color.GRAY);
                        joinButton.setText("Cancel Request");
                        reqState = false;
                        Toast.makeText(advanceMealSearch.this,"Request done", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(advanceMealSearch.this,"Request failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void search() {

        managerID = searchBar.getText().toString().trim();

        db.collection("users").document(managerID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            mealName = documentSnapshot.getString("Meal name");
                            retrieveMealData(mealName);

                            db.collection("meals").document(mealName).collection("Borders").document(currentUser.getUid()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(!documentSnapshot.exists()){

                                                joinButton.setVisibility(View.VISIBLE);

                                                db.collection("meals").document(mealName).collection("Meal Request").document(mAuth.getCurrentUser().getUid())
                                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                if(value.exists()) {
                                                                    joinButton.setBackgroundColor(Color.GRAY);
                                                                    joinButton.setText("Cancel Request");

                                                                    joinButton.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            cancelRequest(mealName);
                                                                        }
                                                                    });
                                                                }
                                                                else {
                                                                    joinButton.setBackgroundColor(Color.BLUE);
                                                                    joinButton.setText("Join Meal");

                                                                    joinButton.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            mealRequest(mealName);
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
                                            }
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(advanceMealSearch.this,"Search failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void retrieveMealData(String mealName) {

        db.collection("meals").document(mealName).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        mealView.setVisibility(View.VISIBLE);

                        DocumentSnapshot d = task.getResult();
                        TmealName.setText(d.getString("meal name"));
                        managerName.setText(d.getString("manager name"));
                        Toast.makeText(advanceMealSearch.this, "Action successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(advanceMealSearch.this, "Action failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mealView.setVisibility(View.INVISIBLE);
        joinButton.setVisibility(View.INVISIBLE);

    }
}