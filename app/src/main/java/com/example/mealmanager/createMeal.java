package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class createMeal extends AppCompatActivity {

    public EditText mealName, numOfMeals, mealFinishDate;
    public Button createMeal;
    public FirebaseFirestore docs;
    public FirebaseAuth mAuth;
    public DocumentReference refU, refM;
    public FirebaseUser user;
    public String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);

        docs = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mealName = (EditText) findViewById(R.id.eTMealName);
        numOfMeals = (EditText) findViewById(R.id.eTNumOfMeals);
        mealFinishDate = (EditText) findViewById(R.id.eTMealFinishDate);
        createMeal = (Button) findViewById(R.id.button7);

        refU = docs.collection("users").document(user.getUid());

        createMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storingMealData();
                Intent i = new Intent(createMeal.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refU.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName = value.getString("Name");
            }
        });
    }

    private void storingMealData() {

        String meal_name = mealName.getText().toString().trim();
        String num_of_meals = numOfMeals.getText().toString().trim();
        String meal_finish_date = mealFinishDate.getText().toString().trim();

        refM = docs.collection("meals").document(meal_name);

        String userId = user.getUid();

        Map<String, Object> meal = new HashMap<>();
        meal.put("meal name", meal_name);
        meal.put("number of meals", num_of_meals);
        meal.put("meal finish date", meal_finish_date);
        meal.put("manager ID", userId);
        meal.put("manager name", userName);

        Map<String, Object> UM = getUserData();

        refM.set(meal)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        refU.update("Meal name", meal_name)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        refM.collection("Borders").document(userId).set(UM)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(createMeal.this,"Meal & user data entry done", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(createMeal.this,"Meal data entry failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(createMeal.this,"Meal data entry failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public Map<String, Object> getUserData(){

        Map<String, Object> userM = new HashMap<>();

        refU.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot d = task.getResult();
                            userM.put("Border name", d.getString("Name"));
                            userM.put("Border institution", d.getString("Institution"));
                        }
                    }
                });

        return userM;

    }
}