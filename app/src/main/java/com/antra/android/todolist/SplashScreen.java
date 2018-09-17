package com.antra.android.todolist;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.antra.android.todolist.db.SqliteDataBase;
import com.antra.android.todolist.ui.MainActivity;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SqliteDataBase mDatabase = new SqliteDataBase(this);
//        mDatabase.addList(new Task("Inbox"));//added first list
        ImageView imgPen = findViewById(R.id.img_pen_id);
        TextView txtslogan = findViewById(R.id.txt_slogan_id);
        ObjectAnimator sloganAnimation = ObjectAnimator.ofFloat(txtslogan,View.TRANSLATION_X, -350f, 0f);
        sloganAnimation.setDuration(750);
        sloganAnimation.setInterpolator(new LinearInterpolator());
        sloganAnimation.start();

        ObjectAnimator penObjectAnimator = ObjectAnimator.ofFloat(imgPen , View.ROTATION_Y, 0f, 360f);
        penObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        penObjectAnimator.setDuration(750);
        penObjectAnimator.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                finish();
            }
        }, 2000);

    }
}
