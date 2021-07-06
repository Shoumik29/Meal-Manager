package com.example.mealmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mealAdapter extends RecyclerView.Adapter<mealAdapter.ViewHolder> {

    ArrayList<mealModel> dataList;
    Context context;

    public mealAdapter(ArrayList<mealModel> dataList, Context context){
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public mealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mealAdapter.ViewHolder holder, int position) {

        holder.mealName.setText(dataList.get(position).getMealName());
        holder.managerName.setText(dataList.get(position).getManagerName());

        holder.ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, mealViewForOthers.class);
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
        public ImageView mealImage;
        public ConstraintLayout ct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mealName = itemView.findViewById(R.id.textView28);
            managerName = itemView.findViewById(R.id.textView29);
            mealImage = itemView.findViewById(R.id.imageView11);
            ct = itemView.findViewById(R.id.touchLayout1);

        }
    }
}
