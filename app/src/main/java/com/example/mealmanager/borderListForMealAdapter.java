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



    public Map<String, String> getBordersForMeal() {
        return bordersForMeal;
    }

    public borderListForMealAdapter(ArrayList<userSearchModel> dataList, Context context){
        bordersForMeal = new HashMap<>();
        this.dataList = dataList;
        this.context = context;
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
        final StringBuffer string = new StringBuffer("111");

        holder.userName.setText(dataList.get(position).getUserName());
        holder.userInstitution.setText(dataList.get(position).getUserInstitution());
        holder.breakfast.setChecked(true);
        holder.lunch.setChecked(true);
        holder.dinner.setChecked(true);

        bordersForMeal.put(temp.getUserName(),"111");

        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.breakfast.isChecked() || holder.lunch.isChecked() || holder.dinner.isChecked()){
                    holder.breakfast.setChecked(false);
                    holder.lunch.setChecked(false);
                    holder.dinner.setChecked(false);
                    bordersForMeal.remove(temp.getUserName());
                }
                else{
                    holder.breakfast.setChecked(true);
                    holder.lunch.setChecked(true);
                    holder.dinner.setChecked(true);
                    bordersForMeal.put(temp.getUserName(),"111");
                }
            }
        });

        holder.breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.breakfast.isChecked()) string.setCharAt(0,'1');
                else string.setCharAt(0,'0');
                bordersForMeal.put(temp.getUserName(), string.toString());
                if(string.toString().equals("000")) bordersForMeal.remove(temp.getUserName());
            }
        });

        holder.lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.lunch.isChecked())string.setCharAt(1,'1');
                else string.setCharAt(1,'0');
                bordersForMeal.put(temp.getUserName(), string.toString());
                if(string.toString().equals("000")) bordersForMeal.remove(temp.getUserName());
            }
        });

        holder.dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.dinner.isChecked()) string.setCharAt(2,'1');
                else string.setCharAt(2,'0');
                bordersForMeal.put(temp.getUserName(), string.toString());
                if(string.toString().equals("000")) bordersForMeal.remove(temp.getUserName());
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userInstitution;
        public ImageView profileImage;
        public ConstraintLayout cl;
        public CheckBox breakfast, lunch, dinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.BLMName);
            userInstitution = itemView.findViewById(R.id.BLMInstitution);
            profileImage = itemView.findViewById(R.id.BLMmealimage);
            breakfast = itemView.findViewById(R.id.checkBox3);
            lunch = itemView.findViewById(R.id.checkBox2);
            dinner = itemView.findViewById(R.id.checkBox);
            cl = itemView.findViewById(R.id.BLMconstrain);

        }
    }
}
