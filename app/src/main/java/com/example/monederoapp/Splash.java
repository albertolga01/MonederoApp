package com.example.monederoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1200;

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        logo = (ImageView) findViewById(R.id.ar_logo);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void  run() {
                        Intent MainAct = new Intent(Splash.this, Login.class);
                        startActivity(MainAct);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });








    }
}