package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class mealHistory extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ArrayList<mealModel> mealArrayList;
    public FirebaseFirestore mDocs;
    public mealHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_history);

        mealArrayList = new ArrayList<>();
        mDocs = FirebaseFirestore.getInstance();
        mAdapter = new mealHistoryAdapter(mealArrayList, getApplicationContext());

        recyclerView = (RecyclerView)findViewById(R.id.mealHistoryRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mealHistoryDataFetch();
    }

    private void mealHistoryDataFetch() {
        mDocs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Meal History").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            mealModel obj = d.toObject(mealModel.class);
                            obj.setDate(d.getString("Date"));
                            mealArrayList.add(obj);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mealHistory.this, "search failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}