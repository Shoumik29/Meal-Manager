package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class todayMeal extends AppCompatActivity {

    public Calendar calendar;
    public TextView dateText, numberOfBorders;
    public Button selectBorder;
    public FirebaseFirestore docs;
    public String date;
    public ArrayList<String> bordersForMeal;
    public Button confirmMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_meal);

        calendar = Calendar.getInstance();
        docs = FirebaseFirestore.getInstance();
        bordersForMeal = new ArrayList<>();

        numberOfBorders = (TextView)findViewById(R.id.textView33);
        dateText = (TextView)findViewById(R.id.textView36);
        selectBorder = (Button) findViewById(R.id.button18);
        confirmMeal = (Button) findViewById(R.id.button19);

        numberOfBorders.setText("Number of Borders : ");

        selectBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(todayMeal.this, borderListForMeal.class);
                i.putExtra("mealName", getIntent().getStringExtra("mealName"));
                startActivityForResult(i,1);
            }
        });

        confirmMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmMeal();
            }
        });

    }

    private void confirmMeal() {

        Map<String, Object> bordersMeal = new HashMap<>();
        bordersMeal.put("Borders", bordersForMeal);
        bordersMeal.put("Date", date);

        docs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Meal History").document(String.format("Meal %s", date))
                .set(bordersMeal)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(todayMeal.this, "Meal Created", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(todayMeal.this, "Action Failed", Toast.LENGTH_SHORT).show();
                    }
                });
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                bordersForMeal = data.getStringArrayListExtra("Borders List");
                numberOfBorders.setText("Number of Borders : "+String.valueOf(bordersForMeal.size()));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
        dateText.setText(DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime()));
        dateText.setTextColor(Color.GREEN);

    }
}