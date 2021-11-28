package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BorderList extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ArrayList<userSearchModel> borderArrayList;
    public FirebaseFirestore mDocs;
    public borderAdapter mAdapter;
    public TextView searchBox;
    public ImageView search, notFound;
    public ConstraintLayout backgroundCL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_border_list);

        borderArrayList = new ArrayList<>();
        mDocs = FirebaseFirestore.getInstance();
        mAdapter = new borderAdapter(borderArrayList, getApplicationContext());


        backgroundCL = (ConstraintLayout)findViewById(R.id.constraintLayout4);
        searchBox = (TextView)findViewById(R.id.searchBordersList);
        search = (ImageView)findViewById(R.id.imageViewBorder);
        notFound = (ImageView)findViewById(R.id.imageView18);
        recyclerView = (RecyclerView)findViewById(R.id.borderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        retrieveBorders();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSearch();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        notFound.setVisibility(View.INVISIBLE);
    }

    private void userSearch() {
        borderArrayList.clear();
        mDocs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Borders").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            String data = d.getString("B name");
                            if(searchBox.getText().toString().trim().equals("")) {Toast.makeText(BorderList.this,"Insert text to search", Toast.LENGTH_SHORT).show(); return;}
                            else if (data.startsWith(searchBox.getText().toString().trim())) {
                                userSearchModel obj = d.toObject(userSearchModel.class);
                                obj.setUserName(d.getString("B name"));
                                obj.setUserInstitution(d.getString("B institution"));
                                obj.setUserId(d.getId());
                                borderArrayList.add(obj);
                            }
                            if(borderArrayList.isEmpty()) notFound.setVisibility(View.VISIBLE);
                            else notFound.setVisibility(View.INVISIBLE);
                        }
                        Collections.sort(borderArrayList, new Comparator<userSearchModel>() {
                            @Override
                            public int compare(userSearchModel o1, userSearchModel o2) {
                                return o1.getUserName().compareToIgnoreCase(o2.getUserName());
                            }
                        });
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

    public void retrieveBorders(){
        mDocs.collection("meals").document(getIntent().getStringExtra("mealName")).collection("Borders").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            userSearchModel obj = d.toObject(userSearchModel.class);
                            obj.setUserName(d.getString("B name"));
                            obj.setUserInstitution(d.getString("B institution"));
                            obj.setUserId(d.getId());
                            borderArrayList.add(obj);
                        }
                        Collections.sort(borderArrayList, new Comparator<userSearchModel>() {
                            @Override
                            public int compare(userSearchModel o1, userSearchModel o2) {
                                return o1.getUserName().compareToIgnoreCase(o2.getUserName());
                            }
                        });
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