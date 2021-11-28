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

public class cashHistoryAdapter extends RecyclerView.Adapter<cashHistoryAdapter.ViewHolder> {

    ArrayList<cashModel> dataList;
    Context context;

    public cashHistoryAdapter(ArrayList<cashModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public cashHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_history, parent, false);
        return new cashHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cashHistoryAdapter.ViewHolder holder, int position) {
        //holder.userName.setText(String.format("Meal %s",dataList.get(position).getDate()));


//        holder.ct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, mealHistoryView.class);
//                //i.putExtra("mealName", temp.getMealName());
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userID, amount, date;
        ConstraintLayout ct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.textView72);
            userID = itemView.findViewById(R.id.textView73);
            amount = itemView.findViewById(R.id.textView74);
            date = itemView.findViewById(R.id.textView75);
        }
    }
}
