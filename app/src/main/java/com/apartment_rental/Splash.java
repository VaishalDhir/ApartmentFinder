package com.apartment_rental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;
    Animation topAnim, bottomAnim;
    //TextView RSA,App_Name, slogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        SharedPref shr=new SharedPref(getApplicationContext());
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        TextView appName_s = findViewById(R.id.ani1);
        TextView appName_f = findViewById(R.id.anim_2);
        TextView slogan = findViewById(R.id.anim_3);

        appName_s.setAnimation(topAnim);
        appName_f.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, MainActivity.class));
//                if(shr.getLoginStatus().equals("1")){
//                    startActivity(new Intent(SplashDataActivity.this, Speciality.class));
//                    finish();
//                }else
//                    startActivity(new Intent(SplashDataActivity.this, Welcome_Actitivty.class));
//                finish();
            }
        }, SPLASH_SCREEN);
    }
}