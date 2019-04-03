package com.zs.buding;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zs.buding.view.EyeView;

public class MainActivity extends AppCompatActivity {

    private EyeView mainEyeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainEyeView = findViewById(R.id.main_eye_view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainEyeView.startBlinkAnim();
            }
        },1000);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mainEyeView.stopBlinkAnim();
            }
        },5000);

    }

}
