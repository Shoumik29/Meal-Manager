package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class mealRequestList extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ArrayList<userSearchModel> requestList;
    public FirebaseFirestore mDocs;
    public requestAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_request_list);

        requestList = new ArrayList<>();
        mDocs = FirebaseFirestore.getInstance();
        mAdapter = new requestAdapter(requestList, getApplicationContext());

        recyclerView = (RecyclerView)findViewById(R.id.requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestList();
    }

    private void requestList(){
        mDocs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Meal Request").
                get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            userSearchModel obj = d.toObject(userSearchModel.class);
                            obj.setUserName(d.getString("user name"));
                            obj.setUserInstitution(d.getString("user institution"));
                            obj.setUserId(d.getId());
                            obj.setMealName(getIntent().getStringExtra("mealName"));
                            requestList.add(obj);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mealRequestList.this, "error occured", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}