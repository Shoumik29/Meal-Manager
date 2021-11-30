package com.example.mealmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class paymentConfirmation extends AppCompatActivity {
    private static int TIMER = 1000; //stay on this page for 3ms
    ImageView done;
    TextView confirmationText;

    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        done = findViewById(R.id.done);
        confirmationText = findViewById(R.id.paymentConfirmationText);
        confirmationText.setAlpha(0);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  //Getrid off top screen

        Drawable drawable = done.getDrawable();

        if(drawable instanceof AnimatedVectorDrawableCompat){
            avd = (AnimatedVectorDrawableCompat) drawable;
            avd.start();
        }else if(drawable instanceof  AnimatedVectorDrawable){
            avd2 = (AnimatedVectorDrawable) drawable;
            avd2.start();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.FadeIn).duration(800).repeat(0).playOn(confirmationText);
            }
        },TIMER);


       /* new Handler().postDelayed(new Runnable() {   //it's for new activity after this animation done or after some time
            @Override
            public void run() {

                Intent intent = new Intent(paymentConfirmation.this,MainActivity.class); //Move back to mainActivity
                startActivity(intent);
                finish(); //For finishing this acctivity it never comes back

            }
        },TIMER);*/



    }
}