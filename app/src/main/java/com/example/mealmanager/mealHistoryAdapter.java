package com.example.mealmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mealHistoryAdapter extends RecyclerView.Adapter<mealHistoryAdapter.ViewHolder> {

    ArrayList<mealModel> dataList;
    Context context;

    public mealHistoryAdapter(ArrayList<mealModel> dataList, Context context){
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public mealHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_history, parent, false);
        return new mealHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mealHistoryAdapter.ViewHolder holder, int position) {

        final mealModel temp = dataList.get(position);

        holder.mealName.setText(String.format("Meal %s",dataList.get(position).getDate()));

        holder.ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, mealHistoryView.class);
                //i.putExtra("mealName", temp.getMealName());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mealName, managerName;
        public ConstraintLayout ct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mealName = itemView.findViewById(R.id.textView24);
            managerName = itemView.findViewById(R.id.textView37);
            ct = itemView.findViewById(R.id.CTmealHistory);
        }
    }
}
