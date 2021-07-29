package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


   // public CardView profileCv, account_settingsCv, paymentCv, myMealCv;
    public BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public Toolbar toolbar;
    public NavigationView navigationView;
    public FirebaseFirestore db;
    public boolean meal;
    public String meal_name;
    public Fragment home;
    public Bundle data;
    public TextView navHeaderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data =  new Bundle();
        data.putString("mealName", meal_name);

        home = new homeFragment();
        home.setArguments(data);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, home).commit();

        //Initialization
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.nav_view);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        View headerView = navigationView.getHeaderView(0);
        navHeaderName = (TextView) headerView.findViewById(R.id.textView22);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment temp = null;

                switch(item.getItemId()){
                    case R.id.fragment_home: temp = new homeFragment();
                    toolbar.setTitle("Home");
                    break;
                    case R.id.fragment_cash: temp = new cashInFragment();
                    toolbar.setTitle("Cash In");
                    break;
                    case R.id.fragment_myMeal: temp = new myMealFragment();
                    toolbar.setTitle("My Meal");
                }
                data.putString("mealName", meal_name);
                temp.setArguments(data);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, temp).commit();

                return true;
            }
        });


        //Navigation bar section
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.item4);

    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, log_in_activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if the user signed in
        currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, log_in_activity.class);
            startActivity(intent);
            finish();
        }
        else{
            String userId = currentUser.getUid();
            db.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    navHeaderName.setText(value.getString("Name"));

                    if(value.getString("Meal name") != null){
                        meal = true;
                        meal_name = value.getString("Meal name");
                    }
                    else {
                        meal = false;
                        meal_name = null;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent i1;

        switch (item.getItemId()) {
            case R.id.item3:
                logout();
                break;
            case R.id.item4:
                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_LONG).show();
                i1 = new Intent(MainActivity.this, searchMeal.class);
                startActivity(i1);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}