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
    public TextView dateText, numberOfBorders, breakFast, lunch, dinner;
    public Button selectBorder;
    public FirebaseFirestore docs;
    public String date;
    public Map<String, String> bordersForMeal;
    public Button confirmMeal;
    public Switch switchB, switchL,switchD;
    public ArrayList<Integer> mealSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_meal);

        calendar = Calendar.getInstance();
        docs = FirebaseFirestore.getInstance();
        bordersForMeal = new HashMap<>();
        mealSelect = new ArrayList<Integer>(Arrays.asList(0,0,0));

        numberOfBorders = (TextView)findViewById(R.id.textView33);
        dateText = (TextView)findViewById(R.id.textView36);
        breakFast = (TextView)findViewById(R.id.editTextTextPersonName);
        lunch = (TextView)findViewById(R.id.editTextTextPersonName4);
        dinner = (TextView)findViewById(R.id.editTextTextPersonName2);
        selectBorder = (Button) findViewById(R.id.button18);
        confirmMeal = (Button) findViewById(R.id.button19);
        switchB = (Switch) findViewById(R.id.switch4);
        switchL = (Switch) findViewById(R.id.switch5);
        switchD = (Switch) findViewById(R.id.switch6);

        dateText.setText("Today's Date : ");
        numberOfBorders.setText("Number of Borders : ");

        dateText.append(DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime()));

        breakFast.setEnabled(false);
        lunch.setEnabled(false);
        dinner.setEnabled(false);

        switchB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {breakFast.setEnabled(true); mealSelect.add(0,1);}
                else {breakFast.setEnabled(false); mealSelect.add(0,0);}
            }
        });

        switchL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {lunch.setEnabled(true); mealSelect.add(1,1);}
                else {lunch.setEnabled(false); mealSelect.add(1,0);}
            }
        });

        switchD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {dinner.setEnabled(true); mealSelect.add(2,1);}
                else {dinner.setEnabled(false); mealSelect.add(2,0);}
            }
        });

        selectBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!switchB.isChecked() && !switchL.isChecked() && !switchD.isChecked()){
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
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
    }
}