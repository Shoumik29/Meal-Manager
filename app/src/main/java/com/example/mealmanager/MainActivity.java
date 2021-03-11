package com.example.mealmanager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    public CardView profileCv, statisticsCv, account_settingsCv, data_entryCv, paymentCv, searchCv;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileCv = (CardView) findViewById(R.id.cv2);
        statisticsCv = (CardView) findViewById(R.id.cv4);
        account_settingsCv = (CardView) findViewById(R.id.cv1);
        data_entryCv = (CardView) findViewById(R.id.cv3);
        paymentCv = (CardView) findViewById(R.id.cv5);
        searchCv = (CardView) findViewById(R.id.cv6);
        drawerLayout = (DrawerLayout) findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        profileCv.setOnClickListener(this);
        statisticsCv.setOnClickListener(this);
        account_settingsCv.setOnClickListener(this);
        data_entryCv.setOnClickListener(this);
        paymentCv.setOnClickListener(this);
        searchCv.setOnClickListener(this);
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
            case R.id.cv3 :
                i = new Intent(this,data_entry.class);
                startActivity(i);
                break;
            case R.id.cv4 :
                i = new Intent(this,statistics.class);
                startActivity(i);
                break;
            case R.id.cv5 :
                i = new Intent(this,payment.class);
                startActivity(i);
                break;
            case R.id.cv6 :
                i = new Intent(this,search.class);
                startActivity(i);
                break;

        }

    }
}