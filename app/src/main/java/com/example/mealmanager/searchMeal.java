package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class searchMeal extends AppCompatActivity {

    public EditText searchText;
    public ImageView search;
    public RecyclerView recyclerView;
    public ArrayList<mealModel> mealArrayList;
    public FirebaseFirestore mDocs;
    public mealAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal);

        mealArrayList = new ArrayList<>();
        mDocs = FirebaseFirestore.getInstance();
        mAdapter = new mealAdapter(mealArrayList, getApplicationContext());

        searchText = (EditText)findViewById(R.id.searchMT);
        search = (ImageView)findViewById(R.id.imageView15);
        recyclerView = (RecyclerView)findViewById(R.id.resultMList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        search.setClickable(true);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealArrayList.clear();
                mealSearch();
            }
        });
    }

    private void mealSearch() {
        mDocs.collection("meals").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            String data = d.getString("meal name");
                            if(searchText.getText().toString().trim().equals("")) Toast.makeText(searchMeal.this,"Insert Text To Search", Toast.LENGTH_SHORT).show();
                            else if(data.toLowerCase().startsWith(searchText.getText().toString().trim().toLowerCase())){
                                mealModel obj = d.toObject(mealModel.class);
                                obj.setMealName(d.getString("meal name"));
                                obj.setManagerName(d.getString("manager name"));
                                mealArrayList.add(obj);
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(searchMeal.this, "search failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}