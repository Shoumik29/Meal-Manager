package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class borderListForMeal extends AppCompatActivity{

    public RecyclerView recyclerView;
    public ArrayList<userSearchModel> borderArrayList;
    public FirebaseFirestore mDocs;
    public borderListForMealAdapter mAdapter;
    public Calendar calendar;
    public Map<String, String> bordersForMeal;
    public Button ok;
    public String date;
    public ArrayList<Integer> mealSelection;
    public int halfMeal, fullMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_border_list_for_meal);

        //Initializations
        mealSelection = new ArrayList<>();
        borderArrayList = new ArrayList<>();
        bordersForMeal = new HashMap<>();
        mealSelection = getIntent().getIntegerArrayListExtra("mealSelect");
        mDocs = FirebaseFirestore.getInstance();
        mAdapter = new borderListForMealAdapter(borderArrayList, getApplicationContext(), mealSelection);
        calendar = Calendar.getInstance();
        halfMeal = 0;
        fullMeal = 0;

        ok = (Button)findViewById(R.id.button20);
        recyclerView = (RecyclerView)findViewById(R.id.borderListMeal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        bordersForMeal = mAdapter.getBordersForMeal();


        date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());

        mDocs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Borders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        for(DocumentSnapshot d: list){
                            userSearchModel obj = d.toObject(userSearchModel.class);
                            obj.setUserName(d.getString("B name"));
                            obj.setUserInstitution(d.getString("B institution"));
                            obj.setUserId(d.getId());
                            borderArrayList.add(obj);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

        mAdapter.notifyDataSetChanged();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressOK();
            }
        });
    }

    private void pressOK() {
       //Toast.makeText(borderListForMeal.this, String.format("Total Selected Full Meal %s", String.valueOf(bordersForMeal.size())) , Toast.LENGTH_SHORT).show();
//        int i=5;
        //String value = bordersForMeal.values().toString();
        for (Map.Entry<String, String> entry : bordersForMeal.entrySet()) {
            //String key = entry.getKey();
            String value = entry.getValue();
            if(value.charAt(0) == '1') halfMeal++;
            if(value.charAt(1) == '1') fullMeal++;
        }

        //Toast.makeText(borderListForMeal.this, String.format("Value is : %s", String.valueOf(halfMeal)), Toast.LENGTH_SHORT).show();

       Bundle bundle = new Bundle();
       bundle.putSerializable("Borders List", (Serializable) bordersForMeal);


       Intent intent = new Intent();
       intent.putExtra("Borders List", bundle);
       intent.putExtra("halfMeal", halfMeal);
       intent.putExtra("fullMeal", fullMeal);
       setResult(RESULT_OK, intent);
       finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}