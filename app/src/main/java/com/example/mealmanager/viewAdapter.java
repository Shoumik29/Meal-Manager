package com.example.mealmanager;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayList;
import java.util.List;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView userName, userInstitution;
        public ImageView profileImage;
        public ConstraintLayout ct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.textView6);
            userInstitution = itemView.findViewById(R.id.textView24);
            profileImage = itemView.findViewById(R.id.imageView9);
            ct = itemView.findViewById(R.id.touchLayout);

        }

        @Override
        public void onClick(View v) {
        }
    }
}
