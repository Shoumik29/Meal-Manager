package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.zip.Inflater;

public class todayMeal extends AppCompatActivity {

    public Calendar calendar;
    public EditText ETmealRate;
    public TextView dateText, numberOfBorders, numHalfMeal, numFullMeal;
    public Button selectBorder, confirmMeal, checkTotal;
    public FirebaseFirestore docs;
    public String date;
    public Map<String, String> bordersForMeal;
    public RadioButton halfMeal, fullMeal;
    public ArrayList<Integer> mealSelect;
    public int HalfMeal, FullMeal;
    public boolean mealToday;
    public double totalCost;

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
        selectBorder = (Button)findViewById(R.id.button18);
        confirmMeal = (Button)findViewById(R.id.button19);
        checkTotal = (Button)findViewById(R.id.button9);
        halfMeal = (RadioButton)findViewById(R.id.radioButton2);
        fullMeal = (RadioButton)findViewById(R.id.radioButton);
        numHalfMeal = (TextView)findViewById(R.id.textView38);
        numFullMeal = (TextView)findViewById(R.id.textView57);
        ETmealRate = (EditText)findViewById(R.id.editTextNumber);


        dateText.setText("Today's Date : ");
        numberOfBorders.setText("Number of Borders : 0");

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

        checkTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ETmealRate.getText().toString().trim())) ETmealRate.setError("Enter Meal Rate");
                else popDialog();
            }
        });

        confirmMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ETmealRate.getText().toString().trim())) ETmealRate.setError("Enter Meal Rate");
                else confirmMeal();
            }
        });

    }

    private void popDialog() {
        if(bordersForMeal.size() == 0) Toast.makeText(todayMeal.this, "Select Borders First", Toast.LENGTH_SHORT).show();
        else{
            String mealRate = ETmealRate.getText().toString().trim();

            double mealRateINT = HalfMeal*((Integer.parseInt(mealRate))/2) + (FullMeal*Integer.parseInt(mealRate));

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_layout);
            TextView dialogMealRate = dialog.findViewById(R.id.textView67);
            dialogMealRate.setText(String.format("Total Meal Cost: %s",String.valueOf(mealRateINT)));
            dialog.show();
        }
    }

    private void confirmMeal() {
        if(mealToday){
            Toast.makeText(todayMeal.this,"You have Already Created a Meal Today", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String mealRate = ETmealRate.getText().toString().trim();
            double mealRateDb = HalfMeal*((Double.parseDouble(mealRate))/2) + (FullMeal*Double.parseDouble(mealRate));

            Map<String, Object> bordersMeal = new HashMap<>();
            bordersMeal.put("Borders", bordersForMeal);
            bordersMeal.put("Date", date);
            bordersMeal.put("Num of HalfMeal", HalfMeal);
            bordersMeal.put("Num of FullMeal", FullMeal);
            bordersMeal.put("Meal Rate", mealRate);
            bordersMeal.put("Total Meal Cost", mealRateDb);

            docs.collection("meals").document(getIntent().getStringExtra("mealName")).update("total cash out", totalCost+mealRateDb);

            eliminatingMealCost((Double.parseDouble(mealRate))/2, Double.parseDouble(mealRate));

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
    }

    public void eliminatingMealCost(double half, double full){
        for (Map.Entry<String, String> entry : bordersForMeal.entrySet()) {
            //String key = entry.getKey();
            //entry.getValue();

            final double[] numHalf = new double[1];
            final double[] numFull = new double[1];
            final double[] spend = new double[1];

            docs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Borders").document(entry.getKey())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null) {Toast.makeText(todayMeal.this,"first", Toast.LENGTH_SHORT).show(); return;}
                            if(value != null && value.exists()){
                                numHalf[0] = value.getDouble("number of half meals");
                                numFull[0] = value.getDouble("number of full meals");
                                spend[0] = value.getDouble("total spend");
                                //Toast.makeText(todayMeal.this,"working", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            if(entry.getValue().charAt(0) == '1'){
                docs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Borders").document(entry.getKey())
                        .update("number of half meals", numHalf[0] +1.0,
                                "total spend", spend[0] +half);
            }
            if(entry.getValue().charAt(1) == '1'){
                docs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Borders").document(entry.getKey())
                        .update("number of full meals", numFull[0] +1.0,
                                "total spend", spend[0]+full);
            }

        }
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

        docs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Meal History").document(String.format("Meal %s", date))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) mealToday = true;
                        else mealToday = false;
                    }
                });

        docs.collection("meals").document(getIntent().getStringExtra("mealName"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) totalCost = documentSnapshot.getDouble("total cash out");
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        numFullMeal.setText("Number of Full Meal : "+String.valueOf(FullMeal));
        numHalfMeal.setText("Number of Half Meal : "+String.valueOf(HalfMeal));
    }
}