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

public class BorderList extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ArrayList<userSearchModel> borderArrayList;
    public FirebaseFirestore mDocs;
    public borderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_border_list);

        borderArrayList = new ArrayList<>();
        mDocs = FirebaseFirestore.getInstance();
        mAdapter = new borderAdapter(borderArrayList, getApplicationContext());

        recyclerView = (RecyclerView)findViewById(R.id.borderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        retrieveBorders();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void retrieveBorders(){
        mDocs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Borders").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            userSearchModel obj = d.toObject(userSearchModel.class);
                            obj.setUserName(d.getString("Border name"));
                            obj.setUserInstitution(d.getString("Border institution"));
                            obj.setUserId(d.getId());
                            borderArrayList.add(obj);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BorderList.this, "Search Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}