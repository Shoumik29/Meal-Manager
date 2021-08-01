package com.example.mealmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class createMyMeal extends Fragment {

    public CardView searchMeal, createMealCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_my_meal, container, false);

        searchMeal = view.findViewById(R.id.searchCard);
        createMealCard = view.findViewById(R.id.cardView3);

        searchMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), searchMeal.class);
                startActivity(i);
            }
        });
        createMealCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), createMeal.class);
                startActivity(i);
            }
        });

        return view;
    }
}