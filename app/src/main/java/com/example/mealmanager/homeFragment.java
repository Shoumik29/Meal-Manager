package com.example.mealmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;


public class homeFragment extends Fragment {

    public CardView mealCard, profileCard;
    public Bundle data;
    public FirebaseFirestore docs;
    public TextView TCashIn, TCashOut, TmealName, Tbalance, Tdate, Tname, Tinstitution;
    public String userId;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        data = getArguments();

        docs = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        calendar = Calendar.getInstance();

        mealCard = view.findViewById(R.id.mealView);
        profileCard = view.findViewById(R.id.cardView);
        TCashIn = view.findViewById(R.id.textView42);
        TCashOut = view.findViewById(R.id.textView43);
        TmealName = view.findViewById(R.id.textView41);
        Tbalance = view.findViewById(R.id.textView46);
        Tdate = view.findViewById(R.id.textView44);
        Tname = view.findViewById(R.id.textView39);
        Tinstitution = view.findViewById(R.id.textView40);


        mealCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), myMeal.class);
                i.putExtra("mealName", data.getString("mealName"));
                startActivity(i);
            }
        });

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), profile.class);
                startActivity(i);
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //append er bodole setText korte hbe
        Tdate.append(" "+ DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime()));

        if(currentUser != null){
            userId = currentUser.getUid();
            docs.collection("users").document(userId)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(value != null && value.exists()){
                                if(value.getString("Meal name") != null){
                                    docs.collection("meals").document(value.getString("Meal name"))
                                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if(value != null && value.exists()){
                                                        TCashIn.append(String.valueOf(value.getDouble("total cash in")));
                                                        TCashOut.append(String.valueOf(value.getDouble("total cash out")));
                                                        double balance;
                                                        balance = value.getDouble("total cash in") - value.getDouble("total cash out");
                                                        if(balance<0){
                                                            Tbalance.append(String.valueOf(balance));
                                                            Tbalance.setTextColor(Color.RED);
                                                        }
                                                        else{
                                                            Tbalance.append(String.valueOf(balance));
                                                            Tbalance.setTextColor(Color.parseColor("#228B22"));
                                                        }
                                                    }
                                                }
                                            });
                                    TmealName.setText(value.getString("Meal name"));
                                }
                                Tname.setText(value.getString("Name"));
                                Tinstitution.setText(value.getString("Institution"));
                            }
                        }
                    });
        }
        else{
            Intent intent = new Intent(getActivity(), log_in_activity.class);
            startActivity(intent);
        }
    }
}