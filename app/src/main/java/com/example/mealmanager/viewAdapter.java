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

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.ViewHolder>{

    ArrayList<userSearchModel> dataList;
    Context context;

    public viewAdapter(ArrayList<userSearchModel> dataList, Context context){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public viewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewAdapter.ViewHolder holder, int position) {

        final userSearchModel temp = dataList.get(position);

        holder.userName.setText(dataList.get(position).getUserName());
        holder.userInstitution.setText(dataList.get(position).getUserInstitution());

        holder.ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, profileViewForOthers.class);
                i.putExtra("userId", temp.getUserId());
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

        public TextView userName, userInstitution;
        public ImageView profileImage;
        public ConstraintLayout ct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.BLName);
            userInstitution = itemView.findViewById(R.id.BLInstitution);
            profileImage = itemView.findViewById(R.id.BLmealimage);
            ct = itemView.findViewById(R.id.BLconstrain);

        }
    }
}
