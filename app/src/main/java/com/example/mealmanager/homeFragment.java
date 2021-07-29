package com.example.mealmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class homeFragment extends Fragment {

    public CardView mealCard, profileCard;
    public Bundle data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        data = getArguments();

        mealCard = view.findViewById(R.id.mealView);
        profileCard = view.findViewById(R.id.cardView);

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
}