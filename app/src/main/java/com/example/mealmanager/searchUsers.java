package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class searchUsers extends AppCompatActivity {

    public EditText searchText;
    public ImageButton searchButton;
    public RecyclerView recyclerView;
    public ArrayList<userSearchModel> userArrayList;
    public FirebaseFirestore mDocs;
    public viewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        userArrayList = new ArrayList<>();
        mDocs = FirebaseFirestore.getInstance();
        mAdapter = new viewAdapter(userArrayList);

        searchText = (EditText)findViewById(R.id.searchT);
        searchButton = (ImageButton) findViewById(R.id.searchB);

        recyclerView = (RecyclerView)findViewById(R.id.resultList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userArrayList.clear();
                userSearch();
            }
        });

    }

    private void userSearch(){
        mDocs.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d: list){
                    String data = d.getString("Name");
                    if(data.contains(searchText.getText().toString().trim())){
                        userSearchModel obj = d.toObject(userSearchModel.class);
                        obj.setUserName(d.getString("Name"));
                        obj.setUserInstitution(d.getString("Institution"));
                        userArrayList.add(obj);
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(searchUsers.this, "Search Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

}