package com.example.mealmanager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class cashInFragment extends Fragment {

    public Calendar calendar;
    public TextView date;
    public EditText ETuserId, ETcashAmount;
    public Button confirm;
    public RecyclerView recyclerView;
    public FirebaseFirestore docs;
    public Bundle data;
    public ArrayList<cashModel> cashArrayList;
    public cashHistoryAdapter mAdapter;
    public double tCashIn, bTCashIn;
    long seconds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_in, container, false);

        calendar = Calendar.getInstance();
        docs = FirebaseFirestore.getInstance();
        cashArrayList = new ArrayList<>();
        data = getArguments();
        mAdapter = new cashHistoryAdapter(cashArrayList, getActivity());


        date = view.findViewById(R.id.textView6);
        confirm = view.findViewById(R.id.button21);
        ETuserId = view.findViewById(R.id.editTextTextPersonName3);
        ETcashAmount = view.findViewById(R.id.editTextNumberDecimal2);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        
        date.append(" "+DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime()));
        
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ETuserId.getText().toString().trim())) ETuserId.setError("Enter User id");
                else confirmCashIn();
            }
        });

       return view;
    }

    public void confirmCashIn() {
        String userID = ETuserId.getText().toString().trim();
        String cash = ETcashAmount.getText().toString().trim();
        String date = DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime());
        double cashD = Double.parseDouble(cash);

        Map<String, Object> cashHistory = new HashMap<>();
        cashHistory.put("User Id", userID);
        cashHistory.put("Cash In Date", DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime()));
        cashHistory.put("Amount", cashD);

        tCashIn = tCashIn + cashD;

        docs.collection("meals").document(data.getString("mealName")).collection("Borders").document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) bTCashIn = documentSnapshot.getDouble("total paid");
                    }
                });

        bTCashIn = bTCashIn + cashD;

        docs.collection("meals").document(data.getString(("mealName"))).collection("Borders").document(userID).update("total paid", bTCashIn)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        docs.collection("meals").document(data.getString("mealName")).collection("Cash In History").document(String.format("Cash %s",String.valueOf(seconds)))
                                .set(cashHistory)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Cash in Successful",Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getActivity(), payment_confirmed.class);
                                        startActivity(i);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Cash in failed",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        docs.collection("meals").document(data.getString("mealName")).update("total cash in", tCashIn)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });

                        cashHistoryDataFetch();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "User Id Invalid", Toast.LENGTH_SHORT).show();
                    }
                });

       // Toast.makeText(getActivity(), data.getString("mealName"),Toast.LENGTH_SHORT).show();

    }
    private void cashHistoryDataFetch() {
        cashArrayList.clear();
        docs.collection("meals").document(data.getString("mealName")).collection("Cash In History").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            cashModel obj = d.toObject(cashModel.class);
                            obj.setDate(d.getString("Date"));
                            cashArrayList.add(obj);
                        }
                        //Collections.reverse(cashArrayList);
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
        docs.collection("meals").document(data.getString("mealName"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) tCashIn = documentSnapshot.getDouble("total cash in");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        seconds = System.currentTimeMillis();
        ETuserId.setText(null);
        ETcashAmount.setText(null);
        cashHistoryDataFetch();
    }

}