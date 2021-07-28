package com.example.mealmanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class cashInFragment extends Fragment {

    public Calendar calendar;
    public TextView date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_in, container, false);

        calendar = Calendar.getInstance();

        date = view.findViewById(R.id.textView6);
        date.append(" "+DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime()));

       return view;
    }
}