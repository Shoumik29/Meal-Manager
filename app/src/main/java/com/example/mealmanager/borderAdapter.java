package com.example.mealmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class borderAdapter extends RecyclerView.Adapter<borderAdapter.ViewHolder> {

    ArrayList<userSearchModel> dataList;
    Context context;

    public borderAdapter(ArrayList<userSearchModel> dataList, Context context){
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public borderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new borderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull borderAdapter.ViewHolder holder, int position) {
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

        holder.copyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", temp.getUserId());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(context,"User ID Copied",Toast.LENGTH_SHORT).show();
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
        public CardView copyCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.BLName);
            userInstitution = itemView.findViewById(R.id.BLInstitution);
            profileImage = itemView.findViewById(R.id.BLmealimage);
            ct = itemView.findViewById(R.id.BLconstrain);
            copyCard = itemView.findViewById(R.id.copyCardView);

        }
    }
}
