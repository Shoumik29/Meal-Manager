package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class todayMeal extends AppCompatActivity {

    public Calendar calendar;
    public TextView dateText;
    public TextView numberOfBorders, numHalfMeal, numFullMeal;
    public Button selectBorder;
    public FirebaseFirestore docs;
    public String date;
    public Map<String, String> bordersForMeal;
    public Button confirmMeal;
    public RadioButton halfMeal, fullMeal;
    public ArrayList<Integer> mealSelect;
    public int HalfMeal, FullMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_meal);

        calendar = Calendar.getInstance();
        docs = FirebaseFirestore.getInstance();
        bordersForMeal = new HashMap<>();
        mealSelect = new ArrayList<Integer>(Arrays.asList(0,0));

        numberOfBorders = (TextView)findViewById(R.id.textView33);
        dateText = (TextView)findViewById(R.id.textView36);
        selectBorder = (Button) findViewById(R.id.button18);
        confirmMeal = (Button) findViewById(R.id.button19);
        halfMeal = (RadioButton)findViewById(R.id.radioButton2);
        fullMeal = (RadioButton)findViewById(R.id.radioButton);
        numHalfMeal = (TextView)findViewById(R.id.textView38);
        numFullMeal = (TextView)findViewById(R.id.textView57);


        dateText.setText("Today's Date : ");
        numberOfBorders.setText("Number of Borders : ");

        dateText.append(DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime()));

        halfMeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { mealSelect.set(0,1); fullMeal.setChecked(false);}
                else {mealSelect.set(0,0);}
            }
        });

        fullMeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {mealSelect.set(1,1); halfMeal.setChecked(false);}
                else {mealSelect.set(1,0);}
            }
        });


        selectBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!halfMeal.isChecked() && !fullMeal.isChecked()){
                    Toast.makeText(todayMeal.this, "Please Select At least One Meal", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(todayMeal.this, borderListForMeal.class);
                    i.putExtra("mealName", getIntent().getStringExtra("mealName"));
                    i.putIntegerArrayListExtra("mealSelect", mealSelect);
                    startActivityForResult(i,1);
                }
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
                if(data.hasExtra("Borders List")){
                    Bundle wrapper = data.getBundleExtra("Borders List");
                    bordersForMeal = (Map<String, String>) wrapper.getSerializable("Borders List");
                    numberOfBorders.setText("Number of Borders : "+String.valueOf(bordersForMeal.size()));
                }
                HalfMeal = data.getIntExtra("halfMeal",0);
                FullMeal = data.getIntExtra("fullMeal",0);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        numFullMeal.setText("Number of Full Meal : "+String.valueOf(FullMeal));
        numHalfMeal.setText("Number of Half Meal : "+String.valueOf(HalfMeal));
    }
}