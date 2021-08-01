package com.example.mealmanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class requestAdapter extends RecyclerView.Adapter<requestAdapter.ViewHolder> {


    ArrayList<userSearchModel> dataList;
    Context context;

    public requestAdapter(ArrayList<userSearchModel> dataList, Context context){
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public requestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_request, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull requestAdapter.ViewHolder holder, int position) {

        holder.userName.setText(dataList.get(position).getUserName());
        holder.userInstitution.setText(dataList.get(position).getUserInstitution());
        holder.mealName = dataList.get(position).getMealName();
        holder.uId = dataList.get(position).getUserId();
        holder.borderName = dataList.get(position).getUserName();
        holder.borderInstitution = dataList.get(position).getUserInstitution();
        holder.borderMobile = dataList.get(position).getUserMobile();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userInstitution;
        public Button confirmB, declineB;
        public FirebaseFirestore db;
        public String mealName, uId, borderName, borderInstitution, borderMobile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            db = FirebaseFirestore.getInstance();

            userName = itemView.findViewById(R.id.reqName);
            userInstitution = itemView.findViewById(R.id.reqIns);
            confirmB = itemView.findViewById(R.id.button13);
            declineB = itemView.findViewById(R.id.button14);

            confirmB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmRequest();
                    confirmB.setEnabled(false);
                    confirmB.setBackgroundColor(Color.GRAY);
                    declineB.setEnabled(false);
                    declineB.setBackgroundColor(Color.GRAY);
                }
            });

            declineB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    declineRequest();
                }
            });
        }

        private void declineRequest() {
            db.collection("meals").document(mealName).collection("Meal Request").document(uId).delete();
            confirmB.setEnabled(false);
            confirmB.setBackgroundColor(Color.GRAY);
            declineB.setEnabled(false);
            declineB.setBackgroundColor(Color.GRAY);
        }

        private void confirmRequest() {

            Map<String, Object> UM = new HashMap<>();
            UM.put("B name", borderName);
            UM.put("B institution", borderInstitution);
            UM.put("B Mobile", borderMobile);
            UM.put("paid", 0);
            UM.put("spend", 0);
            UM.put("number of meals", 0);

            db.collection("meals").document(mealName).collection("Meal Request").document(uId).delete();
            db.collection("meals").document(mealName).collection("Borders").document(uId)
                    .set(UM)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            db.collection("users").document(uId).update("Meal name", mealName);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

    }
}
