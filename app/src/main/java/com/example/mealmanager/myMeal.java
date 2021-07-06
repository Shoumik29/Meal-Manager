package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class myMeal extends AppCompatActivity {

    public Button leaveMeal, deleteMeal;
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meal);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        leaveMeal = (Button)findViewById(R.id.button9);
        deleteMeal = (Button)findViewById(R.id.button10);

        leaveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveMeal();
            }
        });
        deleteMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMeal();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("meals").document(getIntent().getStringExtra("meal_name")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    if(!value.getString("manager ID").equals(mAuth.getCurrentUser().getUid())) deleteMeal.setVisibility(View.INVISIBLE);
                    else deleteMeal.setVisibility(View.VISIBLE);
                }
                else{
                    Intent i = new Intent(myMeal.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void deleteMeal() {
        db.collection("meals").document(getIntent().getStringExtra("meal_name")).collection("Borders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot d: task.getResult()){
                            db.collection("meals").document(getIntent().getStringExtra("meal_name")).collection("Borders").document(d.getId()).delete();
                            db.collection("meals").document(getIntent().getStringExtra("meal_name")).delete();
                        }

                        db.collection("users").document(mAuth.getCurrentUser().getUid()).update("Meal name", null)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(myMeal.this, "Meal deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(myMeal.this, "Action failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(myMeal.this, "Delete action failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void leaveMeal() {
        String userID = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userID).update("Meal name", null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(myMeal.this,"Successful",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(myMeal.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
