package com.example.countdowndays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {

    //持续时间
    private static final int DELAY_TIME = 3000;
    private ImageView splashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.launch_screen);

        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                // 跳转到登陆界面
                SplashScreenActivity.this.startActivity(i); // 启动登陆界面
                finish(); // 结束Acitivity
            }
        }, DELAY_TIME);
    }
}