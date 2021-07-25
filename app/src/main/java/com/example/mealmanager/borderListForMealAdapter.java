package com.example.mealmanager;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.Transliterator;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;

public class borderListForMealAdapter extends RecyclerView.Adapter<borderListForMealAdapter.ViewHolder> {

    ArrayList<userSearchModel> dataList;
    Context context;
    ArrayList<String> bordersForMeal;

    public ArrayList<String> getBordersForMeal() {
        return bordersForMeal;
    }

    public borderListForMealAdapter(ArrayList<userSearchModel> dataList, Context context){
        bordersForMeal = new ArrayList<>();
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public borderListForMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.border_list_meal, parent, false);
        return new borderListForMealAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull borderListForMealAdapter.ViewHolder holder, int position) {

        final userSearchModel temp = dataList.get(position);

        holder.userName.setText(dataList.get(position).getUserName());
        holder.userInstitution.setText(dataList.get(position).getUserInstitution());
        holder.selectB.setChecked(true);
        bordersForMeal.add(temp.getUserName());

        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.selectB.isChecked()){
                    holder.selectB.setChecked(false);
                    bordersForMeal.remove(temp.getUserName());
                }
                else{
                    holder.selectB.setChecked(true);
                    bordersForMeal.add(temp.getUserName());
                }
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
        public CheckBox selectB;
        public ConstraintLayout cl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.BLMName);
            userInstitution = itemView.findViewById(R.id.BLMInstitution);
            profileImage = itemView.findViewById(R.id.BLMmealimage);
            selectB = itemView.findViewById(R.id.checkBox);
            cl = itemView.findViewById(R.id.BLMconstrain);

        }
    }
}
