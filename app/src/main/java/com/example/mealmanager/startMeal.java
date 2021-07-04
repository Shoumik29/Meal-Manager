package com.example.mealmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startMeal extends AppCompatActivity {

    public Button createMeal, searchMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_meal);

        createMeal = (Button) findViewById(R.id.button5);
        searchMeal = (Button) findViewById(R.id.button6);

        createMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(startMeal.this, createMeal.class);
                startActivity(i);
            }
        });

        searchMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(startMeal.this, searchMeal.class);
                startActivity(i1);
            }
        });

    }
}