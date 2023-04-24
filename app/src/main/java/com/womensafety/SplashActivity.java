package com.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    TextView power;
    TextView creat;
    ImageView log;
    Animation top , bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        log = findViewById(R.id.log);
        power = findViewById(R.id.powered);
        creat = findViewById(R.id.creator);
        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        log.setAnimation(bottom);
        power.setAnimation(top);
        creat.setAnimation(top);

        new Handler().postDelayed(() -> {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
             .getBoolean("isFirstRun", true);

         if (isFirstRun) {
                //show start activity

               startActivity(new Intent(SplashActivity.this, OnBoarding.class));
               finish();
           }
        else {
               Intent i = new Intent(SplashActivity.this, Login.class);
              startActivity(i);
               finish();

         }


          getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();



        }, 2500);
    }

    @Override
    public void onBackPressed(){
        // Nothing
    }


}