package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    public CardView profileCv, account_settingsCv, paymentCv, myMealCv;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public Toolbar toolbar;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization
        mAuth = FirebaseAuth.getInstance();

        //Tools id section
        profileCv = (CardView) findViewById(R.id.cv2);
        account_settingsCv = (CardView) findViewById(R.id.cv1);
        myMealCv = (CardView) findViewById(R.id.cv6);
        //searchCv = (CardView) findViewById(R.id.cv6);
        drawerLayout = (DrawerLayout) findViewById(R.id.nav_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);

        //Navigation bar section
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        profileCv.setOnClickListener(this);
        account_settingsCv.setOnClickListener(this);
        myMealCv.setOnClickListener(this);

        //searchCv.setOnClickListener(this);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.item1);

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
    public void onClick(View v) {

        Intent i;

        switch (v.getId()){
            case R.id.cv2 :
                i = new Intent(this,profile.class);
                startActivity(i);
                break;
            case R.id.cv1 :
                i = new Intent(this,account_settings.class);
                startActivity(i);
                break;
            case R.id.cv6 :
                i = new Intent(this,startMeal.class);
                startActivity(i);
                break;
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent i1;

        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(MainActivity.this, "Authentication Successful", Toast.LENGTH_LONG).show();
                i1 = new Intent(MainActivity.this, statistics.class);
                startActivity(i1);
                break;
            case R.id.item3:
                logout();
                break;
            case R.id.item4:
                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_LONG).show();
                i1 = new Intent(MainActivity.this, searchUsers.class);
                startActivity(i1);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}