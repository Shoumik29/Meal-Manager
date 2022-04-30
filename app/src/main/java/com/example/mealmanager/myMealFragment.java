package com.example.mealmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class myMealFragment extends Fragment {

    public ExtendedFloatingActionButton extendedFab;
    public RecyclerView recyclerView;
    public ArrayList<mealModel> mealArrayList;
    public FirebaseFirestore mDocs;
    public mealHistoryAdapter mAdapter;
    public Bundle data;
    public CardView joinMealCv, borderListCv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_meal, container, false);

        data = getArguments();

        extendedFab = view.findViewById(R.id.fab);
        joinMealCv = view.findViewById(R.id.joinMealcv);
        borderListCv = view.findViewById(R.id.borderCv);
        mealArrayList = new ArrayList<>();
        mDocs = FirebaseFirestore.getInstance();
        mAdapter = new mealHistoryAdapter(mealArrayList, getActivity());

        recyclerView = view.findViewById(R.id.mealHistoryView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

//        mealHistoryDataFetch();


        borderListCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BorderList.class);
                i.putExtra("mealName", data.getString("mealName"));
                startActivity(i);
            }
        });

        joinMealCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), mealRequestList.class);
                i.putExtra("mealName", data.getString("mealName"));
                startActivity(i);
            }
        });


        extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), todayMeal.class);
                i.putExtra("mealName", data.getString("mealName"));
                startActivity(i);
            }
        });

        return view;
    }



    private void mealHistoryDataFetch() {
        mealArrayList.clear();
        mDocs.collection("meals").document(data.getString("mealName")).collection("Meal History").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            mealModel obj = d.toObject(mealModel.class);
                            obj.setDate(d.getString("Date"));
                            mealArrayList.add(obj);
                        }
                        //Collections.reverse(mealArrayList);
                        Toast.makeText(getActivity(),String.valueOf(mealArrayList.size()), Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "search failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mDocs.collection("meals").document(data.getString("mealName"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) mealHistoryDataFetch();
                        else{
                            Intent i = new Intent(getActivity(), startMeal.class);
                            i.putExtra("mealName", data.getString("mealName"));
                            startActivity(i);
                        }
                    }
                });
    }
}


