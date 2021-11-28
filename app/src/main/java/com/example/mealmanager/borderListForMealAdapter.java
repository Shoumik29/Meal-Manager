package com.example.mealmanager;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class borderListForMealAdapter extends RecyclerView.Adapter<borderListForMealAdapter.ViewHolder> {

    ArrayList<userSearchModel> dataList;
    Context context;
    Map<String, String> bordersForMeal;
    ArrayList<Integer>  mealSelect;

    public Map<String, String> getBordersForMeal() {
        return bordersForMeal;
    }

    public borderListForMealAdapter(ArrayList<userSearchModel> dataList, Context context, ArrayList<Integer> mealSelect){
        bordersForMeal = new HashMap<>();
        this.dataList = dataList;
        this.context = context;
        this.mealSelect = mealSelect;
    }

    @NonNull
    @Override
    public borderListForMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.border_list_meal, parent, false);
        return new borderListForMealAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull borderListForMealAdapter.ViewHolder holder, int position) {

        final userSearchModel temp = dataList.get(position);
        final StringBuffer string = new StringBuffer(String.valueOf(mealSelect.get(0))+String.valueOf(mealSelect.get(1)));

        holder.userName.setText(dataList.get(position).getUserName());
        holder.userInstitution.setText(dataList.get(position).getUserInstitution());

        bordersForMeal.put(temp.getUserId(),String.valueOf(mealSelect.get(0))+String.valueOf(mealSelect.get(1)));

        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.halfMeal.isChecked() || holder.fullMeal.isChecked()){
                    holder.halfMeal.setChecked(false);
                    holder.fullMeal.setChecked(false);
                    bordersForMeal.remove(temp.getUserId());

                }
                else{
                    if(mealSelect.get(0).equals(0)) {holder.halfMeal.setChecked(false);}
                    else {holder.halfMeal.setChecked(true);}
                    if(mealSelect.get(1).equals(0)) {holder.fullMeal.setChecked(false);}
                    else {holder.fullMeal.setChecked(true);}

                    bordersForMeal.put(temp.getUserId(),String.valueOf(mealSelect.get(0))+String.valueOf(mealSelect.get(1)));

                }
            }
        });

        holder.halfMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.halfMeal.isChecked()) {string.setCharAt(0,'1'); string.setCharAt(1,'0'); holder.fullMeal.setChecked(false);}
                else {string.setCharAt(0,'0');}
                bordersForMeal.put(temp.getUserId(), string.toString());
                if(string.toString().equals("00")) bordersForMeal.remove(temp.getUserName());

            }
        });

        holder.fullMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.fullMeal.isChecked()) {string.setCharAt(1,'1'); string.setCharAt(0,'0'); holder.halfMeal.setChecked(false);}
                else {string.setCharAt(1,'0');}
                bordersForMeal.put(temp.getUserId(), string.toString());
                if(string.toString().equals("00")) bordersForMeal.remove(temp.getUserName());

            }
        });

        if(mealSelect.get(0).equals(0)) {holder.halfMeal.setChecked(false);}
        else {holder.halfMeal.setChecked(true); holder.fullMeal.setEnabled(false);}
        if(mealSelect.get(1).equals(0)) {holder.fullMeal.setChecked(false);}
        else {holder.fullMeal.setChecked(true);}

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userInstitution;
        public ImageView profileImage;
        public ConstraintLayout cl;
        public CheckBox halfMeal, fullMeal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.BLMName);
            userInstitution = itemView.findViewById(R.id.BLMInstitution);
            profileImage = itemView.findViewById(R.id.BLMmealimage);
            halfMeal = itemView.findViewById(R.id.checkBox2);
            fullMeal = itemView.findViewById(R.id.checkBox);
            cl = itemView.findViewById(R.id.BLMconstrain);

        }
    }
}
