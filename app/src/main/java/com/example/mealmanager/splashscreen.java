package com.example.mealmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class splashscreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 3000; //stay on this page for 3ms

    ImageView img;
    TextView text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  //Getrid off top screen

        img = (ImageView) findViewById(R.id.imageview);
        text2 = (TextView) findViewById(R.id.text2);

        YoYo.with(Techniques.BounceInRight).duration(2500).repeat(0).playOn(img);
        YoYo.with(Techniques.Shake).delay(2000).duration(400).repeat(0).playOn(img);

        YoYo.with(Techniques.BounceInLeft).duration(2500).repeat(0).playOn(text2);
        YoYo.with(Techniques.Shake).delay(2000).duration(400).repeat(0).playOn(text2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(splashscreen.this,MainActivity.class); //Move back to mainActivity
                startActivity(intent);
                finish(); //For finishing this acctivity it never comes back

            }
        },SPLASH_TIMER);




    }
}